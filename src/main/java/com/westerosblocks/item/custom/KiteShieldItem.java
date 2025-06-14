package com.westerosblocks.item.custom;

import com.westerosblocks.WesterosBlocks;
import com.westerosblocks.item.client.KiteShieldRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;

import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class KiteShieldItem extends ModShieldItem  {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Item repairItem;
    private final String path;

    public KiteShieldItem(Settings settings, int cooldownTicks, int enchantability, Item repairItems, String path) {
        super(settings, cooldownTicks, enchantability, repairItems);
        this.repairItem = repairItems;
        this.path = path;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoItemRenderer<KiteShieldItem> renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null) {

                    this.renderer = new KiteShieldRenderer(
                            new DefaultedItemGeoModel<>(WesterosBlocks.id(path))
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