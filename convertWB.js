'use strict'

const fs = require('fs');

let rawdata = fs.readFileSync('./WesterosBlocks.json');
let content = JSON.parse(rawdata);
let newcontent = { blocks: [] };

function makeName(label) {
    label = label.toLowerCase();
    label = label.replace(/\s+/g,"_");
    return label.replace(/[^0-9a-z_]/gi, '')
}
const skippedFields = [ 'blockName', 'blockID', 'blockIDs', 'subBlocks', 'modelBlockMeta' ];
const skippedSubBlockFields = [ 'meta' ];

let ids = new Set();

content.blocks.forEach(block => {
    block.subBlocks.forEach(subblock => {
        let newblock = { blockName: makeName(subblock.label) };
        let cnt = 1;
        while (ids.has(newblock.blockName)) {
            cnt++;
            newblock.blockName = makeName(subblock.label) + cnt;
        }
        ids.add(newblock.blockName);
        Object.keys(block).forEach(k => {
            if (skippedFields.includes(k)) return;
            newblock[k] = block[k];
        });
        Object.keys(subblock).forEach(k => {
            if (skippedSubBlockFields.includes(k)) return;
            newblock[k] = subblock[k];
        });
        newblock.legacyBlockID = block.blockName + ":" + subblock.meta;
        subblock.newName = newblock.blockName;	// Remember new name
        
        newcontent.blocks.push(newblock);
    });
});
// Second pass for sake of modelBlockNames
content.blocks.forEach(block => {
	if (block.modelBlockName) {
		let modelLegacyID = block.modelBlockName + ":" + block.modelBlockMeta;
		let model = newcontent.blocks.find(r => r.legacyBlockID == modelLegacyID);
		let modelID;
		if (model) {
			modelID = model.blockName;
		}
		else if (block.modelBlockName.startsWith("minecraft:")) {
			modelID = block.modelBlockName;
		}
		else {
			console.log(`Bad modelBlockName ${block.modelBlockName} for ${block.blockName}`);
		}
		if (modelID) {
			let newrec = newcontent.blocks.find(r => r.blockName == block.subBlocks[0].newName);
			newrec.modelBlockName = modelID;
		}
    }
});

console.log(JSON.stringify(newcontent, null, '    '));

