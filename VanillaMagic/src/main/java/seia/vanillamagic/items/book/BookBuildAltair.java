package seia.vanillamagic.items.book;

import static seia.vanillamagic.utils.TextHelper.ENTER;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.quest.QuestBuildAltar;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.utils.TextHelper;

public class BookBuildAltair implements IBook
{
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getBook(), new Object[]{
				"   ",
				"BBB",
				"   ",
				'B', Items.BOOK
		});
	}
	
	public ItemStack getBook()
	{
		ItemStack infoBook = new ItemStack(BookRegistry.BOOK_ITEM);
		NBTTagCompound data = new NBTTagCompound();
		{
			// Constructing TagCompound
			NBTTagList pages = new NBTTagList();
			{
				// Pages
				pages.appendTag(new NBTTagString("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== Altar Building ====" + 
						TextHelper.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR));
				{
					// Tier 1
					{
						QuestBuildAltar quest1 = QuestList.QUEST_BUILD_ALTAR_TIER_1;
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest1.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest1.uniqueName + ".desc") + TextHelper.getEnters(2) +
								// How Altar should look
										"RRR" + ENTER +
										"RCR" + ENTER +
										"RRR"
								));
					}
					// Tier 2
					{
						QuestBuildAltar quest2 = QuestList.QUEST_BUILD_ALTAR_TIER_2;
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest2.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest2.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString(TextHelper.getEnters(1) +
								// How Altar should look
								"I     I" + ENTER +
								" RRR " + ENTER +
								" RCR " + ENTER +
								" RRR " + ENTER +
								"I     I"
								));
					}
					// Tier 3
					{
						QuestBuildAltar quest3 = QuestList.QUEST_BUILD_ALTAR_TIER_3;
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest3.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest3.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString(TextHelper.getEnters(1) +
								// How Altar should look
								"     G   " + ENTER +
								"  I     I " + ENTER +
								"   RRR  " + ENTER +
								"G  RCR  G" + ENTER +
								"   RRR  " + ENTER +
								"  I     I " + ENTER +
								"     G   "
								));
					}
					// Tier 4
					{
						QuestBuildAltar quest4 = QuestList.QUEST_BUILD_ALTAR_TIER_4;
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest4.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest4.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString(TextHelper.getEnters(1) +
								// How Altar should look
								"R   G    R" + ENTER +
								"  I     I " + ENTER +
								"   RRR  " + ENTER +
								"G  RCR  G" + ENTER +
								"   RRR  " + ENTER +
								"  I     I " + ENTER +
								"R   G    R"
								));
					}
					// Tier 5
					{
						QuestBuildAltar quest5 = QuestList.QUEST_BUILD_ALTAR_TIER_5;
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest5.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest5.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString(TextHelper.getEnters(1) +
								// How Altar should look
								"         L" + ENTER +
								"                        " + ENTER +
								"   R    G    R" + ENTER +
								"     I      I " + ENTER +
								"       RRR  " + ENTER +
								"L  G  RCR  G  L" + ENTER +
								"       RRR  " + ENTER +
								"     I      I " + ENTER +
								"   R    G    R" + ENTER +
								"                        " + ENTER +
								"         L"
								));
					}
					// Tier 6
					{
						QuestBuildAltar quest6 = QuestList.QUEST_BUILD_ALTAR_TIER_6;
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest6.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest6.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString(TextHelper.getEnters(1) +
								// How Altar should look
								"          L" + ENTER +
								"  D              D" + ENTER +
								"    R    G    R" + ENTER +
								"      I      I " + ENTER +
								"        RRR  " + ENTER +
								"L   G  RCR   G    L" + ENTER +
								"        RRR  " + ENTER +
								"      I      I " + ENTER +
								"    R    G    R" + ENTER +
								"  D              D" + ENTER +
								"          L"
								));
					}
					// Tier 7
					{
						QuestBuildAltar quest7 = QuestList.QUEST_BUILD_ALTAR_TIER_7;
						pages.appendTag(new NBTTagString(BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest7.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest7.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"));
						pages.appendTag(new NBTTagString(
								// How Altar should look
								"              E" + ENTER +
								"                       " + ENTER +
								"              L" + ENTER +
								"      D              D" + ENTER +
								"        R    G    R" + ENTER +
								"          I      I " + ENTER +
								"            RRR  " + ENTER +
								"E  L   G  RCR   G   L  E" + ENTER +
								"            RRR  " + ENTER +
								"          I      I " + ENTER +
								"        R    G    R" + ENTER +
								"      D              D" + ENTER +
								"              L" + ENTER +
								"                       " + ENTER +
								"              E"
								));
					}
				}
			}
			data.setTag("pages", pages);
			data.setString("author", BookRegistry.AUTHOR);
			data.setString("title", BookRegistry.BOOK_NAME_BUILD_ALTAR);
		}
		infoBook.setTagCompound(data);
		infoBook.setStackDisplayName(BookRegistry.BOOK_NAME_BUILD_ALTAR);
		return infoBook.copy();
	}
}