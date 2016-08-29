package seia.vanillamagic.utils;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class ListHelper 
{
	private ListHelper()
	{
	}
	
	@Nullable
	public static <T> T getRandomObjectFromlist(List<T> list)
	{
		int randIndex = new Random().nextInt(list.size() - 1);
		return list.get(randIndex);
	}
}