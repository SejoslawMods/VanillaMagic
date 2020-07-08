package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.ItemUpgradeRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.ItemUpgradeRegistryEntry;
import com.github.sejoslaw.vanillamagic.common.util.TranslationUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookItemUpgrade implements IBook {
	public int getBookID() {
		return BookRegistry.BOOK_ITEM_UPGRADES_UID;
	}

	public void registerRecipe() {
		OnGroundCraftingHandler.addRecipe(getItem(), new ItemStack(Items.BOOK, 5));
	}

	public ItemStack getItem() {
		ItemStack book = new ItemStack(BookRegistry.BOOK_ITEM);

		CompoundNBT data = new CompoundNBT();
		{
			// Constructing TagCompound
			ListNBT pages = new ListNBT();
			{
				// Pages
				pages.add(StringNBT.valueOf("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== "
						+ TranslationUtil.translateToLocal("book.itemUpgrades.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (ItemUpgradeRegistryEntry entry : ItemUpgradeRegistry.ENTRIES) {
					for (IItemUpgrade upgrade : entry.upgrades) {
						pages.add(StringNBT.valueOf(
								"Upgrade name: " + upgrade.getUpgradeName() + TextUtil.getEnters(2) +
								"Ingredient item: " + upgrade.getIngredient().getDisplayName()
						));
					}
				}
			}

			data.put("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_ITEM_UPGRADES);
			data.putInt(BookRegistry.BOOK_NBT_UID, getBookID());
		}

		book.setTag(data);
		book.setDisplayName(new StringTextComponent(BookRegistry.BOOK_NAME_ITEM_UPGRADES));

		return book;
	}
}
