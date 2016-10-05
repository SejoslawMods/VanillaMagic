package seia.vanillamagic.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.quest.IQuest;

/**
 * Base class for all the Quest based events.
 */
public class EventQuest extends Event
{
	public final IQuest quest;
	
	public EventQuest(IQuest quest)
	{
		this.quest = quest;
	}
	
	/**
	 * This {@link Event} is fired BEFORE the given {@link IQuest} is added to the list.
	 */
	public static class EventAddQuest extends EventQuest
	{
		public EventAddQuest(IQuest quest) 
		{
			super(quest);
		}
	}
}