package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.quest.QuestCraftOnAltar;
import com.github.sejoslaw.vanillamagic.common.util.CraftingUtil;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.api.quest.QuestRegistry;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookAltarCrafting implements IBook {
	public int getBookID() {
		return BookRegistry.BOOK_ALTAR_CRAFTING_UID;
	}

	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(), new Object[] { "B  ", "B  ", "B  ", 'B', Items.BOOK });

		Ingredient book = Ingredient.fromStacks(new ItemStack(Items.BOOK));
		NonNullList<Ingredient> ingredients = NonNullList.from(book, book);
		ShapedRecipe recipe = new ShapedRecipe(null, null, 3, 3, ingredients, getItem());
		ForgeRegistries.
	}

	public ItemStack getItem() {
		ItemStack infoBook = new ItemStack(BookRegistry.BOOK_ITEM);
		CompoundNBT data = new CompoundNBT();
		{
			// Constructing TagCompound
			ListNBT pages = new ListNBT();
			{
				// Pages
				pages.add(new StringNBT("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== "
						+ TextUtil.translateToLocal("book.altarCrafting.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (int i = 0; i < QuestRegistry.size(); ++i) {
					IQuest quest = QuestRegistry.get(i);

					if (quest instanceof QuestCraftOnAltar) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
					}
				}
			}
			data.setTag("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_ALTAR_CRAFTING);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getBookID());
		}
		infoBook.setTagCompound(data);
		infoBook.setDisplayName(BookRegistry.BOOK_NAME_ALTAR_CRAFTING);
		return infoBook;
	}
}