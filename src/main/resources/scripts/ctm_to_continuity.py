"""A script for converting a pack from CTM+WesterosCTM format to Optifine/Continuity format.

Usage
-----
`python src/main/resources/scripts/ctm_to_continuity.py`

Notes
-----
Todo:
- Need to handle case where CTM textures have an animation .mcmeta defined (need to slice tiles for each frame and also copy over mcmeta)
- Test and refine

To debug:
- CV2 throwing the following error from some textures: "libpng warning: iCCP: known incorrect sRGB profile"
- Some processed tiles appear to be incorrect when previewed with CV2.imshow

Continuity limitations:
- Custom connect methods such as `connect_to_state` and `connect_to_tag` are used extensively in our pack; these do not
  appear to be supported by Continuity, which only has the following connect values: block, tile, or state. The `state`
  connect type is equivalent to the default CTM connect setting, whereas `block` is equivalent to our `ignore_state` setting.
  In contrast, `connect_to_state` takes a *particular* state (e.g., type) as an argument and causes blocks to connect if they
  both contain this state with equivalent values (e.g., =bottom). Unless the devs are willing to implement these custom methods,
  we will need to create a custom fork of Continuity in order to add support.
- Continuity doesn't appear to support `layer` as a generic property anymore (though this needs to be confirmed). We use this in
  many CTMs to force a texture onto a particular render layer, without setting the entire block to that render layer in
  WesterosBlocks.json. An example of this is the fruit leaf overlay textures, which use `westeros_single_cond` to force the
  fruit textures to the CUTOUT_MIPPED layer. Continuity does appear to have `overlay_` variants of a few methods that support
  the `layer` property (seems adequate, though currently unused), but some necessary methods such as `overlay_vertical` are
  missing (at least from the documentation). Needs experimentation before we try to consider workarounds.
- Continuity is missing equivalents for a few types of methods that the current CTM supports, particularly `edges`,
  `edges_full`, and `ctm_simple`. Fortunately, these aren't actually used for any blocks at the moment except for the CTM
  demo blocks. The effect for the first two might be reproducible using other methods but I'm not immediately sure.
- Continuity doesn't have an equivalent for `westeros_pillar`, although it seems to have an `orient` property that accomplishes
  the same thing, and furthermore should already be enabled by default for log-type blocks. So not a limitation per se, just
  requires testing.
"""

import os
import string
import argparse
import numpy as np
import json
import cv2
import shutil

from pathlib import Path

# Supported extensions
TILE_EXTS = {'png', 'jpg', 'jpeg'}
CTM_EXT = 'mcmeta'

# Property values supported by Optifine for validation
VALID_CONNECT = ['block', 'tile', 'state']
VALID_LAYER = ['cutout_mipped', 'cutout', 'translucent']

MIN_HEIGHT = -64
MAX_HEIGHT = 65535

# Directory structure
SCRIPTS_DIR = 'scripts'
ASSETS_DIR = 'assets'
TEXTURES_DIR = 'textures'
MODELS_DIR = 'models'
BLOCK_TEXTURES_DIR = TEXTURES_DIR + '/block'
BLOCK_MODELS_DIR = MODELS_DIR + '/block'

CALLING_DIR = os.path.dirname(__file__)
WORKING_DIR = os.getcwd()
OUTPUT_DIR = os.path.join(CALLING_DIR, 'continuity')

VANILLA_PATHS_FNAME = os.path.join(CALLING_DIR, 'vanillapaths.txt')
VANILLA_PATHS = []
WB_FNAME = 'WesterosBlocks.json'

# Global state
TEXTURE_PATHS = {}
CTM_PAIRS = {}
MODEL_TEXTURES = set()
CTM_ROOTS = set()
EXPLORED_CTM = set()

FINISHED_CTM = {}
ERROR_CTM = {}
IGNORED_CTM = set()
TYPE_STATS = {}


# =======================================================
# Mappings
# =======================================================


METHOD_MAP = {
    'normal': 'fixed',
    'ctm': 'ctm_compact',
    'pattern': 'repeat',
    'westeros_pattern': 'repeat',
    'ctm_vertical': 'vertical',
    'ctm_horizontal': 'horizontal',
    'westeros_vertical': 'vertical',
    'westeros_horizontal': 'horizontal',
    'westeros_v+h': 'vertical+horizontal',
    'westeros_h+v': 'horizontal+vertical',
    'westeros_pillar': 'vertical',
    'westeros_ctm_single': 'ctm',
    'westeros_ctm': 'ctm',
}
"""Mapping from CTM+WesterosCTM methods to Optifine/Continuity methods."""

TILE_MAP = {
    'vertical': [(1,1), (1,0), (0,1), (0,0)],
    'horizontal': [(1,0), (0,1), (1,1), (0,0)],
    'horizontal+vertical': [(0,i) for i in range(7)],
    'vertical+horizontal': [(0,i) for i in range(7)],
    'ctm_compact': [(0,0), (0,1), (1,0), (1,1)]
}
"""Mapping from CTM+WesterosCTM texture formats to Optifine tile lists."""

CTM_SIZE_MAP = {
    'normal': (1,1),
    'ctm_simple': (2,2),
    'vertical': (2,2),
    'horizontal': (2,2),
    'ctm_vertical': (2,2),
    'ctm_horizontal': (2,2),
    'westeros_vertical': (2,2),
    'westeros_horizontal': (2,2),
    'westeros_v+h': (1,7),
    'westeros_h+v': (1,7),
    'westeros_pillar': (2,2),
    'westeros_ctm_single': (4,12),
    'westeros_ctm': (4,12)
}
"""Mapping from CTM+WesterosCTM types to texture sizes."""


# =======================================================
# Abstract CTM model
# =======================================================


class CTMRoot:
    """The root CTM texture and definition associated with a model texture."""
    def __init__(self, name, source):
        self.name = name
        self.source = source
        self.ctm = None
        self.texture = None

    def __hash__(self):
        return hash(self.name)

    def __str__(self):
        return f'{self.name} : {self.ctm}'


class CTMProp:
    """An individual CTM property definition (roughly corresponding to a .properties file in Optifine format)."""
    def __init__(
            self,
            method = None,
            layer = None,
            connect = None,
            tiles = None,
            method_props = None,
            nested_props = None
    ):
        self.method = method
        self.layer = layer
        self.connect = connect
        self.tiles = tiles if tiles else []
        self.method_props = method_props if method_props else {}
        self.nested_props = nested_props if nested_props else {}

    def iterate_props(self):
        ret = [self]
        for p in self.nested_props.values():
            ret += p.iterate_props()
        return ret

    def to_files(self, name, label=None, conds=None):
        label = label if label is not None else name
        name_stem = get_stem(name)
        label_stem = get_stem(label)
        tiles = {f'{label}/{idx}.png': ('Image', tile) for idx, tile in enumerate(self.tiles)}
        tile_refs = [f'{label_stem}/{idx}' for idx in range(len(self.tiles))]
        contents = {
            'method': self.method,
            'matchTiles': name_stem,
            'layer': self.layer,
            'connect': self.connect,
            'tiles': tile_refs
        }
        contents |= self.method_props
        contents |= (conds if conds is not None else {})
        contents_str = '\n'.join([
            f"{k}={format_value(v)}" for k,v in contents.items() if v is not None
        ])

        properties = {f'{label}.properties': ('Properties', contents_str)}
        ret = tiles | properties
        for tile, prop in self.nested_props.items():
            ret |= prop.to_files(f'{label}/{tile}')
        return ret

    def __str__(self):
        propdict = {k:(v if k != 'tiles' else list(range(len(v)))) for k,v in self.__dict__.items() if k not in ['nested_props']}
        ret = json.dumps(propdict)
        for t,p in self.nested_props.items():
            ret += f'\n    > {t}: {str(p)}'
        return ret


class CTMDef:
    """Abstract CTM definition class."""
    def __init__(self, name):
        self.name = name

    def iterate_props(self) -> list[CTMProp]:
        pass

    def to_files(self) -> dict[str, str]:
        pass

    def __hash__(self):
        return hash(self.name)


class CTMSingleDef(CTMDef):
    """A single CTM definition with a single properties definition."""
    def __init__(self, name):
        super().__init__(name)
        self.prop = CTMProp()

    def iterate_props(self):
        return self.prop.iterate_props()

    def to_files(self):
        return self.prop.to_files(self.name)

    def __str__(self):
        return f'{self.name}:\n  ' + str(self.prop)


class CTMCond:
    """A CTM application condition (either height range or biome list)."""
    def __init__(
            self,
            biomes=set(),
            heights=(float('-inf'), float('inf'))
    ):
        self.biomes = biomes
        self.heights = heights

    def to_dict(self):
        ret = {}
        if self.biomes:
            ret['biomes'] = list(self.biomes)
        if self.has_heights():
            ret['heights'] = (
                max(MIN_HEIGHT, self.heights[0]),
                min(MAX_HEIGHT, self.heights[1])
            )
        return ret

    def has_heights(self):
        return self.heights and (self.heights[0] > float('-inf') or self.heights[1] < float('inf'))

    def __str__(self):
        conds = list(self.biomes)
        if self.heights[0] != float('-inf') and self.heights[1] != float('inf'):
            conds.append(f'{self.heights[0]} <= y <= {self.heights[1]}')
        elif self.heights[0] != float('-inf'):
            conds.append(f'{self.heights[0]} <= y')
        elif self.heights[1] != float('inf'):
            conds.append(f'y <= {self.heights[1]}')
        return ', '.join(conds)


class CTMCondDef(CTMDef):
    """A conditional CTM definition mapping conditions to properties definitions."""
    def __init__(self, name, props=None, default=None):
        super().__init__(name)
        self.props = props if props else {}
        self.default = default

    def iterate_props(self):
        ret = []
        for p in self.props.values():
            ret += p.iterate_props()
        if self.default:
            ret += self.default.iterate_props()
        return ret

    def to_files(self):
        ret = {}
        for idx, (cond, prop) in enumerate(self.props.items()):
            suffix = string.ascii_lowercase[idx] if idx < len(string.ascii_lowercase) else str(idx)
            ret |= prop.to_files(self.name, label=f"{self.name}_{suffix}", conds=cond.to_dict())
        if self.default and self.get_default_dict():
            ret |= self.default.to_files(self.name, label=f"{self.name}_default", conds=self.get_default_dict())
        return ret

    def get_default_dict(self):
        ret = {}
        all_biomes = self.get_all_biomes()
        if all_biomes:
            ret['biomes'] = '!' + ' '.join(all_biomes)
        all_heights = self.get_all_heights()
        if all_heights:
            complement = compute_complement_intervals(all_heights)
            if complement and complement != [(float('-inf'), float('inf'))]:
                ret['heights'] = [(max(MIN_HEIGHT, h), min(MAX_HEIGHT, h)) for h in complement]
        return ret

    def get_all_biomes(self):
        biomes = set()
        for cond in self.props.keys():
            biomes |= cond.biomes
        return biomes

    def get_all_heights(self):
        all_heights = []
        for cond in self.props.keys():
            if cond.has_heights():
                all_heights.append(cond.heights)
        return all_heights

    def __str__(self):
        strs = []
        for cond, prop in self.props.items():
            strs.append(f'<{str(cond)}>\n  ' + str(prop))
        if self.default:
            strs.append(f'<default>\n  ' + str(self.default))
        return f'{self.name}:\n' + '\n'.join(strs)


# =======================================================
# Conversion helper methods
# =======================================================


def get_method(ctm):
    """Get the Optifine/Continuity method corresponding to the given CTM definition."""
    typ = ctm['type'].replace('_cond','')
    return METHOD_MAP[typ] if typ in METHOD_MAP else typ


def get_connect(ctm, default='state'):
    """Get the Optifine/Continuity connect value corresponding to the given CTM definition."""
    if 'extra' in ctm:
        connects = []
        if 'ignore_states' in ctm['extra']:
            if ctm['extra']['ignore_states']:
                connects.append('block')
            else:
                connects.append('state')
        if 'connect_to_state' in ctm['extra']:
            state = ctm['extra']['connect_to_state']
            connects.append(f'state:{state}')
        if 'connect_to_tag' in ctm['extra']:
            tag = ctm['extra']['connect_to_tag']
            connects.append(f'tag:{tag}')
        if 'connect_to' in ctm['extra']:
            lst = str(ctm['extra']['connect_to'])
            connects.append(f'{lst}')
        if connects:
            return ','.join(connects)
        else:
            print(f'** Unhandled extra: {ctm["extra"]}')
    return default


def get_layer(ctm):
    """Get the layer of the given CTM definition."""
    if 'layer' in ctm:
        layer = ctm['layer'].lower()
        if layer == 'solid':
            return None
        else:
            return layer
    return None


def get_tiles(texture, method):
    """Get a list of tiles from a stitched texture."""
    tiles = []
    grid = split_tiles(texture)
    if method in TILE_MAP:
        for (i,j) in TILE_MAP[method]:
            tiles.append(grid[i][j])
    else:
        for i in grid:
            for j in i:
                tiles.append(j)
    return tiles


def get_cond_coords(ctm_cond):
    """Get the top-left coordinates of a given CTM condition in a stitched atlas."""
    if 'rowOut' in ctm_cond and 'colOut' in ctm_cond:
        return ctm_cond['rowOut'], ctm_cond['colOut']
    elif 'ctmRow' in ctm_cond and 'ctmCol' in ctm_cond:
        return ctm_cond['ctmRow'], ctm_cond['ctmCol']
    else:
        print(f'** row/col information not found in conditional CTM def: {ctm_cond}')
        return -1, -1


def get_texture_cond(texture, type, ctm_cond):
    """Get the slice of a stitched atlas corresponding to the given CTM condition."""
    if type in ['pattern', 'westeros_pattern']:
        row, col = ctm_cond['patRow'], ctm_cond['patCol']
        h, w = ctm_cond['patHeight'], ctm_cond['patWidth']
        return split_slice(texture, row, col, h, w)
    elif type == 'random':
        row, col = ctm_cond['rndRow'], ctm_cond['rndCol']
        h, w = ctm_cond['rndHeight'], ctm_cond['rndWidth']
        return split_slice(texture, row, col, h, w)
    elif type == 'westeros_ctm+pattern':
        row_ctm, col_ctm = ctm_cond['ctmRow'], ctm_cond['ctmCol']
        row_pat, col_pat = ctm_cond['patRow'], ctm_cond['patCol']
        h, w = ctm_cond['patHeight'], ctm_cond['patWidth']
        return (
            split_slice(texture, row_ctm, col_ctm, 4, 12),
            split_slice(texture, row_pat, col_pat, h, w)
        )
    else:
        row, col = get_cond_coords(ctm_cond)
        if row < 0 or col < 0:
            print(f'** could not find row or col index for ctm_cond: {ctm_cond}')
            return None
        if type in ['normal', 'fixed']:
            return split_slice(texture, row, col, 1, 1)
        elif type == 'ctm':
            print(f'** get_texture_cond not implemented for "ctm"')
            return None
        elif type == 'edges':
            print(f'** get_texture_cond not implemented for "edges"')
            return None
        elif type == 'edges_full':
            print(f'** get_texture_cond not implemented for "edges_full"')
            return None
        elif type in CTM_SIZE_MAP:
            h, w = CTM_SIZE_MAP[type]
            return split_slice(texture, row, col, h, w)
    print(f'** could not find cond slice dimensions for ctm_cond: {ctm_cond}')
    return None


def get_cond(ctm_cond):
    """Create a CTMCond from a CTM condition."""
    if not any([x in ctm_cond for x in ['biomeNames', 'yPosMin', 'yPosMax']]):
        return None
    biomes = set()
    heights = [float('-inf'), float('inf')]
    if 'biomeNames' in ctm_cond:
        biomes = ctm_cond['biomeNames']
    if 'yPosMin' in ctm_cond:
        heights[0] = ctm_cond['yPosMin']
    if 'yPosMax' in ctm_cond:
        heights[1] = ctm_cond['yPosMax']
    return CTMCond(biomes=set(biomes), heights=tuple(heights))


def form_cond_root(type, texture, ctm, ctm_cond):
    """Form a CTMRoot object from a CTM condition."""
    ctm_new = {k:v for k,v in ctm.items() if k in ['ctm_version', 'layer']}
    ctm_new['extra'] = {
        k:v for k,v in ctm['extra'].items() if k in ['ignore_states', 'connect_to_state', 'connect_to_tag', 'connect_to']
    } if 'extra' in ctm else {}
    ctm_new['type'] = type

    ctm_cond = {
        k:v for k,v in ctm_cond.items() if k not in [
            'biomeNames', 'yPosMin', 'yPosMax', 'rowOut', 'colOut', 'ctmRow', 'ctmCol', 'patRow', 'patCol'
        ]
    }

    # if ctm_cond:
    #   print(ctm_cond, '\n')

    if type in ['random', 'normal']:
        if 'size' in ctm_cond:
            ctm_new['extra']['size'] = ctm_cond['size']
        elif 'width' in ctm_cond and 'height' in ctm_cond:
            ctm_new['extra']['size'] = ctm_cond['width'] * ctm_cond['height']
        elif 'rndWidth' in ctm_cond and 'rndHeight' in ctm_cond:
            ctm_new['extra']['size'] = ctm_cond['rndWidth'] * ctm_cond['rndHeight']
        for extra in ['weights', 'rndSameAllSides', 'rndOffY', 'rndOffX']:
            if extra in ctm_cond:
                ctm_new['extra'][extra] = ctm_cond[extra]
    elif type in ['ctm_simple']:
        # NOTE - unimplemented
        return None
    elif type in ['ctm']:
        # NOTE - unimplemented
        return None
    elif type in ['edges']:
        pass
    elif type in ['edges_full']:
        pass
    elif type in ['pattern', 'westeros_pattern']:
        ctm_new['extra']['width'] = ctm_cond['patWidth']
        ctm_new['extra']['height'] = ctm_cond['patHeight']
    elif type in ['vertical', 'horizontal', 'ctm_vertical', 'ctm_horizontal',
                  'westeros_vertical', 'westeros_horizontal',
                  'westeros_v+h', 'westeros_h+v', 'westeros_pillar']:
        pass
    elif type in ['westeros_ctm']:
        # NOTE - unimplemented
        return None
    elif type in ['westeros_ctm+pattern']:
        # NOTE - unimplemented
        return None
    elif '_cond' in type:
        # NOTE - unimplemented
        return None
    else:
        return None

    cond_root = CTMRoot('_', '_')
    cond_root.ctm = ctm_new
    cond_root.texture = texture
    return cond_root


def add_rec_props(ctm: CTMCondDef, cond: CTMCond, rec_ctm: CTMDef):
    """Add the CTMProps from a recursive CTMDef to a top-level CTMCondDef."""
    if isinstance(rec_ctm, CTMCondDef):
        print(f'** Recursive conditional CTM definitions not supported')
    elif isinstance(rec_ctm, CTMSingleDef):
        ctm.props[cond] = rec_ctm.prop


def validate_tile_len(method, l):
    """Validate that a tile list has correct dimensions for the given method."""
    if method == 'ctm' and l != 48:
        return False
    if method == 'ctm_compact' and l != 5:
        return False
    if method in ['horizontal', 'vertical'] and l != 4:
        return False
    if method in ['horizontal+vertical', 'vertical+horizontal'] and l != 7:
        return False
    return True


def validate_ctm(ctm: CTMDef):
    """Validate a CTM definition."""
    for prop in ctm.iterate_props():
        if prop.connect and prop.connect not in VALID_CONNECT:
            return False
        if prop.layer and prop.layer not in VALID_LAYER:
            return False
        if not validate_tile_len(prop.method, len(prop.tiles)):
            return False
    return True


def add_ctm(root, ctm):
    """Add a CTM to the finished list or error list depending on whether it passes validation."""
    if validate_ctm(ctm):
        FINISHED_CTM[root] = ctm
    else:
        ERROR_CTM[root] = ctm


# =======================================================
# .mcmeta -> abstract CTM parser implementations
# =======================================================


def parse_static(root: CTMRoot):
    ctm = CTMSingleDef(root.name)
    method = get_method(root.ctm)
    ctm.prop.method = method
    ctm.prop.layer = get_layer(root.ctm)
    if method == 'normal':
        ctm.prop.tiles = [read_image(root.texture)]
    else:
        ctm.prop.tiles = get_tiles(read_image(root.texture), method)
    if 'extra' in root.ctm:
        if 'weights' in root.ctm['extra']:
            ctm.prop.method_props['weights'] = root.ctm['extra']['weights']
        if 'rndSameAllSides' in root.ctm['extra']:
            ctm.prop.method_props['symmetry'] = 'all'
        if 'rndOffY' in root.ctm['extra']:
            ctm.prop.method_props['linked'] = True
        if 'rndOffX' in root.ctm['extra']:
            print(f'** rndOffX attribute unsupported; ignoring')
    return ctm


def parse_ctm_simple(root: CTMRoot):
    # NOTE - unsupported
    pass


def parse_ctm(root: CTMRoot):
    ctm = CTMSingleDef(root.name)
    method = get_method(root.ctm)
    ctm.prop.method = method
    ctm.prop.layer = get_layer(root.ctm)
    ctm.prop.connect = get_connect(root.ctm, default='state')
    texture_ctm = get_texture_path(root.ctm['textures'][0])
    ctm.prop.tiles = [read_image(root.texture)]
    ctm.prop.tiles += get_tiles(read_image(texture_ctm), method)
    return ctm


def parse_pattern(root: CTMRoot):
    ctm = CTMSingleDef(root.name)
    method = get_method(root.ctm)
    ctm.prop.method = method
    ctm.prop.layer = get_layer(root.ctm)
    ctm.prop.tiles = get_tiles(read_image(root.texture), method)
    ctm.prop.method_props['width'] = root.ctm['extra']['width']
    ctm.prop.method_props['height'] = root.ctm['extra']['height']
    return ctm


def parse_pattern_cond(root: CTMRoot):
    ctm = CTMCondDef(root.name)
    method = get_method(root.ctm)
    layer = get_layer(root.ctm)

    texture_cond = get_texture_path(root.ctm['textures'][0])
    for ctm_cond in root.ctm['extra']['conds']:
        cond = get_cond(ctm_cond)
        texture_cond_slice = get_texture_cond(read_image(texture_cond), 'pattern', ctm_cond)
        tiles = get_tiles(texture_cond_slice, method)
        ctm.props[cond] = CTMProp(
            method=method,
            layer=layer,
            tiles=tiles,
            method_props={
                'width': root.ctm['extra']['condWidth'],
                'height': root.ctm['extra']['condHeight']
            }
        )

    tiles_default = get_tiles(read_image(root.texture), method)
    ctm.default = CTMProp(
        method=method,
        layer=layer,
        tiles=tiles_default,
        method_props={
            'width': root.ctm['extra']['width'],
            'height': root.ctm['extra']['height']
        }
    )
    return ctm


def parse_edges(root: CTMRoot):
    # NOTE - unsupported
    pass


def parse_edges_full(root: CTMRoot):
    # NOTE - unsupported
    pass


def parse_ctm_directional(root: CTMRoot):
    ctm = CTMSingleDef(root.name)
    method = get_method(root.ctm)
    ctm.prop.method = method
    ctm.prop.layer = get_layer(root.ctm)
    ctm.prop.connect = get_connect(root.ctm, default='state')
    ctm.prop.tiles = get_tiles(read_image(root.texture), method)
    return ctm


def parse_ctm_directional_cond(root: CTMRoot):
    ctm = CTMCondDef(root.name)
    type = root.ctm['type'].replace('_cond', '')
    method = get_method(root.ctm)
    layer = get_layer(root.ctm)
    connect = get_connect(root.ctm, default='state')

    texture_cond = get_texture_path(root.ctm['textures'][0])
    for ctm_cond in root.ctm['extra']['conds']:
        cond = get_cond(ctm_cond)
        texture_cond_slice = get_texture_cond(read_image(texture_cond), type, ctm_cond)
        tiles = get_tiles(texture_cond_slice, method)
        ctm.props[cond] = CTMProp(method=method, layer=layer, connect=connect, tiles=tiles)

    tiles_default = get_tiles(read_image(root.texture), method)
    ctm.default = CTMProp(method=method, layer=layer, connect=connect, tiles=tiles_default)
    return ctm


def parse_westeros_pillar(root: CTMRoot):
    return parse_ctm_directional(root)


def parse_westeros_ctm_single(root: CTMRoot):
    ctm = CTMSingleDef(root.name)
    method = get_method(root.ctm)
    ctm.prop.method = method
    ctm.prop.layer = get_layer(root.ctm)
    ctm.prop.connect = get_connect(root.ctm, default='state')
    texture_ctm = get_texture_path(root.ctm['textures'][0])
    ctm.prop.tiles = get_tiles(read_image(texture_ctm), method)
    return ctm


def parse_westeros_ctm(root: CTMRoot):
    ctm = CTMSingleDef(root.name)
    method = get_method(root.ctm)
    ctm.prop.method = method
    ctm.prop.layer = get_layer(root.ctm)
    ctm.prop.connect = get_connect(root.ctm, default='state')
    ctm.prop.tiles = [read_image(get_texture_path(t)) for t in root.ctm['textures']]
    if len(ctm.prop.tiles) < 48:
        if len(ctm.prop.tiles) != 47:
            print(f'** Too few tiles provided for CTM definition {root.name}')
            return
        ctm.prop.tiles.append(read_image(get_texture_path(root.ctm['textures'][0])))
    return ctm


def parse_westeros_ctm_pattern(root: CTMRoot):
    ctm = CTMSingleDef(root.name)
    layer = get_layer(root.ctm)
    connect = get_connect(root.ctm, default='state')
    texture_ctm = get_texture_path(root.ctm['textures'][0])
    texture_repeat = get_texture_path(root.ctm['textures'][1])

    ctm.prop.method = 'ctm'
    ctm.prop.layer = layer
    ctm.prop.connect = connect
    ctm.prop.tiles = get_tiles(read_image(texture_ctm), 'ctm')

    nested_prop = CTMProp()
    nested_prop.method = 'repeat'
    nested_prop.layer = layer
    nested_prop.tiles = get_tiles(read_image(texture_repeat), 'repeat')
    nested_prop.method_props={
        'width': root.ctm['extra']['width'],
        'height': root.ctm['extra']['height']
    }
    ctm.prop.nested_props[26] = nested_prop
    return ctm


def parse_westeros_ctm_pattern_cond(root: CTMRoot):
    ctm = CTMCondDef(root.name)
    layer = get_layer(root.ctm)
    connect = get_connect(root.ctm, default='state')
    texture_ctm = get_texture_path(root.ctm['textures'][0])
    texture_repeat = get_texture_path(root.ctm['textures'][1])
    texture_cond = get_texture_path(root.ctm['textures'][2])

    for ctm_cond in root.ctm['extra']['conds']:
        cond = get_cond(ctm_cond)
        h, w = ctm_cond['patHeight'], ctm_cond['patWidth']
        texture_cond_slice_ctm, texture_cond_slice_repeat = get_texture_cond(read_image(texture_cond), 'westeros_ctm+pattern', ctm_cond)
        cond_prop = CTMProp(
            method='ctm',
            layer=layer,
            connect=connect,
            tiles=get_tiles(texture_cond_slice_ctm, 'ctm')
        )
        cond_nested_prop = CTMProp(
            method='repeat',
            layer=layer,
            tiles=get_tiles(texture_cond_slice_repeat, 'repeat'),
            method_props={
                'width': w,
                'height': h
            }
        )
        cond_prop.nested_props[26] = cond_nested_prop
        ctm.props[cond] = cond_prop

    default_prop = CTMProp(
        method='ctm',
        layer=layer,
        connect=connect,
        tiles=get_tiles(read_image(texture_ctm), 'ctm')
    )
    default_nested_prop = CTMProp(
        method='repeat',
        layer=layer,
        tiles=get_tiles(read_image(texture_repeat), 'repeat'),
        method_props={
            'width': root.ctm['extra']['width'],
            'height': root.ctm['extra']['height']
        }
    )
    default_prop.nested_props[26] = default_nested_prop
    ctm.default = default_prop
    return ctm


def parse_westeros_cond(root: CTMRoot):
    root.ctm['extra']['conds'] = [c for c in root.ctm['extra']['conds'] if 'type' not in c or c['type'] != 'null']

    ctm = CTMCondDef(root.name)
    layer = get_layer(root.ctm)
    connect = get_connect(root.ctm, default='state')

    texture_cond = get_texture_path(root.ctm['textures'][0])
    for ctm_cond in root.ctm['extra']['conds']:
        cond = get_cond(ctm_cond)
        type = ctm_cond['type'].replace('-','_') if 'type' in ctm_cond else 'normal'
        texture = get_texture_cond(read_image(texture_cond), type.replace('_cond', ''), ctm_cond)
        cond_root = form_cond_root(type, texture, root.ctm, ctm_cond)
        if cond_root is None:
            print(f'** Conversion of embedded CTM type {type} not implemented yet')
            return None
        rec_ctm = parse(cond_root)
        if rec_ctm is None:
            print(f'** Embedded CTM type {type} not yet supported')
            return None
        if cond:
            add_rec_props(ctm, cond, rec_ctm)
        else:
            rec_ctm.name = root.name
            return rec_ctm

    # NOTE: the following shouldn't be necessary since default is a single tile
    # ctm.default = CTMProp(
    #   method='fixed',
    #   layer=layer,
    #   connect=connect,
    #   tiles=[read_image(root.texture)]
    # )
    return ctm


def parse_westeros_single_cond(root: CTMRoot):
    root.ctm['extra']['conds'] = [c for c in root.ctm['extra']['conds'] if 'type' not in c or c['type'] != 'null']

    layer = get_layer(root.ctm)
    connect = get_connect(root.ctm, default='state')

    # Sometimes this CTM type is used only to force a fixed texture onto the CUTOUT_MIPPED layer
    if not root.ctm['extra']['conds']:
        ctm = CTMSingleDef(root.name)
        ctm.prop.method = 'fixed'
        ctm.prop.layer = layer
        ctm.prop.connect = connect
        ctm.prop.tiles = [read_image(root.texture)]
        return ctm

    ctm = CTMCondDef(root.name)

    for ctm_cond in root.ctm['extra']['conds']:
        cond = get_cond(ctm_cond)
        if not cond:
            print(f'** Unsupported condition: {ctm_cond}')
            return None
        type = ctm_cond['type'].replace('-','_') if 'type' in ctm_cond else 'normal'
        texture = get_texture_cond(read_image(root.texture), type.replace('_cond', ''), ctm_cond)
        cond_root = form_cond_root(type, texture, root.ctm, ctm_cond)
        if cond_root is None:
            print(f'** Conversion of embedded CTM type {type} not implemented yet')
            return None
        rec_ctm = parse(cond_root)
        if rec_ctm is None:
            print(f'** Embedded CTM type {type} not yet supported')
            return None
        add_rec_props(ctm, cond, rec_ctm)

    ctm.default = CTMProp(
        method='fixed',
        layer=layer,
        connect=connect,
        tiles=[split_slice(read_image(root.texture), 0, 0, 1, 1)]
    )
    return ctm


# =======================================================
# Top-level parser routing
# =======================================================


PARSE_DISPATCH = {
    'random': parse_static,
    'normal': parse_static,
    'ctm_simple': parse_ctm_simple,
    'ctm': parse_ctm,
    'edges': parse_edges,
    'edges_full': parse_edges_full,
    'pattern': parse_pattern,
    'vertical': parse_ctm_directional,
    'horizontal': parse_ctm_directional,
    'ctm_vertical': parse_ctm_directional,
    'ctm_horizontal': parse_ctm_directional,
    'westeros_vertical': parse_ctm_directional,
    'westeros_horizontal': parse_ctm_directional,
    'westeros_v+h': parse_ctm_directional,
    'westeros_h+v': parse_ctm_directional,
    'westeros_vertical_cond': parse_ctm_directional_cond,
    'westeros_horizontal_cond': parse_ctm_directional_cond,
    'westeros_pillar': parse_westeros_pillar,
    'westeros_ctm_single': parse_westeros_ctm_single,
    'westeros_ctm': parse_westeros_ctm,
    'westeros_pattern': parse_pattern,
    'westeros_pattern_cond': parse_pattern_cond,
    'westeros_ctm+pattern': parse_westeros_ctm_pattern,
    'westeros_ctm+pattern_cond': parse_westeros_ctm_pattern_cond,
    'westeros_cond': parse_westeros_cond,
    'westeros_single_cond': parse_westeros_single_cond,
}


def parse(root: CTMRoot):
    """Dispatch each CTM root to a parse function depending on the CTM type."""
    typ = root.ctm['type']
    if typ in PARSE_DISPATCH:
        ctm = PARSE_DISPATCH[typ](root)
        return ctm
    else:
        # print(f'No parse function defined for type "{typ}"')
        return None


def parse_wrapper(root: CTMRoot):
    typ = root.ctm['type']
    ctm = parse(root)
    if ctm is not None:
        add_ctm(root, ctm)
    else:
        IGNORED_CTM.add(root)
    if typ in TYPE_STATS:
        TYPE_STATS[typ] += 1
    else:
        TYPE_STATS[typ] = 1


# =======================================================
# Utils
# =======================================================


def namespace_to_path(name):
    """Convert namespace to full path."""
    if ':' not in name:
        name += 'minecraft:'
    namespace, loc = name.split(':')
    return f'{namespace}/{TEXTURES_DIR}/{loc}'


def get_texture_path(name):
    """Get full texture path from namespace."""
    path = namespace_to_path(name)
    if path not in TEXTURE_PATHS:
        raise Exception(f'Referenced texture {path} not found')
    return TEXTURE_PATHS[path]


def get_stem(name):
    """Get the stem of a texture path."""
    return name.split('/')[-1]


def clear_dir(path):
    """Clear a directory."""
    if os.path.isdir(path):
        shutil.rmtree(path)
    Path(path).mkdir(parents=True, exist_ok=True)


def remove_file(fname):
    """Remove a file if it exists."""
    if os.path.isfile(fname):
        os.remove(fname)


def ensure_path(fname):
    """Ensures that all directories along the path to a given filename exist."""
    path = '/'.join(fname.split('/')[:-1])
    Path(path).mkdir(parents=True, exist_ok=True)


def read_json(fname):
    """Read a JSON file."""
    with open(fname, 'r') as f:
        return json.load(f)


def write_json(fname, contents):
    """Write a JSON file."""
    with open(fname, 'w') as f:
        return json.dump(contents, f, indent=2)


def read_file(fname):
    """Read a file as text."""
    with open(fname, 'r') as f:
        return f.read()


def write_file(fname, contents):
    """Write text to a file."""
    with open(fname, 'w') as f:
        return f.write(contents)


def read_lines(fname):
    """Read lines of a text file."""
    with open(fname, 'r') as f:
        return [l.strip() for l in f.readlines()]


def read_image(source):
    """Read an image file."""
    if isinstance(source, str):
        texture = cv2.imread(source, cv2.IMREAD_UNCHANGED)
        if texture.shape[2] == 3:
            texture = cv2.cvtColor(texture, cv2.COLOR_RGB2RGBA)
            texture[:,:,3] = np.ones((texture.shape[0], texture.shape[1]))*255
        return texture
    else:
        return source


def write_image(fname, contents):
    """Write an image (CV2 array format) to an image file."""
    cv2.imwrite(fname, contents)


def split_tiles(texture):
    """Split a stitched texture into an nxm array of 32x32 tiles."""
    h, w = texture.shape[0], texture.shape[1]
    if h%32 != 0 or w%32 != 0:
        raise Exception(f'Attempting to split {h} x {w} texture; not divisible by 32')
    tiles = []
    for i in range(0, h//32):
        rows = []
        for j in range(0, w//32):
            rows.append(texture[i*32:(i+1)*32, j*32:(j+1)*32, :])
        tiles.append(rows)
    return tiles


def split_slice(texture, row, col, h, w):
    """Split a texture given an origin row+col and height+width (assuming 32x32 grid size)."""
    hp, wp = texture.shape[0], texture.shape[1]
    if hp%32 != 0 or wp%32 != 0:
        raise Exception(f'Attempting to split {hp} x {wp} texture; not divisible by 32')
    hp = hp//32
    wp = wp//32
    if row >= hp:
        raise Exception(f'Row {row} out of range for height {hp}')
    if col >= wp:
        raise Exception(f'Col {col} out of range for width {wp}')
    if row+h > hp:
        raise Exception(f'Height {h} from row {row} out of range for height {hp}')
    if col+w > wp:
        raise Exception(f'Width {w} from col {col} out of range for width {wp}')
    return texture[row*32:(row+h)*32, col*32:(col+w)*32, :]


def compute_complement_intervals(intervals):
    if not intervals:
        return [(float('-inf'), float('inf'))]

    sorted_intervals = sorted(intervals)
    complement_intervals = []

    if sorted_intervals[0][0] > float('-inf'):
        complement_intervals.append((float('-inf'), sorted_intervals[0][0] - 1))

    for i in range(len(sorted_intervals) - 1):
        end_current = sorted_intervals[i][1]
        start_next = sorted_intervals[i + 1][0]

        if end_current + 1 < start_next:
            if end_current + 1 == start_next - 1:
                complement_intervals.append(end_current + 1)
            else:
                complement_intervals.append((end_current + 1, start_next - 1))

    if sorted_intervals[-1][1] < float('inf'):
        complement_intervals.append((sorted_intervals[-1][1] + 1, float('inf')))

    return complement_intervals


def testdisplay(ctm: CTMDef):
    """Print CTM info and display tiles for debugging."""
    print(ctm)
    for prop in ctm.iterate_props():
        for t in prop.tiles:
            cv2.imshow("test", t)
            cv2.waitKey(0)


def is_mcmeta(path):
    """Check if path is an mcmeta file."""
    return path.endswith(CTM_EXT)


def is_tile(path):
    """Check if a path corresponds to a texture tile."""
    return any([e in path for e in TILE_EXTS]) and CTM_EXT not in path


def is_model(path):
    """Check if a path is a model file."""
    return path.endswith('.json') and 'textures' in read_json(path)


# =======================================================
# Preprocessing
# =======================================================


def register_texture(path):
    """Register each texture path and the corresponding tile name."""
    name = '.'.join(path.split('.')[:-1])
    TEXTURE_PATHS[name] = path


def register_ctm_pair(path):
    """Register each texture tile that has a corresponding .mcmeta containing CTM."""
    name = '.'.join(path.split('.')[:-1])
    ctm_path = path + '.' + CTM_EXT
    if os.path.exists(ctm_path):
        ctm = read_json(ctm_path)
        if 'ctm' in ctm:
            CTM_PAIRS[name] = (path, ctm_path)


def register_ctm_root(path):
    """Parse and register the CTM for a root texture, i.e., one referenced by a model file."""
    parts = path.split('.')
    name = '.'.join(parts[:-1])
    if name not in MODEL_TEXTURES:
        return
    if name not in CTM_PAIRS:
        return

    ctm_root = CTMRoot(name, path)

    _, ctm_path = CTM_PAIRS[name]
    EXPLORED_CTM.add(ctm_path)
    ctm = read_json(ctm_path)['ctm']

    if 'type' not in ctm and 'proxy' in ctm:
        proxy_ref = namespace_to_path(ctm['proxy'])
        proxy_tile, proxy_ctm_path = CTM_PAIRS[proxy_ref]
        proxy_ctm = read_json(proxy_ctm_path)['ctm']
        ctm_root.ctm = proxy_ctm
        ctm_root.texture = proxy_tile
        EXPLORED_CTM.add(proxy_ctm_path)
    elif 'type' in ctm:
        ctm_root.ctm = ctm
        ctm_root.texture = path
    else:
        print(f'* WARNING: {ctm_path} has unexpected format; type and proxy should be mutually exclusive')
        return

    CTM_ROOTS.add(ctm_root)


def register_model_textures(path):
    """Register the texture paths contained within each model."""
    model = read_json(path)
    if 'textures' in model:
        for text in model['textures'].values():
            if not text.startswith('#'):
                MODEL_TEXTURES.add(namespace_to_path(text))


# =======================================================
# Compilation
# =======================================================


def get_output_path(path):
    return f'{OUTPUT_DIR}/{ASSETS_DIR}/{path}'


def strip_ctm(contents):
    """Remove CTM from the contents of a .mcmeta file."""
    return {k:v for k,v in contents.items() if k.lower() != 'ctm'}


def copy_assets(path):
    """Copy each asset to the output directory."""
    new_path = get_output_path(path)
    ensure_path(new_path)
    if is_mcmeta(path):
        contents = read_json(path)
        new_contents = strip_ctm(contents)
        if new_contents:
            write_json(new_path, new_contents)
    else:
        shutil.copyfile(path, new_path)


def format_value(v):
    if isinstance(v, str):
        return v
    elif isinstance(v, (int, float)):
        return str(v)
    elif isinstance(v, bool):
        return 'true' if v else 'false'
    elif isinstance(v, tuple) and len(v) == 2 and isinstance(v[0], int) and isinstance(v[1], int):
        l = str(v[0]) if v[0] >= 0 else f'({v[0]})'
        u = str(v[1]) if v[1] >= 0 else f'({v[1]})'
        return f'{l}-{u}'
    elif isinstance(v, list):
        return ' '.join([format_value(x) for x in v])


# =======================================================
# Execution
# =======================================================


def traverse(rootdir, cond, func):
    """Recursively traverse from a root directory, applying `func` to each path that satisfies `cond`."""
    for d in os.listdir(rootdir):
        path = rootdir+'/'+d
        if os.path.isdir(path):
            traverse(path, cond, func)
        elif os.path.isfile(path) and cond(path):
            func(path)


def init(roots):
    """Initialize global lists."""
    # Register all textures reachable from model files, all texture paths, and all texture+CTM pairs in the pack
    for root in roots:
        traverse(
            root+'/'+BLOCK_MODELS_DIR,
            is_model,
            register_model_textures
        )
        traverse(
            root+'/'+BLOCK_TEXTURES_DIR,
            is_tile,
            register_texture
        )
        traverse(
            root+'/'+BLOCK_TEXTURES_DIR,
            is_tile,
            register_ctm_pair
        )
    # Add in vanilla texture paths
    for t in VANILLA_PATHS:
        MODEL_TEXTURES.add('.'.join(t.split('.')[:-1]))
    # Register all root CTM definitions
    for root in roots:
        traverse(
            root+'/'+BLOCK_TEXTURES_DIR,
            is_tile,
            register_ctm_root
        )


def dump(roots):
    """Compile and dump CTM classes to .properties files."""
    clear_dir(OUTPUT_DIR)

    # Copy all textures and mcmeta (stripped of CTM) to output directory
    for root in roots:
        traverse(
            root+'/'+BLOCK_TEXTURES_DIR,
            lambda x: is_tile(x) or is_mcmeta(x),
            copy_assets
        )

    # Remove textures and mcmeta referenced in any CTMRoot (apart from the source texture).
    textures_to_remove = set([v for k,v in TEXTURE_PATHS.items() if k not in MODEL_TEXTURES])
    for path in textures_to_remove:
        new_path = get_output_path(path)
        new_path_mcmeta = new_path + '.mcmeta'
        remove_file(new_path)
        remove_file(new_path_mcmeta)

    # Dump each finished CTMRoot and CTMDef
    for ctm_root, ctm in FINISHED_CTM.items():
        out_files = ctm.to_files()
        for fname, (ftype, contents) in out_files.items():
            out_fname = get_output_path(fname)
            ensure_path(out_fname)
            if ftype == 'Properties':
                write_file(out_fname, contents)
            elif ftype == 'Image':
                write_image(out_fname, contents)


def main(roots, verbose=False):
    init(roots)

    all_ctm = {ctm_path for _, ctm_path in CTM_PAIRS.values()}
    unexplored_ctm = all_ctm - EXPLORED_CTM
    print(f'\nExplored {len(EXPLORED_CTM)}/{len(all_ctm)} CTM .mcmeta files (missed {len(unexplored_ctm)})')
    if verbose:
        print('\nMissed CTM .mcmeta files:')
        for x in unexplored_ctm:
            print(x)

    print(f'\nParsing {len(CTM_ROOTS)} CTM roots')
    for ctm_root in CTM_ROOTS:
        parse_wrapper(ctm_root)

    print()
    print(f'{len(FINISHED_CTM)}/{len(CTM_ROOTS)} CTMs parsed')
    print(f'{len(ERROR_CTM)}/{len(CTM_ROOTS)} CTMs parsed with errors')
    if verbose:
        for ctm in ERROR_CTM:
            print(f'  - {ctm.name}')
    print(f'{len(IGNORED_CTM)}/{len(CTM_ROOTS)} CTMs skipped')
    if verbose:
        for ctm in IGNORED_CTM:
            print(f'  - {ctm.name}')

    if verbose:
        print('\nCTM type stats:')
        for k,v in sorted(TYPE_STATS.items(), key=lambda x: x[1], reverse=True):
            print(f'{k}: {v}')

    dump(roots)


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-r', '--roots', nargs='+', default=['westerosblocks', 'minecraft'])
    parser.add_argument('-v', '--verbose', action='store_true')

    args = parser.parse_args()
    roots = set(args.roots)
    if os.path.isfile(VANILLA_PATHS_FNAME):
        VANILLA_PATHS = read_lines(VANILLA_PATHS_FNAME)

    os.chdir(os.path.abspath('/'.join(CALLING_DIR.split('/')[:-1])+'/'+ASSETS_DIR))

    main(roots, verbose=args.verbose)

