package com.westerosblocks.block.entity.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class WCFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private final String furnaceType;

    public WCFurnaceBlockEntity(BlockPos pos, BlockState state, String furnaceType) {
        super(ModBlockEntities.getBlockEntityType(furnaceType), pos, state, RecipeType.SMELTING);
        this.furnaceType = furnaceType;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container." + WesterosBlocks.MOD_ID + "." + furnaceType);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
