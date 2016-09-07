package seia.vanillamagic.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.machine.IMachine;

/**
 * This {@link Event} is fired BEFORE the given {@link IMachine} performs it's work.
 */
public class EventMachineWork extends Event
{
	public final IMachine machine;
	
	public EventMachineWork(IMachine machine)
	{
		this.machine = machine;
	}
}