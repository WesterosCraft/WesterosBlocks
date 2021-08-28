package com.westeroscraft.westerosblocks.modelexport;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.westeroscraft.westerosblocks.WesterosBlockDef;
import com.westeroscraft.westerosblocks.WesterosBlocks;

import net.minecraft.block.Block;

public class LogVertBlockModelExport extends ModelExport {
    // Template objects for Gson export of block models
    public static class ModelObjectBottomTop {
        public String parent = "minecraft:block/cube_bottom_top";    // Use 'cube' model for multiple textures
        public Texture textures = new Texture();
    }
    public static class Texture {
        public String particle, bottom, top, side;
    }

	public static class ModelObject {
		public String parent;
	}

	public LogVertBlockModelExport(Block blk, WesterosBlockDef def, File dest) {
		super(blk, def, dest);
		addNLSString("block." + WesterosBlocks.MOD_ID + "." + def.blockName, def.label);
	}

	private static class ModelRec {
		String cond;
		int xrot, yrot;
		String ext;

		ModelRec(String c, int x, int y, String ex) {
			cond = c;
			xrot = x;
			yrot = y;
			ext = ex;
		}
	}

	private static final ModelRec[] MODELS = { new ModelRec("axis=x,up=false,down=false", 90, 90, "base"),
			new ModelRec("axis=x,up=false,down=true", 90, 90, "down"),
			new ModelRec("axis=x,up=true,down=false", 90, 90, "up"),
			new ModelRec("axis=x,up=true,down=true", 90, 90, "both"),
			new ModelRec("axis=y,up=false,down=false", 0, 0, "base"),
			new ModelRec("axis=y,up=false,down=true", 0, 0, "down"),
			new ModelRec("axis=y,up=true,down=false", 0, 0, "up"),
			new ModelRec("axis=y,up=true,down=true", 0, 0, "both"),
			new ModelRec("axis=z,up=false,down=false", 90, 0, "base"),
			new ModelRec("axis=z,up=false,down=true", 90, 0, "down"),
			new ModelRec("axis=z,up=true,down=false", 90, 0, "up"),
			new ModelRec("axis=z,up=true,down=true", 90, 0, "both"), };

	@Override
	public void doBlockStateExport() throws IOException {
		StateObject so = new StateObject();

		for (int i = 0; i < MODELS.length; i++) {
			ModelRec rec = MODELS[i];
			// Loop over the random sets we've got
			for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
				WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
				Variant var = new Variant();
				var.model = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName(rec.ext, setidx);
				if (rec.xrot > 0)
					var.x = rec.xrot;
				if (rec.yrot > 0)
					var.y = rec.yrot;
				var.weight = set.weight;
				so.addVariant(rec.cond, var, set.condIDs);
			}
		}
		this.writeBlockStateFile(def.blockName, so);
	}

	@Override
	public void doModelExports() throws IOException {
		boolean isTinted = def.isTinted();
		// Loop over the random sets we've got
		for (int setidx = 0; setidx < def.getRandomTextureSetCount(); setidx++) {
			WesterosBlockDef.RandomTextureSet set = def.getRandomTextureSet(setidx);
			// down=false, up=false
			ModelObjectBottomTop mod = new ModelObjectBottomTop();
			mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
			mod.textures.top = getTextureID(set.getTextureByIndex(1));
			mod.textures.side = getTextureID(set.getTextureByIndex(2)); // Standard side (up=false, down=false)
			mod.textures.particle = getTextureID(set.getTextureByIndex(2));
			if (isTinted) {
				mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube_bottom_top";
			}
			this.writeBlockModelFile(getModelName("base", setidx), mod);
			// down=true, up=false
			mod = new ModelObjectBottomTop();
			mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
			mod.textures.top = getTextureID(set.getTextureByIndex(1));
			mod.textures.side = getTextureID(set.getTextureByIndex(3)); // (up=false, down=true)
			mod.textures.particle = getTextureID(set.getTextureByIndex(2));
			if (isTinted) {
				mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube_bottom_top";
			}
			this.writeBlockModelFile(getModelName("down", setidx), mod);
			// down=false, up=true
			mod = new ModelObjectBottomTop();
			mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
			mod.textures.top = getTextureID(set.getTextureByIndex(1));
			mod.textures.side = getTextureID(set.getTextureByIndex(4)); // (up=true, down=false)
			mod.textures.particle = getTextureID(set.getTextureByIndex(2));
			if (isTinted) {
				mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube_bottom_top";
			}
			this.writeBlockModelFile(getModelName("up", setidx), mod);
			// down=true, up=true
			mod = new ModelObjectBottomTop();
			mod.textures.bottom = getTextureID(set.getTextureByIndex(0));
			mod.textures.top = getTextureID(set.getTextureByIndex(1));
			mod.textures.side = getTextureID(set.getTextureByIndex(5)); // (up=true, down=true)
			mod.textures.particle = getTextureID(set.getTextureByIndex(2));
			if (isTinted) {
				mod.parent = WesterosBlocks.MOD_ID + ":block/tinted/cube_bottom_top";
			}
			this.writeBlockModelFile(getModelName("both", setidx), mod);
		}
		// Build simple item model that refers to block model
		ModelObject mo = new ModelObject();
		mo.parent = WesterosBlocks.MOD_ID + ":block/generated/" + getModelName("base", 0);
		this.writeItemModelFile(def.blockName, mo);
		// Add tint overrides
		if (isTinted) {
			String tintres = def.getBlockColorMapResource();
			if (tintres != null) {
				ModelExport.addTintingOverride(def.blockName, "", tintres);
			}
		}
	}

	@Override
	public void doWorldConverterMigrate() throws IOException {
		String oldID = def.getLegacyBlockName();
		if (oldID == null)
			return;
		String oldVariant = def.getLegacyBlockVariant();
		addWorldConverterComment(def.legacyBlockID + "(" + def.label + ")");
		// BUild old variant map
		HashMap<String, String> oldstate = new HashMap<String, String>();
		HashMap<String, String> newstate = new HashMap<String, String>();
		oldstate.put("variant", oldVariant);
		for (String axis : AXIS) {
			oldstate.put("axis", axis);
			newstate.put("axis", axis);
			addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
		}
		oldstate.put("axis", "none");
		newstate.put("axis", "y");
		addWorldConverterRecord(oldID, oldstate, def.getBlockName(), newstate);
	}

}
