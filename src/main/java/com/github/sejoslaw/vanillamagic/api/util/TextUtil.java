package com.github.sejoslaw.vanillamagic.api.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class TextUtil {
    public static final String ENTER = "\n";

    public static final String CHANGING = TextFormatting.OBFUSCATED.toString();
    public static final String BOLD = TextFormatting.BOLD.toString();
    public static final String ITALIC = TextFormatting.ITALIC.toString();
    public static final String STRIKED = TextFormatting.STRIKETHROUGH.toString();
    public static final String UNDERLINED = TextFormatting.UNDERLINE.toString();
    public static final String NONE = TextFormatting.RESET.toString();

    public static final String COLOR_BLACK = TextFormatting.BLACK.toString();
    public static final String COLOR_DARK_BLUE = TextFormatting.DARK_BLUE.toString();
    public static final String COLOR_DARK_GREEN = TextFormatting.DARK_GREEN.toString();
    public static final String COLOR_DARK_AQUA = TextFormatting.DARK_AQUA.toString();
    public static final String COLOR_DARK_RED = TextFormatting.DARK_RED.toString();
    public static final String COLOR_DARK_PURPLE = TextFormatting.DARK_PURPLE.toString();
    public static final String COLOR_GOLD = TextFormatting.GOLD.toString();
    public static final String COLOR_GREY = TextFormatting.GRAY.toString();
    public static final String COLOR_DARK_GREY = TextFormatting.DARK_GRAY.toString();
    public static final String COLOR_BLUE = TextFormatting.BLUE.toString();
    public static final String COLOR_GREEN = TextFormatting.GREEN.toString();
    public static final String COLOR_AQUA = TextFormatting.AQUA.toString();
    public static final String COLOR_RED = TextFormatting.RED.toString();
    public static final String COLOR_LIGHT_PURPLE = TextFormatting.LIGHT_PURPLE.toString();
    public static final String COLOR_YELLOW = TextFormatting.YELLOW.toString();
    public static final String COLOR_WHITE = TextFormatting.WHITE.toString();

    private TextUtil() {
    }

    /**
     * @return Returns given number of enters in text.
     */
    public static String getEnters(int numberOfEnters) {
        String s = "";

        for (int i = 0; i < numberOfEnters; ++i) {
            s += ENTER;
        }

        return s;
    }

    /**
     * @return Returns given string with VM prefix.
     */
    public static ITextComponent getVanillaMagicInfo(ITextComponent s) {
        return wrap("�a[VanillaMagic] �f" + s.getFormattedText());
    }

    /**
     * @return Returns the given position in form of String.
     */
    public static String constructPositionString(DimensionType dimensionType, BlockPos pos) {
        if (dimensionType == null || pos == null) {
            return "";
        }

        return "Dim=" + dimensionType.getId() + ", X=" + pos.getX() + ", Y=" + pos.getY() + ", Z=" + pos.getZ();
    }

    /**
     * @return Returns wrapped string with TextComponentString.
     */
    public static ITextComponent wrap(String s) {
        return new StringTextComponent(s);
    }
}