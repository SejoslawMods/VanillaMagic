package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.event.EventQuest.EventAddQuest;

public class QuestList
{
	private static List<IQuest> QUESTS = new ArrayList<IQuest>();
	private static Map<String, IQuest> QUESTS_MAP = new HashMap<String, IQuest>();
	
	private QuestList()
	{
	}
	
	/**
	 * Main method for adding Quests.
	 */
	public static void addQuest(IQuest q)
	{
		EventAddQuest event = new EventAddQuest(q);
		QuestList.QUESTS.add(q);
		QuestList.QUESTS_MAP.put(q.getUniqueName(), q);
	}
	
	/**
	 * Returns Quest from the Map, where "key" is a uniqueName of the Quest.<br>
	 * Returns NULL if there is no Quest registered at the given "key".
	 */
	@Nullable
	public static IQuest get(String key)
	{
		IQuest q = QUESTS_MAP.get(key);
		if(q == null)
		{
			VanillaMagic.LOGGER.log(Level.ERROR, "Can't find Quest for key: " + key);
		}
		return q;
	}
	
	/**
	 * Returns Quest from the List.
	 */
	public static IQuest get(int index)
	{
		return QUESTS.get(index);
	}
	
	/**
	 * Returns Quest List size.
	 */
	public static int size()
	{
		return QUESTS.size();
	}
	
	/**
	 * Returns Quests as List.
	 */
	public static List<IQuest> getQuests()
	{
		return QUESTS;
	}
	
	/**
	 * Returns Quests as Map. Where key is a 'uniqueName' of the Quest.
	 */
	public static Map<String, IQuest> getQuestsMap()
	{
		return QUESTS_MAP;
	}
}