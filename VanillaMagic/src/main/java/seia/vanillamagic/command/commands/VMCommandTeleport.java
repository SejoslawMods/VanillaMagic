package seia.vanillamagic.command.commands;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import seia.vanillamagic.magic.spell.spells.teleport.TeleportHelper;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.WorldHelper;

public class VMCommandTeleport
{
	public static final String COMMAND_NAME = "tp";
	
	public static void execute(MinecraftServer server, ICommandSender sender, String[] args)
	{
		Entity commandSender = sender.getCommandSenderEntity();
		if(!(commandSender instanceof EntityPlayerSP))
		{
			return;
		}
		EntityPlayerMP playerMP = EntityHelper.getCommandSender(server, sender);
		if(playerMP != null)
		{
			try
			{
				float posX = Float.valueOf(args[1]);
				float posY = Float.valueOf(args[2]);
				float posZ = Float.valueOf(args[3]);
				BlockPos pos = new BlockPos(posX, posY, posZ);
				int currentDim = WorldHelper.getDimensionID(playerMP);
				try
				{
					int newDim = Integer.valueOf(args[4]); // Exception can be thrown here
					if(!DimensionManager.isDimensionRegistered(newDim))
					{
						EntityHelper.addChatComponentMessageNoSpam(playerMP, "Dimension " + newDim + " is not registered."); // Register new Dimension ???
						return;
						//TODO: Add dynamic World creation
//						WorldHelper.regiterNewDimension(newDim, pos);
					}
					
					if(currentDim == newDim) // Teleport in the same Dimension
					{
						TeleportHelper.teleportEntitySynchronized(playerMP, (EntityPlayerSP) commandSender, pos);
					}
					else // Teleport to new Dimension TODO:
					{
//						TeleportHelper.changePlayerDimensionWithoutPortal(playerMP, newDim, pos);
//						TeleportHelper.entityChangeDimension(playerMP, newDim);
//						TeleportHelper.teleportEntity(playerMP, pos);
//						TeleportHelper.entityChangeDimension(commandSender, newDim);
//						TeleportHelper.teleportEntity(commandSender, pos);
//						TeleportHelper.changeDimension(playerMP, newDim, pos);
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					// If Exception was thrown teleport inside this Dimension because new Dimension wasn't specified.
					TeleportHelper.teleportEntitySynchronized(playerMP, (EntityPlayerSP) commandSender, pos);
				}
				catch(Exception e)
				{
				}
			}
			catch(Exception e)
			{
			}
		}
	}
}