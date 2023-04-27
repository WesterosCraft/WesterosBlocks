'use strict';

const fs = require('fs');
const path = require('path');



function traverseDirectories(dir) {
  fs.readdir(dir, function(err, files) {
    if (err) {
      console.error("Error reading directory: " + err);
      return;
    }


    files.forEach(function(file) {
      const filePath = path.join(dir, file);

      fs.stat(filePath, function(err, stat) {
        if (err) {
          console.error("Error stating file: " + err);
          return;
        }

        if (stat.isDirectory()) {
          traverseDirectories(filePath);
        } else if (path.extname(filePath) === ".mcmeta") {
          fs.readFile(filePath, function(err, data) {
            if (err) {
              console.error("Error reading file: " + err);
              return;
            }

            const contents = JSON.parse(data.toString());

             // Make sure extra key exists
            if (!contents["animation"] && !contents?.ctm?.extra && !contents?.ctm?.proxy) {
                contents["ctm"] = {...contents.ctm, extra: {}}
            }

            // Ignore proxy files and files that already contain connect_to logic
            if (!contents["animation"] && !contents?.ctm?.proxy && !contents?.ctm?.extra?.connect_to) {

              contents.ctm.extra.ignoreStates = true

              const updatedContents = JSON.stringify(contents, null, 2);
              fs.writeFile(filePath, updatedContents, function(err) {
                if (err) {
                  console.error("Error writing file: " + err);
                  return;
                }
                console.log("Added new property to " + filePath);
              });
            }
          });
        }
      });
    });
  });
}

traverseDirectories("src/main/resources/assets/westerosblocks/textures/block");
// traverseDirectories("src/main/resources/assets/minecraft/textures/block");
