#!/usr/bin/env python3
"""One-off migration: merge per-file block definitions into definitions/WesterosBlocks.json.

Reads definitions/block_definitions/*.json (each a JSON array, with a
single-object fallback mirroring the old BlockDefinitionLoader) and
definitions/block_set_definitions/*.json (each a single object), in the same
order the old Java loaders walked them, and writes one sectioned file:

    { "blocks": [...], "blockSets": [...] }

Ordering equivalence: the old loaders sorted full path strings
(Comparator.comparing(Path::toString)). Both directories are flat, so the
shared directory prefix cancels out and sorting by filename is identical —
all filenames are ASCII, and Java String.compareTo and Python sorted() both
compare by code unit. The flatness assertion below guards this assumption.
"""
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
DEFS = ROOT / "src" / "main" / "resources" / "definitions"
BLOCK_DIR = DEFS / "block_definitions"
SET_DIR = DEFS / "block_set_definitions"
OUT = DEFS / "WesterosBlocks.json"


def sorted_json_files(directory: Path) -> list[Path]:
    subdirs = [p for p in directory.rglob("*") if p.is_dir()]
    assert not subdirs, f"unexpected subdirectories in {directory}: {subdirs}"
    return sorted((p for p in directory.iterdir() if p.suffix == ".json"),
                  key=lambda p: p.name)


def main() -> int:
    blocks = []
    for f in sorted_json_files(BLOCK_DIR):
        data = json.loads(f.read_text(encoding="utf-8"))
        entries = data if isinstance(data, list) else [data]
        blocks.extend(entries)

    block_sets = []
    for f in sorted_json_files(SET_DIR):
        data = json.loads(f.read_text(encoding="utf-8"))
        assert isinstance(data, dict), f"{f} is not a single object"
        block_sets.append(data)

    # Surface duplicates that were previously spread across files
    # (loader semantics: last content wins, first position kept).
    seen = set()
    for b in blocks:
        name = b.get("blockName")
        if name in seen:
            print(f"NOTE: duplicate blockName '{name}'")
        seen.add(name)
    seen_sets = set()
    for s in block_sets:
        name = s.get("baseBlockName")
        if name in seen_sets:
            print(f"NOTE: duplicate baseBlockName '{name}'")
        seen_sets.add(name)

    OUT.write_text(
        json.dumps({"blocks": blocks, "blockSets": block_sets},
                   indent=4, ensure_ascii=False) + "\n",
        encoding="utf-8")

    # Self-check: order and counts survive a round trip.
    rt = json.loads(OUT.read_text(encoding="utf-8"))
    assert [b.get("blockName") for b in rt["blocks"]] == \
           [b.get("blockName") for b in blocks]
    assert [s.get("baseBlockName") for s in rt["blockSets"]] == \
           [s.get("baseBlockName") for s in block_sets]
    print(f"Wrote {OUT}: {len(blocks)} blocks, {len(block_sets)} block sets")
    return 0


if __name__ == "__main__":
    sys.exit(main())
