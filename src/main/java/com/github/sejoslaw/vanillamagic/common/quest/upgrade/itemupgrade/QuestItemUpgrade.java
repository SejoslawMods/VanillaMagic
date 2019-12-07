package com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import com.github.sejoslaw.vanillamagic.quest.QuestSpawnOnCauldron;
import com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade.ItemUpgradeRegistry.ItemEntry;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestItemUpgrade extends QuestSpawnOnCauldron {
	public boolean canGetUpgrade(ItemStack base) {
		base.setDisplayName(base.getDisplayName() + " ");
		CompoundNBT tag = base.getTagCompound();

		if (tag == null) {
			return false;
		}

		return !tag.getBoolean(IItemUpgrade.NBT_ITEM_CONTAINS_UPGRADE);
	}

	public boolean isBaseItem(ItemEntity entityItem) {
		for (ItemEntry ie : ItemUpgradeRegistry.getBaseItems()) {
			if ((entityItem.getItem().getItem() == ie.item)
					&& (ItemStackUtil.getStackSize(entityItem.getItem()) == ItemStackUtil.getStackSize(ie.stack))) {
				return true;
			}
		}

		return false;
	}

	public ItemStack getResultSingle(ItemEntity base, ItemEntity ingredient) {
		return ItemUpgradeRegistry.getResult(base.getItem(), ingredient.getItem());
	}
}