package seia.vanillamagic.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.api.quest.IQuest;

/**
 * Base class for all the Quest-based events.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventQuest extends Event {
	private final IQuest quest;

	public EventQuest(IQuest quest) {
		this.quest = quest;
	}

	/**
	 * @return Returns the Quest connected with this Event.
	 */
	public IQuest getQuest() {
		return quest;
	}

	/**
	 * This Event is fired BEFORE the given {@link IQuest} is added to the list.
	 */
	public static class Add extends EventQuest {
		public Add(IQuest quest) {
			super(quest);
		}
	}
}