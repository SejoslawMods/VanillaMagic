package seia.vanillamagic.util;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;

public class ListHelper 
{
	private ListHelper()
	{
	}
	
	@Nullable
	public static <T> T getRandomObjectFromList(List<T> list)
	{
		int randIndex = new Random().nextInt(list.size() - 1);
		return list.get(randIndex);
	}
	
	@Nullable
	public static <T> T getRandomObjectFromTab(T[] tab)
	{
		int randIndex = new Random().nextInt(tab.length - 1);
		return tab[randIndex];
	}
	
	public static int getRandomObjectFromTab(int[] tab)
	{
		int randIndex = new Random().nextInt(tab.length - 1);
		return tab[randIndex];
	}

	public static List<Block> getList(String className, String field) 
	{
		try
		{
			return (List<Block>) ClassUtils.getFieldFromStaticInstance(className, field);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Returns the combined lists.
	 */
	public static <T> List<T> combineLists(List<T> l1, List<T> l2)
	{
		for(T element : l2)
		{
			l1.add(element);
		}
		return l1;
	}
}