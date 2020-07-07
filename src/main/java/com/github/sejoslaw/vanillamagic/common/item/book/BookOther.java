package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.api.quest.QuestRegistry;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.item.accelerationcrystal.QuestAccelerationCrystal;
import com.github.sejoslaw.vanillamagic.common.item.liquidsuppressioncrystal.QuestLiquidSuppressionCrystal;
import com.github.sejoslaw.vanillamagic.common.item.thecrystalofmothernature.QuestMotherNatureCrystal;
import com.github.sejoslaw.vanillamagic.common.quest.QuestBuildAltar;
import com.github.sejoslaw.vanillamagic.common.quest.QuestCraftOnAltar;
import com.github.sejoslaw.vanillamagic.common.quest.spell.QuestCastSpell;
import com.github.sejoslaw.vanillamagic.common.tileentity.chunkloader.QuestChunkLoader;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.QuestQuarry;
import com.github.sejoslaw.vanillamagic.common.util.CraftingUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.StringTextComponent;

import static com.github.sejoslaw.vanillamagic.api.util.TextUtil.ENTER;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookOther implements IBook {
	public int getBookID() {
		return BookRegistry.BOOK_OTHER_UID;
	}

	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(), new Object[] { "   ", "   ", "BBB", 'B', Items.BOOK });
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
						+ TextUtil.translateToLocal("book.other.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (IQuest quest : QuestRegistry.getQuests()) {
					if (quest instanceof QuestChunkLoader) {
						pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.add(StringNBT.valueOf("Layer 1:" + ENTER + "  �6T " + ENTER + "�6T�0E�6T" + ENTER
								+ "  �6T " + TextUtil.getEnters(2) + "�0Layer 2:" + ENTER + "  �5O " + ENTER + "�5OOO"
								+ ENTER + "  �5O "));
					}
					else if (quest instanceof QuestQuarry) {
						pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
						pages.add(StringNBT.valueOf("Quarry from top:" + ENTER + "           �aN           "
								+ ENTER + "" + ENTER + "            �cR" + ENTER + "�aW          �0C�9D          �aE"
								+ ENTER + "" + ENTER + "           �aS"));
					}
					else if (quest instanceof QuestAccelerationCrystal) {
						pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.add(StringNBT.valueOf("Crafting:" + TextUtil.getEnters(2) + "[ ][�6B�0][ ]" + ENTER
								+ "[�6B�0][�8NS�0][�6B�0]" + ENTER + "[ ][�6B�0][ ]"));
					}
					else if (quest instanceof QuestLiquidSuppressionCrystal) {
						pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.add(StringNBT.valueOf("Crafting:" + TextUtil.getEnters(2) + "[�7B�0][�7B�0][�7B�0]"
								+ ENTER + "[�7B�0][�8NS�0][�7B�0]" + ENTER + "[�7B�0][�7B�0][�7B�0]"));
					}
					else if (quest instanceof QuestMotherNatureCrystal) {
						pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.add(StringNBT.valueOf("Crafting:" + TextUtil.getEnters(2) + "[�2M�0][�aS�0][�2M�0]"
								+ ENTER + "[�aS�0][�8NS�0][�aS�0]" + ENTER + "[�6P�0][�aS�0][�6P�0]"));
					}
					// Others
					else if (!(quest instanceof QuestCraftOnAltar) && !(quest instanceof QuestCastSpell) && !(quest instanceof QuestBuildAltar)) {
						pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
					}
				}

				// Item Selector
				pages.add(StringNBT.valueOf(BookRegistry.COLOR_HEADER + "Inventory Selector"
						+ TextUtil.getEnters(2) + "�0" + "Crafting (shapeless): 1x Blaze Rod + 1x Chest" + ENTER
						+ "Right-Click on Block = Save position" + ENTER + "Right-Click on Air = Show saved position"
						+ ENTER + "Shift-Right-Click on Air = Clear saved position"));
			}

			data.put("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_OTHER);
			data.putInt(BookRegistry.BOOK_NBT_UID, getBookID());
		}

		infoBook.setTag(data);
		infoBook.setDisplayName(new StringTextComponent(BookRegistry.BOOK_NAME_OTHER));

		return infoBook;
	}
}
