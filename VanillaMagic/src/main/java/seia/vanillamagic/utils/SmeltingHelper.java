package seia.vanillamagic.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmeltingHelper 
{
	public static List<EntityItem> getSmeltable(List<EntityItem> entitiesInCauldron)
	{
		List<EntityItem> itemsToSmelt = new ArrayList<EntityItem>();
		for(int i = 0; i < entitiesInCauldron.size(); i++)
		{
			EntityItem entityItemInCauldron = entitiesInCauldron.get(i);
			ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(entityItemInCauldron.getEntityItem());
			// if null than item cannot be smelt
			if(smeltResult != null)
			{
				itemsToSmelt.add(entityItemInCauldron);
			}
		}
		return itemsToSmelt;
	}
	
	public static List<EntityItem> getSmeltable(World world, BlockPos cauldronPos)
	{
		return getSmeltable(CauldronHelper.getItemsInCauldron(world, cauldronPos));
	}
	
	public static List<EntityItem> getFuelFromCauldron(World world, BlockPos cauldronPos)
	{
		List<EntityItem> itemsInCauldron = CauldronHelper.getItemsInCauldron(world, cauldronPos);
		List<EntityItem> fuels = new ArrayList<EntityItem>();
		if(itemsInCauldron.size() == 0)
		{
			return fuels;
		}
		for(int i = 0; i < itemsInCauldron.size(); i++)
		{
			EntityItem entityItemInCauldron = itemsInCauldron.get(i);
			ItemStack stack = entityItemInCauldron.getEntityItem();
			if(isItemFuel(stack))
			{
				fuels.add(entityItemInCauldron);
			}
		}
		return fuels;
	}
	
	public static int countTicks(ItemStack stackOffHand)
	{
		return stackOffHand.stackSize * getItemBurnTimeTicks(stackOffHand);
	}
	
	/*
	 * e.g. will return 1600 if the item was Coal
	 * Won't care about stackSize
	 */
	public static int getItemBurnTimeTicks(ItemStack fuel)
	{
		return TileEntityFurnace.getItemBurnTime(fuel);
	}
	
	public static boolean isItemFuel(ItemStack stackInOffHand)
	{
		return TileEntityFurnace.isItemFuel(stackInOffHand);
	}
	
	public static ItemStack getSmeltingResultAsNewStack(ItemStack stackToSmelt)
	{
		return FurnaceRecipes.instance().getSmeltingResult(stackToSmelt).copy();
	}
	
	public static int getExperienceToAddFromWholeStack(ItemStack entityItemToSmeltStack)
	{
		return ((int)(FurnaceRecipes.instance().getSmeltingExperience(entityItemToSmeltStack) * entityItemToSmeltStack.stackSize));
	}
}