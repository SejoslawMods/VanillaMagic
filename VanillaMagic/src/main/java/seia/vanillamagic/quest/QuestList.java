package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventQuest;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.core.VanillaMagic;

public class QuestList
{
	private static List<IQuest> _QUESTS = new ArrayList<IQuest>();
	private static Map<String, IQuest> _QUESTS_MAP = new HashMap<String, IQuest>();
	
	private QuestList()
	{
	}
	
	/**
	 * Main method for adding Quests.
	 */
	public static void addQuest(IQuest q)
	{
//		EventAddQuest event = new EventAddQuest(q);
//		QuestList._QUESTS.add(q);
//		QuestList._QUESTS_MAP.put(q.getUniqueName(), q);
		if(!MinecraftForge.EVENT_BUS.post(new EventQuest.Add(q)))
		{
			QuestList._QUESTS.add(q);
			QuestList._QUESTS_MAP.put(q.getUniqueName(), q);
		}
	}
	
	/**
	 * Returns Quest from the Map, where "key" is a uniqueName of the Quest.<br>
	 * Returns NULL if there is no Quest registered at the given "key".
	 */
	@Nullable
	public static IQuest get(String key)
	{
		IQuest q = _QUESTS_MAP.get(key);
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
		return _QUESTS.get(index);
	}
	
	/**
	 * Returns Quest List size.
	 */
	public static int size()
	{
		return _QUESTS.size();
	}
	
	/**
	 * Returns Quests as List.
	 */
	public static List<IQuest> getQuests()
	{
		return _QUESTS;
	}
	
	/**
	 * Returns Quests as Map. Where key is a 'uniqueName' of the Quest.
	 */
	public static Map<String, IQuest> getQuestsMap()
	{
		return _QUESTS_MAP;
	}
}