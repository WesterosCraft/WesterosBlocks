package com.westerosblocks.block.blockentity.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.block.blockentity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class WCFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    private final String blockName;

    protected WCFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        super(blockEntityType, pos, state, recipeType);
        this.blockName = "furnace";
    }

    public WCFurnaceBlockEntity(BlockPos pos, BlockState state, String blockName) {
        super(ModBlockEntities.getBlockEntityType(blockName), pos, state, RecipeType.SMELTING);
        this.blockName = blockName;
    }


    @Override
    protected Text getContainerName() {
        return Text.translatable("container." + WesterosBlocks.MOD_ID + ".furnace");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}
