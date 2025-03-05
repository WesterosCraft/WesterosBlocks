package com.westerosblocks.item.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.item.client.StarkShieldRenderer;
import com.westerosblocks.item.client.TullyShieldRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class TullyShieldItem extends ModShieldItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int cooldownTicks;
    private final Item repairItem;

    public TullyShieldItem(Settings settings, int cooldownTicks, int enchantability, Item repairItems) {
        super(settings, cooldownTicks, enchantability, repairItems);
        this.cooldownTicks = cooldownTicks;
        this.repairItem = repairItems;

        //TODO do we need this? Register our item for server-side animation handling
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    // Animation predicate for the shield
//    private PlayState predicate(AnimationState<StarkShieldItem> state) {
    // Check if we're in first person view to handle perspective-specific animations
    // You can customize this based on your needs
//        state.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
//        return PlayState.CONTINUE;
//    }

//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
//    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private TullyShieldRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null) {

                    this.renderer = new TullyShieldRenderer(
                            new DefaultedItemGeoModel<>(WesterosBlocks.id("tully_heater_shield"))
                    );
                }
                return this.renderer;
            }
        });
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(this.repairItem) || super.canRepair(stack, ingredient);
    }

    @Override
    public int getEnchantability() {
        return 15;
    }

}
