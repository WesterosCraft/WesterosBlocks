#!/usr/bin/env python3
"""
Validate JSON schemas against Java source code.

Checks that:
  1. All block types in ModBlocks.FACTORIES are in the schema blockType enum
  2. No stale types in the schema enum that aren't registered in code
  3. All block types in ModModelProvider.EXPORTERS are in the schema enum
  4. Every block type that reads options has a conditional schema (if/then)
  5. Each conditional schema allows exactly the options that the block type reads

Usage:
    python3 scripts/validate_schemas.py           # Run all checks
    python3 scripts/validate_schemas.py --verbose  # Show passing checks too
"""

import argparse
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
JAVA_SRC = ROOT / "src" / "main" / "java" / "com" / "westerosblocks"

SCHEMA_PATH = ROOT / "schemas" / "block-definition.schema.json"
MOD_BLOCKS_PATH = JAVA_SRC / "block" / "ModBlocks.java"
MODEL_PROVIDER_PATH = JAVA_SRC / "datagen" / "providers" / "ModModelProvider.java"
OPTIONS_PROPS_PATH = JAVA_SRC / "data" / "OptionsProperties.java"
BLOCK_DEF_PATH = JAVA_SRC / "data" / "BlockDefinition.java"
BLOCK_CUSTOM_DIR = JAVA_SRC / "block" / "custom"
DATAGEN_CUSTOM_DIR = JAVA_SRC / "datagen" / "custom"


# ---------------------------------------------------------------------------
# Parsing helpers
# ---------------------------------------------------------------------------

def extract_factory_entries(java_text: str) -> dict[str, str]:
    """Extract Map.entry("key", new WCFooBlock.Factory()) pairs, returning {key: class_name}."""
    entries = {}
    for m in re.finditer(r'Map\.entry\("([^"]+)",\s*new\s+(\w+)\.Factory\(\)\)', java_text):
        entries[m.group(1)] = m.group(2)
    return entries


def extract_exporter_entries(java_text: str) -> dict[str, str]:
    """Extract Map.entry("key", FooExporter::method) pairs, returning {key: class_name}."""
    entries = {}
    for m in re.finditer(r'Map\.entry\("([^"]+)",\s*(\w+)::\w+\)', java_text):
        entries[m.group(1)] = m.group(2)
    return entries


def extract_options_fields(java_text: str) -> list[str]:
    """Extract @SerializedName field names from OptionsProperties.java."""
    return re.findall(r'@SerializedName\("(\w+)"\)', java_text)


def extract_option_reads_from_block(java_text: str) -> set[str]:
    """Find options read by a block class via BlockDefinition helper methods or direct OptionsProperties access."""
    options = set()
    # Map BlockDefinition helper methods to their option names
    helper_to_option = {
        "toggleOnUse": "toggleOnUse",
        "isConnectState": "connectstate",
        "isSymmetrical": "symmetrical",
        "isAllowUnsupported": "allowUnsupported",
        "isLocked": "locked",
        "isLayerSensitive": "layerSensitive",
        "isNoClimb": "noClimb",
        "isNoInWeb": "noInWeb",
        "isNoParticle": "noParticle",
        "isAlwaysOn": "alwaysOn",
        "isNoDecay": "noDecay",
        "isAllowHalfBreak": "allowHalfBreak",
        "isNoBreakUnder": "noBreakUnder",
        "isUnconnect": "unconnect",
        "getUnconnectDefault": "unconnect",
        "hasDown": "hasDown",
        "canGrowDownward": "hasDown",
        "hasClimb": "hasClimb",
        "hasOverlay": "overlay",
        "hasBetterFoliage": "betterFoliage",
        "isBarsModel": "barsModel",
        "getBedType": "bedType",
        "hasBedType": "bedType",
        "getWallSize": "wallSize",
        "getPlantId": "plantId",
        "getNoUvlock": "noUvlock",
        "hasRotateRandom": "rotateRandom",
    }
    for helper, opt in helper_to_option.items():
        # Match def.helper() or definition.helper() patterns
        if re.search(rf"(?:def|definition)\s*\.\s*{re.escape(helper)}\s*\(", java_text):
            options.add(opt)

    # Also detect direct OptionsProperties getter calls: opts.getXxx() or options.getXxx()
    getter_to_option = {
        "getUnconnect": "unconnect",
        "getConnectstate": "connectstate",
        "getNoUvlock": "noUvlock",
        "getBarsModel": "barsModel",
        "getLegacyModel": "legacyModel",
        "getNoDecay": "noDecay",
        "getBetterFoliage": "betterFoliage",
        "getOverlay": "overlay",
        "getAllowUnsupported": "allowUnsupported",
        "getNoParticle": "noParticle",
        "getLocked": "locked",
        "getAlwaysOn": "alwaysOn",
        "getPlantId": "plantId",
        "getNoInWeb": "noInWeb",
        "getNoClimb": "noClimb",
        "getToggleOnUse": "toggleOnUse",
        "getLayerSensitive": "layerSensitive",
        "getSymmetrical": "symmetrical",
        "getNoBreakUnder": "noBreakUnder",
        "getAllowHalfBreak": "allowHalfBreak",
        "getHasClimb": "hasClimb",
        "getHasDown": "hasDown",
        "getWallSize": "wallSize",
        "getBedType": "bedType",
        "getRotateRandom": "rotateRandom",
    }
    for getter, opt in getter_to_option.items():
        # Match opts.getXxx() or options.getXxx() patterns
        if re.search(rf"(?:opts|options)\s*\.\s*{re.escape(getter)}\s*\(", java_text):
            options.add(opt)

    return options


def extract_option_reads_from_exporter(java_text: str) -> set[str]:
    """Find options read by an exporter class via definition.hasX/isX/getX methods."""
    options = set()
    helper_to_option = {
        "hasRotateRandom": "rotateRandom",
        "isLayerSensitive": "layerSensitive",
        "hasBetterFoliage": "betterFoliage",
        "hasOverlay": "overlay",
        "getWallSize": "wallSize",
        "getNoUvlock": "noUvlock",
        "getBedType": "bedType",
        "hasBedType": "bedType",
        "isConnectState": "connectstate",
        "toggleOnUse": "toggleOnUse",
        "isAllowUnsupported": "allowUnsupported",
        "isLocked": "locked",
        "isUnconnect": "unconnect",
        "canGrowDownward": "hasDown",
    }
    for helper, opt in helper_to_option.items():
        if re.search(rf"definition\s*\.\s*{re.escape(helper)}\s*\(", java_text):
            options.add(opt)
    return options


# ---------------------------------------------------------------------------
# Schema helpers
# ---------------------------------------------------------------------------

def get_schema_block_types(schema: dict) -> list[str]:
    """Get blockType enum values from schema."""
    return schema["$defs"]["BlockDefinition"]["properties"]["blockType"]["enum"]


def get_schema_conditionals(schema: dict) -> dict[str, str]:
    """Get blockType -> $ref mapping from allOf if/then conditionals."""
    result = {}
    for entry in schema["$defs"]["BlockDefinition"].get("allOf", []):
        if "if" not in entry:
            continue
        bt_schema = entry["if"]["properties"]["blockType"]
        if "const" in bt_schema:
            block_type = bt_schema["const"]
        else:
            continue
        then_opts = entry["then"]["properties"]["options"]["oneOf"]
        for opt in then_opts:
            if "$ref" in opt:
                result[block_type] = opt["$ref"].split("/")[-1]
    return result


def get_schema_def_properties(schema: dict, def_name: str) -> set[str]:
    """Get property names from a $defs schema."""
    defn = schema["$defs"].get(def_name, {})
    return set(defn.get("properties", {}).keys())


# ---------------------------------------------------------------------------
# Main checks
# ---------------------------------------------------------------------------

def main():
    parser = argparse.ArgumentParser(description="Validate block definition schemas against Java source")
    parser.add_argument("--verbose", "-v", action="store_true", help="Show passing checks")
    args = parser.parse_args()

    errors = []
    warnings = []
    info = []

    # Load sources
    schema = json.loads(SCHEMA_PATH.read_text())
    mod_blocks_text = MOD_BLOCKS_PATH.read_text()
    model_provider_text = MODEL_PROVIDER_PATH.read_text()
    options_props_text = OPTIONS_PROPS_PATH.read_text()

    # Parse — factories: {type: block_class}, exporters: {type: exporter_class}
    factories = extract_factory_entries(mod_blocks_text)
    exporters = extract_exporter_entries(model_provider_text)
    all_option_fields = extract_options_fields(options_props_text)
    schema_types = set(get_schema_block_types(schema))
    conditionals = get_schema_conditionals(schema)

    # -----------------------------------------------------------------------
    # Check 1: All FACTORIES types are in schema enum
    # -----------------------------------------------------------------------
    for bt in sorted(factories):
        if bt not in schema_types:
            errors.append(f"[ENUM] Block type '{bt}' in ModBlocks.FACTORIES but missing from schema blockType enum")
        elif args.verbose:
            info.append(f"[ENUM] OK: '{bt}' in both FACTORIES and schema")

    # -----------------------------------------------------------------------
    # Check 2: No stale types in schema enum
    # -----------------------------------------------------------------------
    for bt in sorted(schema_types):
        if bt not in factories:
            errors.append(f"[STALE] Block type '{bt}' in schema blockType enum but not in ModBlocks.FACTORIES")

    # -----------------------------------------------------------------------
    # Check 3: All EXPORTERS types are in schema enum
    # -----------------------------------------------------------------------
    for bt in sorted(exporters):
        if bt not in schema_types:
            warnings.append(f"[EXPORTER] Block type '{bt}' in ModModelProvider.EXPORTERS but missing from schema enum")

    # -----------------------------------------------------------------------
    # Check 4 & 5: Option coverage per block type
    # -----------------------------------------------------------------------
    for bt in sorted(factories):
        block_class = factories[bt]

        # Collect options from block class
        block_file = BLOCK_CUSTOM_DIR / f"{block_class}.java"
        block_options = set()
        if block_file.exists():
            block_options = extract_option_reads_from_block(block_file.read_text())

        # Collect options from exporter class
        exporter_options = set()
        if bt in exporters:
            exporter_class = exporters[bt]
            exporter_file = DATAGEN_CUSTOM_DIR / f"{exporter_class}.java"
            if exporter_file.exists():
                exporter_options = extract_option_reads_from_exporter(exporter_file.read_text())

        all_opts = block_options | exporter_options

        if not all_opts:
            if bt in conditionals and args.verbose:
                info.append(f"[OPTIONS] '{bt}' has conditional schema but reads no options (may be overly strict)")
            elif args.verbose:
                info.append(f"[OPTIONS] OK: '{bt}' has no options and no conditional schema")
            continue

        # Block type reads options — check it has a conditional schema
        if bt not in conditionals:
            warnings.append(
                f"[MISSING-SCHEMA] '{bt}' reads options {sorted(all_opts)} but has no conditional schema in allOf"
            )
            continue

        # Check that the schema properties match
        schema_def = conditionals[bt]
        schema_opts = get_schema_def_properties(schema, schema_def)

        missing = all_opts - schema_opts
        extra = schema_opts - all_opts

        if missing:
            errors.append(
                f"[OPTIONS] '{bt}' reads options {sorted(missing)} but they're missing from {schema_def}"
            )
        if extra:
            warnings.append(
                f"[EXTRA] '{bt}' schema {schema_def} allows {sorted(extra)} but code doesn't read them"
            )
        if not missing and not extra and args.verbose:
            info.append(f"[OPTIONS] OK: '{bt}' schema matches code — {sorted(all_opts)}")

    # -----------------------------------------------------------------------
    # Report
    # -----------------------------------------------------------------------
    if args.verbose and info:
        for msg in info:
            print(f"  \033[90m{msg}\033[0m")
        print()

    if warnings:
        print(f"\033[33mWarnings ({len(warnings)}):\033[0m")
        for msg in warnings:
            print(f"  {msg}")
        print()

    if errors:
        print(f"\033[31mErrors ({len(errors)}):\033[0m")
        for msg in errors:
            print(f"  {msg}")
        print()
        print(f"FAILED — {len(errors)} error(s), {len(warnings)} warning(s)")
        sys.exit(1)
    else:
        if warnings:
            print(f"PASSED with {len(warnings)} warning(s)")
        else:
            print("\033[32mPASSED — schemas match Java source\033[0m")
        sys.exit(0)


if __name__ == "__main__":
    main()
