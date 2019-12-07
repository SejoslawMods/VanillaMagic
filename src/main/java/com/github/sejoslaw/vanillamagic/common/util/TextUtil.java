package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.ForgeI18n;

/**
 * Class which store various methods connected with text and messages.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class TextUtil {
    public static final String ENTER = "\n";

    public static final String CHANGING = "�k";
    public static final String BOLD = "�l";
    public static final String ITALIC = "�o";
    public static final String STRIKED = "�m";
    public static final String UNDERLINED = "�n";
    public static final String NONE = "�r";

    public static final String COLOR_BLACK = "�0";
    public static final String COLOR_DARK_BLUE = "�1";
    public static final String COLOR_DARK_GREEN = "�2";
    public static final String COLOR_TUEQUOISE = "�3";
    public static final String COLOR_DARK_RED = "�4";
    public static final String COLOR_PURPLE = "�5";
    public static final String COLOR_ORANGE = "�6";
    public static final String COLOR_GREY = "�7";
    public static final String COLOR_DARK_GREY = "�8";
    public static final String COLOR_BLUE = "�9";
    public static final String COLOR_GREEN = "�a";
    public static final String COLOR_CYAN = "�b";
    public static final String COLOR_RED = "�c";
    public static final String COLOR_PINK = "�d";
    public static final String COLOR_YELLOW = "�e";
    public static final String COLOR_WHITE = "�f";

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
     * @return Returns translated string.
     */
    public static String translateToLocal(String text) {
        return ForgeI18n.parseMessage(text);
    }

    /**
     * @return Returns given string with VM prefix.
     */
    public static ITextComponent getVanillaMagicInfo(ITextComponent s) {
        return new StringTextComponent("�a[VanillaMagic] �f" + s.getFormattedText());
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