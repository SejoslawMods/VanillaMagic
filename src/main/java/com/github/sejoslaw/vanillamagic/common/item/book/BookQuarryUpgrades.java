package com.github.sejoslaw.vanillamagic.item.book;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import com.github.sejoslaw.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry;
import com.github.sejoslaw.vanillamagic.util.CraftingUtil;
import com.github.sejoslaw.vanillamagic.util.TextUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookQuarryUpgrades implements IBook {
	public int getUID() {
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
				pages.appendTag(new NBTTagString("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== "
						+ TextUtil.translateToLocal("book.quarryUpgrades.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (IQuarryUpgrade iqu : QuarryUpgradeRegistry.getUpgrades()) {
					pages.appendTag(new NBTTagString(
							BookRegistry.COLOR_HEADER + iqu.getUpgradeName() + TextUtil.getEnters(2) + "�0" + "Block: "
									+ ForgeRegistries.BLOCKS.getKey(iqu.getBlock()).getResourcePath()));
				}
			}
			data.setTag("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_QUARRY_UPGRADES);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getUID());
		}
		book.setTagCompound(data);
		book.setDisplayName(BookRegistry.BOOK_NAME_QUARRY_UPGRADES);
		return book;
	}
}