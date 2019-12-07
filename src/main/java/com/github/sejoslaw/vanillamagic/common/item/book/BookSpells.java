package com.github.sejoslaw.vanillamagic.item.book;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTTagString;
import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.api.quest.QuestList;
import com.github.sejoslaw.vanillamagic.quest.spell.QuestCastSpell;
import com.github.sejoslaw.vanillamagic.util.CraftingUtil;
import com.github.sejoslaw.vanillamagic.util.TextUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookSpells implements IBook {
	public int getUID() {
		return BookRegistry.BOOK_SPELLS_UID;
	}

	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(), new Object[] { " B ", " B ", " B ", 'B', Items.BOOK });
	}

	public ItemStack getItem() {
		ItemStack infoBook = new ItemStack(BookRegistry.BOOK_ITEM);
		CompoundNBT data = new CompoundNBT();
		{
			// Constructing TagCompound
			ListNBT pages = new ListNBT();
			{
				// Pages
				pages.appendTag(new NBTTagString("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== "
						+ TextUtil.translateToLocal("book.spells.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (int i = 0; i < QuestList.size(); ++i) {
					IQuest quest = QuestList.get(i);

					if (quest instanceof QuestCastSpell) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
					}
				}
			}
			data.setTag("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_SPELLS);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getUID());
		}
		infoBook.setTagCompound(data);
		infoBook.setDisplayName(BookRegistry.BOOK_NAME_SPELLS);
		return infoBook;
	}
}