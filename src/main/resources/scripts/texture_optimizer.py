#!/usr/bin/env python3
"""
Alternative Texture Optimizer for Minecraft Mod Development

This script optimizes PNG images using PIL/Pillow without requiring an API key.
It's a local alternative that maintains transparency and image quality while
reducing file size.

Usage:
  python texture_optimizer_local.py [--path PATH] [--backup] [--quality QUALITY]

Arguments:
  --path PATH         Path to the textures directory (default: resources/assets/westerosblocks/textures)
  --backup            Create backup of original images before optimizing
  --quality QUALITY   Optimization quality (1-9, higher = better quality but larger files, default: 9)
  --width WIDTH       Max width to resize textures to (default: no resize)
  --height HEIGHT     Max height to resize textures to (default: no resize)
"""

import os
import sys
import argparse
import shutil
import time
from datetime import datetime
import concurrent.futures
from pathlib import Path
from PIL import Image, ImageOps

# Default settings
DEFAULT_PATH = os.path.join("resources", "assets", "westerosblocks", "textures")
BACKUP_FOLDER = "texture_backup_" + datetime.now().strftime("%Y%m%d_%H%M%S")
MAX_WORKERS = 10  # Can use more workers since it's not API-limited

def parse_arguments():
    """Parse command line arguments."""
    parser = argparse.ArgumentParser(description="Optimize PNG textures locally for Minecraft mod")
    parser.add_argument("--path", type=str, default=DEFAULT_PATH,
                        help=f"Path to textures directory (default: {DEFAULT_PATH})")
    parser.add_argument("--backup", action="store_true",
                        help="Create backup of original images")
    parser.add_argument("--quality", type=int, default=9, choices=range(1, 10),
                        help="Optimization quality (1-9, higher = better quality but larger files)")
    parser.add_argument("--width", type=int, help="Max width to resize textures to")
    parser.add_argument("--height", type=int, help="Max height to resize textures to")

    return parser.parse_args()

def find_png_files(directory):
    """Find all PNG files in the given directory and its subdirectories."""
    png_files = []

    for root, _, files in os.walk(directory):
        for file in files:
            if file.lower().endswith(".png"):
                png_files.append(os.path.join(root, file))

    return png_files

def create_backup(file_path, backup_dir):
    """Create a backup of the original file."""
    # Preserve the directory structure
    rel_path = os.path.relpath(file_path, os.path.dirname(backup_dir))
    backup_path = os.path.join(backup_dir, rel_path)

    # Create the directory structure if it doesn't exist
    os.makedirs(os.path.dirname(backup_path), exist_ok=True)

    # Copy the file
    shutil.copy2(file_path, backup_path)
    return backup_path

def optimize_image(file_path, backup_dir=None, quality=9, resize=None):
    """Optimize a single PNG image using PIL/Pillow."""
    try:
        # Create backup if requested
        if backup_dir:
            create_backup(file_path, backup_dir)

        # Get the file size before optimization
        original_size = os.path.getsize(file_path)

        # Open the image
        with Image.open(file_path) as img:
            # Remember if the image has transparency
            has_alpha = img.mode == 'RGBA'

            # Resize if requested (maintaining aspect ratio)
            if resize:
                max_width, max_height = resize
                if max_width or max_height:
                    width, height = img.size
                    new_width, new_height = width, height

                    if max_width and width > max_width:
                        ratio = max_width / width
                        new_width = max_width
                        new_height = int(height * ratio)

                    if max_height and new_height > max_height:
                        ratio = max_height / new_height
                        new_height = max_height
                        new_width = int(new_width * ratio)

                    if (new_width, new_height) != (width, height):
                        img = img.resize((new_width, new_height), Image.LANCZOS)

            # Convert image for optimization
            if has_alpha:
                # Store alpha channel separately (better compression)
                r, g, b, a = img.split()
                rgb_img = Image.merge('RGB', (r, g, b))

                # Save optimized version with transparency
                rgb_img.save(file_path, 'PNG', optimize=True, quality=quality)

                # Re-open and add back the alpha channel
                with Image.open(file_path) as saved_img:
                    rgba_img = saved_img.convert('RGBA')
                    rgba_img.putalpha(a)
                    rgba_img.save(file_path, 'PNG', optimize=True, quality=quality)
            else:
                # Save optimized version without transparency
                img.save(file_path, 'PNG', optimize=True, quality=quality)

        # Get the file size after optimization
        optimized_size = os.path.getsize(file_path)
        savings = original_size - optimized_size
        savings_percent = (savings / original_size) * 100 if original_size > 0 else 0

        return {
            "file": file_path,
            "original_size": original_size,
            "optimized_size": optimized_size,
            "savings": savings,
            "savings_percent": savings_percent,
            "status": "success"
        }

    except Exception as e:
        return {
            "file": file_path,
            "status": "error",
            "message": str(e)
        }

def bytes_to_human_readable(size_bytes):
    """Convert bytes to human-readable format."""
    if size_bytes < 1024:
        return f"{size_bytes} B"
    elif size_bytes < 1024 * 1024:
        return f"{size_bytes / 1024:.2f} KB"
    else:
        return f"{size_bytes / (1024 * 1024):.2f} MB"

def main():
    args = parse_arguments()

    # Check if the textures directory exists
    textures_dir = Path(args.path)
    if not textures_dir.exists() or not textures_dir.is_dir():
        print(f"Error: Textures directory not found: {textures_dir}")
        sys.exit(1)

    # Create backup directory if needed
    backup_dir = None
    if args.backup:
        backup_dir = os.path.join(os.getcwd(), BACKUP_FOLDER)
        os.makedirs(backup_dir, exist_ok=True)
        print(f"Backup directory created: {backup_dir}")

    # Determine resize dimensions
    resize = None
    if args.width or args.height:
        resize = (args.width, args.height)

    # Find all PNG files
    png_files = find_png_files(textures_dir)

    if not png_files:
        print(f"No PNG files found in {textures_dir}")
        sys.exit(0)

    print(f"Found {len(png_files)} PNG files to optimize")

    # Process files with a progress counter
    successful = 0
    failed = 0
    total_original = 0
    total_optimized = 0

    print(f"\nProcessing {len(png_files)} files with quality level {args.quality}...")

    # Use ThreadPoolExecutor to optimize images in parallel
    with concurrent.futures.ThreadPoolExecutor(max_workers=MAX_WORKERS) as executor:
        future_to_file = {
            executor.submit(optimize_image, file_path, backup_dir, args.quality, resize): file_path
            for file_path in png_files
        }

        for i, future in enumerate(concurrent.futures.as_completed(future_to_file), 1):
            result = future.result()
            file_path = result["file"]
            rel_path = os.path.relpath(file_path, textures_dir)

            # Print progress
            print(f"[{i}/{len(png_files)}] Processing: {rel_path}...", end=" ")

            if result["status"] == "success":
                original = result["original_size"]
                optimized = result["optimized_size"]
                savings_percent = result["savings_percent"]

                total_original += original
                total_optimized += optimized
                successful += 1

                print(f"Optimized! {bytes_to_human_readable(original)} → {bytes_to_human_readable(optimized)} ({savings_percent:.1f}% saved)")
            else:
                failed += 1
                print(f"Failed: {result['message']}")

    # Print summary
    total_savings = total_original - total_optimized
    total_savings_percent = (total_savings / total_original) * 100 if total_original > 0 else 0

    print("\n" + "=" * 50)
    print("Optimization Summary:")
    print(f"Successfully optimized: {successful} files")
    print(f"Failed: {failed} files")
    print(f"Total original size: {bytes_to_human_readable(total_original)}")
    print(f"Total optimized size: {bytes_to_human_readable(total_optimized)}")
    print(f"Total savings: {bytes_to_human_readable(total_savings)} ({total_savings_percent:.1f}%)")
    print("=" * 50)

    if args.backup:
        print(f"\nOriginal files backed up to: {backup_dir}")

if __name__ == "__main__":
    main()