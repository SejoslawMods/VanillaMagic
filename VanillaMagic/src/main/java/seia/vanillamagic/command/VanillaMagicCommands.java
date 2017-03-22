package seia.vanillamagic.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class VanillaMagicCommands 
{
	public static final List<ICommand> REGISTERED_COMMANDS = new ArrayList<>();
	
	public static void init(FMLInitializationEvent event) 
	{
		registerCommand(new VMCommand());
	}
	
	private static void registerCommand(ICommand command)
	{
		ClientCommandHandler.instance.registerCommand(command);
		REGISTERED_COMMANDS.add(command);
	}
}