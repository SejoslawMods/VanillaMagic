package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class TextUtils {
    /**
     * @return Text component with translated text.
     */
    public static ITextComponent translate(String key) {
        return new TranslationTextComponent(key);
    }

    /**
     * Adds message from translation file with the given key to the Chat window.
     */
    public static void addChatMessage(String key) {
        ITextComponent message = translate(key);
        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(message);
    }

    /**
     * @return Message wrapped in TextComponent.
     */
    public static ITextComponent toComponent(String message) {
        return new StringTextComponent(message);
    }

    /**
     * @return Combined component from given key with the message from the second argument.
     */
    public static ITextComponent combine(ITextComponent component, String text) {
        return toComponent(component.getFormattedText() + text);
    }

    /**
     * @return String formatted information about the specified position.
     */
    public static String getPosition(World world, BlockPos pos) {
        return world.getDimension().getType().getRegistryName().toString() + "[" + pos.toString() + "]";
    }
}
