package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class TextUtils {
    /**
     * @return Translated text component.
     */
    public static String getFormattedText(String key) {
        return getFormattedText(translate(key));
    }

    /**
     * @return Formatted string from given component.
     */
    public static String getFormattedText(ITextComponent comp) {
        return comp.getString();
    }

    /**
     * @return Text component with translated text.
     */
    public static ITextComponent translate(String key) {
        return new TranslationTextComponent(key);
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
        return toComponent(getFormattedText(component) + text);
    }

    /**
     * @return String formatted information about the specified position.
     */
    public static String getPosition(IWorld world, BlockPos pos) {
        return WorldUtils.getIdName(world).toString() + "[x=" + pos.getX() + ", y=" + pos.getY() + ", z=" + pos.getZ() + "]";
    }

    /**
     * Adds new formatted message line to specified collection.
     */
    public static void addLine(List<ITextComponent> lines, String key, String value) {
        lines.add(toComponent(buildMessageLine(key, value)));
    }

    /**
     * @return Input string with capitalised first letter.
     */
    public static String firstLetterToUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Adds new formatted message line to specified collection.
     */
    public static String buildMessageLine(String key, String value) {
        return TextFormatting.GREEN + getFormattedText(key) + TextFormatting.WHITE + " " + value;
    }

    /**
     * @return Combines given key and value and returns it in a form of sendable message.
     */
    public static ITextComponent buildServerSideMessage(String key, String value) {
        return toComponent(key + "!<>!" + value);
    }

    /**
     * @return Parses specified message read from server.
     */
    public static ITextComponent parseServerSideMessage(String message) {
        String[] parts = message.split("!<>!");
        return toComponent(buildMessageLine(parts[0], getFormattedText(parts[1])));
    }
}
