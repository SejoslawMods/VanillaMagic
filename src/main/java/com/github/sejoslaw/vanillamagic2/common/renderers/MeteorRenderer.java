package com.github.sejoslaw.vanillamagic2.common.renderers;

import com.github.sejoslaw.vanillamagic2.common.entities.EntityMeteor;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class MeteorRenderer extends EntityRenderer<EntityMeteor> {
    public MeteorRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public ResourceLocation getEntityTexture(EntityMeteor entity) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }
}
