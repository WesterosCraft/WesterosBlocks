package com.westerosblocks.item.custom;
import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricShieldItem;
import net.minecraft.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class ModShieldItem extends FabricShieldItem implements GeoItem {
    public ModShieldItem(Settings settings, int cooldownTicks, int enchantability, Item repairItems) {
        super(settings, cooldownTicks, enchantability, repairItems);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }
}