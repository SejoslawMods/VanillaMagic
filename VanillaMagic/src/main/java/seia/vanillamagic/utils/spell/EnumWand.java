package seia.vanillamagic.utils.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum EnumWand 
{
	STICK(1, new ItemStack(Items.STICK)), // basic wand
	BLAZE_ROD(2, new ItemStack(Items.BLAZE_ROD)), // the most common wand
	NETHER_STAR(3, new ItemStack(Items.NETHER_STAR)), // used mainly for summoning and resurrecting
	END_ROD(4, new ItemStack(Blocks.END_ROD)); // ???
	
	public final int wandTier;
	public final ItemStack wandItemStack;
	
	EnumWand(int wandTier, ItemStack wandItemStack)
	{
		this.wandTier = wandTier;
		this.wandItemStack = wandItemStack;
	}
	
	/*
	 * Method to check if the wand has got the required Tier to do the work
	 */
	public boolean canWandDoWork(int requiredTier)
	{
		if(this.wandTier >= requiredTier)
		{
			return true;
		}
		return false;
	}
	
	//=============================================================================================
	
	/**
	 * @param player - player we are checking
	 * @return - the Wand which player has got in main hand - null if the item is not a Wand
	 */
	public static EnumWand isWandInMainHand(EntityPlayer player)
	{
		ItemStack mainHand = player.getHeldItemMainhand();
		EnumWand[] wands = values();
		for(int i = 0; i < wands.length; i++)
		{
			EnumWand currentlyCheckingWand = wands[i];
			if(mainHand.getItem().equals(currentlyCheckingWand.wandItemStack.getItem()))
			{
				if(mainHand.getItemDamage() == currentlyCheckingWand.wandItemStack.getItemDamage())
				{
					return currentlyCheckingWand;
				}
			}
		}
		return null;
	}
	
	/**
	 * @param player - player we are checking
	 * @param requiredWandMinimalTier - minimal tier of wark thet we want to finish
	 * @return - true if the player has got in hand the Wand that can do the work
	 */
	public static boolean isWandInMainHandRight(EntityPlayer player, int requiredWandMinimalTier)
	{
		EnumWand wandInMainHand = isWandInMainHand(player);
		// player has got wand in hand
		if(wandInMainHand != null)
		{
			return wandInMainHand.canWandDoWork(requiredWandMinimalTier);
		}
		return false;
	}
	
	public static EnumWand getWandByItemStack(ItemStack inHand)
	{
		EnumWand[] wands = EnumWand.values();
		for(int i = 0; i < wands.length; i++)
		{
			EnumWand currentlyCheckingEnumWand = wands[i];
			ItemStack currentlyCheckingWand = currentlyCheckingEnumWand.wandItemStack;
			if(currentlyCheckingWand.getItem().equals(inHand.getItem()))
			{
				return currentlyCheckingEnumWand;
			}
		}
		return null;
	}

	public static EnumWand getCasterWand(EntityPlayer caster)
	{
		return getWandByItemStack(caster.getHeldItemMainhand());
	}
	
	public static boolean areWandsEqual(EnumWand wand1, EnumWand wand2)
	{
		return areWandsEqual(wand1.wandItemStack, wand2.wandItemStack);
	}
	
	public static boolean areWandsEqual(ItemStack wand1, ItemStack wand2)
	{
		return wand1.getItem().equals(wand2.getItem());
	}
	
	public static boolean areWandsEqualTotally(EnumWand wand1, EnumWand wand2)
	{
		return ItemStack.areItemsEqual(wand1.wandItemStack, wand2.wandItemStack);
	}

	public static boolean isWandRightForSpell(EnumWand wandPlayerHand, EnumSpell spell) 
	{
		return wandPlayerHand.wandTier >= spell.minimalWandTier.wandTier;
	}
}