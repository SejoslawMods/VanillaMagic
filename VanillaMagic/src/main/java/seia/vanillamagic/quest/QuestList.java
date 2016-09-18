package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.event.EventQuest.EventAddQuest;

public class QuestList
{
	private static List<Quest> QUESTS = new ArrayList<Quest>();
	private static Map<String, Quest> QUESTS_MAP = new HashMap<String, Quest>();
	
	private QuestList()
	{
	}
	
	/**
	 * Main method for adding Quests.
	 */
	public static void addQuest(Quest q)
	{
		EventAddQuest event = new EventAddQuest(q);
		QuestList.QUESTS.add(q);
		QuestList.QUESTS_MAP.put(q.uniqueName, q);
	}
	
	/**
	 * Returns Quest from the Map.
	 */
	public static Quest get(String key)
	{
		Quest q = QUESTS_MAP.get(key);
		if(q == null)
		{
			VanillaMagic.logger.log(Level.ERROR, "Can't find Quest for key: " + key);
		}
		return q;
	}
	
	/**
	 * Returns Quest from the List.
	 */
	public static Quest get(int index)
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
	public static List<Quest> getQuests()
	{
		return QUESTS;
	}
	
	/**
	 * Returns Quests as Map. Where key is a 'uniqueName' of the Quest.
	 */
	public static Map<String, Quest> getQuestsMap()
	{
		return QUESTS_MAP;
	}
}