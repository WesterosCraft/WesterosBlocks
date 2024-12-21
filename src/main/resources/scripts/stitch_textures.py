import os
import argparse
import numpy as np
import json
import cv2

EXTS = {'png', 'jpg', 'jpeg'}
CALLING_DIR = os.path.dirname(__file__)
WORKING_DIR = os.getcwd()
SCRIPTS_DIR = 'scripts'
ASSETS_DIR = 'assets'
TEXTURES_DIR = 'textures'
OUTPUT_DIR = os.path.join(CALLING_DIR, 'stitch')

WB_FNAME = 'WesterosBlocks.json'

ROOTS_CONFIG_FNAME = os.path.join(CALLING_DIR, 'roots.txt')
BLACKLIST_CONFIG_FNAME = os.path.join(CALLING_DIR, 'blacklist.txt')

TEXTURES = []


def trim(stitch):
  idx = stitch.shape[0]-1
  trim_h = 0
  while idx >= 0:
    if not stitch[idx,:,:].any():
      trim_h += 1
    else:
      break
    idx -= 1
  if trim_h > 0:
    return stitch[:-trim_h,:,:]
  else:
    return stitch


def pack(W, textures):
  I = textures.copy()
  H_bound = sum([t.shape[1] for t in I])
  total_height = 0
  ret = np.zeros((H_bound, W, 4))
  ypos = 0
  xpos = 0
  while I:
    if xpos+I[-1].shape[1] > ret.shape[1]:
      ypos += I[-1].shape[0]
      xpos = 0
    texture = I.pop()
    ret[ypos:ypos+texture.shape[0], xpos:xpos+texture.shape[1], :] = texture.copy()
    if xpos == 0:
      total_height += texture.shape[0]
    xpos += texture.shape[1]
  return trim(ret[:total_height, :, :])


def register(path):
  texture = cv2.imread(path, cv2.IMREAD_UNCHANGED)
  if texture.shape[2] == 3:
    texture = cv2.cvtColor(texture, cv2.COLOR_RGB2RGBA)
    texture[:,:,3] = np.ones((texture.shape[0], texture.shape[1]))*255
  TEXTURES.append(texture)


def traverse(rootdir, blacklist):
  for d in os.listdir(rootdir):
    path = rootdir+'/'+d
    if os.path.isdir(path) and not (d in blacklist or path in blacklist):
      traverse(path, blacklist)
    elif os.path.isfile(path) and any([e in path for e in EXTS]) and 'mcmeta' not in path:
      register(path)


def create_stitch(textures):
  textures.sort(key=lambda t:t.shape[0])

  W_max = max([t.shape[1] for t in textures])
  W_bound = sum([t.shape[1] for t in textures])

  min_stitch = None
  for W in np.linspace(W_max*2, W_bound, 50, dtype=int):
    ret = pack(W, textures)
    if min_stitch is None or ret.shape[0]*ret.shape[1] < min_stitch.shape[0]*min_stitch.shape[1]:
      min_stitch = ret
  print(f'Stitched {len(textures)}: {min_stitch.shape}')
  return min_stitch


def main(roots, blacklist, disaggregate=False, noctm=False):
  for root in roots:
    traverse(root+'/'+TEXTURES_DIR, blacklist)

  textures_all = TEXTURES.copy()
  min_stitch = create_stitch(textures_all)

  os.chdir(WORKING_DIR)
  cv2.imwrite(OUTPUT_DIR+'/atlas.png', min_stitch)


if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument('-r', '--roots', nargs='+', default=[])
  parser.add_argument('-b', '--blacklist', nargs='+', default=[])
  parser.add_argument('-d', '--disaggregate', action='store_true')
  parser.add_argument('-n', '--noctm', action='store_true')

  roots = []
  if os.path.exists(ROOTS_CONFIG_FNAME):
    with open(ROOTS_CONFIG_FNAME, 'r') as f:
      roots = [l.strip() for l in f.readlines()]
  blacklist = []
  if os.path.exists(BLACKLIST_CONFIG_FNAME):
    with open(BLACKLIST_CONFIG_FNAME, 'r') as f:
      blacklist = [l.strip() for l in f.readlines()]
  roots = set(roots)
  blacklist = set(blacklist)

  args = parser.parse_args()
  roots = roots.union(set(args.roots))
  blacklist = blacklist.union(set(args.blacklist))
  disaggregate = args.disaggregate
  noctm = args.noctm

  os.chdir(os.path.abspath('/'.join(CALLING_DIR.split('/')[:-1])+'/'+ASSETS_DIR))

  main(roots, blacklist, disaggregate=disaggregate, noctm=noctm)
  

