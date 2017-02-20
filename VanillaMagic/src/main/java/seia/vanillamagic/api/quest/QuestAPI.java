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
	
	/**
	 * Returns Quest from the List at given index.
	 */
	@Nullable
	public static IQuest get(int index)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.quest.QuestList");
			return (IQuest) clazz.getMethod("get", int.class).invoke(null, index);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns Quest List size.<br>
	 * If something went wrong the size will be -1.
	 */
	public static int size()
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.quest.QuestList");
			return (int) clazz.getMethod("size").invoke(null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
}