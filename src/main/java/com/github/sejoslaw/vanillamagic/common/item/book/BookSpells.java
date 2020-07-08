package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.api.quest.QuestRegistry;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.quest.spell.QuestCastSpell;
import com.github.sejoslaw.vanillamagic.common.util.TranslationUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookSpells extends AbstractBook {
	public void registerRecipe() {
		OnGroundCraftingHandler.addRecipe(getItem(), new ItemStack(Items.BOOK, 8));
	}

	public void addPages(ListNBT pages) {
		for (IQuest quest : QuestRegistry.getQuests()) {
			if (quest instanceof QuestCastSpell) {
				pages.add(StringNBT.valueOf(
						BookRegistry.COLOR_HEADER +
						TranslationUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2) + "�0" +
						TranslationUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
			}
		}
	}

	public String getBookTranslationKey() {
		return "spells";
	}
}
