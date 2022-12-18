rm -r -f src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/lang
mkdir -p src/main/resources/assets/westerosblocks/models/block/generated
mkdir -p src/main/resources/assets/westerosblocks/models/item
mkdir -p src/main/resources/assets/westerosblocks/blockstates
mkdir -p src/main/resources/data/minecraft/tags/blocks
cp ../forge-1.18.2/config/westerosblocks/assets/westerosblocks/lang/*.json ../WesterosBlocks/src/main/resources/assets/westerosblocks/lang
cp ../forge-1.18.2/config/westerosblocks/assets/westerosblocks/blockstates/*.json ../WesterosBlocks/src/main/resources/assets/westerosblocks/blockstates
cp -R ../forge-1.18.2/config/westerosblocks/assets/westerosblocks/models/block/generated/. ../WesterosBlocks/src/main/resources/assets/westerosblocks/models/block/generated
cp ../forge-1.18.2/config/westerosblocks/assets/westerosblocks/models/item//*.json ../WesterosBlocks/src/main/resources/assets/westerosblocks/models/item
cp ../forge-1.18.2/config/westerosblocks/data/minecraft/tags/blocks/*.json ../WesterosBlocks/src/main/resources/data/minecraft/tags/blocks
