package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.util.TranslationUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractBook implements IBook {
    public ItemStack getItem() {
        ItemStack infoBook = new ItemStack(BookRegistry.BOOK_ITEM);

        CompoundNBT data = new CompoundNBT();
        {
            ListNBT pages = new ListNBT();
            {
                pages.add(StringNBT.valueOf("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " +
                        this.getTitle() + " ====" + TextUtil.getEnters(4) + "-" +
                        BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

                this.addPages(pages);
            }

            data.put("pages", pages);
            data.putString("author", BookRegistry.AUTHOR);
            data.putString("title", this.getItemName());
        }

        infoBook.setTag(data);
        infoBook.setDisplayName(new StringTextComponent(this.getItemName()));

        return infoBook;
    }

    public String getTitle() {
        return TranslationUtil.translateToLocal("book." + this.getBookTranslationKey() + ".title");
    }

    public String getItemName() {
        return TranslationUtil.translateToLocal("book." + this.getBookTranslationKey() + ".itemName");
    }

    /**
     * Adds pages to the current book.
     * @param pages
     */
    public abstract void addPages(ListNBT pages);

    /**
     * @return The key from translation for this book.
     */
    public abstract String getBookTranslationKey();
}
