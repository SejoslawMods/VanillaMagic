package seia.vanillamagic.spell;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.utils.TextHelper;

public enum EnumWand 
{
	STICK(1, new ItemStack(Items.STICK), TextHelper.translateToLocal("wand.stick")), // basic wand - Stick Wand
	BLAZE_ROD(2, new ItemStack(Items.BLAZE_ROD), TextHelper.translateToLocal("wand.blazeRod")), // the most common wand - Blaze Wand
	NETHER_STAR(3, new ItemStack(Items.NETHER_STAR), TextHelper.translateToLocal("wand.netherStar")), // used mainly for summoning and resurrecting - Nether Star
	END_ROD(4, new ItemStack(Blocks.END_ROD), TextHelper.translateToLocal("wand.endRod")); // ??? - End Wand
	
	public final int wandTier;
	public final ItemStack wandItemStack;
	public final String wandName;
	
	EnumWand(int wandTier, ItemStack wandItemStack, String wandName)
	{
		this.wandTier = wandTier;
		this.wandItemStack = wandItemStack;
		this.wandName = wandName;
	}
	
	/**
	 * Method to check if the wand has got the required Tier to do the work
	 */
	public boolean canWandDoWork(int requiredTier)
	{
		if(this.wandTier == requiredTier) // If equal than we could have more combinations
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
	@Nullable
	public static EnumWand isWandInMainHand(EntityPlayer player)
	{
		return getWandByItemStack(player.getHeldItemMainhand());
	}
	
	/**
	 * @param player - player we are checking
	 * @return - the Wand which player has got in off hand - null if the item is not a Wand
	 */
	@Nullable
	public static EnumWand isWandInOffHand(EntityPlayer player)
	{
		return getWandByItemStack(player.getHeldItemOffhand());
	}
	
	/**
	 * @param player - player we are checking
	 * @param requiredWandMinimalTier - minimal tier of wark thet we want to finish
	 * @return - true if the player has got in hand the Wand that can do the work
	 */
	public static boolean isWandInMainHandRight(EntityPlayer player, int requiredWandMinimalTier)
	{
		EnumWand wandInMainHand = isWandInMainHand(player);
		if(wandInMainHand == null)
		{
			return false;
		}
		return wandInMainHand.canWandDoWork(requiredWandMinimalTier);
	}
	
	@Nullable
	public static EnumWand getWandByItemStack(ItemStack inHand)
	{
		EnumWand[] wands = EnumWand.values();
		for(int i = 0; i < wands.length; i++)
		{
			EnumWand currentlyCheckingEnumWand = wands[i];
			ItemStack currentlyCheckingWand = currentlyCheckingEnumWand.wandItemStack;
			if(ItemStack.areItemsEqual(currentlyCheckingWand, inHand))
			{
				// TODO: Fox language issue with different languages.
				// Added checker for wand name.
				//if(currentlyCheckingEnumWand.wandName.equals(inHand.getDisplayName()))
				{
					return currentlyCheckingEnumWand;
				}
			}
		}
		return null;
	}

	@Nullable
	public static EnumWand getCasterWand(EntityPlayer caster)
	{
		return getWandByItemStack(caster.getHeldItemMainhand());
	}
	
	public static boolean areWandsEqual(EnumWand wand1, EnumWand wand2)
	{
		return areWandsEqual(wand1.wandItemStack, wand2.wandItemStack);
	}
	
	// TODO: Fox language issue with different languages.
	public static boolean areWandsEqual(ItemStack wand1, ItemStack wand2)
	{
		return ItemStack.areItemsEqual(wand1, wand2);// &&
				//(wand1.getDisplayName().equals(wand2.getDisplayName()));
	}

	public static boolean isWandRightForSpell(EnumWand wandPlayerHand, EnumSpell spell) 
	{
		return wandPlayerHand.canWandDoWork(spell.minimalWandTier.wandTier);
	}
	
	@Nullable
	public static EnumWand getWandByTier(int tier)
	{
		EnumWand[] wands = EnumWand.values();
		for(int i = 0; i < wands.length; i++)
		{
			EnumWand currentlyCheckingEnumWand = wands[i];
			if(currentlyCheckingEnumWand.wandTier == tier)
			{
				return currentlyCheckingEnumWand;
			}
		}
		return null;
	}
}