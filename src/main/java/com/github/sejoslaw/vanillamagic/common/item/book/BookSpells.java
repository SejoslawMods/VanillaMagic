package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.api.quest.QuestRegistry;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.quest.spell.QuestCastSpell;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookSpells implements IBook {
	public int getBookID() {
		return BookRegistry.BOOK_SPELLS_UID;
	}

	public void registerRecipe() {
		OnGroundCraftingHandler.addRecipe(getItem(), new ItemStack(Items.BOOK, 8));
	}

	public ItemStack getItem() {
		ItemStack infoBook = new ItemStack(BookRegistry.BOOK_ITEM);

		CompoundNBT data = new CompoundNBT();
		{
			// Constructing TagCompound
			ListNBT pages = new ListNBT();
			{
				// Pages
				pages.add(StringNBT.valueOf("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== "
						+ TranslationUtil.translateToLocal("book.spells.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (IQuest quest : QuestRegistry.getQuests()) {
					if (quest instanceof QuestCastSpell) {
						pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
								+ TranslationUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TranslationUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
					}
				}
			}

			data.put("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_SPELLS);
			data.putInt(BookRegistry.BOOK_NBT_UID, getBookID());
		}

		infoBook.setTag(data);
		infoBook.setDisplayName(new StringTextComponent(BookRegistry.BOOK_NAME_SPELLS));

		return infoBook;
	}
}
