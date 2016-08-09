package seia.vanillamagic.utils;

import net.minecraft.util.text.translation.I18n;

public class TextHelper 
{
	public static final String ENTER = "\n";
	
	public static final String CHANGING = "§k";
	public static final	String BOLD = "§l";
	public static final	String ITALIC = "§o";
	public static final	String STRIKED = "§m";
	public static final	String UNDERLINED = "§n";
	public static final	String NONE = "§r";
			
	public static final	String COLOR_BLACK = "§0";
	public static final	String COLOR_DARK_BLUE = "§1";
	public static final	String COLOR_DARK_GREEN = "§2";
	public static final	String COLOR_TUEQUOISE = "§3";
	public static final	String COLOR_DARK_RED = "§4";
	public static final	String COLOR_PURPLE = "§5";
	public static final	String COLOR_ORANGE = "§6";
	public static final	String COLOR_GREY = "§7";
	public static final	String COLOR_DARK_GREY = "§8";
	public static final	String COLOR_BLUE = "§9";
	public static final	String COLOR_GREEN = "§a";
	public static final	String COLOR_CYAN = "§b";
	public static final	String COLOR_RED = "§c";
	public static final	String COLOR_PINK = "§d";
	public static final	String COLOR_YELLOW = "§e";
	public static final	String COLOR_WHITE = "§f";
	
	private TextHelper()
	{
	}
	
	public static String getEnters(int numberOfEnters)
	{
		String s = "";
		for(int i = 0; i < numberOfEnters; i++)
		{
			s += ENTER;
		}
		return s;
	}
	
	public static String translateToLocal(String text)
	{
		return I18n.translateToLocal(text);
	}
}