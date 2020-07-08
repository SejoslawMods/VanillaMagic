package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.ItemUpgradeRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.ItemUpgradeRegistryEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookItemUpgrade extends AbstractBook {
	public void registerRecipe() {
		OnGroundCraftingHandler.addRecipe(getItem(), new ItemStack(Items.BOOK, 5));
	}

	public void addPages(ListNBT pages) {
		for (ItemUpgradeRegistryEntry entry : ItemUpgradeRegistry.ENTRIES) {
			for (IItemUpgrade upgrade : entry.upgrades) {
				pages.add(StringNBT.valueOf(
						"Upgrade name: " + upgrade.getUpgradeName() + TextUtil.getEnters(2) +
						"Ingredient item: " + upgrade.getIngredient().getDisplayName()
				));
			}
		}
	}

	public String getBookTranslationKey() {
		return "itemUpgrades";
	}
}
