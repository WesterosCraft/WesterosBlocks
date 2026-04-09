#!/usr/bin/env python3
"""
Audit WesterosBlocks resources to find unused files.

Parses all block definitions, block set definitions, custom models, OptiFine CTM,
particles, and colormaps to build a complete set of referenced resources, then
cross-references against actual files on disk.

Usage:
    python3 scripts/audit_resources.py                    # Full report (markdown)
    python3 scripts/audit_resources.py --json             # JSON output
    python3 scripts/audit_resources.py --category textures  # Single category
"""

import argparse
import json
import os
import shutil
import sys
from pathlib import Path


# ---------------------------------------------------------------------------
# Constants — must match BlockSetExpander.java lines 53-78
# ---------------------------------------------------------------------------

VARIANT_TEXTURES = {
    "solid":               ["bottom", "top", "west", "east", "south", "north"],
    "stairs":              ["bottom", "top", "sides"],
    "slab":                ["bottom", "top", "sides"],
    "wall":                ["bottom", "top", "sides"],
    "fence":               ["bottom", "top", "sides"],
    "hopper":              ["sides"],
    "tip":                 ["sides"],
    "carpet":              ["sides"],
    "fence_gate":          ["sides"],
    "half_door":           ["sides"],
    "cover":               ["cover"],
    "hollow_hopper":       ["sides"],
    "log":                 ["bottom", "top", "sides"],
    "directional":         ["bottom", "top", "west", "east", "south", "north"],
    "layer":               ["sides"],
    "pane":                ["sides", "top"],
    "sand":                ["bottom", "top", "west", "east", "south", "north"],
    "path":                ["sides"],
    "window_frame":        ["window-topbottom", "window-topbottom", "window-frame"],
    "window_frame_mullion":["window-topbottom", "window-topbottom", "window-frame-mullion"],
    "arrow_slit":          ["window-topbottom", "window-topbottom", "arrow-slit"],
    "arrow_slit_window":   ["window-topbottom", "window-topbottom", "arrow-slit-window"],
    "arrow_slit_ornate":   ["window-topbottom", "window-topbottom", "arrow-slit-ornate"],
    "bench":               ["sides"],
}


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

def preprocess_texture_map(tex_map):
    """Replicate BlockSetExpander.preprocessTextureMap() logic."""
    if not tex_map:
        return {}
    p = dict(tex_map)
    if "all" in p:
        for k in ("bottom", "top", "sides"):
            p.setdefault(k, p["all"])
    if "sides" in p:
        for k in ("west", "east", "south", "north"):
            p.setdefault(k, p["sides"])
    if "sides" not in p and "bottom" in p:
        p["sides"] = p["bottom"]
    p.setdefault("window-topbottom", "transparent")
    if "sides" in p:
        p.setdefault("cover", p["sides"])
    return p


def get_textures_for_variant(tex_map, variant):
    """Get texture list for a specific variant from processed texture map."""
    if not tex_map or variant not in VARIANT_TEXTURES:
        return []
    return [tex_map[k] for k in VARIANT_TEXTURES[variant] if k in tex_map]


def preprocess_variant_map(variant_map):
    """Expand comma-separated keys like 'stairs,slab,wall' into individual entries."""
    if not variant_map:
        return {}
    result = {}
    for key, value in variant_map.items():
        if "," in key:
            for k in key.split(","):
                result[k.strip()] = value
        else:
            result[key] = value
    return result


def pick_variant_textures(textures_map, alt_textures, variant):
    """Pick textures for a variant, checking alt overrides first."""
    alt = preprocess_variant_map(alt_textures) if alt_textures else {}
    if variant in alt:
        return alt[variant]
    return get_textures_for_variant(preprocess_texture_map(textures_map), variant)


def walk_files(directory, extension):
    """Walk directory tree once and collect files matching extension. Fast on WSL."""
    results = set()
    base = str(directory)
    if not os.path.isdir(base):
        return results
    for dirpath, _, filenames in os.walk(base):
        for fn in filenames:
            if fn.endswith(extension):
                rel = os.path.relpath(os.path.join(dirpath, fn), base)
                results.add(rel.replace("\\", "/"))
    return results


def strip_wb_block(identifier):
    """'westerosblocks:block/foo/bar' -> 'foo/bar'"""
    s = identifier
    if ":" in s:
        s = s.split(":", 1)[1]
    if s.startswith("block/"):
        s = s[6:]
    return s


def strip_mc_block(identifier):
    """'minecraft:block/foo/bar' -> 'foo/bar'"""
    return strip_wb_block(identifier)


# ---------------------------------------------------------------------------
# File System Index — single-pass walk to avoid repeated rglobs
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Vanilla Resource Pack Index
# ---------------------------------------------------------------------------

class VanillaIndex:
    """Index the vanilla Minecraft resource pack for cross-referencing."""

    def __init__(self, vanilla_dir):
        self.block_textures = set()   # relative, no ext (e.g., "stone")
        self.particle_textures = set()
        self.block_models = set()
        self.particles = set()
        self._build(vanilla_dir)

    def _build(self, base):
        for subdir, target_set, ext in [
            ("textures/block", self.block_textures, ".png"),
            ("textures/particle", self.particle_textures, ".png"),
            ("models/block", self.block_models, ".json"),
            ("particles", self.particles, ".json"),
        ]:
            full = os.path.join(base, subdir)
            if not os.path.isdir(full):
                continue
            for dirpath, _, filenames in os.walk(full):
                for fn in filenames:
                    if fn.endswith(ext):
                        rel = os.path.relpath(
                            os.path.join(dirpath, fn), full
                        ).replace("\\", "/")
                        target_set.add(rel[:-len(ext)])


# ---------------------------------------------------------------------------
# File System Index — single-pass walk to avoid repeated rglobs
# ---------------------------------------------------------------------------

class FileIndex:
    """Pre-index all files under resources/ in a single os.walk pass."""

    def __init__(self, resources_dir):
        self.resources = str(resources_dir)
        # Indexed sets (relative paths with forward slashes)
        self.wb_block_pngs = set()       # relative to textures/block/, no ext
        self.wb_item_pngs = set()        # relative to textures/item/, no ext
        self.wb_particle_pngs = set()    # relative to textures/particle/, no ext
        self.mc_block_pngs = set()       # relative to minecraft/textures/block/, no ext
        self.mc_particle_pngs = set()    # relative to minecraft/textures/particle/, no ext
        self.custom_model_dirs = set()   # directory names under models/block/custom/
        self.mc_model_jsons = set()      # relative to minecraft/models/block/, no ext
        self.all_mcmeta = []             # (rel_path, expected_png_path) tuples
        self.colormap_pngs = set()       # filenames without ext
        self.wb_ctm_pngs = set()         # full paths relative to resources
        self.mc_ctm_pngs = set()         # full paths relative to resources
        self.wb_ctm_properties = []      # (dirpath, filename) for .properties
        self.mc_ctm_properties = []      # (dirpath, filename) for .properties
        self.wb_continuity_properties = []  # (dirpath, filename) for .properties in textures/block/
        self.wb_entity_pngs = set()      # relative to textures/entity/, no ext

        self._build_index()

    def _build_index(self):
        res = self.resources
        for dirpath, dirnames, filenames in os.walk(res):
            rel_dir = os.path.relpath(dirpath, res).replace("\\", "/")

            for fn in filenames:
                rel_file = f"{rel_dir}/{fn}" if rel_dir != "." else fn

                # WB block textures
                prefix = "assets/westerosblocks/textures/block/"
                if rel_file.startswith(prefix) and fn.endswith(".png"):
                    self.wb_block_pngs.add(rel_file[len(prefix):-4])

                # WB item textures
                prefix = "assets/westerosblocks/textures/item/"
                if rel_file.startswith(prefix) and fn.endswith(".png"):
                    self.wb_item_pngs.add(rel_file[len(prefix):-4])

                # WB particle textures
                prefix = "assets/westerosblocks/textures/particle/"
                if rel_file.startswith(prefix) and fn.endswith(".png"):
                    self.wb_particle_pngs.add(rel_file[len(prefix):-4])

                # MC block textures
                prefix = "assets/minecraft/textures/block/"
                if rel_file.startswith(prefix) and fn.endswith(".png"):
                    self.mc_block_pngs.add(rel_file[len(prefix):-4])

                # MC particle textures
                prefix = "assets/minecraft/textures/particle/"
                if rel_file.startswith(prefix) and fn.endswith(".png"):
                    self.mc_particle_pngs.add(rel_file[len(prefix):-4])

                # MC model overrides
                prefix = "assets/minecraft/models/block/"
                if rel_file.startswith(prefix) and fn.endswith(".json"):
                    self.mc_model_jsons.add(rel_file[len(prefix):-5])

                # Polytone colormaps
                prefix = "assets/westerosblocks/polytone/colormaps/"
                if rel_file.startswith(prefix) and fn.endswith(".png"):
                    self.colormap_pngs.add(fn[:-4])

                # MCMETA files
                if fn.endswith(".png.mcmeta"):
                    png_path = os.path.join(dirpath, fn[:-7])  # strip .mcmeta
                    self.all_mcmeta.append((rel_file, png_path))

                # WB entity textures
                prefix = "assets/westerosblocks/textures/entity/"
                if rel_file.startswith(prefix) and fn.endswith(".png"):
                    self.wb_entity_pngs.add(rel_file[len(prefix):-4])

                # OptiFine CTM PNGs and properties
                if "optifine/ctm" in rel_dir:
                    if fn.endswith(".png"):
                        if rel_file.startswith("assets/westerosblocks/"):
                            self.wb_ctm_pngs.add(os.path.join(dirpath, fn))
                        elif rel_file.startswith("assets/minecraft/"):
                            self.mc_ctm_pngs.add(os.path.join(dirpath, fn))
                    elif fn.endswith(".properties"):
                        if rel_file.startswith("assets/westerosblocks/"):
                            self.wb_ctm_properties.append((dirpath, fn))
                        elif rel_file.startswith("assets/minecraft/"):
                            self.mc_ctm_properties.append((dirpath, fn))

                # Continuity CTM .properties in textures/block/
                prefix = "assets/westerosblocks/textures/block/"
                if rel_file.startswith(prefix) and fn.endswith(".properties"):
                    self.wb_continuity_properties.append((dirpath, fn))

            # Custom model directories
            if rel_dir == "assets/westerosblocks/models/block/custom":
                for d in dirnames:
                    self.custom_model_dirs.add(d)


# ---------------------------------------------------------------------------
# Reference Collector
# ---------------------------------------------------------------------------

class ReferenceCollector:
    def __init__(self, project_root, file_index):
        self.root = project_root
        self.resources = os.path.join(project_root, "src", "main", "resources")
        self.defs_dir = os.path.join(self.resources, "definitions")
        self.wb_assets = os.path.join(self.resources, "assets", "westerosblocks")
        self.mc_assets = os.path.join(self.resources, "assets", "minecraft")
        self.index = file_index

        # Referenced texture paths
        self.wb_block_textures = set()
        self.wb_item_textures = set()
        self.wb_entity_textures = set()
        self.mc_block_textures = set()
        self.mc_particle_textures = set()
        self.wb_particle_textures = set()
        self.colormap_names = set()
        self.custom_model_names = set()
        self.all_block_names = set()
        self.ctm_referenced_pngs = set()  # absolute paths

    def collect_all(self):
        self._collect_block_definitions()
        self._collect_block_set_definitions()
        self._collect_color_maps()
        self._collect_all_model_refs()
        self._collect_mc_blockstate_refs()
        self._collect_mc_model_refs()
        self._collect_optifine_refs()
        self._collect_continuity_refs()
        self._collect_particle_refs()
        self._collect_entity_refs()
        # Must run last — depends on wb_block_textures being fully populated
        self._collect_continuity_companions()

    # -- Block Definitions --------------------------------------------------

    def _collect_block_definitions(self):
        defs_path = os.path.join(self.defs_dir, "block_definitions")
        if not os.path.isdir(defs_path):
            return
        for fn in sorted(os.listdir(defs_path)):
            if not fn.endswith(".json"):
                continue
            fpath = os.path.join(defs_path, fn)
            try:
                with open(fpath) as f:
                    data = json.load(f)
            except (json.JSONDecodeError, OSError) as e:
                print(f"WARNING: {fpath}: {e}", file=sys.stderr)
                continue
            entries = data if isinstance(data, list) else [data]
            for d in entries:
                self._process_block_def(d)

    def _process_block_def(self, d):
        if not isinstance(d, dict):
            return
        name = d.get("blockName", "")
        if name:
            self.all_block_names.add(name)
        if d.get("isCustomModel"):
            self.custom_model_names.add(name)

        # textures
        for t in (d.get("textures") or []):
            if isinstance(t, str):
                self.wb_block_textures.add(t)
        # overlayTextures
        for t in (d.get("overlayTextures") or []):
            if isinstance(t, str):
                self.wb_block_textures.add(t)
        # randomTextures
        for rt in (d.get("randomTextures") or []):
            if isinstance(rt, dict):
                for t in (rt.get("textures") or []):
                    if isinstance(t, str):
                        self.wb_block_textures.add(t)
        # states
        for state in (d.get("states") or []):
            if not isinstance(state, dict):
                continue
            if state.get("isCustomModel") and name:
                self.custom_model_names.add(name)
            for t in (state.get("textures") or []):
                if isinstance(t, str):
                    self.wb_block_textures.add(t)
            for t in (state.get("overlayTextures") or []):
                if isinstance(t, str):
                    self.wb_block_textures.add(t)
            for rt in (state.get("randomTextures") or []):
                if isinstance(rt, dict):
                    for t in (rt.get("textures") or []):
                        if isinstance(t, str):
                            self.wb_block_textures.add(t)
            cm = state.get("colorMult", "")
            if isinstance(cm, str) and cm.startswith("textures/colormap/"):
                self.colormap_names.add(cm.rsplit("/", 1)[-1])
        # stack
        for el in (d.get("stack") or []):
            if isinstance(el, dict):
                for t in (el.get("textures") or []):
                    if isinstance(t, str):
                        self.wb_block_textures.add(t)
        # itemTexture
        it = d.get("itemTexture")
        if isinstance(it, str) and it:
            self.wb_block_textures.add(it)
        # customItemTexture
        if d.get("customItemTexture") and name:
            self.wb_item_textures.add(name)
        # colorMult
        cm = d.get("colorMult", "")
        if isinstance(cm, str) and cm.startswith("textures/colormap/"):
            self.colormap_names.add(cm.rsplit("/", 1)[-1])
        # colorMults
        for cm in (d.get("colorMults") or []):
            if isinstance(cm, str) and cm.startswith("textures/colormap/"):
                self.colormap_names.add(cm.rsplit("/", 1)[-1])

    # -- Block Set Definitions ----------------------------------------------

    def _collect_block_set_definitions(self):
        sets_path = os.path.join(self.defs_dir, "block_set_definitions")
        if not os.path.isdir(sets_path):
            return
        for fn in sorted(os.listdir(sets_path)):
            if not fn.endswith(".json"):
                continue
            fpath = os.path.join(sets_path, fn)
            try:
                with open(fpath) as f:
                    data = json.load(f)
            except (json.JSONDecodeError, OSError) as e:
                print(f"WARNING: {fpath}: {e}", file=sys.stderr)
                continue
            if isinstance(data, dict):
                self._process_block_set(data)

    def _process_block_set(self, bs):
        base_name = bs.get("baseBlockName", "")
        variants = bs.get("variants", ["solid", "stairs", "slab", "wall", "fence", "hopper"])
        textures_map = bs.get("textures") or {}
        alt_textures = bs.get("altTextures")
        alt_names = bs.get("altNames") or {}

        cm = bs.get("colorMult", "")
        if isinstance(cm, str) and cm.startswith("textures/colormap/"):
            self.colormap_names.add(cm.rsplit("/", 1)[-1])

        for variant in variants:
            if variant in alt_names:
                block_name = alt_names[variant]
            else:
                suffix = "" if variant == "solid" else f"_{variant}"
                block_name = base_name + suffix
            self.all_block_names.add(block_name)

            tex_list = pick_variant_textures(textures_map, alt_textures, variant)
            for t in tex_list:
                if isinstance(t, str):
                    self.wb_block_textures.add(t)

        # randomTextures
        for rt_entry in (bs.get("randomTextures") or []):
            if not isinstance(rt_entry, dict):
                continue
            rt_map = rt_entry.get("textures") or {}
            if isinstance(rt_map, dict):
                for variant in variants:
                    for t in get_textures_for_variant(preprocess_texture_map(rt_map), variant):
                        if isinstance(t, str):
                            self.wb_block_textures.add(t)

        # overlayTextures
        overlay_map = bs.get("overlayTextures")
        if isinstance(overlay_map, dict):
            for variant in variants:
                for t in get_textures_for_variant(preprocess_texture_map(overlay_map), variant):
                    if isinstance(t, str):
                        self.wb_block_textures.add(t)

        # altRandomTextures (per-variant random texture overrides)
        alt_random = bs.get("altRandomTextures")
        if isinstance(alt_random, dict):
            expanded = preprocess_variant_map(alt_random)
            for variant in variants:
                if variant in expanded:
                    for rt_entry in expanded[variant]:
                        if isinstance(rt_entry, dict):
                            for t in (rt_entry.get("textures") or []):
                                if isinstance(t, str):
                                    self.wb_block_textures.add(t)

        # states
        for state_rec in (bs.get("states") or []):
            if not isinstance(state_rec, dict):
                continue
            state_textures = state_rec.get("textures") or {}
            state_alt_textures = state_rec.get("altTextures")
            state_overlay = state_rec.get("overlayTextures")
            exclude_str = state_rec.get("excludeVariants", "")
            excluded = set(v.strip() for v in exclude_str.split(",")) if exclude_str else set()

            for variant in variants:
                if variant in excluded:
                    continue
                if isinstance(state_textures, dict):
                    for t in pick_variant_textures(state_textures, state_alt_textures, variant):
                        if isinstance(t, str):
                            self.wb_block_textures.add(t)
                if isinstance(state_overlay, dict):
                    for t in get_textures_for_variant(preprocess_texture_map(state_overlay), variant):
                        if isinstance(t, str):
                            self.wb_block_textures.add(t)

            scm = state_rec.get("colorMult", "")
            if isinstance(scm, str) and scm.startswith("textures/colormap/"):
                self.colormap_names.add(scm.rsplit("/", 1)[-1])

    # -- Color Maps ---------------------------------------------------------

    def _collect_color_maps(self):
        cm_file = os.path.join(self.defs_dir, "color_maps.json")
        if not os.path.isfile(cm_file):
            return
        try:
            with open(cm_file) as f:
                data = json.load(f)
        except (json.JSONDecodeError, OSError):
            return
        for entry in (data.get("colorMaps") or []):
            cm = entry.get("colorMult", "")
            if isinstance(cm, str) and cm.startswith("textures/colormap/"):
                self.colormap_names.add(cm.rsplit("/", 1)[-1])

    # -- Model texture refs (all WB model directories) ----------------------

    def _collect_all_model_refs(self):
        for subdir in ("models/block", "models/item"):
            base = os.path.join(self.wb_assets, subdir)
            if not os.path.isdir(base):
                continue
            for dirpath, _, filenames in os.walk(base):
                for fn in filenames:
                    if not fn.endswith(".json"):
                        continue
                    fpath = os.path.join(dirpath, fn)
                    try:
                        with open(fpath) as f:
                            data = json.load(f)
                    except (json.JSONDecodeError, OSError):
                        continue
                    for key, value in (data.get("textures") or {}).items():
                        if not isinstance(value, str) or value.startswith("#"):
                            continue
                        if value.startswith("westerosblocks:block/"):
                            self.wb_block_textures.add(strip_wb_block(value))
                        elif value.startswith("westerosblocks:item/"):
                            self.wb_item_textures.add(value.split(":", 1)[1][5:])
                        elif value.startswith("minecraft:block/"):
                            self.mc_block_textures.add(strip_mc_block(value))

    # -- Minecraft Blockstate Overrides --------------------------------------

    def _collect_mc_blockstate_refs(self):
        bs_dir = os.path.join(self.mc_assets, "blockstates")
        if not os.path.isdir(bs_dir):
            return
        for fn in sorted(os.listdir(bs_dir)):
            if not fn.endswith(".json"):
                continue
            fpath = os.path.join(bs_dir, fn)
            try:
                with open(fpath) as f:
                    data = json.load(f)
            except (json.JSONDecodeError, OSError):
                continue
            self._extract_custom_model_names(data)

    def _extract_custom_model_names(self, data):
        for key, value in (data.get("variants") or {}).items():
            entries = value if isinstance(value, list) else [value]
            for entry in entries:
                model = entry.get("model", "")
                if "block/custom/" in model:
                    parts = model.split("block/custom/", 1)[1].split("/")
                    if parts:
                        self.custom_model_names.add(parts[0])
        for part in (data.get("multipart") or []):
            apply_data = part.get("apply", {})
            entries = apply_data if isinstance(apply_data, list) else [apply_data]
            for entry in entries:
                model = entry.get("model", "")
                if "block/custom/" in model:
                    parts = model.split("block/custom/", 1)[1].split("/")
                    if parts:
                        self.custom_model_names.add(parts[0])

    # -- Minecraft Model Overrides -------------------------------------------

    def _collect_mc_model_refs(self):
        models_dir = os.path.join(self.mc_assets, "models", "block")
        if not os.path.isdir(models_dir):
            return
        for dirpath, _, filenames in os.walk(models_dir):
            for fn in filenames:
                if not fn.endswith(".json"):
                    continue
                fpath = os.path.join(dirpath, fn)
                try:
                    with open(fpath) as f:
                        data = json.load(f)
                except (json.JSONDecodeError, OSError):
                    continue
                for key, value in (data.get("textures") or {}).items():
                    if not isinstance(value, str):
                        continue
                    if value.startswith("minecraft:block/"):
                        self.mc_block_textures.add(strip_mc_block(value))
                    elif value.startswith("westerosblocks:block/"):
                        self.wb_block_textures.add(strip_wb_block(value))

    # -- OptiFine CTM -------------------------------------------------------

    def _collect_optifine_refs(self):
        all_props = self.index.wb_ctm_properties + self.index.mc_ctm_properties
        for dirpath, fn in all_props:
            self._parse_ctm_properties(os.path.join(dirpath, fn), dirpath)

    def _parse_ctm_properties(self, fpath, props_dir):
        try:
            with open(fpath) as f:
                content = f.read()
        except OSError:
            return
        for line in content.splitlines():
            line = line.strip()
            if line.startswith("matchTiles="):
                value = line[11:].strip()
                if value.startswith("westerosblocks:textures/block/"):
                    self.wb_block_textures.add(value[30:])
                elif value.startswith("minecraft:textures/block/"):
                    self.mc_block_textures.add(value[24:])
            elif line.startswith("tiles="):
                for tile in line[6:].strip().split():
                    tile = tile.strip()
                    if tile:
                        png_path = os.path.join(props_dir, tile + ".png")
                        self.ctm_referenced_pngs.add(os.path.normpath(png_path))

    # -- Particles -----------------------------------------------------------

    def _collect_particle_refs(self):
        wb_p = os.path.join(self.wb_assets, "particles")
        if os.path.isdir(wb_p):
            for fn in os.listdir(wb_p):
                if not fn.endswith(".json"):
                    continue
                try:
                    with open(os.path.join(wb_p, fn)) as f:
                        data = json.load(f)
                except (json.JSONDecodeError, OSError):
                    continue
                for tex in (data.get("textures") or []):
                    if isinstance(tex, str) and tex.startswith("westerosblocks:"):
                        self.wb_particle_textures.add(tex[15:])

        mc_p = os.path.join(self.mc_assets, "particles")
        if os.path.isdir(mc_p):
            for fn in os.listdir(mc_p):
                if not fn.endswith(".json"):
                    continue
                try:
                    with open(os.path.join(mc_p, fn)) as f:
                        data = json.load(f)
                except (json.JSONDecodeError, OSError):
                    continue
                for tex in (data.get("textures") or []):
                    if isinstance(tex, str) and tex.startswith("minecraft:"):
                        self.mc_particle_textures.add(tex[10:])

    # -- Continuity CTM in textures/block/ ------------------------------------

    def _collect_continuity_refs(self):
        """Parse .properties files found inside textures/block/ (Continuity CTM format).

        These tile PNGs live under textures/block/ so they show up in wb_block_pngs.
        We must add them to wb_block_textures so they aren't flagged as unused.
        """
        tex_block_dir = os.path.join(
            self.wb_assets, "textures", "block") + os.sep
        for dirpath, fn in self.index.wb_continuity_properties:
            fpath = os.path.join(dirpath, fn)
            self._parse_ctm_properties(fpath, dirpath)
            # Also parse tiles= to add relative paths to wb_block_textures
            try:
                with open(fpath) as f:
                    content = f.read()
            except OSError:
                continue
            for line in content.splitlines():
                line = line.strip()
                if line.startswith("tiles="):
                    for tile in line[6:].strip().split():
                        tile = tile.strip()
                        if tile:
                            abs_png = os.path.normpath(
                                os.path.join(dirpath, tile + ".png"))
                            if abs_png.startswith(tex_block_dir):
                                rel = abs_png[len(tex_block_dir):]
                                rel = rel.replace("\\", "/")
                                if rel.endswith(".png"):
                                    rel = rel[:-4]
                                self.wb_block_textures.add(rel)

    # -- Continuity Companion Files -------------------------------------------

    CONTINUITY_SUFFIXES = [
        "_ctm", "_single", "_2x2", "_3x3", "_both", "_up", "_down",
        "_cutout_3x3", "_cutout_2x2",
    ]

    def _collect_continuity_companions(self):
        """Mark Continuity auto-loaded companion textures as used.

        Handles two patterns:
        1. Suffix companions: 'foo/all' → 'foo/all_ctm.png', 'foo/all_single.png', etc.
        2. Tile directories: 'foo/all' → 'foo/all/0.png', 'foo/all/1.png', etc.
        """
        # Build parent→children index for tile directory lookups
        tile_dir_children = {}
        for on_disk in self.index.wb_block_pngs:
            idx = on_disk.rfind("/")
            if idx > 0:
                parent = on_disk[:idx]
                tile_dir_children.setdefault(parent, []).append(on_disk)

        companions = set()
        for base in self.wb_block_textures:
            # Suffix companions
            for suffix in self.CONTINUITY_SUFFIXES:
                candidate = base + suffix
                if candidate in self.index.wb_block_pngs:
                    companions.add(candidate)
            # Tile directory: any PNG under base/ is an auto-loaded CTM tile
            if base in tile_dir_children:
                companions.update(tile_dir_children[base])
        self.wb_block_textures |= companions

    # -- Entity Textures (hardcoded in Java) ----------------------------------

    def _collect_entity_refs(self):
        """Entity textures referenced by RopeRenderer.java (not in any JSON)."""
        self.wb_entity_textures.add("chain")
        self.wb_entity_textures.add("straight_rope")


# ---------------------------------------------------------------------------
# Audit Helpers
# ---------------------------------------------------------------------------

def _resolve_file_path(resources_dir, rel_path):
    """Resolve a relative unused-file path to an absolute path on disk."""
    if rel_path.startswith("assets/"):
        return os.path.join(resources_dir, rel_path)
    return os.path.join(resources_dir, "assets", "westerosblocks", rel_path)


def _annotate_vanilla(result, unused_keys, vanilla_set, path_prefix, ext):
    """Split unused files into vanilla-matched overrides vs. no-match (stale/renamed).

    Adds 'vanilla_overrides' and 'no_vanilla_match' lists to result dict.
    """
    overrides = []
    no_match = []
    for key in unused_keys:
        rel = f"{path_prefix}{key}{ext}"
        if key in vanilla_set:
            overrides.append(rel)
        else:
            no_match.append(rel)
    result["vanilla_overrides"] = sorted(overrides)
    result["no_vanilla_match"] = sorted(no_match)


def _compute_unused_size(resources_dir, rel_paths):
    """Sum byte sizes of unused files; returns total bytes."""
    total = 0
    for rp in rel_paths:
        fpath = _resolve_file_path(resources_dir, rp)
        try:
            total += os.path.getsize(fpath)
        except OSError:
            pass
    return total


def _fmt_size(nbytes):
    if nbytes >= 1_048_576:
        return f"{nbytes / 1_048_576:.1f} MB"
    if nbytes >= 1024:
        return f"{nbytes / 1024:.1f} KB"
    return f"{nbytes} B"


# ---------------------------------------------------------------------------
# Audit Functions
# ---------------------------------------------------------------------------

def audit_block_textures(collector, index):
    """Unused block textures (westerosblocks)"""
    on_disk = index.wb_block_pngs
    referenced = collector.wb_block_textures
    unused = sorted(on_disk - referenced)
    rel_paths = [f"textures/block/{p}.png" for p in unused]
    return {
        "category": "Block Textures (westerosblocks)",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": rel_paths,
        "unused_size_bytes": _compute_unused_size(collector.resources, rel_paths),
    }


def audit_item_textures(collector, index):
    """Unused item textures (westerosblocks)"""
    on_disk = index.wb_item_pngs
    referenced = collector.wb_item_textures
    unused = sorted(on_disk - referenced)
    rel_paths = [f"textures/item/{p}.png" for p in unused]
    return {
        "category": "Item Textures (westerosblocks)",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": rel_paths,
        "unused_size_bytes": _compute_unused_size(collector.resources, rel_paths),
    }


def audit_entity_textures(collector, index):
    """Unused entity textures (westerosblocks)"""
    on_disk = index.wb_entity_pngs
    referenced = collector.wb_entity_textures
    unused = sorted(on_disk - referenced)
    rel_paths = [f"textures/entity/{p}.png" for p in unused]
    return {
        "category": "Entity Textures (westerosblocks)",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": rel_paths,
        "unused_size_bytes": _compute_unused_size(collector.resources, rel_paths),
    }


def audit_custom_models(collector, index):
    """Unused custom model directories"""
    on_disk = index.custom_model_dirs
    referenced = collector.custom_model_names
    unused = sorted(on_disk - referenced)
    return {
        "category": "Custom Models",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": [f"models/block/custom/{d}/" for d in unused],
        "unused_size_bytes": 0,
    }


def audit_mcmeta_files(collector, index):
    """Orphaned MCMETA files (no corresponding PNG)"""
    orphaned = []
    for rel_path, png_path in index.all_mcmeta:
        if not os.path.isfile(png_path):
            orphaned.append(rel_path)
    return {
        "category": "Orphaned MCMETA Files",
        "total": len(index.all_mcmeta),
        "referenced": len(index.all_mcmeta) - len(orphaned),
        "unused_count": len(orphaned),
        "unused": sorted(orphaned),
        "unused_size_bytes": 0,
    }


def audit_colormaps(collector, index):
    """Unused Polytone colormaps"""
    on_disk = index.colormap_pngs
    referenced = collector.colormap_names
    unused = sorted(on_disk - referenced)
    rel_paths = [f"polytone/colormaps/{n}.png" for n in unused]
    return {
        "category": "Polytone Colormaps",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": rel_paths,
        "unused_size_bytes": _compute_unused_size(collector.resources, rel_paths),
    }


def audit_wb_particles(collector, index):
    """Unused particle textures (westerosblocks)"""
    on_disk = index.wb_particle_pngs
    referenced = collector.wb_particle_textures
    unused = sorted(on_disk - referenced)
    rel_paths = [f"textures/particle/{p}.png" for p in unused]
    return {
        "category": "Particle Textures (westerosblocks)",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": rel_paths,
        "unused_size_bytes": _compute_unused_size(collector.resources, rel_paths),
    }


def audit_mc_particles(collector, index, vanilla=None):
    """Unused particle textures (minecraft)"""
    on_disk = index.mc_particle_pngs
    referenced = collector.mc_particle_textures
    unused = sorted(on_disk - referenced)
    rel_paths = [f"assets/minecraft/textures/particle/{p}.png" for p in unused]
    result = {
        "category": "Particle Textures (minecraft)",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": rel_paths,
        "unused_size_bytes": _compute_unused_size(collector.resources, rel_paths),
    }
    if vanilla:
        _annotate_vanilla(result, unused, vanilla.particle_textures,
                          "assets/minecraft/textures/particle/", ".png")
    return result


def audit_mc_block_textures(collector, index, vanilla=None):
    """Unused minecraft block texture overrides"""
    on_disk = index.mc_block_pngs
    referenced = collector.mc_block_textures
    unused = sorted(on_disk - referenced)
    rel_paths = [f"assets/minecraft/textures/block/{p}.png" for p in unused]
    result = {
        "category": "Block Textures (minecraft overrides)",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": rel_paths,
        "unused_size_bytes": _compute_unused_size(collector.resources, rel_paths),
    }
    if vanilla:
        _annotate_vanilla(result, unused, vanilla.block_textures,
                          "assets/minecraft/textures/block/", ".png")
    return result


def audit_mc_models(collector, index, vanilla=None):
    """Unused minecraft model overrides"""
    on_disk = index.mc_model_jsons
    # Collect model refs from blockstates
    referenced = set()
    bs_dir = os.path.join(collector.mc_assets, "blockstates")
    if os.path.isdir(bs_dir):
        for fn in os.listdir(bs_dir):
            if not fn.endswith(".json"):
                continue
            try:
                with open(os.path.join(bs_dir, fn)) as f:
                    data = json.load(f)
            except (json.JSONDecodeError, OSError):
                continue
            _collect_mc_model_ids(data, referenced)

    # Parent references between models
    models_dir = os.path.join(collector.mc_assets, "models", "block")
    if os.path.isdir(models_dir):
        for dirpath, _, filenames in os.walk(models_dir):
            for mfn in filenames:
                if not mfn.endswith(".json"):
                    continue
                try:
                    with open(os.path.join(dirpath, mfn)) as f:
                        data = json.load(f)
                except (json.JSONDecodeError, OSError):
                    continue
                parent = data.get("parent", "")
                if parent.startswith("minecraft:block/"):
                    referenced.add(parent[16:])

    unused = sorted(on_disk - referenced)
    rel_paths = [f"assets/minecraft/models/block/{m}.json" for m in unused]
    result = {
        "category": "Model Overrides (minecraft)",
        "total": len(on_disk),
        "referenced": len(on_disk) - len(unused),
        "unused_count": len(unused),
        "unused": rel_paths,
        "unused_size_bytes": _compute_unused_size(collector.resources, rel_paths),
    }
    if vanilla:
        _annotate_vanilla(result, unused, vanilla.block_models,
                          "assets/minecraft/models/block/", ".json")
    return result


def _collect_mc_model_ids(blockstate_data, result_set):
    for key, value in (blockstate_data.get("variants") or {}).items():
        entries = value if isinstance(value, list) else [value]
        for entry in entries:
            model = entry.get("model", "")
            if model.startswith("minecraft:block/"):
                result_set.add(model[16:])
    for part in (blockstate_data.get("multipart") or []):
        apply_data = part.get("apply", {})
        entries = apply_data if isinstance(apply_data, list) else [apply_data]
        for entry in entries:
            model = entry.get("model", "")
            if model.startswith("minecraft:block/"):
                result_set.add(model[16:])


def audit_optifine_ctm(collector, index):
    """Orphaned OptiFine CTM PNGs"""
    all_ctm_pngs = index.wb_ctm_pngs | index.mc_ctm_pngs
    referenced = collector.ctm_referenced_pngs

    orphaned = []
    for png_path in sorted(all_ctm_pngs):
        normalized = os.path.normpath(png_path)
        if normalized not in referenced:
            rel = os.path.relpath(png_path, collector.resources).replace("\\", "/")
            orphaned.append(rel)

    size = 0
    for png_path in sorted(all_ctm_pngs):
        normalized = os.path.normpath(png_path)
        if normalized not in referenced:
            try:
                size += os.path.getsize(png_path)
            except OSError:
                pass

    return {
        "category": "OptiFine CTM Orphaned PNGs",
        "total": len(all_ctm_pngs),
        "referenced": len(all_ctm_pngs) - len(orphaned),
        "unused_count": len(orphaned),
        "unused": sorted(orphaned),
        "unused_size_bytes": size,
    }


# ---------------------------------------------------------------------------
# Reporting
# ---------------------------------------------------------------------------

def format_markdown(results):
    lines = ["# WesterosBlocks Resource Audit Report", ""]
    total_unused = sum(r["unused_count"] for r in results)
    total_files = sum(r["total"] for r in results)
    total_size = sum(r.get("unused_size_bytes", 0) for r in results)
    lines.append(f"**Total files scanned:** {total_files}")
    lines.append(f"**Total unused/orphaned:** {total_unused} ({_fmt_size(total_size)})")
    lines.append("")

    for r in results:
        size_str = _fmt_size(r.get("unused_size_bytes", 0))
        lines.append(f"## {r['category']}")
        lines.append(f"Scanned: {r['total']} | Referenced: {r['referenced']} | "
                      f"**Unused: {r['unused_count']}** ({size_str})")
        lines.append("")

        # Vanilla split display
        if "vanilla_overrides" in r:
            vo = r["vanilla_overrides"]
            nm = r["no_vanilla_match"]
            lines.append(f"### Valid vanilla overrides: {len(vo)}")
            lines.append("These override a texture that exists in vanilla 1.21.1 — "
                          "likely intentional.")
            lines.append("")
            if vo:
                shown = vo[:50]
                for f in shown:
                    lines.append(f"- `{f}`")
                remaining = len(vo) - len(shown)
                if remaining > 0:
                    lines.append(f"- ... and {remaining} more")
                lines.append("")

            lines.append(f"### No vanilla match: {len(nm)}")
            lines.append("No matching file in vanilla 1.21.1 — possibly stale, renamed, "
                          "or targeting a removed asset.")
            lines.append("")
            if nm:
                shown = nm[:50]
                for f in shown:
                    lines.append(f"- `{f}`")
                remaining = len(nm) - len(shown)
                if remaining > 0:
                    lines.append(f"- ... and {remaining} more")
                lines.append("")
        elif r["unused"]:
            # Show first 50 per category, with count of remaining
            shown = r["unused"][:50]
            for f in shown:
                lines.append(f"- `{f}`")
            remaining = len(r["unused"]) - len(shown)
            if remaining > 0:
                lines.append(f"- ... and {remaining} more")
            lines.append("")
        else:
            lines.append("All files are referenced.")
            lines.append("")

    return "\n".join(lines)


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

CATEGORIES = {
    "textures":     audit_block_textures,
    "items":        audit_item_textures,
    "entities":     audit_entity_textures,
    "custom":       audit_custom_models,
    "mcmeta":       audit_mcmeta_files,
    "colormaps":    audit_colormaps,
    "particles":    audit_wb_particles,
    "mc-particles": audit_mc_particles,
    "mc-textures":  audit_mc_block_textures,
    "mc-models":    audit_mc_models,
    "optifine":     audit_optifine_ctm,
}


def _move_unused_files(resources_dir, results, dest_dir, confirm=False):
    """Move unused files to a backup directory preserving structure."""
    moved = 0
    skipped = 0
    total_bytes = 0
    for r in results:
        for rel in r["unused"]:
            src = _resolve_file_path(resources_dir, rel)
            if not os.path.isfile(src):
                skipped += 1
                continue
            dst = os.path.join(dest_dir, rel)
            if confirm:
                os.makedirs(os.path.dirname(dst), exist_ok=True)
                total_bytes += os.path.getsize(src)
                shutil.move(src, dst)
                moved += 1
            else:
                total_bytes += os.path.getsize(src)
                moved += 1

    return moved, skipped, total_bytes


def main():
    parser = argparse.ArgumentParser(description="Audit WesterosBlocks for unused resources")
    parser.add_argument("--json", action="store_true", help="Output as JSON")
    parser.add_argument("--category", choices=list(CATEGORIES.keys()),
                        help="Run only a specific audit category")
    parser.add_argument("--root", default=".", help="Project root directory")
    parser.add_argument("--full", action="store_true",
                        help="Show all unused files (no truncation)")
    parser.add_argument("--vanilla", metavar="DIR",
                        help="Path to vanilla 1.21.1 resource pack for MC override analysis")
    parser.add_argument("--move-to", metavar="DIR",
                        help="Move unused files to this backup directory")
    parser.add_argument("--confirm", action="store_true",
                        help="Actually move files (without this, --move-to is dry-run)")
    args = parser.parse_args()

    root = os.path.realpath(args.root)
    defs_check = os.path.join(root, "src", "main", "resources", "definitions")
    if not os.path.isdir(defs_check):
        print(f"ERROR: {root} does not appear to be a WesterosBlocks project root.",
              file=sys.stderr)
        sys.exit(2)

    resources = os.path.join(root, "src", "main", "resources")

    print("Indexing files...", file=sys.stderr)
    index = FileIndex(resources)
    print(f"  WB block textures on disk: {len(index.wb_block_pngs)}", file=sys.stderr)
    print(f"  Custom model dirs: {len(index.custom_model_dirs)}", file=sys.stderr)
    print(f"  CTM properties (optifine): "
          f"{len(index.wb_ctm_properties) + len(index.mc_ctm_properties)}",
          file=sys.stderr)
    print(f"  CTM properties (continuity): {len(index.wb_continuity_properties)}",
          file=sys.stderr)
    print(f"  CTM PNGs: {len(index.wb_ctm_pngs) + len(index.mc_ctm_pngs)}", file=sys.stderr)

    print("Collecting references...", file=sys.stderr)
    collector = ReferenceCollector(root, index)
    collector.collect_all()
    print(f"  WB block texture refs: {len(collector.wb_block_textures)}", file=sys.stderr)
    print(f"  Block names: {len(collector.all_block_names)}", file=sys.stderr)
    print(f"  Custom model name refs: {len(collector.custom_model_names)}", file=sys.stderr)
    print(f"  Colormap refs: {len(collector.colormap_names)}", file=sys.stderr)
    print(f"  CTM tile refs: {len(collector.ctm_referenced_pngs)}", file=sys.stderr)

    # Vanilla index (optional)
    vanilla = None
    if args.vanilla:
        vpath = os.path.realpath(args.vanilla)
        if not os.path.isdir(vpath):
            print(f"ERROR: vanilla path not found: {vpath}", file=sys.stderr)
            sys.exit(2)
        print("Indexing vanilla resource pack...", file=sys.stderr)
        vanilla = VanillaIndex(vpath)
        print(f"  Vanilla block textures: {len(vanilla.block_textures)}", file=sys.stderr)
        print(f"  Vanilla block models: {len(vanilla.block_models)}", file=sys.stderr)
        print(f"  Vanilla particle textures: {len(vanilla.particle_textures)}", file=sys.stderr)

    # MC audit functions that accept vanilla kwarg
    MC_AUDITS = {audit_mc_particles, audit_mc_block_textures, audit_mc_models}

    # Run audits
    if args.category:
        audits = [CATEGORIES[args.category]]
    else:
        audits = list(CATEGORIES.values())

    results = []
    for audit_fn in audits:
        print(f"Auditing: {audit_fn.__doc__}...", file=sys.stderr)
        if audit_fn in MC_AUDITS:
            results.append(audit_fn(collector, index, vanilla=vanilla))
        else:
            results.append(audit_fn(collector, index))

    # Output
    if args.json:
        print(json.dumps(results, indent=2))
    else:
        if args.full:
            for r in results:
                r["_show_all"] = True
        print(format_markdown(results))

    total_unused = sum(r["unused_count"] for r in results)
    total_size = sum(r.get("unused_size_bytes", 0) for r in results)
    print(f"\nDone. {total_unused} unused/orphaned files found ({_fmt_size(total_size)}).",
          file=sys.stderr)

    # Move unused files if requested
    if args.move_to:
        dest = os.path.realpath(args.move_to)
        moved, skipped, nbytes = _move_unused_files(
            resources, results, dest, confirm=args.confirm)
        if args.confirm:
            print(f"\nMoved {moved} files to {dest} ({_fmt_size(nbytes)}).", file=sys.stderr)
            if skipped:
                print(f"  Skipped {skipped} files (not found on disk).", file=sys.stderr)
        else:
            print(f"\n[DRY RUN] Would move {moved} files to {dest} ({_fmt_size(nbytes)}).",
                  file=sys.stderr)
            print("  Pass --confirm to actually move files.", file=sys.stderr)

    sys.exit(1 if total_unused > 0 else 0)


if __name__ == "__main__":
    main()
