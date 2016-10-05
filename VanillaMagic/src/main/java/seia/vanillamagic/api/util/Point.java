package seia.vanillamagic.api.util;

import seia.vanillamagic.api.quest.IQuest;

/**
 * Simple presentation of 2D Point. <br>
 * Used in {@link IQuest}
 */
public class Point 
{
	protected int x, y;
	
	public Point(int x, int y)
	{
		this.setPosition(x, y);
	}
	
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}