rm -r -f src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/lang
mkdir -p src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/models/item
mkdir -p src/main/resources/assets/westerosblocks/blockstates
mkdir -p src/main/resources/data/minecraft/tags/blocks
cp ../../../Desktop/minecraft_servers/spongeforge-1.16.5/config/westerosblocks/assets/westerosblocks/lang/*.json src/main/resources/assets/westerosblocks/lang
cp ../../../Desktop/minecraft_servers/spongeforge-1.16.5/config/westerosblocks/assets/westerosblocks/blockstates/*.json src/main/resources/assets/westerosblocks/blockstates
cp -R ../../../Desktop/minecraft_servers/spongeforge-1.16.5/config/westerosblocks/assets/westerosblocks/models/block/generated/. src/main/resources/assets/westerosblocks/models/block/generated
cp ../../../Desktop/minecraft_servers/spongeforge-1.16.5/config/westerosblocks/assets/westerosblocks/models/item//*.json src/main/resources/assets/westerosblocks/models/item
cp ../../../Desktop/minecraft_servers/spongeforge-1.16.5/config/westerosblocks/data/minecraft/tags/blocks/*.json src/main/resources/data/minecraft/tags/blocks