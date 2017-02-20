package seia.vanillamagic.api.magic;

import net.minecraft.item.ItemStack;

/**
 * Basic Wand description.
 */
public interface IWand 
{
	/**
	 * @return Returns the unique Wand ID.
	 */
	int getWandID();
	
	/**
	 * @return Returns the Wand as ItemStack.
	 */
	ItemStack getWandStack();
	
	/**
	 * @return Returns the name of the Wand.
	 */
	String getWandName();
	
	/**
	 * @param wandID Wand ID
	 * @return Returns TRUE if the Wand with given ID can do the work.
	 */
	boolean canWandDoWork(int wandID);
}