//package com.westerosblocks.needsported.mixin;
//
//import com.westeroscraft.westerosblocks.Config;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.LevelReader;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.DoorBlock;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.shapes.Shapes;
//import net.minecraft.world.phys.shapes.VoxelShape;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(DoorBlock.class)
//public abstract class MixinDoorBlock extends Block
//{
//    public MixinDoorBlock(BlockBehaviour.Properties props) {
//    	super(props);
//    }
//
//	@Inject(method = "canSurvive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable=true)
//	public void doCanSurvive(BlockState p_52783_, LevelReader p_52784_, BlockPos p_52785_, CallbackInfoReturnable<Boolean> ci) {
//		if ((p_52783_.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) && Config.doorSurviveAny) {
//			ci.setReturnValue(true);
//		}
//	}
//
//	@Override
//    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
//		if (Config.doorNoConnect) {
//			return Shapes.empty();
//		}
//		return super.getBlockSupportShape(state, reader, pos);
//    }
//
//}
