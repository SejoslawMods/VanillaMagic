package seia.vanillamagic.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.quest.Quest;

/**
 * Base class for all the Quest based events.
 */
public class EventQuest extends Event
{
	public final Quest quest;
	
	public EventQuest(Quest quest)
	{
		this.quest = quest;
	}
	
	/**
	 * This {@link Event} is fired BEFORE the given {@link Quest} is added to the list.
	 */
	public static class EventAddQuest extends EventQuest
	{
		public EventAddQuest(Quest quest) 
		{
			super(quest);
		}
	}
}