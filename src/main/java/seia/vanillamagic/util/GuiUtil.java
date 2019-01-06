package seia.vanillamagic.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;

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
	public static GuiNewChat getChatGui() {
		return Minecraft.getMinecraft().ingameGUI.getChatGUI();
	}
}