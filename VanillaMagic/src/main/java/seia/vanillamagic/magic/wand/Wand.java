package seia.vanillamagic.magic.wand;

import net.minecraft.item.ItemStack;

public class Wand implements IWand
{
	private final int wandID;
	private final ItemStack wandStack;
	private final String wandName;
	
	public Wand(int wandID, ItemStack wandStack, String wandName)
	{
		this.wandID = wandID;
		this.wandStack = wandStack;
		this.wandName = wandName;
		WandRegistry.addWand(this);
	}
	
	public int getWandID() 
	{
		return wandID;
	}
	
	public ItemStack getWandStack() 
	{
		return wandStack;
	}
	
	public String getWandName() 
	{
		return wandName;
	}
	
	public boolean canWandDoWork(int wandID) 
	{
		if(this.wandID == wandID)
		{
			return true;
		}
		return false;
	}
}