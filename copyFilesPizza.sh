rm -r -f src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/lang
mkdir -p src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/models/item
mkdir -p src/main/resources/assets/westerosblocks/blockstates
mkdir -p src/main/resources/data/minecraft/tags/blocks
cd /mnt/c/Users/Jacob/Desktop
cp ./minecraft_servers/spongeforge-1.16.5/config/westerosblocks/assets/westerosblocks/lang/*.json ~/Working/WesterosBlocks/src/main/resources/assets/westerosblocks/lang
cp ./minecraft_servers/spongeforge-1.16.5/config/westerosblocks/assets/westerosblocks/blockstates/*.json ~/Working/WesterosBlocks/src/main/resources/assets/westerosblocks/blockstates
cp -R ./minecraft_servers/spongeforge-1.16.5/config/westerosblocks/assets/westerosblocks/models/block/generated/. ~/Working/WesterosBlocks/src/main/resources/assets/westerosblocks/models/block/generated
cp ./minecraft_servers/spongeforge-1.16.5/config/westerosblocks/assets/westerosblocks/models/item//*.json ~/Working/WesterosBlocks/src/main/resources/assets/westerosblocks/models/item
cp ./minecraft_servers/spongeforge-1.16.5/config/westerosblocks/data/minecraft/tags/blocks/*.json ~/Working/WesterosBlocks/src/main/resources/data/minecraft/tags/blocks
