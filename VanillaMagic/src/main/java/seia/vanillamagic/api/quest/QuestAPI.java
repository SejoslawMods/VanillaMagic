package seia.vanillamagic.api.quest;

import javax.annotation.Nullable;

/**
 * This is Your way to work with VanillaMagic internal QuestList.
 */
public class QuestAPI 
{
	private QuestAPI()
	{
	}
	
	/**
	 * Main method for adding Quests.
	 */
	public static void addQuest(IQuest q)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.quest.QuestList");
			clazz.getMethod("addQuest", IQuest.class).invoke(null, q);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns Quest from the Map, where "key" is a uniqueName of the Quest.<br>
	 * Returns NULL if there is no Quest registered for the given "key".
	 */
	@Nullable
	public static IQuest get(String key)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.quest.QuestList");
			return (IQuest) clazz.getMethod("get", String.class).invoke(null, key);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}