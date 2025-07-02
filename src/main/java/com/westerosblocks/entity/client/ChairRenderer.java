package com.westerosblocks.entity.client;

import com.westerosblocks.entity.custom.ChairEntity;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class ChairRenderer extends EntityRenderer<ChairEntity> {
    public ChairRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(ChairEntity entity) {
        return null;
    }

    @Override
    public boolean shouldRender(ChairEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}