package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.QuarryUpgradeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookQuarryUpgrades extends AbstractBook {
	public void registerRecipe() {
		OnGroundCraftingHandler.addRecipe(getItem(), new ItemStack(Items.BOOK, 7));
	}

	public void addPages(ListNBT pages) {
		for (IQuarryUpgrade iqu : QuarryUpgradeRegistry.getUpgrades()) {
			pages.add(StringNBT.valueOf(
					BookRegistry.COLOR_HEADER + iqu.getUpgradeName() + TextUtil.getEnters(2) + "�0" +
					"Block: " + ForgeRegistries.BLOCKS.getKey(iqu.getBlock())));
		}
	}

	public String getBookTranslationKey() {
		return "quarryUpgrades";
	}
}
