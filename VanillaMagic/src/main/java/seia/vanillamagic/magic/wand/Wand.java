package seia.vanillamagic.magic.wand;

import net.minecraft.item.ItemStack;

public class Wand implements IWand
{
	private final int _wandID;
	private final ItemStack _wandStack;
	private final String _wandName;
	
	public Wand(int wandID, ItemStack wandStack, String wandName)
	{
		this._wandID = wandID;
		this._wandStack = wandStack;
		this._wandName = wandName;
		WandRegistry.addWand(this);
	}
	
	public int getWandID() 
	{
		return _wandID;
	}
	
	public ItemStack getWandStack() 
	{
		return _wandStack;
	}
	
	public String getWandName() 
	{
		return _wandName;
	}
	
	public boolean canWandDoWork(int wandID) 
	{
		if(this._wandID == wandID)
		{
			return true;
		}
		return false;
	}
}