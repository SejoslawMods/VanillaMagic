package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import com.github.sejoslaw.vanillamagic2.common.guis.QuestGui;
import com.github.sejoslaw.vanillamagic2.common.guis.VMGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class OpenQuestGuiHandler {
    private static final KeyBinding OPEN_QUEST_GUI_KEY_BINDING = new KeyBinding("key.openQuestGui.desc", GLFW.GLFW_KEY_O, "key.vm.category");

    public OpenQuestGuiHandler() {
        ClientRegistry.registerKeyBinding(OPEN_QUEST_GUI_KEY_BINDING);
    }

    @SubscribeEvent()
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (event.getKey() == OPEN_QUEST_GUI_KEY_BINDING.getKey().getKeyCode() &&
                event.getAction() == GLFW.GLFW_PRESS &&
                Minecraft.getInstance().world != null &&
                Minecraft.getInstance().player != null &&
                Minecraft.getInstance().currentScreen == null) {
            VMGui.displayGui(new QuestGui());
        }
    }
}
