package com.github.sejoslaw.vanillamagic.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;
import com.github.sejoslaw.vanillamagic.api.item.IEnchantedBucket;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.github.sejoslaw.vanillamagic.item.accelerationcrystal.ItemAccelerationCrystal;
import com.github.sejoslaw.vanillamagic.item.evokercrystal.ItemEvokerCrystal;
import com.github.sejoslaw.vanillamagic.item.inventoryselector.ItemInventorySelector;
import com.github.sejoslaw.vanillamagic.item.liquidsuppressioncrystal.ItemLiquidSuppressionCrystal;
import com.github.sejoslaw.vanillamagic.item.potionedcrystal.IPotionedCrystal;
import com.github.sejoslaw.vanillamagic.item.thecrystalofmothernature.ItemMotherNatureCrystal;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * Class which holds data about ALL VM CustomItems.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VanillaMagicItems {
	/**
	 * All VanillaMagic items except these with additional lists.
	 */
	public static final List<ICustomItem> CUSTOM_ITEMS;
	/**
	 * All EnchantedBuckets list.
	 */
	public static final List<IEnchantedBucket> ENCHANTED_BUCKETS;
	/**
	 * All PotionedCrystals list.
	 */
	public static final List<IPotionedCrystal> POTIONED_CRYSTALS;

	public static final CustomItemCrystal ACCELERATION_CRYSTAL;
	public static final CustomItemCrystal LIQUID_SUPPRESSION_CRYSTAL;
	public static final CustomItemCrystal MOTHER_NATURE_CRYSTAL;
	public static final CustomItem INVENTORY_SELECTOR;
	public static final CustomItemCrystal EVOKER_CRYSTAL;

	private VanillaMagicItems() {
	}

	static {
		CUSTOM_ITEMS = new ArrayList<ICustomItem>();

		ENCHANTED_BUCKETS = new ArrayList<IEnchantedBucket>();
		POTIONED_CRYSTALS = new ArrayList<IPotionedCrystal>();

		ACCELERATION_CRYSTAL = new ItemAccelerationCrystal();
		CUSTOM_ITEMS.add(ACCELERATION_CRYSTAL);

		LIQUID_SUPPRESSION_CRYSTAL = new ItemLiquidSuppressionCrystal();
		CUSTOM_ITEMS.add(LIQUID_SUPPRESSION_CRYSTAL);

		MOTHER_NATURE_CRYSTAL = new ItemMotherNatureCrystal();
		CUSTOM_ITEMS.add(MOTHER_NATURE_CRYSTAL);

		INVENTORY_SELECTOR = new ItemInventorySelector();
		CUSTOM_ITEMS.add(INVENTORY_SELECTOR);

		EVOKER_CRYSTAL = new ItemEvokerCrystal();
		CUSTOM_ITEMS.add(EVOKER_CRYSTAL);
	}

	/**
	 * Register new CustomItem.
	 */
	public static void addCustomItem(ICustomItem item) {
		CUSTOM_ITEMS.add(item);
	}

	/**
	 * PostInitialization stage. Register all recipes.
	 */
	public static void postInit() {
		for (ICustomItem customItem : CUSTOM_ITEMS) {
			customItem.registerRecipe();
		}

		VanillaMagic.logInfo("Custom Items registered: " + CUSTOM_ITEMS.size());
	}

	/**
	 * @return Returns TRUE if the given ItemStack is a given CustomItem.
	 */
	public static boolean isCustomItem(ItemStack checkingStack, ICustomItem customItem) {
		if (ItemStackUtil.isNullStack(checkingStack) || (customItem == null)) {
			return false;
		}

		CompoundNBT stackTag = checkingStack.getTagCompound();
		if (stackTag == null) {
			return false;
		}

		if ((stackTag.hasKey(ICustomItem.NBT_UNIQUE_NAME))
				&& (stackTag.getString(ICustomItem.NBT_UNIQUE_NAME).equals(customItem.getUniqueNBTName()))) {
			return true;
		}

		return false;
	}

	/**
	 * @return Returns TRUE if the given ItemStack is a given IEnchantedBucket.
	 */
	public static boolean isCustomBucket(ItemStack checkingStack, IEnchantedBucket customBucket) {
		if (ItemStackUtil.isNullStack(checkingStack) || customBucket == null) {
			return false;
		}

		CompoundNBT stackTag = checkingStack.getTagCompound();
		if (stackTag == null) {
			return false;
		}

		if (stackTag.hasKey(IEnchantedBucket.NBT_ENCHANTED_BUCKET)
				&& stackTag.getString(IEnchantedBucket.NBT_ENCHANTED_BUCKET).equals(customBucket.getUniqueNBTName())
				&& stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME)
						.equals(customBucket.getFluidInBucket().getName())) {
			return true;
		}

		return false;
	}

	/**
	 * @return Returns given list filled with all CustomItems.
	 */
	public static NonNullList<ItemStack> fillList(NonNullList<ItemStack> list) {
		for (ICustomItem ci : CUSTOM_ITEMS) {
			list.add(ci.getItem());
		}

		for (IEnchantedBucket eb : ENCHANTED_BUCKETS) {
			list.add(eb.getItem());
		}

		for (IPotionedCrystal pc : POTIONED_CRYSTALS) {
			list.add(pc.getItem());
		}

		return list;
	}
}