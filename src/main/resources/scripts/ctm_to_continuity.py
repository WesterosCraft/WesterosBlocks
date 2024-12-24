# NOTE:
# - some .mcmeta contain both animation and ctm; will need to split these
# - we will not be able to handle custom connect methods, e.g., connect_state
# - optifine doesn't seem to support 'layer' as a generic property anymore; need to test and ensure that blocks have correct render layer in definitions

import os
import argparse
import numpy as np
import json
import cv2

TILE_EXTS = {'png', 'jpg', 'jpeg'}
CTM_EXT = 'mcmeta'

SCRIPTS_DIR = 'scripts'
ASSETS_DIR = 'assets'
TEXTURES_DIR = 'textures'
MODELS_DIR = 'models'
BLOCK_TEXTURES_DIR = TEXTURES_DIR + '/block'
BLOCK_MODELS_DIR = MODELS_DIR + '/block'

CALLING_DIR = os.path.dirname(__file__)
WORKING_DIR = os.getcwd()
OUTPUT_DIR = os.path.join(CALLING_DIR, 'continuity')

WB_FNAME = 'WesterosBlocks.json'

DEBUG_IDX = []


METHOD_MAP = {
  'ctm_vertical': 'vertical',
  'ctm_horizontal': 'horizontal',
  'westeros_vertical': 'vertical',
  'westeros_horizontal': 'horizontal',
  'westeros_v+h': 'vertical+horizontal',
  'westeros_h+v': 'horizontal+vertical'
}

TILE_MAP = {
  'vertical': [(1,1), (1,0), (0,1), (0,0)],
  'horizontal': [(1,0), (0,1), (1,1), (0,0)],
}


class CTMRoot:
  def __init__(self, name, source):
    self.name = name
    self.source = source,
    self.ctm = None
    self.texture = None

  def __hash__(self):
    return hash(self.name)
  

class CTMDef:
  def __init__(self, name, method):
    self.name = name
    self.method = method
    self.tiles = []
    self.tilenames = []
    self.layer = None
    self.connect = None
    self.method_props = {}

  def __hash__(self):
    return hash(self.name)
  
  def __str__(self):
    return json.dumps({k:v for k,v in self.__dict__.items() if k != 'tiles'})


class CTMCond:
  def __init__(self, name):
    self.name = name
    self.biomes = {}
    self.heights = {}

  def __hash__(self):
    return hash(self.name)
  
  def __str__(self):
    return json.dumps(self.__dict__)


CTM_PAIRS = {}
MODEL_TEXTURES = set()
CTM_ROOTS = set()
EXPLORED_CTM = set()

FINISHED_CTM = set()
ERROR_CTM = set()
TYPE_STATS = {}


def get_connect(ctm, default='state'):
  if 'extra' in ctm:
    if 'ignore_states' in ctm['extra']:
      if ctm['extra']['ignore_states']:
        return 'block'
      else:
        return 'state'
    else:
      print(f'** Unhandled extra: {ctm["extra"]}')
  return default


def parse_random(root: CTMRoot):
  # TODO
  pass


def parse_normal(root: CTMRoot):
  # TODO
  pass


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
  method = METHOD_MAP[root.ctm['type']]
  ctm = CTMDef(root.name, method)
  if 'layer' in root.ctm:
    ctm.layer = root.ctm['layer'].lower()
  ctm.connect = get_connect(root.ctm, default='state')
  
  grid = split_tiles(read_image(root.texture))
  for idx, (i,j) in enumerate(TILE_MAP[method]):
    ctm.tiles.append(grid[i][j])
    ctm.tilenames.append(f'{idx}')
  FINISHED_CTM.add(ctm)
  

def parse_westeros_directional(root: CTMRoot):
  # TODO
  pass


def parse_westeros_directional_cond(root: CTMRoot):
  # TODO
  pass


def parse_westeros_pillar(root: CTMRoot):
  # TODO
  pass


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
  'random': parse_random,
  'normal': parse_normal,
  'ctm_simple': parse_ctm_simple,
  'ctm': parse_ctm,
  'edges': parse_edges,
  'edges_full': parse_edges_full,
  'pattern': parse_pattern,
  'ctm_vertical': parse_ctm_directional,
  'ctm_horizontal': parse_ctm_directional,
  'westeros_vertical': parse_westeros_directional,
  'westeros_horizontal': parse_westeros_directional,
  'westeros_v+h': parse_westeros_directional,
  'westeros_h+v': parse_westeros_directional,
  'westeros_vertical_cond': parse_westeros_directional_cond,
  'westeros_horizontal_cond': parse_westeros_directional_cond,
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


def read_json(fname):
  """Read a JSON file."""
  with open(fname, 'r') as f:
    return json.load(f)
  

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
    raise Exception('Attempting to split {h} x {w} texture; not divisible by 32')
  tiles = []
  for i in range(0, h//32):
    rows = []
    for j in range(0, w//32):
      rows.append(texture[i*32:(i+1)*32, j*32:(j+1)*32, :])
    tiles.append(rows)
  return tiles


def is_tile(path):
  """Check if a path corresponds to a texture tile."""
  return any([e in path for e in TILE_EXTS]) and CTM_EXT not in path


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
  for root in roots:
    traverse(
      root+'/'+BLOCK_MODELS_DIR,
      is_model,
      register_model_textures
    )
    traverse(
      root+'/'+BLOCK_TEXTURES_DIR,
      is_tile,
      register_ctm_pair
    )
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
  print(f'Explored {len(EXPLORED_CTM)}/{len(all_ctm)} CTM .mcmeta files (missed {len(unexplored_ctm)})')
  # for x in unexplored_ctm:
  #   print(x)
  
  print(f'Parsing {len(CTM_ROOTS)} CTM roots')
  for idx, ctm_root in enumerate(CTM_ROOTS):
    if DEBUG_IDX and idx not in DEBUG_IDX:
      continue
    parse(ctm_root)

  print()
  print(f'{len(FINISHED_CTM)}/{len(CTM_ROOTS)} CTMs parsed')
  print(f'{len(ERROR_CTM)}/{len(CTM_ROOTS)} CTMs parsed with errors')
  print(f'{len(CTM_ROOTS)-(len(FINISHED_CTM)+len(ERROR_CTM))}/{len(CTM_ROOTS)} CTMs skipped')

  if verbose:
    print()
    print('CTM type stats:')
    for k,v in sorted(TYPE_STATS.items(), key=lambda x: x[1], reverse=True):
      print(f'{k}: {v}')

  # TODO: compile and write CTM classes to Continuity format


if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument('-r', '--roots', nargs='+', default=['westerosblocks', 'minecraft'])
  parser.add_argument('-v', '--verbose', action='store_true')

  args = parser.parse_args()
  roots = set(args.roots)

  os.chdir(os.path.abspath('/'.join(CALLING_DIR.split('/')[:-1])+'/'+ASSETS_DIR))

  main(roots, verbose=args.verbose)
  

