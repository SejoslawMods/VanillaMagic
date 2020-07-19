package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class TextUtils {
    /**
     * @param key Key taken from translation file.
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
    public static ITextComponent combine(String key, String text) {
        return toComponent(translate(key).getFormattedText() + text);
    }
}
