These scripts are set up to do batch operations on the WC resource pack by recursively traversing textures.

Supply a list of root directories (from the `assets` directory) to use for the traversal (e.g., `minecraft` or `westerosblocks`) using either the `roots.txt` config file or supplying a list of command-line directory names using the `-r` flag.

Supply a list of blacklisted directories (i.e., the names of texture directories that are prevented from being traversed) using either the `blacklist.txt` config file or supplying a list of command-line directory names using the `-b` flag. These names may be either a single directory name (e.g. `colormap`) or a path relative to the assets directory (e.g. `westerosblocks/textures/block/ashlar_half/black`).