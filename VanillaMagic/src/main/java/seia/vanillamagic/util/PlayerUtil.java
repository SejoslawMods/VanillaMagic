package seia.vanillamagic.util;

import javax.annotation.Nullable;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatisticsManager;

/**
 * Class which wraps various EntityPlayer related things.
 */
public class PlayerUtil 
{
	private PlayerUtil()
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
		if (isSinglePlayer(player)) 
			return toSinglePlayer(player).getStatFileWriter();
		else if (isMultiPlayer(player))
			return toMultiPlayer(player).getStatFile();
		return null;
	}
}