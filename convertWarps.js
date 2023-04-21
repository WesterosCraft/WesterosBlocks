'use strict';

const fs = require('fs');

const worldmap = {
    "2da99365-09e1-4c4c-8234-e0cc408de7e1": "westeroscraft:westerosnew",
    "529971ea-d2be-4409-a3d0-43b09890e844": "westeroscraft:newtest",
    "0040c34c-8b08-4a1c-a131-0344083fcd4f": "westeroscraft:blocktestworld",
};

function escape(s) {

}
let rawdata = fs.readFileSync('general.json');
let config = JSON.parse(rawdata);
let warps = config.warps;
console.log("{");
console.log("\twarps: {");
Object.keys(warps).forEach(warp => {
    let warpdef = warps[warp];
    let world = worldmap[warpdef.world];
    if (!world) return;
    console.log(`\t\t${JSON.stringify(warp)}: { dim: "${world}", x: ${Math.round(warpdef.x)}, y: ${Math.round(warpdef.y)}, z: ${Math.round(warpdef.z)}, time: ${Date.now()}L, xRot: ${warpdef.rotx}f, yRot: ${warpdef.roty}f }`);
});
console.log("\t}");
console.log("}");
