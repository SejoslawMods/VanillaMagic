package seia.vanillamagic.util;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

/**
 * Class which wraps various EntityPlayer related things.
 */
public class EntityUtil 
{
	private static final int _DELETION_ID = 2525277;
	private static int LAST_ADDED;
	
	private EntityUtil()
	{
	}
	
	/**
	 * @return Returns true if given player is a singleplayer.
	 */
	public static boolean isSinglePlayer(EntityPlayer player)
	{
		return player instanceof EntityPlayerSP;
	}
	
	/**
	 * @return Returns true if given player is a multiplayer.
	 */
	public static boolean isMultiPlayer(EntityPlayer player)
	{
		return player instanceof EntityPlayerMP;
	}
	
	/**
	 * @return Returns given Player casted as EntityPlayerSP.
	 */
	public static EntityPlayerSP toSinglePlayer(EntityPlayer player)
	{
		return (EntityPlayerSP) player;
	}
	
	/**
	 * @return Returns given Player casted as EntityPlayerMP.
	 */
	public static EntityPlayerMP toMultiPlayer(EntityPlayer player)
	{
		return (EntityPlayerMP) player;
	}
	
	/**
	 * @return Returns StatisticsManager connected with given Player.
	 */
	@Nullable
	public static StatisticsManager getStatisticsManager(EntityPlayer player)
	{
		if (isSinglePlayer(player)) return toSinglePlayer(player).getStatFileWriter();
		else if (isMultiPlayer(player)) return toMultiPlayer(player).getStatFile();
		return null;
	}
	
	/**
	 * @return Returns wrapped string with TextComponentString.
	 */
	public static ITextComponent wrap(String s)
	{
		return new TextComponentString(s);
	}
	
	/**
	 * Adds a message to Player's chat with no spam.
	 */
	public static void addChatComponentMessageNoSpam(EntityPlayer player, String msg)
	{
		addChatComponentMessageNoSpam(player, new String[]{ msg });
	}
	
	/**
	 * Adds a message to Player's chat with no spam.
	 */
	public static void addChatComponentMessageNoSpam(EntityPlayer player, String[] msg)
	{
		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		for (int i = _DELETION_ID + msg.length - 1; i <= LAST_ADDED; ++i) 
			chat.deleteChatLine(i);
		for (int i = 0; i < msg.length; ++i)
			chat.printChatMessageWithOptionalDeletion(wrap(TextUtil.getVanillaMagicInfo(msg[i])), _DELETION_ID + i);
		LAST_ADDED = _DELETION_ID + msg.length - 1;
	}
}