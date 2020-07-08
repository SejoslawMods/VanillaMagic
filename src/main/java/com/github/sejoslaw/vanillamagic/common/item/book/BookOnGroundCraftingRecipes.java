package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.util.TranslationUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

import java.util.Arrays;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookOnGroundCraftingRecipes implements IBook {
    public int getBookID() {
        return BookRegistry.BOOK_ON_GROUND_CRAFTING_RECIPES_UID;
    }

    public void registerRecipe() {
        OnGroundCraftingHandler.addRecipe(getItem(), new ItemStack(Items.BOOK, 1));
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
                        + TranslationUtil.translateToLocal("book.onGroundCrafting.title") + " ====" + TextUtil.getEnters(4) + "-"
                        + BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

                pages.add(StringNBT.valueOf(TranslationUtil.translateToLocal("book.onGroundCrafting.desc")));

                for (OnGroundCraftingHandler.OnGroundCraftingEntry entry : OnGroundCraftingHandler.ENTRIES) {
                    StringBuilder pageText = new StringBuilder(BookRegistry.COLOR_TITLE + TranslationUtil.translateToLocal("book.onGroundCrafting.ingredients") + "�0" + TextUtil.ENTER);

                    Arrays.stream(entry.ingredients).forEach(ingredient -> pageText.append(ingredient.toString()).append(TextUtil.ENTER));
                    pageText.append(BookRegistry.COLOR_TITLE).append(TranslationUtil.translateToLocal("book.onGroundCrafting.output")).append("�0").append(TextUtil.ENTER);
                    pageText.append(entry.output.toString());

                    pages.add(StringNBT.valueOf(pageText.toString()));
                }
            }

            data.put("pages", pages);
            data.putString("author", BookRegistry.AUTHOR);
            data.putString("title", BookRegistry.BOOK_NAME_ON_GROUND_CRAFTING);
            data.putInt(BookRegistry.BOOK_NBT_UID, getBookID());
        }

        infoBook.setTag(data);
        infoBook.setDisplayName(new StringTextComponent(BookRegistry.BOOK_NAME_ON_GROUND_CRAFTING));

        return infoBook;
    }
}
