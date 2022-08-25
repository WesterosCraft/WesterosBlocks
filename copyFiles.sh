rm -r -f src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/lang
mkdir -p src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/models/item
mkdir -p src/main/resources/assets/westerosblocks/blockstates
mkdir -p src/main/resources/data/minecraft/tags/blocks
cp ~/westeros-1.18.2/config/westerosblocks/assets/westerosblocks/lang/*.json src/main/resources/assets/westerosblocks/lang
cp ~/westeros-1.18.2/config/westerosblocks/assets/westerosblocks/blockstates/*.json src/main/resources/assets/westerosblocks/blockstates
cp -R ~/westeros-1.18.2/config/westerosblocks/assets/westerosblocks/models/block/generated/. src/main/resources/assets/westerosblocks/models/block/generated
cp ~/westeros-1.18.2/config/westerosblocks/assets/westerosblocks/models/item//*.json src/main/resources/assets/westerosblocks/models/item
cp ~/westeros-1.18.2/config/westerosblocks/data/minecraft/tags/blocks/*.json src/main/resources/data/minecraft/tags/blocks
