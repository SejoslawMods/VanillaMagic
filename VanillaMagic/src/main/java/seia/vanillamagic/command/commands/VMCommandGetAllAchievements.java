package seia.vanillamagic.command.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import seia.vanillamagic.core.VanillaMagicDebug;
import seia.vanillamagic.util.EntityHelper;

public class VMCommandGetAllAchievements 
{
	public static final String COMMAND_NAME = "giveAllAchievements";
	
	public static void execute(MinecraftServer server, ICommandSender sender, String[] args)
	{
		EntityPlayerMP playerMP = EntityHelper.getCommandSender(server, sender);
		if(playerMP != null)
		{
			VanillaMagicDebug.activate(playerMP);
		}
	}
}