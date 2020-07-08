package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.util.TranslationUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

import java.util.Arrays;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookOnGroundCraftingRecipes extends AbstractBook {
    public void registerRecipe() {
        OnGroundCraftingHandler.addRecipe(getItem(), new ItemStack(Items.BOOK, 1));
    }

    public void addPages(ListNBT pages) {
        pages.add(StringNBT.valueOf(TranslationUtil.translateToLocal("book.onGroundCrafting.desc")));

        for (OnGroundCraftingHandler.OnGroundCraftingEntry entry : OnGroundCraftingHandler.ENTRIES) {
            StringBuilder pageText = new StringBuilder(BookRegistry.COLOR_TITLE + TranslationUtil.translateToLocal("book.onGroundCrafting.ingredients") + "�0" + TextUtil.ENTER);

            Arrays.stream(entry.ingredients).forEach(ingredient -> pageText.append(ingredient.toString()).append(TextUtil.ENTER));
            pageText.append(BookRegistry.COLOR_TITLE).append(TranslationUtil.translateToLocal("book.onGroundCrafting.output")).append("�0").append(TextUtil.ENTER);
            pageText.append(entry.output.toString());

            pages.add(StringNBT.valueOf(pageText.toString()));
        }
    }

    public String getBookTranslationKey() {
        return "onGroundCrafting";
    }
}
