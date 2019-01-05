package seia.vanillamagic.api.quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.VanillaMagicAPI;
import seia.vanillamagic.api.event.EventQuest;

/**
 * Class which stores all registered Quests.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestList {
	/**
	 * All registered Quests
	 */
	private static List<IQuest> QUESTS = new ArrayList<IQuest>();

	/**
	 * Map: <br>
	 * Key - Quest Unique Name <br>
	 * Value - Quest <br>
	 */
	private static Map<String, IQuest> QUESTS_MAP = new HashMap<String, IQuest>();

	private QuestList() {
	}

	/**
	 * Main method for adding Quests.
	 */
	public static void addQuest(IQuest q) {
		if (!MinecraftForge.EVENT_BUS.post(new EventQuest.Add(q))) {
			QuestList.QUESTS.add(q);
			QuestList.QUESTS_MAP.put(q.getUniqueName(), q);
		}
	}

	/**
	 * @return Returns Quest from the Map, where "key" is a uniqueName of the
	 *         Quest.<br>
	 *         Returns NULL if there is no Quest registered at the given "key".
	 */
	@Nullable
	public static IQuest get(String key) {
		IQuest q = QUESTS_MAP.get(key);
		if (q == null) {
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Can't find Quest for key: " + key);
		}
		return q;
	}

	/**
	 * @return Returns Quest from the List.
	 */
	public static IQuest get(int index) {
		return QUESTS.get(index);
	}

	/**
	 * @return Returns Quest List size.
	 */
	public static int size() {
		return QUESTS.size();
	}

	/**
	 * @return Returns Quests as List.
	 */
	public static List<IQuest> getQuests() {
		return QUESTS;
	}

	/**
	 * @return Returns Quests as Map. Where key is a 'uniqueName' of the Quest.
	 */
	public static Map<String, IQuest> getQuestsMap() {
		return QUESTS_MAP;
	}
}