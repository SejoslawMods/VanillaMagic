package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.quest.QuestRegistry;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.item.CustomItemRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.QuestBuildAltar;
import com.github.sejoslaw.vanillamagic.common.util.TranslationUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

import static com.github.sejoslaw.vanillamagic.api.util.TextUtil.ENTER;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookBuildAltar extends AbstractBook
{
	public void registerRecipe() {
		CustomItemRegistry.addRecipe(this, new ItemStack(Items.BOOK, 4));
	}

	public void addPages(ListNBT pages) {
		// Tier 1
		{
			QuestBuildAltar quest1 = (QuestBuildAltar) QuestRegistry.get("buildAltarTier1");

			pages.add(StringNBT.valueOf(
					BookRegistry.COLOR_HEADER +
							TranslationUtil.translateToLocal("quest." + quest1.getUniqueName()) +
							TextUtil.getEnters(2) +
							"�0" +
							TranslationUtil.translateToLocal("quest." + quest1.getUniqueName() + ".desc") + TextUtil.getEnters(2) +
							// How Altar should look
							"�cRRR" + ENTER +
							"�cR�0C�cR" + ENTER +
							"�cRRR"
			));
		}
		// Tier 2
		{
			QuestBuildAltar quest2 = (QuestBuildAltar) QuestRegistry.get("buildAltarTier2");

			pages.add(StringNBT.valueOf(
					BookRegistry.COLOR_HEADER +
							TranslationUtil.translateToLocal("quest." + quest2.getUniqueName()) +
							TextUtil.getEnters(2) +
							"�0" +
							TranslationUtil.translateToLocal("quest." + quest2.getUniqueName() + ".desc") + TextUtil.getEnters(2) + "--->"
			));
			pages.add(StringNBT.valueOf(
					TextUtil.getEnters(1) +
							// How Altar should look
							"�7I     I" + ENTER +
							" �cRRR " + ENTER +
							" �cR�0C�cR " + ENTER +
							" �cRRR " + ENTER +
							"�7I     I"
			));
		}
		// Tier 3
		{
			QuestBuildAltar quest3 = (QuestBuildAltar) QuestRegistry.get("buildAltarTier3");

			pages.add(StringNBT.valueOf(
					BookRegistry.COLOR_HEADER +
							TranslationUtil.translateToLocal("quest." + quest3.getUniqueName()) +
							TextUtil.getEnters(2) +
							"�0" +
							TranslationUtil.translateToLocal("quest." + quest3.getUniqueName() + ".desc") + TextUtil.getEnters(2) + "--->"
			));
			pages.add(StringNBT.valueOf(
					TextUtil.getEnters(1) +
							// How Altar should look
							"     �6G   " + ENTER +
							"  �7I     I " + ENTER +
							"   �cRRR  " + ENTER +
							"�6G  �cR�0C�cR  �6G" + ENTER +
							"   �cRRR  " + ENTER +
							"  �7I     I " + ENTER +
							"     �6G   "
			));
		}
		// Tier 4
		{
			QuestBuildAltar quest4 = (QuestBuildAltar) QuestRegistry.get("buildAltarTier4");

			pages.add(StringNBT.valueOf(
					BookRegistry.COLOR_HEADER +
							TranslationUtil.translateToLocal("quest." + quest4.getUniqueName()) +
							TextUtil.getEnters(2) +
							"�0" +
							TranslationUtil.translateToLocal("quest." + quest4.getUniqueName() + ".desc") + TextUtil.getEnters(2) + "--->"
			));
			pages.add(StringNBT.valueOf(
					TextUtil.getEnters(1) +
							// How Altar should look
							"�cR   �6G    �cR" + ENTER +
							"  �7I     I " + ENTER +
							"   �cRRR  " + ENTER +
							"�6G  �cR�0C�cR  �6G" + ENTER +
							"   �cRRR  " + ENTER +
							"  �7I     I " + ENTER +
							"�cR   �6G    �cR"
			));
		}
		// Tier 5
		{
			QuestBuildAltar quest5 = (QuestBuildAltar) QuestRegistry.get("buildAltarTier5");

			pages.add(StringNBT.valueOf(
					BookRegistry.COLOR_HEADER +
							TranslationUtil.translateToLocal("quest." + quest5.getUniqueName()) +
							TextUtil.getEnters(2) +
							"�0" +
							TranslationUtil.translateToLocal("quest." + quest5.getUniqueName() + ".desc") + TextUtil.getEnters(2) + "--->"
			));
			pages.add(StringNBT.valueOf(
					TextUtil.getEnters(1) +
							// How Altar should look
							"         �1L" + ENTER +
							"                        " + ENTER +
							"   �cR    �6G    �cR" + ENTER +
							"     �7I      I " + ENTER +
							"       �cRRR  " + ENTER +
							"�1L  �6G  �cR�0C�cR  �6G  �1L" + ENTER +
							"       �cRRR  " + ENTER +
							"     �7I      I " + ENTER +
							"   �cR    �6G    �cR" + ENTER +
							"                        " + ENTER +
							"         �1L"
			));
		}
		// Tier 6
		{
			QuestBuildAltar quest6 = (QuestBuildAltar) QuestRegistry.get("buildAltarTier6");

			pages.add(StringNBT.valueOf(
					BookRegistry.COLOR_HEADER +
							TranslationUtil.translateToLocal("quest." + quest6.getUniqueName()) +
							TextUtil.getEnters(2) +
							"�0" +
							TranslationUtil.translateToLocal("quest." + quest6.getUniqueName() + ".desc") + TextUtil.getEnters(2) + "--->"
			));
			pages.add(StringNBT.valueOf(
					TextUtil.getEnters(1) +
							// How Altar should look
							"          �1L" + ENTER +
							"  �9D              D" + ENTER +
							"    �cR    �6G    �cR" + ENTER +
							"      �7I      I " + ENTER +
							"        �cRRR  " + ENTER +
							"�1L   �6G  �cR�0C�cR   �6G    �1L" + ENTER +
							"        �cRRR  " + ENTER +
							"      �7I      I " + ENTER +
							"    �cR    �6G    �cR" + ENTER +
							"  �9D              D" + ENTER +
							"          �1L"
			));
		}
		// Tier 7
		{
			QuestBuildAltar quest7 = (QuestBuildAltar) QuestRegistry.get("buildAltarTier7");

			pages.add(StringNBT.valueOf(
					BookRegistry.COLOR_HEADER +
							TranslationUtil.translateToLocal("quest." + quest7.getUniqueName()) +
							TextUtil.getEnters(2) +
							"�0" +
							TranslationUtil.translateToLocal("quest." + quest7.getUniqueName() + ".desc") + TextUtil.getEnters(2) + "--->"
			));
			pages.add(StringNBT.valueOf(
					// How Altar should look
					"              �aE" + ENTER +
							"                       " + ENTER +
							"              �1L" + ENTER +
							"      �9D              D" + ENTER +
							"        �cR    �6G    �cR" + ENTER +
							"          �7I      I " + ENTER +
							"            �cRRR  " + ENTER +
							"�aE  �1L   �6G  �cR�0C�cR   �6G   �1L  �aE" + ENTER +
							"            �cRRR  " + ENTER +
							"          �7I      I " + ENTER +
							"        �cR    �6G    �cR" + ENTER +
							"      �9D              D" + ENTER +
							"              �1L" + ENTER +
							"                       " + ENTER +
							"              �aE"
			));
		}
	}

	public String getBookTranslationKey() {
		return "altarBuilding";
	}
}
