package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.registries.EntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class RenderingHandler extends EventHandler {
    private static final RenderingHandler INSTANCE = new RenderingHandler();

    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        INSTANCE.registerRenderers(event, minecraft -> {
            RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SPELL.get(), manager -> new SpriteRenderer(manager, Minecraft.getInstance().getItemRenderer()));
            RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.METEOR.get(), manager -> new SpriteRenderer(manager, Minecraft.getInstance().getItemRenderer(), 6.0F, true));
        });
    }
}
