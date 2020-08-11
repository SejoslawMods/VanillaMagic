package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_O;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class OpenQuestBookHandler {
    private static final KeyBinding OPEN_QUEST_BOOK_KEY_BINDING = new KeyBinding("key.openQuestBook.desc", GLFW_KEY_O, "key.vm.category");

    public OpenQuestBookHandler() {
        ClientRegistry.registerKeyBinding(OPEN_QUEST_BOOK_KEY_BINDING);
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (OPEN_QUEST_BOOK_KEY_BINDING.isPressed()) {
            // TODO: Open GUI
        }
    }
}
