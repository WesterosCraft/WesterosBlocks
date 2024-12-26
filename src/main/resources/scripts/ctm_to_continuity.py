# NOTE:
# - some .mcmeta contain both animation and ctm; will need to split these
# - we will not be able to handle custom connect methods, e.g., connect_state
# - optifine doesn't seem to support 'layer' as a generic property anymore; need to test and ensure that blocks have correct render layer in definitions
# - the 'orient' property for westeros_pillar replacements needs verification (supposedly, Continuity should use orient=texture by default)

import os
import argparse
import numpy as np
import json
import cv2

TILE_EXTS = {'png', 'jpg', 'jpeg'}
CTM_EXT = 'mcmeta'

VALID_CONNECT = ['block', 'tile', 'state']
VALID_LAYER = ['cutout_mipped', 'cutout', 'translucent']

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

DEBUG_IDX = []


METHOD_MAP = {
  'normal': 'fixed',
  'ctm_vertical': 'vertical',
  'ctm_horizontal': 'horizontal',
  'westeros_vertical': 'vertical',
  'westeros_horizontal': 'horizontal',
  'westeros_v+h': 'vertical+horizontal',
  'westeros_h+v': 'horizontal+vertical',
  'westeros_pillar': 'vertical'
}

TILE_MAP = {
  'vertical': [(1,1), (1,0), (0,1), (0,0)],
  'horizontal': [(1,0), (0,1), (1,1), (0,0)],
  'horizontal+vertical': [(0,i) for i in range(7)],
  'vertical+horizontal': [(0,i) for i in range(7)]
}


class CTMRoot:
  def __init__(self, name, source):
    self.name = name
    self.source = source,
    self.ctm = None
    self.texture = None

  def __hash__(self):
    return hash(self.name)
  
  def __str__(self):
    return f'{self.name} : {self.ctm}'
  

class CTMProp:
  def __init__(
      self,
      method = None,
      layer = None,
      connect = None,
      tiles = None,
      method_props = None
  ):
    self.method = method
    self.layer = layer
    self.connect = connect
    self.tiles = tiles if tiles else []
    self.method_props = method_props if method_props else {}

  def __str__(self):
    propdict = {k:(v if k != 'tiles' else list(range(len(v)))) for k,v in self.__dict__.items()}
    return json.dumps(propdict)
  

class CTMDef:
  def __init__(self, name):
    self.name = name

  def iterate_props(self) -> list[CTMProp]:
    pass

  def __hash__(self):
    return hash(self.name)
  

class CTMSingleDef(CTMDef):
  def __init__(self, name):
    super().__init__(name)
    self.prop = CTMProp()

  def iterate_props(self):
    return [self.prop]

  def __str__(self):
    return f'{self.name}:\n  ' + str(self.prop)
  

class CTMCond:
  def __init__(
      self,
      biomes=set(),
      heights=(float('-inf'), float('inf'))
    ):
    self.biomes = biomes
    self.heights = heights

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
  def __init__(self, name, props=None, default=None):
    super().__init__(name)
    self.props = props if props else {}
    self.default = default

  def iterate_props(self):
    ret = list(self.props.values())
    if self.default:
      ret.append(self.default)
    return ret
  
  def get_all_biomes(self):
    biomes = set()
    for propcond in self.props.keys():
      biomes |= propcond.biomes
    return biomes
  
  def __str__(self):
    strs = []
    for cond, prop in self.props.items():
      strs.append(f'<{str(cond)}>\n  ' + str(prop))
    if self.default:
      strs.append(f'<default>\n  ' + str(self.default))
    return f'{self.name}:\n' + '\n'.join(strs)


TEXTURE_PATHS = {}
CTM_PAIRS = {}
MODEL_TEXTURES = set()
CTM_ROOTS = set()
EXPLORED_CTM = set()

FINISHED_CTM = set()
ERROR_CTM = set()
TYPE_STATS = {}


def get_method(ctm):
  typ = ctm['type'].replace('_cond','')
  return METHOD_MAP[typ] if typ in METHOD_MAP else typ


def get_connect(ctm, default='state'):
  if 'extra' in ctm:
    if 'ignore_states' in ctm['extra']:
      if ctm['extra']['ignore_states']:
        return 'block'
      else:
        return 'state'
    elif 'connect_to_state' in ctm['extra']:
      state = ctm['extra']['connect_to_state']
      return f'state:{state}'
    else:
      print(f'** Unhandled extra: {ctm["extra"]}')
  return default


def get_layer(ctm):
  if 'layer' in ctm:
    layer = ctm['layer'].lower()
    if layer == 'solid':
      return None
    else:
      return layer
  return None


def get_tiles(texture, method):
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


def get_cond(ctm_cond):
  biomes = set()
  heights = (float('-inf'), float('inf'))
  if 'biomeNames' in ctm_cond:
    biomes = ctm_cond['biomeNames']
  if 'yPosMin' in ctm_cond:
    heights[0] = ctm_cond['yPosMin']
  if 'yPosMax' in ctm_cond:
    heights[1] = ctm_cond['yPosMax']
  return CTMCond(biomes=biomes, heights=heights)


def validate_ctm(ctm: CTMDef):
  for prop in ctm.iterate_props():
    if prop.connect and prop.connect not in VALID_CONNECT:
      return False
    if prop.layer and prop.layer not in VALID_LAYER:
      return False
  return True


def add_ctm(ctm):
  if validate_ctm(ctm):
    FINISHED_CTM.add(ctm)
  else:
    ERROR_CTM.add(ctm)


def parse_static(root: CTMRoot):
  ctm = CTMSingleDef(root.name)
  method = get_method(root.ctm)
  ctm.prop.method = method
  ctm.prop.layer = get_layer(root.ctm)
  ctm.prop.tiles = get_tiles(read_image(root.texture), method)
  add_ctm(ctm)


def parse_ctm_simple(root: CTMRoot):
  # TODO
  pass


def parse_ctm(root: CTMRoot):
  # TODO
  pass


def parse_pattern(root: CTMRoot):
  # TODO
  pass


def parse_edges(root: CTMRoot):
  # TODO
  pass


def parse_edges_full(root: CTMRoot):
  # TODO
  pass


def parse_ctm_directional(root: CTMRoot):
  ctm = CTMSingleDef(root.name)
  method = METHOD_MAP[root.ctm['type']]
  ctm.prop.method = method
  ctm.prop.layer = get_layer(root.ctm)
  ctm.prop.connect = get_connect(root.ctm, default='state')
  ctm.prop.tiles = get_tiles(read_image(root.texture), method)
  add_ctm(ctm)


def parse_ctm_directional_cond(root: CTMRoot):
  ctm = CTMCondDef(root.name)
  method = get_method(root.ctm)
  layer = get_layer(root.ctm)
  connect = get_connect(root.ctm, default='state')

  texture_cond = get_texture_path(root.ctm['textures'][0])
  for ctm_cond in root.ctm['extra']['conds']:
    cond = get_cond(ctm_cond)
    row, col = ctm_cond['rowOut'], ctm_cond['colOut']
    texture_cond_slice = split_slice(read_image(texture_cond), row, col, 2, 2)
    tiles = get_tiles(texture_cond_slice, method)
    ctm.props[cond] = CTMProp(method=method, layer=layer, connect=connect, tiles=tiles)

  tiles_default = get_tiles(read_image(root.texture), method)
  ctm.default = CTMProp(method=method, layer=layer, connect=connect, tiles=tiles_default)
  add_ctm(ctm)
    

def parse_westeros_pillar(root: CTMRoot):
  parse_ctm_directional(root) # TODO: verify


def parse_westeros_single(root: CTMRoot):
  # TODO
  pass


def parse_westeros_single_cond(root: CTMRoot):
  # TODO
  pass


def parse_westeros_cond(root: CTMRoot):
  # TODO
  pass


def parse_westeros_ctm_single(root: CTMRoot):
  # TODO
  pass


def parse_westeros_ctm(root: CTMRoot):
  # TODO
  pass


def parse_westeros_pattern(root: CTMRoot):
  # TODO
  pass


def parse_westeros_pattern_cond(root: CTMRoot):
  # TODO
  pass


def parse_westeros_ctm_pattern(root: CTMRoot):
  # TODO
  pass


def parse_westeros_ctm_pattern_cond(root: CTMRoot):
  # TODO
  pass


PARSE_DISPATCH = {
  'random': parse_static,
  'normal': parse_static,
  'ctm_simple': parse_ctm_simple,
  'ctm': parse_ctm,
  'edges': parse_edges,
  'edges_full': parse_edges_full,
  'pattern': parse_pattern,
  'ctm_vertical': parse_ctm_directional,
  'ctm_horizontal': parse_ctm_directional,
  'westeros_vertical': parse_ctm_directional,
  'westeros_horizontal': parse_ctm_directional,
  'westeros_v+h': parse_ctm_directional,
  'westeros_h+v': parse_ctm_directional,
  'westeros_vertical_cond': parse_ctm_directional_cond,
  'westeros_horizontal_cond': parse_ctm_directional_cond,
  'westeros_pillar': parse_westeros_pillar,
  'westeros_single': parse_westeros_single,
  'westeros_single_cond': parse_westeros_single_cond,
  'westeros_cond': parse_westeros_cond,
  'westeros_ctm_single': parse_westeros_ctm_single,
  'westeros_ctm': parse_westeros_ctm,
  'westeros_pattern': parse_westeros_pattern,
  'westeros_pattern_cond': parse_westeros_pattern_cond,
  'westeros_ctm+pattern': parse_westeros_ctm_pattern,
  'westeros_ctm+pattern_cond': parse_westeros_ctm_pattern_cond,
}


def parse(root: CTMRoot):
  """Dispatch each CTM root to a parse function depending on the CTM type."""
  typ = root.ctm['type']
  if typ in PARSE_DISPATCH:
    PARSE_DISPATCH[typ](root)
  else:
    print(f'No parse function defined for type "{typ}"')
  if typ in TYPE_STATS:
    TYPE_STATS[typ] += 1
  else:
    TYPE_STATS[typ] = 1


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


def read_json(fname):
  """Read a JSON file."""
  with open(fname, 'r') as f:
    return json.load(f)
  

def read_lines(fname):
  """Read lines of a text file."""
  with open(fname, 'r') as f:
    return [l.strip() for l in f.readlines()]
  

def read_image(fname):
  """Read an image file."""
  texture = cv2.imread(fname, cv2.IMREAD_UNCHANGED)
  if texture.shape[2] == 3:
    texture = cv2.cvtColor(texture, cv2.COLOR_RGB2RGBA)
    texture[:,:,3] = np.ones((texture.shape[0], texture.shape[1]))*255
  return texture


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


def is_tile(path):
  """Check if a path corresponds to a texture tile."""
  return any([e in path for e in TILE_EXTS]) and CTM_EXT not in path


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

  ctm_def = CTMRoot(name, path)
  
  _, ctm_path = CTM_PAIRS[name]
  EXPLORED_CTM.add(ctm_path)
  ctm = read_json(ctm_path)['ctm']

  if 'type' not in ctm and 'proxy' in ctm:
    proxy_ref = namespace_to_path(ctm['proxy'])
    proxy_tile, proxy_ctm_path = CTM_PAIRS[proxy_ref]
    proxy_ctm = read_json(proxy_ctm_path)['ctm']
    ctm_def.ctm = proxy_ctm
    ctm_def.texture = proxy_tile
    EXPLORED_CTM.add(proxy_ctm_path)
  elif 'type' in ctm:
    ctm_def.ctm = ctm
    ctm_def.texture = path
  else:
    print(f'* WARNING: {ctm_path} has unexpected format; type and proxy should be mutually exclusive')
    return
  
  CTM_ROOTS.add(ctm_def)


def is_model(path):
  """Check if a path is a model file."""
  return path.endswith('.json') and 'textures' in read_json(path)


def register_model_textures(path):
  """Register the texture paths contained within each model."""
  model = read_json(path)
  if 'textures' in model:
    for text in model['textures'].values():
      if not text.startswith('#'):
        MODEL_TEXTURES.add(namespace_to_path(text))


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
  for idx, ctm_root in enumerate(CTM_ROOTS):
    if DEBUG_IDX and idx not in DEBUG_IDX:
      continue
    parse(ctm_root)

  print()
  print(f'{len(FINISHED_CTM)}/{len(CTM_ROOTS)} CTMs parsed')
  print(f'{len(ERROR_CTM)}/{len(CTM_ROOTS)} CTMs parsed with errors')
  print(f'{len(CTM_ROOTS)-(len(FINISHED_CTM)+len(ERROR_CTM))}/{len(CTM_ROOTS)} CTMs skipped')

  if verbose:
    print('\nCTM type stats:')
    for k,v in sorted(TYPE_STATS.items(), key=lambda x: x[1], reverse=True):
      print(f'{k}: {v}')

  # for ctm in FINISHED_CTM:
  #   print(ctm)
  #   print()

  # TODO: compile and write CTM classes to Continuity format


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
  

