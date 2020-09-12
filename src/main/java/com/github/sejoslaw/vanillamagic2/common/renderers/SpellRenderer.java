package com.github.sejoslaw.vanillamagic2.common.renderers;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellRenderer extends EntityRenderer<EntitySpell> {
    public SpellRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public ResourceLocation getEntityTexture(EntitySpell entity) {
        return new ResourceLocation("textures/misc/shadow.png");
    }
}
