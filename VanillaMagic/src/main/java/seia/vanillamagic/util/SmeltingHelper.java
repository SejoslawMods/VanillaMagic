package seia.vanillamagic.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestSmeltOnAltar;

public class SmeltingHelper
{
	private SmeltingHelper()
	{
	}
	
	public static List<EntityItem> getOresInCauldron(World world, BlockPos cauldronPos)
	{
		List<EntityItem> oresInCauldron = new ArrayList<EntityItem>();
		// all items that can be smelt
		List<EntityItem> smeltablesInCauldron = getSmeltable(world, cauldronPos);
		// all ore names from dictionary
		List<String> oreNames = getOreNamesFromDictionary();
		// filter each EntityItem
		for(EntityItem entityItem : smeltablesInCauldron)
		{
			ItemStack currentSmeltable = entityItem.getEntityItem();
			// check all Ores names
			for(String oreName : oreNames)
			{
				// all registered stacks for ore
				List<ItemStack> stacksForItem = OreDictionary.getOres(oreName);
				for(ItemStack stackOre : stacksForItem)
				{
					if(ItemStack.areItemsEqual(currentSmeltable, stackOre))
					{
						oresInCauldron.add(entityItem);
						break;
					}
				}
			}
		}
		return oresInCauldron;
	}
	
	public static List<String> getOreNamesFromDictionary()
	{
		List<String> oreNames = new ArrayList<String>();
		String[] all = OreDictionary.getOreNames();
		for(int i = 0; i < all.length; i++)
		{
			if(all[i].contains("ore"))
			{
				oreNames.add(all[i]);
			}
		}
		return oreNames;
	}
	
	/**
	 * Returns the all fuelStacks from the inventory
	 */
	public static List<ItemStack> getFuelFromInventory(IInventory inv)
	{
		List<ItemStack> fuels = new ArrayList<ItemStack>();
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(isItemFuel(stackInSlot))
			{
				fuels.add(stackInSlot);
			}
		}
		return fuels;
	}
	
	/**
	 * Returns the first fuelStack from the inventory.<br>
	 * Indexes:<br>
	 * 0 - ItemStack<br>
	 * 1 - index (slot)
	 */
	@Nullable
	public static Object[] getFuelFromInventoryAndDelete(IInventory inv)
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stackInSlot = inv.getStackInSlot(i);
			if(isItemFuel(stackInSlot))
			{
				//return inv.removeStackFromSlot(i);
				return new Object[]{inv.removeStackFromSlot(i), i};
			}
		}
		return null;
	}
	
	/**
	 * Returns the smeltables EntityItems from the ALL entities in cauldron BlockPos
	 */
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
	
	/**
	 * Returns the all fuel from the Cauldron position
	 */
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
		//return stackOffHand.stackSize * getItemBurnTimeTicks(stackOffHand);
		return ItemStackHelper.getStackSize(stackOffHand) * getItemBurnTimeTicks(stackOffHand);
	}
	
	/**
	 * e.g. will return 1600 if the item was Coal.
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
	
	@Nullable
	public static ItemStack getSmeltingResultAsNewStack(ItemStack stackToSmelt)
	{
		return FurnaceRecipes.instance().getSmeltingResult(stackToSmelt);
	}
	
	public static int getExperienceToAddFromWholeStack(ItemStack entityItemToSmeltStack)
	{
		//return ((int)(FurnaceRecipes.instance().getSmeltingExperience(entityItemToSmeltStack) * entityItemToSmeltStack.stackSize));
		return ((int)(FurnaceRecipes.instance().getSmeltingExperience(entityItemToSmeltStack) * 
				ItemStackHelper.getStackSize(entityItemToSmeltStack)));
	}
	
	/**
	 * TODO: Better Idea for counting and smelting items in Cauldron.
	 * Currently will consume whole stack in offHand for 1 operation
	 * Returns the list of smelted items
	 * oneItemSmeltTicks = default is QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS
	 */
	@Nullable
	public static List<EntityItem> countAndSmelt(EntityPlayer player, List<EntityItem> itemsToSmelt, BlockPos cauldronPos, 
			Quest requiredQuest, boolean spawnSmelted)
	{
		if(!player.hasAchievement(requiredQuest.getAchievement()))
		{
			player.addStat(requiredQuest.getAchievement(), 1);
		}
		if(player.hasAchievement(requiredQuest.getAchievement()))
		{
			List<EntityItem> smelted = new ArrayList<EntityItem>();
			World world = player.worldObj;
			int ticks = 0;
			ticks += SmeltingHelper.countTicks(player.getHeldItemOffhand()); // value for the whole stack
			//player.getHeldItemOffhand().stackSize = 0;
			ItemStackHelper.setStackSize(player.getHeldItemOffhand(), 0);
			for(int i = 0; i < itemsToSmelt.size(); i++)
			{
				EntityItem entityItemToSmelt = itemsToSmelt.get(i);
				ItemStack entityItemToSmeltStack = entityItemToSmelt.getEntityItem();
				//int entityItemToSmeltStackSize = entityItemToSmeltStack.stackSize;
				int entityItemToSmeltStackSize = ItemStackHelper.getStackSize(entityItemToSmeltStack);
				int ticksToSmeltStack = entityItemToSmeltStackSize * QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS;
				ItemStack smeltResult = null;
				EntityItem smeltResultEntityItem = null;
				// will smelt whole stack
				if(ticks >= ticksToSmeltStack)
				{
					smeltResult = SmeltingHelper.getSmeltingResultAsNewStack(entityItemToSmeltStack);
					//smeltResult.stackSize = entityItemToSmeltStack.stackSize;
					ItemStackHelper.setStackSize(smeltResult, ItemStackHelper.getStackSize(entityItemToSmeltStack));
					smeltResultEntityItem = new EntityItem(world, cauldronPos.getX(), cauldronPos.getY(), cauldronPos.getZ(), smeltResult);
					world.removeEntity(entityItemToSmelt);
				}
				else if(ticks >= QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS)// won't smelt whole stack, we need to count how many we can smelt
				{
					int howManyCanSmelt = ticks / QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS;
					//entityItemToSmeltStack.stackSize -= howManyCanSmelt;
					ItemStackHelper.decreaseStackSize(entityItemToSmeltStack, howManyCanSmelt);
					smeltResult = SmeltingHelper.getSmeltingResultAsNewStack(entityItemToSmeltStack);
					//smeltResult.stackSize = howManyCanSmelt;
					ItemStackHelper.setStackSize(smeltResult, howManyCanSmelt);
					smeltResultEntityItem = new EntityItem(world, cauldronPos.getX(), cauldronPos.getY(), cauldronPos.getZ(), smeltResult);
				}
				else // if(ticks < ONE_ITEM_SMELT_TICKS), we can't smelt any more items so let's just break
				{
					break;
				}
				// spawn or not
				if(spawnSmelted)
				{
					world.spawnEntityInWorld(smeltResultEntityItem);
				}
				smelted.add(smeltResultEntityItem);
				ticks -= ticksToSmeltStack;
				//TODO: Fix the experience after smelting.
				int experienceToAdd = SmeltingHelper.getExperienceToAddFromWholeStack(entityItemToSmeltStack);
				player.addExperience(experienceToAdd);
			}
			world.updateEntities();
			return smelted;
		}
		return null;
	}
}