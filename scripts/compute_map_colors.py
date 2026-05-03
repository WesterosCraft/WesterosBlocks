"""
Walk every block definition JSON, sample the actual block textures, and write a
nearest-vanilla MapColor name into the definition's "mapColor" field.

Run from the repo root:

    python scripts/compute_map_colors.py            # dry-run summary
    python scripts/compute_map_colors.py --write    # actually update JSONs
    python scripts/compute_map_colors.py --force    # also overwrite existing mapColor values

Dependencies: Pillow, numpy.

Texture sampling: for each definition we find the textures that best represent the
top-down map view (priority: top/up faces, then 'all', then any side), average all
non-transparent pixels, then match to the closest of vanilla's ~64 MapColor base RGB
values using the redmean color-distance metric — a perceptual approximation that
weights green higher and shifts red/blue weighting based on the average red value.
"""

import argparse
import json
import os
import sys
from collections import OrderedDict

import numpy as np
from PIL import Image


REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DEFINITIONS_DIR = os.path.join(REPO_ROOT, "src", "main", "resources", "definitions")
BLOCK_DEFS_DIR = os.path.join(DEFINITIONS_DIR, "block_definitions")
BLOCK_SET_DEFS_DIR = os.path.join(DEFINITIONS_DIR, "block_set_definitions")
TEXTURES_ROOT = os.path.join(
    REPO_ROOT, "src", "main", "resources", "assets", "westerosblocks", "textures", "block"
)


# Vanilla MapColor base RGB values, decoded from the int color field of each
# net.minecraft.block.MapColor static instance in Yarn 1.21.1. CLEAR (id 0) is
# intentionally excluded — assigning it would render as transparent/black on maps,
# which is exactly the bug we're fixing.
def _rgb_from_int(c):
    return ((c >> 16) & 0xFF, (c >> 8) & 0xFF, c & 0xFF)


_MAP_COLOR_INTS = {
    "PALE_GREEN":            8368696,
    "PALE_YELLOW":          16247203,
    "WHITE_GRAY":           13092807,
    "BRIGHT_RED":           16711680,
    "PALE_PURPLE":          10526975,
    "IRON_GRAY":            10987431,
    "DARK_GREEN":              31744,
    "WHITE":                16777215,
    "LIGHT_BLUE_GRAY":      10791096,
    "DIRT_BROWN":            9923917,
    "STONE_GRAY":            7368816,
    "WATER_BLUE":            4210943,
    "OAK_TAN":               9402184,
    "OFF_WHITE":            16776437,
    "ORANGE":               14188339,
    "MAGENTA":              11685080,
    "LIGHT_BLUE":            6724056,
    "YELLOW":               15066419,
    "LIME":                  8375321,
    "PINK":                 15892389,
    "GRAY":                  5000268,
    "LIGHT_GRAY":           10066329,
    "CYAN":                  5013401,
    "PURPLE":                8339378,
    "BLUE":                  3361970,
    "BROWN":                 6704179,
    "GREEN":                 6717235,
    "RED":                  10040115,
    "BLACK":                 1644825,
    "GOLD":                 16445005,
    "DIAMOND_BLUE":          6085589,
    "LAPIS_BLUE":            4882687,
    "EMERALD_GREEN":           55610,
    "SPRUCE_BROWN":          8476209,
    "DARK_RED":              7340544,
    "TERRACOTTA_WHITE":     13742497,
    "TERRACOTTA_ORANGE":    10441252,
    "TERRACOTTA_MAGENTA":    9787244,
    "TERRACOTTA_LIGHT_BLUE": 7367818,
    "TERRACOTTA_YELLOW":    12223780,
    "TERRACOTTA_LIME":       6780213,
    "TERRACOTTA_PINK":      10505550,
    "TERRACOTTA_GRAY":       3746083,
    "TERRACOTTA_LIGHT_GRAY": 8874850,
    "TERRACOTTA_CYAN":       5725276,
    "TERRACOTTA_PURPLE":     8014168,
    "TERRACOTTA_BLUE":       4996700,
    "TERRACOTTA_BROWN":      4993571,
    "TERRACOTTA_GREEN":      5001770,
    "TERRACOTTA_RED":        9321518,
    "TERRACOTTA_BLACK":      2430480,
    "DULL_RED":             12398641,
    "DULL_PINK":             9715553,
    "DARK_CRIMSON":          6035741,
    "TEAL":                  1474182,
    "DARK_AQUA":             3837580,
    "DARK_DULL_PINK":        5647422,
    "BRIGHT_TEAL":           1356933,
    "DEEPSLATE_GRAY":        6579300,
    "RAW_IRON_PINK":        14200723,
    "LICHEN_GREEN":          8365974,
}

MAP_COLORS = {name: _rgb_from_int(c) for name, c in _MAP_COLOR_INTS.items()}


_MAP_COLOR_NAMES = list(MAP_COLORS.keys())
_MAP_COLOR_RGB = np.array([MAP_COLORS[n] for n in _MAP_COLOR_NAMES], dtype=np.float32)


def nearest_map_color(rgb):
    """Return the MapColor name closest to (r, g, b) by redmean distance.

    Redmean is a low-cost perceptual approximation of color distance — better than
    plain RGB Euclidean and doesn't require a CIELAB conversion. See
    https://www.compuphase.com/cmetric.htm.
    """
    target = np.array(rgb, dtype=np.float32)
    mean_r = (_MAP_COLOR_RGB[:, 0] + target[0]) * 0.5
    diffs = _MAP_COLOR_RGB - target
    dr2 = diffs[:, 0] ** 2
    dg2 = diffs[:, 1] ** 2
    db2 = diffs[:, 2] ** 2
    dist2 = (2.0 + mean_r / 256.0) * dr2 + 4.0 * dg2 + (2.0 + (255.0 - mean_r) / 256.0) * db2
    return _MAP_COLOR_NAMES[int(np.argmin(dist2))]


def texture_path_to_file(rel_path):
    """Resolve a definition texture reference (e.g. 'pebble/iron_islands/wet1') to a PNG file path."""
    if rel_path is None:
        return None
    rel = rel_path.replace("\\", "/").lstrip("/")
    # Definitions sometimes prefix with 'block/' explicitly, sometimes don't.
    if rel.startswith("block/"):
        rel = rel[len("block/"):]
    candidate = os.path.join(TEXTURES_ROOT, *rel.split("/")) + ".png"
    if os.path.isfile(candidate):
        return candidate
    return None


def average_rgb(image_path):
    """Average non-transparent pixel RGB. Returns (r, g, b) ints or None if no valid pixels."""
    try:
        with Image.open(image_path) as img:
            rgba = np.asarray(img.convert("RGBA"))
    except Exception:
        return None
    flat = rgba.reshape(-1, 4)
    mask = flat[:, 3] > 16  # ignore mostly-transparent pixels
    if not mask.any():
        return None
    pixels = flat[mask, :3].astype(np.float64)
    r, g, b = pixels.mean(axis=0)
    return (int(round(r)), int(round(g)), int(round(b)))


# Texture key priorities for top-down map representation.
TOP_KEYS = ("up", "top")
ALL_KEYS = ("all",)
SIDE_KEYS = ("side", "sides", "north", "south", "east", "west")
BOTTOM_KEYS = ("down", "bottom")


def collect_texture_refs_from_map(textures_map):
    """Pick the most representative texture refs from a {face: path} map. Returns a list of refs."""
    if not textures_map:
        return []
    # Prefer top, fall back to all, then sides, then anything.
    for keys in (TOP_KEYS, ALL_KEYS, SIDE_KEYS):
        hits = [textures_map[k] for k in keys if k in textures_map and textures_map[k]]
        if hits:
            return hits
    # Anything except bottom — bottom is rarely visible on a map.
    other = [v for k, v in textures_map.items() if k not in BOTTOM_KEYS and v]
    return other or list(textures_map.values())


SENTINEL_REFS = {"transparent", "none", ""}


def _filter_real(refs):
    """Drop sentinel/empty values from a list of texture refs."""
    return [r for r in refs if isinstance(r, str) and r.lower() not in SENTINEL_REFS]


def _refs_from_random_textures(random_textures):
    """A randomTextures list can hold entries with .textures as either a dict (face map) or
    a list (per-face array). Sample the first variant only — variants are alternates of the
    same block."""
    if not isinstance(random_textures, list):
        return []
    for entry in random_textures:
        if not isinstance(entry, dict):
            continue
        tex = entry.get("textures")
        picked = []
        if isinstance(tex, dict):
            picked = collect_texture_refs_from_map(tex)
        elif isinstance(tex, list):
            picked = [t for t in tex if isinstance(t, str)]
        picked = _filter_real(picked)
        if picked:
            return picked
    return []


def collect_texture_refs(definition):
    """Pull the most representative texture refs from a block_definitions or block_set_definitions JSON."""
    refs = []

    textures = definition.get("textures")
    if isinstance(textures, dict):
        refs.extend(collect_texture_refs_from_map(textures))
    elif isinstance(textures, list) and textures:
        refs.extend([t for t in textures if isinstance(t, str)])

    if not refs:
        refs.extend(_refs_from_random_textures(definition.get("randomTextures")))

    if not refs:
        # cuboid-nsew-stack and similar use a "stack" array, each entry has its own textures.
        stack = definition.get("stack")
        if isinstance(stack, list):
            for entry in stack:
                if isinstance(entry, dict):
                    tex = entry.get("textures")
                    if isinstance(tex, list):
                        refs.extend([t for t in tex if isinstance(t, str)])
                    elif isinstance(tex, dict):
                        refs.extend(collect_texture_refs_from_map(tex))

    if not refs:
        # States can carry their own textures or randomTextures.
        states = definition.get("states") or []
        for state in states:
            if not isinstance(state, dict):
                continue
            tex = state.get("textures")
            if isinstance(tex, dict):
                refs.extend(collect_texture_refs_from_map(tex))
            elif isinstance(tex, list):
                refs.extend([t for t in tex if isinstance(t, str)])
            if not refs:
                refs.extend(_refs_from_random_textures(state.get("randomTextures")))
            if refs:
                break

    return _filter_real(refs)


def average_definition_color(definition):
    """Sample textures referenced by the definition and return (r, g, b) and chosen ref list."""
    refs = collect_texture_refs(definition)
    if not refs:
        return None, []
    sums = np.zeros(3, dtype=np.float64)
    weights = 0
    used = []
    for ref in refs:
        path = texture_path_to_file(ref)
        if path is None:
            continue
        avg = average_rgb(path)
        if avg is None:
            continue
        sums += np.array(avg, dtype=np.float64)
        weights += 1
        used.append(ref)
    if weights == 0:
        return None, refs
    avg = tuple(int(round(v / weights)) for v in sums)
    return avg, used


def load_json_ordered(path):
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f, object_pairs_hook=OrderedDict)


def save_json_preserve(path, data):
    with open(path, "w", encoding="utf-8", newline="\n") as f:
        json.dump(data, f, indent=4, ensure_ascii=False)
        f.write("\n")


def insert_map_color(definition, map_color_name):
    """Insert mapColor into the definition near other color/render properties."""
    if "mapColor" in definition:
        definition["mapColor"] = map_color_name
        return
    # Build a new ordered dict preserving order, inserting mapColor after a sensible neighbor.
    preferred_after = ("colorMults", "colorMult", "renderLayer", "creativeTab", "soundGroup")
    new_def = OrderedDict()
    inserted = False
    for key, value in definition.items():
        new_def[key] = value
        if not inserted and key in preferred_after:
            new_def["mapColor"] = map_color_name
            inserted = True
    if not inserted:
        new_def["mapColor"] = map_color_name
    definition.clear()
    definition.update(new_def)


# Colormap-tinted blocks render with a biome- or texture-driven color overlay applied to a
# grayscale (or near-grayscale) base texture. Sampling the base texture gives a misleading
# answer (e.g. gray for grass), so when the colormap is dramatic-enough to dominate the final
# look we override the sampled value with a sensible MapColor.
COLORMAP_OVERRIDES = {
    "textures/colormap/grass":          "PALE_GREEN",
    "textures/colormap/foliage_oak":    "DARK_GREEN",
    "textures/colormap/foliage_jungle": "DARK_GREEN",
    "textures/colormap/foliage_palm":   "DARK_GREEN",
    "textures/colormap/foliage_vine":   "DARK_GREEN",
    "textures/colormap/pine":           "DARK_GREEN",
    "textures/colormap/birch":          "PALE_GREEN",
}


def _colormap_override_for(definition):
    """If the definition's colorMult points at a colormap that drastically recolors the base
    texture (grass/foliage/pine/birch), return the MapColor name to use; otherwise None.
    Subtle stone/sand colormaps don't qualify — their base textures are already representative."""
    cm = definition.get("colorMult")
    if isinstance(cm, str) and cm in COLORMAP_OVERRIDES:
        return COLORMAP_OVERRIDES[cm]
    cms = definition.get("colorMults")
    if isinstance(cms, list):
        for entry in cms:
            if isinstance(entry, str) and entry in COLORMAP_OVERRIDES:
                return COLORMAP_OVERRIDES[entry]
    return None


# When all texture refs are vanilla-namespaced (`minecraft:...`) we can't resolve them on
# disk, so sampling fails. For grass/foliage textures we still know what color they render
# at — Minecraft's built-in foliage colorizer makes them green at runtime regardless.
VANILLA_PATH_HINTS = (
    ("minecraft:block/grass/",     "PALE_GREEN"),
    ("minecraft:block/foliage",    "DARK_GREEN"),
    ("minecraft:block/leaves/",    "DARK_GREEN"),
    ("minecraft:block/oak_leaves", "DARK_GREEN"),
    ("minecraft:block/sugar_cane", "PALE_GREEN"),
    ("minecraft:block/fern",       "DARK_GREEN"),
    ("minecraft:block/seagrass",   "DARK_GREEN"),
    ("minecraft:block/kelp",       "DARK_GREEN"),
)


def _vanilla_texture_hint(refs):
    """If every texture ref is minecraft-namespaced and any matches a known foliage hint,
    return the override MapColor name."""
    if not refs:
        return None
    if not all(isinstance(r, str) and r.startswith("minecraft:") for r in refs):
        return None
    for ref in refs:
        for prefix, color in VANILLA_PATH_HINTS:
            if ref.startswith(prefix):
                return color
    return None


def process_definition(definition, force):
    """Returns (status, sample_tuple, skip_info).
    status: 'updated', 'skipped_existing', 'skipped_no_texture'.
    skip_info: for skipped-no-texture, a (label, refs) tuple describing what we tried.
    """
    existing = definition.get("mapColor") if isinstance(definition, dict) else None
    if existing and not force:
        return "skipped_existing", None, None

    label = definition.get("blockName") or definition.get("baseBlockName") or "?"

    # Colormap-driven override wins over sampling — base textures for these blocks are
    # grayscale by design and would mis-sample as gray when they actually render green.
    override = _colormap_override_for(definition)
    if override is not None:
        avg, used_refs = average_definition_color(definition)
        return "updated", (label, used_refs[:2] if used_refs else [], avg or (0, 0, 0), override, existing), None

    avg, used_refs = average_definition_color(definition)
    if avg is None:
        # Fall back to vanilla-texture-path hints before giving up — handles grass plants
        # whose only texture refs are minecraft:block/grass/* etc.
        refs_for_hint = collect_texture_refs(definition)
        hint = _vanilla_texture_hint(refs_for_hint)
        if hint is not None:
            return "updated", (label, refs_for_hint[:2], (0, 0, 0), hint, existing), None
        return "skipped_no_texture", None, (label, used_refs)

    name = nearest_map_color(avg)
    return "updated", (label, used_refs[:2], avg, name, existing), None


def process_directory(directory, force, write):
    updated = 0
    skipped_existing = 0
    skipped_no_texture = 0
    samples = []
    no_texture_reports = []

    files = sorted(f for f in os.listdir(directory) if f.endswith(".json"))
    for fname in files:
        path = os.path.join(directory, fname)
        try:
            data = load_json_ordered(path)
        except json.JSONDecodeError as e:
            print(f"  [skip] {fname}: invalid JSON ({e})", file=sys.stderr)
            continue

        # Two shapes:
        #   block_set_definitions/*.json  -> single object
        #   block_definitions/*.json      -> array of objects
        if isinstance(data, dict):
            entries = [data]
            is_array = False
        elif isinstance(data, list):
            entries = data
            is_array = True
        else:
            continue

        file_changed = False
        for entry in entries:
            if not isinstance(entry, dict):
                continue
            status, sample, skip_info = process_definition(entry, force)
            if status == "skipped_existing":
                skipped_existing += 1
                continue
            if status == "skipped_no_texture":
                skipped_no_texture += 1
                no_texture_reports.append((fname, skip_info[0], skip_info[1]))
                continue
            # updated
            label, refs, avg, name, existing = sample
            samples.append((fname, label, refs, avg, name, existing))
            insert_map_color(entry, name)
            file_changed = True
            updated += 1

        if write and file_changed:
            save_json_preserve(path, data if is_array else entries[0])

    return updated, skipped_existing, skipped_no_texture, samples, no_texture_reports


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--write", action="store_true", help="Actually modify JSON files. Without this flag, runs as a preview.")
    parser.add_argument("--force", action="store_true", help="Overwrite existing mapColor values. Default skips them.")
    parser.add_argument("--limit", type=int, default=20, help="How many sample lines to show in the preview output (default 20).")
    parser.add_argument("--only", choices=["sets", "individual"], help="Only process one of the two definition directories.")
    parser.add_argument("--show-skipped", action="store_true", help="Print details of definitions skipped because no texture file resolved.")
    args = parser.parse_args()

    if not os.path.isdir(BLOCK_DEFS_DIR) or not os.path.isdir(BLOCK_SET_DEFS_DIR):
        print(f"Definition directories not found under {DEFINITIONS_DIR}", file=sys.stderr)
        sys.exit(1)

    targets = []
    if args.only != "individual":
        targets.append(("block_set_definitions", BLOCK_SET_DEFS_DIR))
    if args.only != "sets":
        targets.append(("block_definitions", BLOCK_DEFS_DIR))

    grand_updated = 0
    grand_skipped_existing = 0
    grand_skipped_no_texture = 0

    for dir_label, directory in targets:
        print(f"\n=== {dir_label} ===")
        updated, skipped_existing, skipped_no_texture, samples, no_texture_reports = process_directory(
            directory, force=args.force, write=args.write
        )
        grand_updated += updated
        grand_skipped_existing += skipped_existing
        grand_skipped_no_texture += skipped_no_texture

        for fname, label, refs, avg, name, existing in samples[: args.limit]:
            tag = "OVERWRITE" if existing and args.force else "ADD     "
            ref_str = ", ".join(refs) if refs else "(none)"
            extra = f"  was={existing}" if existing else ""
            print(f"  [{tag}] {fname:<35} {label:<40} avg={avg} -> {name}  texs=[{ref_str}]{extra}")
        if len(samples) > args.limit:
            print(f"  ... and {len(samples) - args.limit} more")

        if no_texture_reports and args.show_skipped:
            print(f"\n  --- skipped (no texture resolved) — first {args.limit} of {len(no_texture_reports)} ---")
            for fname, label, refs in no_texture_reports[: args.limit]:
                refs_str = ", ".join(refs) if refs else "(no refs in def)"
                print(f"  [SKIP    ] {fname:<35} {label:<40} tried=[{refs_str}]")

        print(f"  updated={updated}  skipped_existing_mapColor={skipped_existing}  skipped_no_texture_found={skipped_no_texture}")

    action = "wrote" if args.write else "would write"
    print(f"\nTotal: {action} mapColor for {grand_updated} definitions")
    print(f"       skipped {grand_skipped_existing} with existing mapColor (use --force to overwrite)")
    print(f"       skipped {grand_skipped_no_texture} with no resolvable texture")
    if not args.write:
        print("\nDry-run only. Re-run with --write to apply changes.")


if __name__ == "__main__":
    main()
