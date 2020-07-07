package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.QuarryUpgradeRegistry;
import com.github.sejoslaw.vanillamagic.common.util.CraftingUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookQuarryUpgrades implements IBook {
	public int getBookID() {
		return BookRegistry.BOOK_QUARRY_UPGRADES_UID;
	}

	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(), new Object[] { "  B", " B ", "  B", 'B', Items.BOOK });
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
						+ TextUtil.translateToLocal("book.quarryUpgrades.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (IQuarryUpgrade iqu : QuarryUpgradeRegistry.getUpgrades()) {
					pages.add(StringNBT.valueOf(
							BookRegistry.COLOR_HEADER + iqu.getUpgradeName() + TextUtil.getEnters(2) + "�0" + "Block: "
									+ ForgeRegistries.BLOCKS.getKey(iqu.getBlock())));
				}
			}

			data.put("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_QUARRY_UPGRADES);
			data.putInt(BookRegistry.BOOK_NBT_UID, getBookID());
		}

		book.setTag(data);
		book.setDisplayName(new StringTextComponent(BookRegistry.BOOK_NAME_QUARRY_UPGRADES));

		return book;
	}
}
