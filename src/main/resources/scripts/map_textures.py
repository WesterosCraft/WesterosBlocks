import os
import argparse
import numpy as np
import cv2

EXTS = {'png', 'jpg', 'jpeg'}
CALLING_DIR = os.path.dirname(__file__)
WORKING_DIR = os.getcwd()
SCRIPTS_DIR = 'scripts'
ASSETS_DIR = 'assets'
TEXTURES_DIR = 'textures'

ROOTS_CONFIG_FNAME = os.path.join(CALLING_DIR, 'roots.txt')
BLACKLIST_CONFIG_FNAME = os.path.join(CALLING_DIR, 'blacklist.txt')
STITCH_FNAME = os.path.join(CALLING_DIR, 'stitch.png')


def identity(I):
  return I


def traverse(rootdir, blacklist, func):
  for d in os.listdir(rootdir):
    path = rootdir+'/'+d
    if os.path.isdir(path) and not (d in blacklist or path in blacklist):
      traverse(path, blacklist, func)
    elif os.path.isfile(path) and any([e in path for e in EXTS]) and 'mcmeta' not in path:
      # TODO: somehow the format is getting changed here
      I = cv2.imread(path, cv2.IMREAD_UNCHANGED)
      I = func(I)
      cv2.imwrite(path, I)


def main(roots, blacklist, func=identity):
  for root in roots:
    traverse(root+'/'+TEXTURES_DIR, blacklist, func)


if __name__ == '__main__':
  parser = argparse.ArgumentParser()
  parser.add_argument('-r', '--roots', nargs='+', default=[])
  parser.add_argument('-b', '--blacklist', nargs='+', default=[])

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

  os.chdir(os.path.abspath('/'.join(CALLING_DIR.split('/')[:-1])+'/'+ASSETS_DIR))

  main(roots, blacklist)
  

