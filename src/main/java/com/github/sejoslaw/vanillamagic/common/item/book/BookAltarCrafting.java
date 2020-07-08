package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.api.quest.QuestRegistry;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.quest.QuestCraftOnAltar;
import com.github.sejoslaw.vanillamagic.common.util.TranslationUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookAltarCrafting extends AbstractBook {
	public void registerRecipe() {
		OnGroundCraftingHandler.addRecipe(getItem(), new ItemStack(Items.BOOK, 3));
	}

	public void addPages(ListNBT pages) {
		for (int i = 0; i < QuestRegistry.size(); ++i) {
			IQuest quest = QuestRegistry.get(i);

			if (quest instanceof QuestCraftOnAltar) {
				pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
						+ TranslationUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
						+ "�0" + TranslationUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
			}
		}
	}

	public String getBookTranslationKey() {
		return "altarCrafting";
	}
}
