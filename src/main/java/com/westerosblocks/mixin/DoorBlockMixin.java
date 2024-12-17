package com.westerosblocks.mixin;

import com.westerosblocks.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoorBlock.class)
public class DoorBlockMixin extends Block {
    public DoorBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
    private void doCanSurvive(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        boolean doorSurviveAny = ModConfig.get().doorSurviveAny;
        if ((state.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER) && doorSurviveAny) {
            cir.setReturnValue(true);
        }
    }

    // TODO getBlockSupportShape doesnt exist anymore - find replacement
//    @Override
//    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
//        if (Config.doorNoConnect) {
//            return Shapes.empty();
//        }
//        return super.getBlockSupportShape(state, reader, pos);
//    }
}
