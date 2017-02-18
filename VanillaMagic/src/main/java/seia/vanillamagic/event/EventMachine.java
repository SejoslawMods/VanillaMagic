package seia.vanillamagic.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.api.tileentity.machine.IMachine;

public class EventMachine extends Event
{
	public final IMachine machine;
	
	public EventMachine(IMachine machine)
	{
		this.machine = machine;
	}
	
	/**
	 * This {@link Event} is fired BEFORE the given {@link IMachine} performs it's work.
	 */
	public static class Work extends EventMachine
	{
		public Work(IMachine machine) 
		{
			super(machine);
		}
	}
}