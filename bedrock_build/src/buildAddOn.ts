import * as fs from 'fs';
const uuidv5 = require('uuid/v5');
import archiver from 'archiver-promise';

// Map of block sounds from java to bedrock
const soundmap = {
    metal: 'metal',
    wood: 'wood',
    grass: 'grass',
    stone: 'stone',
    sand: 'sand',
    powder: 'sand',
    gravel: 'gravel',
    cloth: 'cloth',
    ladder: 'ladder',
    glass: 'glass'
};

console.log("Loading westerosblocks.js");
// Load resources
let content = fs.readFileSync("../src/main/resources/WesterosBlocks.json");

let blockdefs = JSON.parse(content.toString('utf-8'));

const solidishBlocks = [ "solid", "sand", "soulsand", "leaves", 
    "log", "furnace" ]; // log and furnace are not really simple, but close enough...

// Now select all the block definitions that are solid
let solidblocks = blockdefs.blocks.filter(blk => (solidishBlocks.indexOf(blk.blockType) >= 0));
console.log(`${solidblocks.length} solid-ish blocks found`);

const outpath = 'WesterosRP';
const bppath = 'WesterosBP';
// Make RP output tree
fs.mkdirSync(outpath, { recursive: true });
fs.mkdirSync(`${outpath}/textures/blocks`, { recursive: true });
fs.mkdirSync(`${outpath}/texts`, { recursive: true });
fs.mkdirSync(bppath, { recursive: true });
fs.mkdirSync(`${bppath}/blocks`, { recursive: true });

let uuidNS = "4121bb42-94c1-491c-aae4-b86affe12de0";
let rpuuid = uuidv5('pack', uuidNS);
let rp2uuid = uuidv5('rp', uuidNS);
let bpuuid = uuidv5('pack2', uuidNS);
let bp2uuid = uuidv5('bp', uuidNS);
let now = new Date();
let base = new Date('2020-01-31');
let ts = (now.getTime() - base.getTime()) / 60000;
let version = [ 0, Math.floor(ts / (24*60)), Math.floor(ts % (24*60)) ];

// Initialize RP manifest.json
let manifest = {
    format_version: 2,
    header: {
        description: "WesteorBlocks Bedrock Resource Pack",
        name: "WesterosBlocks Reource Pack",
        uuid: rpuuid,
        version: version,
        min_engine_version: [ 1, 15, 0 ]
    },
    modules: [
        {
            description: "WesteorBlocks Bedrock Resource Pack",
            type: "resources",
            uuid: rp2uuid,
            version: version
        }
    ]
};
fs.writeFileSync(`${outpath}/manifest.json`, JSON.stringify(manifest, undefined, 2));

// Initialize BP manifest.json
let bpmanifest = {
    format_version: 2,
    header: {
        description: "WesteorBlocks Bedrock Behavior Pack",
        name: "WesterosBlocks Behavior Pack",
        uuid: bpuuid,
        version: version,
        min_engine_version: [ 1, 15, 0 ]
    },
    modules: [
        {
            description: "WesteorBlocks Bedrock Behavior Pack",
            type: "data",
            uuid: bp2uuid,
            version: version
        }
    ],
    dependencies: [
        {
          uuid: rpuuid,
          version: version
        }
    ]    
};
fs.writeFileSync(`${bppath}/manifest.json`, JSON.stringify(bpmanifest, undefined, 2));

// Start building blocks.json
let blocks = {
    "format_version": [ 1, 1, 0 ]
};
// Start language file (texts/en_US.lang)
let texts: { [key: string]: string } = {};

// Start terrain_texture.json
let terrain = {
    resource_pack_name: "westerosblocks",
    texture_name: "atlas.terrain",
    padding: 8,
    num_mip_levels: 4,
    texture_data: {}
};

// Start building blockstates lines
let blockstates = {};
let blocknames = {};
let blockids = {};

let mccblocks = [];
let mccblockids = [];
let mccid = 4485;
let elemid = 1;

// Loop through the solid blocks
solidblocks.forEach(blk => {
    // Get base ID
    let blkid = blk.blockName;
    // Map the block sound
    let snd = soundmap[blk.stepSound] || 'stone';
    let lastblkid = "air";
    // Loop through subBlocks
    blk.subBlocks.forEach(sub => {
//        if (elemid > 118) return;
//        let baseid = "element_" + elemid;
//        let id = baseid;
//        elemid++;
        let baseid = blkid + "_" + sub.meta;    // Make full ID
        let id = "wb:" + baseid;
        // Add label to text file 
        texts[`tile.${id}.name`] = sub.label;
        // Add block record
        blocks[id] = { sound: snd };
        // Build texture list
        let txt = sub.textures;
        txt = txt.map(v => v.replace('/', '_'));
        // If only one, just add texture name
        if (txt.length == 1) {
            blocks[id].textures = txt[0];
        }
        else {  // Pad to 6
            for (let i = txt.length; i < 6; i++) {
                txt.push(txt[i-1]);
            }
            blocks[id].textures = {
                up: txt[0],
                down: txt[1],
                north: txt[2],
                south: txt[3],
                east: txt[4],
                west: txt[5]
            }
        }
        // Add line to blockstates.csv mapping
        if (!(blkid in blockstates)) {
            blockstates[blkid] = [];
        }
        if (!(blkid in blocknames)) {
            blocknames[blkid] = [];
        }
        if (!(blk.blockID in blockids)) {
            blockids[blk.blockID] = [];
        }
        blockstates[blkid].push(",,variant=" + sub.meta + "," +
            id + ",,,HEIGHTMAP");
        blocknames[blkid].push(",," + sub.meta + "," +
            id + ",,,");
        blockids[blk.blockID].push(",," + sub.meta + "," +
            id + ",,,");
        // Build MCC blocks.json record
        let mccrec = {
            Key: id,
            Value: {
                bedrock: {
                    id: blk.blockID,
                    data: sub.meta,
                    name: "westerosblocks:" + blk.blockName,
                    nameOld: null,
                    properties: null,
                    typeName: null
                },
                blockState: "",
                cache: true,
                color: 0,
                java: {
                    data: sub.meta,
                    id: blk.blockID,
                    name: "westerosblocks:" + blk.blockName,
                    nameOld: null,
                    properties: null,
                    typeName: null
                },
                label: sub.label,
                link: false,
                linkable: true,
                name: id,
                transparent: false
            }
        };
        mccblocks.push(mccrec);
        mccblockids.push({ id: blk.blockID, data: sub.meta, name: "westerosblocks:" + blk.blockName, runtimeID: mccid });
        mccid++;
        // Copy texture files into pack
        sub.textures.forEach(oldtxt => {
            let newtxt = oldtxt.replace('/','_');
            fs.copyFileSync(`../src/main/resources/assets/westerosblocks/textures/blocks/${oldtxt}.png`,
                `${outpath}/textures/blocks/${newtxt}.png`);
            // Add to terrain
            terrain.texture_data[newtxt] = {
                textures: `textures/blocks/${newtxt}`
            };
        });
        // Create behavior block file
        let bblk = {
            format_version: "1.14.0",
            "minecraft:block": {
                description: {
                    identifier: id,
                    is_experimental: false
                },
                components: {
                    "minecraft:destroy_time": {
                        value: 99999.0
                    },
                    "minecraft:explosion_resistance": {
                        value: 99999
                    },
                    "minecraft:friction": {
                        value: 0.9
                    },
                    "minecraft:flammable": {
                        flame_odds: 0,
                        burn_odds: 0
                    },
                    "minecraft:map_color": {
                        color: "#FFFFFF"
                    },
                    "minecraft:block_light_absorption": {
                        value: 0
                    },
                    "minecraft:block_light_emission": {
                        emission: 0.0
                    }
                }
            }
        };
        fs.writeFileSync(`${bppath}/blocks/${baseid}.json`, JSON.stringify(bblk, undefined, 2));
    });
});

// Write blocks.json
fs.writeFileSync(`${outpath}/blocks.json`, JSON.stringify(blocks, undefined, 2));
// Write terrain_texture.json
fs.writeFileSync(`${outpath}/textures/terrain_texture.json`, JSON.stringify(terrain, undefined, 2));

// Write texts/en_US.lang
let txt = Object.keys(texts).reduce((prev, cur) => {
    return prev + cur + "=" + texts[cur] + "\r\n";
}, "");
fs.writeFileSync(`${outpath}/texts/en_US.lang`, txt);
fs.writeFileSync(`${outpath}/texts/language.json`, JSON.stringify([ "en_US" ], undefined, 2));

// Write pack_icon.png
fs.copyFileSync('src/WesterosSealSquare.png',`${outpath}/pack_icon.png`);
fs.copyFileSync('src/WesterosSealSquare.png',`${bppath}/pack_icon.png`);

//console.log("content=" + JSON.stringify(solidblocks));
console.log("Make RP file");
let rpfile = fs.createWriteStream('WesterosBlocksRP.mcpack');
let bpfile;
let addonfile;
let rppack = archiver('zip');
rppack.pipe(rpfile);
rppack.directory(`${outpath}/`);
rppack.finalize().then(() => {
    rpfile.close();
    console.log("Make BP pack");
    bpfile = fs.createWriteStream('WesterosBlocksBP.mcpack');
    let bppack = archiver('zip');
    bppack.pipe(bpfile);
    bppack.directory(`${bppath}/`);
    return bppack.finalize();
}).then(() => {
    bpfile.close();
    console.log("Make mcaddon pack");
    addonfile = fs.createWriteStream('WesterosBlocks.mcaddon');
    let addon = archiver('zip');
    addon.pipe(addonfile);
    addon.file('WesterosBlocksRP.mcpack', { name: 'WesterosBlocksRP.mcpack' });    
    addon.file('WesterosBlocksBP.mcpack', { name: 'WesterosBlocksBP.mcpack' });  
    return addon.finalize();
}).then(() => {
    addonfile.close();
    console.log("Add on done!");
})

// Sort blockstates.csv
let blkstatekeys = Object.keys(blockstates).sort();
let bs = "";
blkstatekeys.forEach(k => {
    bs += "westerosblocks:" + k + blockstates[k].join("\r\n") + "\r\n";
});
// Write out block_states.csv
fs.writeFileSync(`blocks_states.csv`, bs);

// Sort blocknames.csv
let blknameskeys = Object.keys(blocknames).sort();
let bn = "";
blknameskeys.forEach(k => {
    bn += "westerosblocks:" + k + blocknames[k].join("\r\n") + "\r\n";
});
// Write out block_names.csv
fs.writeFileSync(`blocks_names.csv`, bn);

// Sort block_ids.csv
let blkidskeys = Object.keys(blockids).sort();
let bi = "";
blkidskeys.forEach(k => {
    bi += k + blockids[k].join("\r\n") + "\r\n";
});
// Write out block_ids.csv
fs.writeFileSync(`blocks_ids.csv`, bi);

// Write block.json
fs.writeFileSync('block.json', JSON.stringify(mccblocks, null, 2));

// Write BlockID.json
fs.writeFileSync('BlockID.json', JSON.stringify(mccblockids, null, 2));

