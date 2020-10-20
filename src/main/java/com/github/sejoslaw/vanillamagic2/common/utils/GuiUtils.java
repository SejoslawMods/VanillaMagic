package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.core.VMEvents;

import java.lang.reflect.Method;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class GuiUtils {
    /**
     * Minecraft's Screen client-side class.
     */
    private static final String SCREEN_CLASS = "net.minecraft.client.gui.screen.Screen";

    public static final String VM_GUI_CLASS = "com.github.sejoslaw.vanillamagic2.common.guis.VMGui";
    public static final String QUEST_GUI_CLASS = "com.github.sejoslaw.vanillamagic2.common.guis.QuestGui";
    public static final String VM_TILE_ENTITY_DETAILS_GUI_CLASS = "com.github.sejoslaw.vanillamagic2.common.guis.VMTileEntityDetailsGui";

    /**
     * Opens specified GUI on Client side.
     */
    public static void displayGui(String guiClassName, Object... args) {
        if (!VMEvents.isClient()) {
            return;
        }

        try {
            Class<?> guiClass = Class.forName(guiClassName);
            Object guiObject = guiClass.getConstructors()[0].newInstance(args);

            Class<?> vmGuiClass = Class.forName(VM_GUI_CLASS);
            Method displayGuiMethod = vmGuiClass.getDeclaredMethod("displayGui", Class.forName(SCREEN_CLASS));
            displayGuiMethod.invoke(null, guiObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
