package seia.vanillamagic.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.quest.QuestSmeltOnAltar;

/**
 * Class which store various methods connected with smelting.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class SmeltingUtil {
	private SmeltingUtil() {
	}

	public static boolean isBlockOre(Block block) {
		if (block instanceof BlockOre) { // Coal, Diamond, Lapis, Emerald, Quartz
			return true;
		}

		if ((block instanceof BlockRedstoneOre) || Block.isEqualTo(block, Blocks.REDSTONE_ORE)
				|| Block.isEqualTo(block, Blocks.LIT_REDSTONE_ORE) || Block.isEqualTo(block, Blocks.IRON_ORE)
				|| Block.isEqualTo(block, Blocks.GOLD_ORE)) {
			return true;
		}

		// Other Ores (other mods, etc.)
		String[] oreDictionaryNames = OreDictionary.getOreNames();
		List<String> oreNames = new ArrayList<String>(); // Ores

		for (String oreDictionaryName : oreDictionaryNames) {
			if (oreDictionaryName.contains("ore")) {
				oreNames.add(oreDictionaryName);
			}
		}

		for (String oreName : oreNames) {
			if (oreName.equals(block.getLocalizedName()) || oreName.equals(block.getUnlocalizedName())) {
				return true;
			}
		}

		return false;
	}

	public static List<EntityItem> getOresInCauldron(World world, BlockPos cauldronPos) {
		List<EntityItem> oresInCauldron = new ArrayList<EntityItem>();
		List<EntityItem> smeltablesInCauldron = getSmeltable(world, cauldronPos);
		List<String> oreNames = getOreNamesFromDictionary();

		for (EntityItem entityItem : smeltablesInCauldron) {
			ItemStack currentSmeltable = entityItem.getItem();

			for (String oreName : oreNames) {
				List<ItemStack> stacksforItem = OreDictionary.getOres(oreName);

				for (ItemStack stackOre : stacksforItem) {
					if (ItemStack.areItemsEqual(currentSmeltable, stackOre)) {
						oresInCauldron.add(entityItem);
						break;
					}
				}
			}
		}

		return oresInCauldron;
	}

	public static List<String> getOreNamesFromDictionary() {
		List<String> oreNames = new ArrayList<String>();
		String[] all = OreDictionary.getOreNames();

		for (int i = 0; i < all.length; ++i) {
			if (all[i].contains("ore")) {
				oreNames.add(all[i]);
			}
		}

		return oreNames;
	}

	/**
	 * @return Returns the all fuelStacks from the inventory
	 */
	public static List<ItemStack> getFuelFromInventory(IInventory inv) {
		List<ItemStack> fuels = new ArrayList<ItemStack>();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stackInSlot = inv.getStackInSlot(i);

			if (isItemFuel(stackInSlot)) {
				fuels.add(stackInSlot);
			}
		}

		return fuels;
	}

	/**
	 * @return Returns the first fuelStack from the inventory.<br>
	 *         Indexes:<br>
	 *         0 - ItemStack<br>
	 *         1 - index (slot)
	 */
	@Nullable
	public static Object[] getFuelFromInventoryAndDelete(IInventory inv) {
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stackInSlot = inv.getStackInSlot(i);

			if (isItemFuel(stackInSlot)) {
				return new Object[] { inv.removeStackFromSlot(i), i };
			}
		}

		return null;
	}

	/**
	 * @return Returns the smeltables EntityItems from the ALL entities in cauldron
	 *         BlockPos
	 */
	public static List<EntityItem> getSmeltable(List<EntityItem> entitiesInCauldron) {
		List<EntityItem> itemsToSmelt = new ArrayList<EntityItem>();

		for (int i = 0; i < entitiesInCauldron.size(); ++i) {
			EntityItem entityItemInCauldron = entitiesInCauldron.get(i);
			ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(entityItemInCauldron.getItem());

			if (!ItemStackUtil.isNullStack(smeltResult)) {
				itemsToSmelt.add(entityItemInCauldron);
			}
		}

		return itemsToSmelt;
	}

	public static List<EntityItem> getSmeltable(World world, BlockPos cauldronPos) {
		return getSmeltable(CauldronUtil.getItemsInCauldron(world, cauldronPos));
	}

	/**
	 * @return Returns the all fuel from the Cauldron position
	 */
	public static List<EntityItem> getFuelFromCauldron(World world, BlockPos cauldronPos) {
		List<EntityItem> itemsInCauldron = CauldronUtil.getItemsInCauldron(world, cauldronPos);
		List<EntityItem> fuels = new ArrayList<EntityItem>();

		if (itemsInCauldron.size() == 0) {
			return fuels;
		}

		for (int i = 0; i < itemsInCauldron.size(); ++i) {
			EntityItem entityItemInCauldron = itemsInCauldron.get(i);
			ItemStack stack = entityItemInCauldron.getItem();

			if (isItemFuel(stack)) {
				fuels.add(entityItemInCauldron);
			}
		}

		return fuels;
	}

	public static int countTicks(ItemStack stackOffHand) {
		return ItemStackUtil.getStackSize(stackOffHand) * getItemBurnTimeTicks(stackOffHand);
	}

	/**
	 * @return e.g. will return 1600 if the item was Coal. Won't care about
	 *         stackSize
	 */
	public static int getItemBurnTimeTicks(ItemStack fuel) {
		return TileEntityFurnace.getItemBurnTime(fuel);
	}

	public static boolean isItemFuel(ItemStack stackInOffHand) {
		return TileEntityFurnace.isItemFuel(stackInOffHand);
	}

	@Nullable
	public static ItemStack getSmeltingResultAsNewStack(ItemStack stackToSmelt) {
		return FurnaceRecipes.instance().getSmeltingResult(stackToSmelt).copy();
	}

	public static int getExperienceToAddFromWholeStack(ItemStack entityItemToSmeltStack) {
		return ((int) (FurnaceRecipes.instance().getSmeltingExperience(entityItemToSmeltStack)
				* ItemStackUtil.getStackSize(entityItemToSmeltStack)));
	}

	public static List<EntityItem> countAndSmelt_OneByOneItemFromOffHand(EntityPlayer player,
			List<EntityItem> itemsToSmelt, BlockPos cauldronPos, IQuest requiredQuest, boolean spawnSmelted) {
		if (QuestUtil.canUnlockQuest(player, requiredQuest)) {
			QuestUtil.addStat(player, requiredQuest);
		}

		if (QuestUtil.hasQuestUnlocked(player, requiredQuest)) {
			List<EntityItem> smelted = new ArrayList<EntityItem>();
			World world = player.world;
			int ticks = 0;
			ItemStack rightHand = player.getHeldItemOffhand();

			for (int i = 0; i < itemsToSmelt.size(); ++i) {
				EntityItem entityItemToSmelt = itemsToSmelt.get(i);
				ItemStack entityItemToSmeltStack = entityItemToSmelt.getItem();

				int entityItemToSmeltStackSize = ItemStackUtil.getStackSize(entityItemToSmeltStack);
				int ticksToSmeltStack = entityItemToSmeltStackSize * QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS;

				ItemStack smeltResult = null;
				EntityItem smeltResultEntityItem = null;

				while ((ItemStackUtil.getStackSize(rightHand) > 0) && (ticks < ticksToSmeltStack)) {
					ticks += getItemBurnTimeTicks(rightHand);
					ItemStackUtil.decreaseStackSize(rightHand, 1);
				}

				if (ticks >= ticksToSmeltStack) {
					smeltResult = getSmeltingResultAsNewStack(entityItemToSmeltStack);
					ItemStackUtil.setStackSize(smeltResult, ItemStackUtil.getStackSize(entityItemToSmeltStack));
					smeltResultEntityItem = new EntityItem(world, cauldronPos.getX() + 0.5D, cauldronPos.getY(),
							cauldronPos.getZ() + 0.5D, smeltResult);
					world.removeEntity(entityItemToSmelt);
				} else if (ticks >= QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS) {
					int howManyCanSmelt = ticks / QuestSmeltOnAltar.ONE_ITEM_SMELT_TICKS;
					ItemStackUtil.decreaseStackSize(entityItemToSmeltStack, howManyCanSmelt);
					smeltResult = getSmeltingResultAsNewStack(entityItemToSmeltStack);
					ItemStackUtil.setStackSize(smeltResult, howManyCanSmelt);
					smeltResultEntityItem = new EntityItem(world, cauldronPos.getX() + 0.5D, cauldronPos.getY(),
							cauldronPos.getZ() + 0.5D, smeltResult);
				} else {
					break;
				}

				if (spawnSmelted) {
					world.spawnEntity(smeltResultEntityItem);
				}

				smelted.add(smeltResultEntityItem);
				ticks -= ticksToSmeltStack;

				// TODO: Fix the experience after smelting.
				int experienceToAdd = getExperienceToAddFromWholeStack(entityItemToSmeltStack);
				player.addExperience(experienceToAdd);
			}

			world.updateEntities();

			return smelted;
		}

		return new ArrayList<EntityItem>();
	}
}