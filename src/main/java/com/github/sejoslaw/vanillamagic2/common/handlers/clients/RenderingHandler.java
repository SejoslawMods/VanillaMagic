package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.registries.EntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.renderers.SpellRenderer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class RenderingHandler extends EventHandler {
    private static final RenderingHandler INSTANCE = new RenderingHandler();

    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        INSTANCE.registerRenderers(event, minecraft -> {
            RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.SPELL.get(), SpellRenderer::new);
        });
    }
}
