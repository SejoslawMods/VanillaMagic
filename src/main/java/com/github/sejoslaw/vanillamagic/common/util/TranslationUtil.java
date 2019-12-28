package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraftforge.fml.ForgeI18n;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TranslationUtil {
    /**
     * @return Returns translated string.
     */
    public static String translateToLocal(String text) {
        return ForgeI18n.parseMessage(text);
    }
}
