package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.core.VMEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Provides reflection-styled, server-side safe access to client classes.
 * Allows to have part of code which call client-specific code on server classes.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ClientUtils {
    public static final String SCREEN_CLASS = "net.minecraft.client.gui.screen.Screen";
    public static final String VM_GUI_CLASS = "com.github.sejoslaw.vanillamagic2.common.guis.VMGui";
    public static final String QUEST_GUI_CLASS = "com.github.sejoslaw.vanillamagic2.common.guis.QuestGui";
    public static final String VM_TILE_ENTITY_DETAILS_GUI_CLASS = "com.github.sejoslaw.vanillamagic2.common.guis.VMTileEntityDetailsGui";
    public static final String MINECRAFT_CLASS = "net.minecraft.client.Minecraft";
    public static final String TEXT_COMPONENT_CLASS = "net.minecraft.util.text.ITextComponent";
    public static final String CLIENT_WORLD_CLASS = "net.minecraft.client.world.ClientWorld";

    /**
     * @return Minecraft instance; null on server-side.
     */
    public static Object getMinecraft() {
        if (!VMEvents.isClient()) {
            return null;
        }

        try {
            Class<?> mcClass = Class.forName(MINECRAFT_CLASS);
            Method getInstanceMethod = mcClass.getMethod("getInstance");
            return getInstanceMethod.invoke(null);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @return Client World; null on server-side.
     */
    public static IWorld getClientWorld() {
        if (!VMEvents.isClient()) {
            return null;
        }

        try {
            Class<?> mcClass = Class.forName(MINECRAFT_CLASS);
            Object mcInstance = getMinecraft();
            Field worldField = mcClass.getField("world");
            return (IWorld) worldField.get(mcInstance);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * @return Client Player Entity; null on server-side.
     */
    public static PlayerEntity getClientPlayer() {
        if (!VMEvents.isClient()) {
            return null;
        }

        try {
            Class<?> mcClass = Class.forName(MINECRAFT_CLASS);
            Object mcInstance = getMinecraft();
            Field playerField = mcClass.getField("player");
            return (PlayerEntity) playerField.get(mcInstance);
        } catch (Exception ex) {
            return null;
        }
    }

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

    /**
     * Adds message from translation file with the given key to the Chat window.
     */
    public static void addChatMessage(String key) {
        addChatMessage(TextUtils.translate(key));
    }

    /**
     * Adds message to the Chat window.
     */
    public static void addChatMessage(ITextComponent message) {
        if (!VMEvents.isClient()) {
            return;
        }

        try {
            Class<?> vmGuiClass = Class.forName(VM_GUI_CLASS);
            Method addChatMessageMethod = vmGuiClass.getDeclaredMethod("addChatMessage", Class.forName(TEXT_COMPONENT_CLASS));
            addChatMessageMethod.invoke(null, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds specified Entity on a Client World.
     */
    public static void addEntity(int entityId, Entity entity) {
        IWorld world = getClientWorld();

        try {
            Class<?> clientWorldClass = Class.forName(CLIENT_WORLD_CLASS);
            Method addEntityMethod = clientWorldClass.getMethod("addEntity", int.class, Entity.class);
            addEntityMethod.invoke(world, entityId, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
