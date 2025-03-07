#!/usr/bin/env python3
# Block Analyzer Script
# Creates a CSV file listing all block definitions with their properties
# Output format: blockName,blockType,label,isBlockSet,blockSetName

import os
import json
import csv
import sys
import argparse
from pathlib import Path

def find_project_root():
    """Try to find the project root by looking for common project directories"""
    # Start from the current directory and go up
    current_dir = os.path.abspath(os.getcwd())

    # Try to find the resources directory
    while current_dir != os.path.dirname(current_dir):  # Stop at root
        # Check if resources directory exists
        if os.path.isdir(os.path.join(current_dir, 'resources')):
            return current_dir
        # Or check if src directory exists (common in Java/MC projects)
        if os.path.isdir(os.path.join(current_dir, 'src')):
            return current_dir
        # Move up one directory
        current_dir = os.path.dirname(current_dir)

    # If we can't find a likely project root, return current directory
    return os.path.abspath(os.getcwd())

# Parse command line arguments
parser = argparse.ArgumentParser(description='Analyze block definitions and create a CSV inventory.')
parser.add_argument('--blocks-dir', help='Path to blocks directory')
parser.add_argument('--block-sets-dir', help='Path to block sets directory')
parser.add_argument('--output', default='block_inventory.csv', help='Output CSV file path')
args = parser.parse_args()

# Determine project root and set up paths
PROJECT_ROOT = find_project_root()
print(f"Using project root: {PROJECT_ROOT}")

# Use command line arguments if provided, otherwise use default paths
BLOCKS_DIR = args.blocks_dir or os.path.join(PROJECT_ROOT, 'resources', 'definitions', 'blocks')
BLOCK_SETS_DIR = args.block_sets_dir or os.path.join(PROJECT_ROOT, 'resources', 'definitions', 'block_sets')
OUTPUT_FILE = args.output

print(f"Looking for blocks in: {BLOCKS_DIR}")
print(f"Looking for block sets in: {BLOCK_SETS_DIR}")

# Helper function to recursively find all JSON files in a directory
def find_json_files(directory):
    results = []

    if not os.path.exists(directory):
        print(f"Warning: Directory not found: {directory}")
        # Try alternative paths
        alt_paths = [
            os.path.join(PROJECT_ROOT, directory),
            os.path.join(PROJECT_ROOT, 'src', directory),
            os.path.join(os.path.dirname(PROJECT_ROOT), directory)
        ]

        for alt_path in alt_paths:
            if os.path.exists(alt_path):
                print(f"Found alternative path: {alt_path}")
                directory = alt_path
                break
        else:
            return results

    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith('.json'):
                results.append(os.path.join(root, file))

    return results

# Function to parse a block definition file
def parse_block_definition(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            definition = json.load(f)

        return {
            'blockName': definition.get('blockName', os.path.basename(file_path)[:-5]),  # Remove .json
            'blockType': definition.get('blockType', 'unknown'),
            'label': definition.get('label', definition.get('blockName', '')),
            'isBlockSet': False  # Individual block definition
        }
    except Exception as e:
        print(f"Error parsing {file_path}: {str(e)}")
        return None

# Helper function to capitalize first letter of each word
def capitalize_first_letter(string):
    return ' '.join(word.capitalize() for word in string.split())

# Function to process block sets (which can define multiple blocks)
def process_block_sets(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            block_set = json.load(f)

        results = []

        if not block_set.get('baseBlockName') or not block_set.get('variants'):
            print(f"Warning: Invalid block set in {file_path}: missing baseBlockName or variants")
            return results

        # Process each variant in the block set
        for variant in block_set['variants']:
            block_name = (block_set['baseBlockName']
                          if variant == 'solid'
                          else f"{block_set['baseBlockName']}_{variant}")

            # Check if an alternative name is specified
            alt_names = block_set.get('altNames', {})
            final_block_name = alt_names.get(variant, block_name)

            # Check if an alternative label is specified
            variant_label = '' if variant == 'solid' else capitalize_first_letter(variant)
            base_label = block_set.get('baseLabel', capitalize_first_letter(block_set['baseBlockName'].replace('_', ' ')))
            alt_labels = block_set.get('altLabels', {})
            final_label = alt_labels.get(variant, f"{base_label} {variant_label}".strip())

            # Get the actual block type from the variant using VARIANT_TYPES mapping
            # This follows the same logic as in ModBlockSet.java
            variant_types = {
                "stairs": "stair",
                "hopper": "cuboid",
                "tip": "cuboid",
                "carpet": "cuboid",
                "fence_gate": "fencegate",
                "half_door": "cuboid-nsew",
                "cover": "rail",
                "hollow_hopper": "cuboid",
                "directional": "cuboid-nsew",
                "path": "cuboid",
                "window_frame": "solid",
                "window_frame_mullion": "solid",
                "arrow_slit": "solid",
                "arrow_slit_window": "solid",
                "arrow_slit_ornate": "solid"
            }

            block_type = variant_types.get(variant, variant)

            results.append({
                'blockName': final_block_name,
                'blockType': block_type,
                'label': final_label,
                'isBlockSet': True,  # This block comes from a block set
                'blockSetName': block_set['baseBlockName']  # Store the original block set name
            })

        return results
    except Exception as e:
        print(f"Error processing block set {file_path}: {str(e)}")
        return []

# Prompt user to enter paths if directories not found
def prompt_for_paths():
    global BLOCKS_DIR, BLOCK_SETS_DIR

    if not os.path.exists(BLOCKS_DIR):
        user_path = input(f"Blocks directory not found. Please enter the path to your blocks directory: ")
        if user_path and os.path.exists(user_path):
            BLOCKS_DIR = user_path

    if not os.path.exists(BLOCK_SETS_DIR):
        user_path = input(f"Block sets directory not found. Please enter the path to your block sets directory: ")
        if user_path and os.path.exists(user_path):
            BLOCK_SETS_DIR = user_path

# Main function
def main():
    # Try to find directories if they don't exist
    if not os.path.exists(BLOCKS_DIR) or not os.path.exists(BLOCK_SETS_DIR):
        prompt_for_paths()

    blocks = []

    # Process individual block definitions
    block_files = find_json_files(BLOCKS_DIR)
    print(f"Found {len(block_files)} block definition files")
    for file in block_files:
        block = parse_block_definition(file)
        if block:
            blocks.append(block)

    # Process block sets
    block_set_files = find_json_files(BLOCK_SETS_DIR)
    print(f"Found {len(block_set_files)} block set files")
    for file in block_set_files:
        block_set_blocks = process_block_sets(file)
        blocks.extend(block_set_blocks)

    # Sort blocks by name for easier reading
    blocks.sort(key=lambda b: b['blockName'])

    # Generate CSV content
    with open(OUTPUT_FILE, 'w', newline='', encoding='utf-8') as f:
        fieldnames = ['blockName', 'blockType', 'label', 'isBlockSet', 'blockSetName']
        writer = csv.DictWriter(f, fieldnames=fieldnames)

        writer.writeheader()
        for block in blocks:
            # Prepare row data with appropriate values
            row = {
                'blockName': block['blockName'],
                'blockType': block['blockType'],
                'label': block['label'],
                'isBlockSet': 'Yes' if block.get('isBlockSet', False) else 'No',
                'blockSetName': block.get('blockSetName', '')
            }
            writer.writerow(row)

    print(f"Processed {len(blocks)} blocks")
    print(f"Output written to {OUTPUT_FILE}")

# Run the main function
if __name__ == "__main__":
    main()