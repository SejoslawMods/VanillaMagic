package com.github.sejoslaw.vanillamagic.item.book;

import static com.github.sejoslaw.vanillamagic.util.TextUtil.ENTER;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTTagString;
import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import com.github.sejoslaw.vanillamagic.api.quest.QuestList;
import com.github.sejoslaw.vanillamagic.item.accelerationcrystal.QuestAccelerationCrystal;
import com.github.sejoslaw.vanillamagic.item.liquidsuppressioncrystal.QuestLiquidSuppressionCrystal;
import com.github.sejoslaw.vanillamagic.item.thecrystalofmothernature.QuestMotherNatureCrystal;
import com.github.sejoslaw.vanillamagic.quest.QuestBuildAltar;
import com.github.sejoslaw.vanillamagic.quest.QuestCraftOnAltar;
import com.github.sejoslaw.vanillamagic.quest.spell.QuestCastSpell;
import com.github.sejoslaw.vanillamagic.tileentity.chunkloader.QuestChunkLoader;
import com.github.sejoslaw.vanillamagic.tileentity.machine.autocrafting.QuestAutocrafting;
import com.github.sejoslaw.vanillamagic.tileentity.machine.quarry.QuestQuarry;
import com.github.sejoslaw.vanillamagic.util.CraftingUtil;
import com.github.sejoslaw.vanillamagic.util.TextUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BookOther implements IBook {
	public int getUID() {
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
				pages.appendTag(new NBTTagString("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== "
						+ TextUtil.translateToLocal("book.other.title") + " ====" + TextUtil.getEnters(4) + "-"
						+ BookRegistry.AUTHOR + " " + BookRegistry.YEAR));

				for (int i = 0; i < QuestList.size(); ++i) {
					IQuest quest = QuestList.get(i);

					if (quest instanceof QuestChunkLoader) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString("Layer 1:" + ENTER + "  �6T " + ENTER + "�6T�0E�6T" + ENTER
								+ "  �6T " + TextUtil.getEnters(2) + "�0Layer 2:" + ENTER + "  �5O " + ENTER + "�5OOO"
								+ ENTER + "  �5O "));
					} else if (quest instanceof QuestQuarry) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
						pages.appendTag(new NBTTagString("Quarry from top:" + ENTER + "           �aN           "
								+ ENTER + "" + ENTER + "            �cR" + ENTER + "�aW          �0C�9D          �aE"
								+ ENTER + "" + ENTER + "           �aS"));
					} else if (quest instanceof QuestAccelerationCrystal) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString("Crafting:" + TextUtil.getEnters(2) + "[ ][�6B�0][ ]" + ENTER
								+ "[�6B�0][�8NS�0][�6B�0]" + ENTER + "[ ][�6B�0][ ]"));
					} else if (quest instanceof QuestLiquidSuppressionCrystal) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString("Crafting:" + TextUtil.getEnters(2) + "[�7B�0][�7B�0][�7B�0]"
								+ ENTER + "[�7B�0][�8NS�0][�7B�0]" + ENTER + "[�7B�0][�7B�0][�7B�0]"));
					} else if (quest instanceof QuestMotherNatureCrystal) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString("Crafting:" + TextUtil.getEnters(2) + "[�2M�0][�aS�0][�2M�0]"
								+ ENTER + "[�aS�0][�8NS�0][�aS�0]" + ENTER + "[�6P�0][�aS�0][�6P�0]"));
					} else if (quest instanceof QuestAutocrafting) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")
								+ TextUtil.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString("�0Layer 1: (�7X-Empty space, �6C-Chest�0)" + ENTER + "�7XXXXX"
								+ ENTER + "�7XXXXX" + ENTER + "�7XX�6C�7XX" + ENTER + "�7XXXXX" + ENTER + "�7XXXXX"
								+ TextUtil.getEnters(2) + "�0Layer 2: (�7X-Empty space, �0C-Cauldron�0)" + ENTER
								+ "�7XXXXX" + ENTER + "�7XXXXX" + ENTER + "�7XX�0C�7XX" + ENTER + "�7XXXXX" + ENTER
								+ "�7XXXXX"));
						pages.appendTag(new NBTTagString("�0Layer 3: (�7X-Empty space, �6C-CraftingTable�0)" + ENTER
								+ "�7XXXXX" + ENTER + "�7XXXXX" + ENTER + "�7XX�6C�7XX" + ENTER + "�7XXXXX" + ENTER
								+ "�7XXXXX" + TextUtil.getEnters(2) + "�0Layer 4: (�7X-Empty space, �0H-Hopper�0)"
								+ ENTER + "�7XXXXX" + ENTER + "�7XXXXX" + ENTER + "�7XX�0H�7XX" + ENTER + "�7XXXXX"
								+ ENTER + "�7XXXXX"));
						pages.appendTag(new NBTTagString("�0Layer 5: (�7X-Empty space, �6C-Chest�0)" + ENTER
								+ "�6C�7X�6C�7X�6C" + ENTER + "�7XXXXX" + ENTER + "�6C�7X�6C�7X�6C" + ENTER + "�7XXXXX"
								+ ENTER + "�6C�7X�6C�7X�6C"));
					}
					// Others
					else if (!(quest instanceof QuestCraftOnAltar) && !(quest instanceof QuestCastSpell)
							&& !(quest instanceof QuestBuildAltar)) {
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER
								+ TextUtil.translateToLocal("quest." + quest.getUniqueName()) + TextUtil.getEnters(2)
								+ "�0" + TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc")));
					}
				}

				// Item Selector
				pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER + "Inventory Selector"
						+ TextUtil.getEnters(2) + "�0" + "Crafting (shapeless): 1x Blaze Rod + 1x Chest" + ENTER
						+ "Right-Click on Block = Save position" + ENTER + "Right-Click on Air = Show saved position"
						+ ENTER + "Shift-Right-Click on Air = Clear saved position"));
			}
			data.setTag("pages", pages);
			data.putString("author", BookRegistry.AUTHOR);
			data.putString("title", BookRegistry.BOOK_NAME_OTHER);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getUID());
		}
		infoBook.setTagCompound(data);
		infoBook.setDisplayName(BookRegistry.BOOK_NAME_OTHER);
		return infoBook;
	}
}