package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;
import com.westeroscraft.westerosblocks.blocks.WCWallBlock;

import net.minecraft.world.level.block.Block;

public class WallBlockModelExport extends ModelExport {
	// Template objects for Gson export of block models
	public static class ModelObjectPost {
		public String parent = WesterosBlocks.MOD_ID + ":block/untinted/template_wall_post"; // Use 'wall_post' model
																								// for single texture
		public Texture textures = new Texture();
	}

	public static class ModelObjectSide {
		public String parent = WesterosBlocks.MOD_ID + ":block/untinted/template_wall_side"; // Use 'wall_side' model
																								// for single texture
		public Texture textures = new Texture();
	}

	public static class ModelObjectTall {
		public String parent = WesterosBlocks.MOD_ID + ":block/untinted/template_wall_side_tall"; // Use
																									// 'wall_side_tall'
																									// model for single
																									// texture
		public Texture textures = new Texture();
	}

	public static class Texture {
		public String bottom, top, side;
	}

	public static class ModelObject {
		public String parent = WesterosBlocks.MOD_ID + ":block/untinted/wall_inventory";
		public Texture textures = new Texture();
	}

	private final WCWallBlock wblk;

	public WallBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
		super(blk, def, dest);
		wblk = (WCWallBlock) blk;
		addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
	}

	protected String getModelName(String ext, int setidx) {
		return def.blockName + "/" + ext + ("-v" + (setidx + 1));
	}

	private static class ModelPart {
		String modExt;
		WhenRec when;
		Boolean uvlock;
		Integer y;

		ModelPart(String mx, String u, String n, String s, String e, String w, Boolean uvlock, Integer y) {
			this.modExt = mx;
			this.when = new WhenRec();
			this.when.up = u;
			this.when.north = n;
			this.when.south = s;
			this.when.east = e;
			this.when.west = w;
			this.uvlock = uvlock;
			this.y = y;
		}
	};

	private static ModelPart[] PARTS = {
			// Post
			new ModelPart("post", "true", null, null, null, null, null, null),
			// North low
			new ModelPart("side", null, "low", null, null, null, true, null),
			// North tall
			new ModelPart("side_tall", null, "tall", null, null, null, true, null),
			// East low
			new ModelPart("side", null, null, null, "low", null, true, 90),
			// East tall
			new ModelPart("side_tall", null, null, null, "tall", null, true, 90),
			// South low
			new ModelPart("side", null, null, "low", null, null, true, 180),
			// South tall
			new ModelPart("side_tall", null, null, "tall", null, null, true, 180),
			// East low
			new ModelPart("side", null, null, null, null, "low", true, 270),
			// East tall
			new ModelPart("side_tall", null, null, null, null, "tall", true, 270)
	};

	@Override
	public void doBlockStateExport() throws IOException {
		StateObject so = new StateObject();
		// Loop through the parts
		for (ModelPart mp : PARTS) {
			for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
				WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
				Apply a = new Apply();
				a.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(mp.modExt, setidx);
				a.weight = set.weight;
				if (mp.uvlock != null)
					a.uvlock = mp.uvlock;
				if (mp.y != null)
					a.y = mp.y;

				so.addStates(mp.when, a, null);
			}
		}

		this.writeBlockStateFile(def.blockName, so);
	}

	@Override
	public void doModelExports() throws IOException {
		boolean isTinted = def.isTinted();
		WesterosBlockDef.RandomTextureSet set;

		for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
			set = def.getRandomTextureSet(setidx);

			ModelObjectPost mod = new ModelObjectPost();
			mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
			mod.textures.top = getTextureID(set.getTextureByIndex(1));
			mod.textures.side = getTextureID(set.getTextureByIndex(2));
			if (isTinted)
				mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/template_wall_post";
			this.writeBlockModelFile(getModelName("post", setidx), mod);
			// Side model
			ModelObjectSide smod = new ModelObjectSide();
			smod.textures.bottom = getTextureID(set.getTextureByIndex(0));
			smod.textures.top = getTextureID(set.getTextureByIndex(1));
			smod.textures.side = getTextureID(set.getTextureByIndex(2));
			if (isTinted) {
				smod.parent = WesterosBlocks.MOD_ID + ":block/tinted/template_wall_side" +
						((wblk.wallSize == WCWallBlock.WallSize.SHORT) ? "_2" : "");
			} else {
				smod.parent = WesterosBlocks.MOD_ID + ":block/untinted/template_wall_side" +
						((wblk.wallSize == WCWallBlock.WallSize.SHORT) ? "_2" : "");
			}
			this.writeBlockModelFile(getModelName("side", setidx), smod);
			// Tall side model
			ModelObjectSide tsmod = new ModelObjectSide();
			tsmod.textures.bottom = getTextureID(set.getTextureByIndex(0));
			tsmod.textures.top = getTextureID(set.getTextureByIndex(1));
			tsmod.textures.side = getTextureID(set.getTextureByIndex(2));
			if (isTinted)
				tsmod.parent = WesterosBlocks.MOD_ID + ":block/tinted/template_wall_side_tall";
			else
				tsmod.parent = WesterosBlocks.MOD_ID + ":block/untinted/template_wall_side_tall";
			this.writeBlockModelFile(getModelName("side_tall", setidx), tsmod);
		}
		// Build simple item model that refers to fence inventory model
		ModelObject mo = new ModelObject();
		set = def.getRandomTextureSet(0);
		mo.textures.bottom = getTextureID(set.getTextureByIndex(0));
		mo.textures.top = getTextureID(set.getTextureByIndex(1));
		mo.textures.side = getTextureID(set.getTextureByIndex(2));
		if (isTinted)
			mo.parent = WesterosBlocks.MOD_ID + ":block/tinted/wall_inventory";
		this.writeItemModelFile(def.getBlockName(), mo);

		// Handle tint resources
		if (isTinted) {
			String tintres = def.getBlockColorMapResource();
			if (tintres != null) {
				ModelExport.addTintingOverride(def.getBlockName(), "", tintres);
			}
		}
	}

	@Override
	public void doWorldConverterMigrate() throws IOException {
		String oldID = def.getLegacyBlockName();
		if (oldID == null)
			return;
		String oldVariant = def.getLegacyBlockVariant();
		addWorldConverterComment(def.legacyBlockID + "(" + def.label + ") - need wall connection mapping");
		// BUild old variant map
		HashMap<String, String> oldstate = new HashMap<String, String>();
		HashMap<String, String> newstate = new HashMap<String, String>();
		oldstate.put("variant", "cobblestone");
		oldstate.put("variant2", oldVariant);
		// No metadata other than variant - need filter for all of this - just pass one
		// combination
		oldstate.put("north", "false");
		newstate.put("north", "none");
		oldstate.put("south", "false");
		newstate.put("south", "none");
		oldstate.put("east", "false");
		newstate.put("east", "none");
		oldstate.put("west", "false");
		newstate.put("west", "none");
		oldstate.put("up", "false");
		newstate.put("up", "false");
		newstate.put("waterlogged", "false");
		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
	}
}
