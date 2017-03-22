package seia.vanillamagic.command.commands;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import seia.vanillamagic.command.VMCommand;
import seia.vanillamagic.core.VanillaMagicDebug;
import seia.vanillamagic.util.EntityHelper;

public class VMCommandGetAllAchievements 
{
	public static final String COMMAND_NAME = "giveAllAchievements";
	
	public static void execute(VMCommand vmCommand, MinecraftServer server, ICommandSender sender, String[] args)
	{
		Entity entitySender = sender.getCommandSenderEntity();
		if(entitySender != null)
		{
			if(entitySender instanceof EntityPlayerSP)
			{
				EntityPlayerMP playerMP = EntityHelper.getEntityPlayerMPFromSP(server, (EntityPlayerSP) entitySender);
				if(playerMP != null)
				{
					VanillaMagicDebug.activate(playerMP);
				}
			}
		}
	}
}