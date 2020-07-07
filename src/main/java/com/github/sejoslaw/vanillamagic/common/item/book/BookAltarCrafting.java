package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.api.quest.QuestRegistry;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.quest.QuestCraftOnAltar;
import com.github.sejoslaw.vanillamagic.common.util.CraftingUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookAltarCrafting implements IBook {
	public int getBookID() {
		return BookRegistry.BOOK_ALTAR_CRAFTING_UID;
	}

	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(), new Object[] { "B  ", "B  ", "B  ", 'B', Items.BOOK });

//		Ingredient book = Ingredient.fromStacks(new ItemStack(Items.BOOK));
//		NonNullList<Ingredient> ingredients = NonNullList.from(book, book);
//		ShapedRecipe recipe = new ShapedRecipe(null, null, 3, 3, ingredients, getItem());
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
						+ TextUtil.translateToLocal("book.altarCrafting.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (int i = 0; i < QuestRegistry.size(); ++i) {
					IQuest quest = QuestRegistry.get(i);

					if (quest instanceof QuestCraftOnAltar) {
						pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
					}
				}
			}

			data.put("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_ALTAR_CRAFTING);
			data.putInt(BookRegistry.BOOK_NBT_UID, getBookID());
		}

		infoBook.setTag(data);
		infoBook.setDisplayName(new StringTextComponent(BookRegistry.BOOK_NAME_ALTAR_CRAFTING));

		return infoBook;
	}
}
