rm -r -f src/main/resources/assets/westerosblocks/models/block/generated
rm -r -f src/main/resources/data
mkdir -p src/main/resources/assets/westerosblocks/lang
mkdir -p src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/models/item
mkdir -p src/main/resources/assets/westerosblocks/blockstates
mkdir -p src/main/resources/data/minecraft/tags/blocks
mkdir -p src/main/resources/data/westerosblocks/tags/blocks
cp ../../Servers/neoforge-1.21/config/westerosblocks/assets/westerosblocks/lang/*.json ../WesterosBlocks/src/main/resources/assets/westerosblocks/lang
cp ../../Servers/neoforge-1.21/config/westerosblocks/assets/westerosblocks/blockstates/*.json ../WesterosBlocks/src/main/resources/assets/westerosblocks/blockstates
cp -R ../../Servers/neoforge-1.21/config/westerosblocks/assets/westerosblocks/models/block/generated/. ../WesterosBlocks/src/main/resources/assets/westerosblocks/models/block/generated
cp ../../Servers/neoforge-1.21/config/westerosblocks/assets/westerosblocks/models/item//*.json ../WesterosBlocks/src/main/resources/assets/westerosblocks/models/item
cp ../../Servers/neoforge-1.21/config/westerosblocks/data/minecraft/tags/blocks/*.json ../WesterosBlocks/src/main/resources/data/minecraft/tags/blocks
cp ../../Servers/neoforge-1.21/config/westerosblocks/data/westerosblocks/tags/blocks/*.json ../WesterosBlocks/src/main/resources/data/westerosblocks/tags/blocks
