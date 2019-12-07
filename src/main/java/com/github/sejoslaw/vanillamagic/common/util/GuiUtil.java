package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.NewChatGui;

/**
 * Methods connected with GUIs.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class GuiUtil {
    private GuiUtil() {
    }

    /**
     * @return Returns Minecraft chat GUI.
     */
    public static NewChatGui getChatGui() {
        return Minecraft.getInstance().ingameGUI.getChatGUI();
    }
}