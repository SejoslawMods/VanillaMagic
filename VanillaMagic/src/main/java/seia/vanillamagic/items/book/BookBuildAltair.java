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
				pages.appendTag(new NBTTagString(
						"\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " + TextHelper.translateToLocal("book.altarBuilding.title") + " ====" + 
						TextHelper.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR
						));
				{
					// Tier 1
					{
						QuestBuildAltar quest1 = QuestList.QUEST_BUILD_ALTAR_TIER_1;
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest1.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest1.uniqueName + ".desc") + TextHelper.getEnters(2) +
								// How Altar should look
										"§cRRR" + ENTER +
										"§cR§0C§cR" + ENTER +
										"§cRRR"
								));
					}
					// Tier 2
					{
						QuestBuildAltar quest2 = QuestList.QUEST_BUILD_ALTAR_TIER_2;
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest2.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest2.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"
								));
						pages.appendTag(new NBTTagString(
								TextHelper.getEnters(1) +
								// How Altar should look
								"§7I     I" + ENTER +
								" §cRRR " + ENTER +
								" §cR§0C§cR " + ENTER +
								" §cRRR " + ENTER +
								"§7I     I"
								));
					}
					// Tier 3
					{
						QuestBuildAltar quest3 = QuestList.QUEST_BUILD_ALTAR_TIER_3;
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest3.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest3.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"
								));
						pages.appendTag(new NBTTagString(
								TextHelper.getEnters(1) +
								// How Altar should look
								"     §6G   " + ENTER +
								"  §7I     I " + ENTER +
								"   §cRRR  " + ENTER +
								"§6G  §cR§0C§cR  §6G" + ENTER +
								"   §cRRR  " + ENTER +
								"  §7I     I " + ENTER +
								"     §6G   "
								));
					}
					// Tier 4
					{
						QuestBuildAltar quest4 = QuestList.QUEST_BUILD_ALTAR_TIER_4;
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest4.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest4.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"
								));
						pages.appendTag(new NBTTagString(
								TextHelper.getEnters(1) +
								// How Altar should look
								"§cR   §6G    §cR" + ENTER +
								"  §7I     I " + ENTER +
								"   §cRRR  " + ENTER +
								"§6G  §cR§0C§cR  §6G" + ENTER +
								"   §cRRR  " + ENTER +
								"  §7I     I " + ENTER +
								"§cR   §6G    §cR"
								));
					}
					// Tier 5
					{
						QuestBuildAltar quest5 = QuestList.QUEST_BUILD_ALTAR_TIER_5;
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest5.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest5.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"
								));
						pages.appendTag(new NBTTagString(
								TextHelper.getEnters(1) +
								// How Altar should look
								"         §1L" + ENTER +
								"                        " + ENTER +
								"   §cR    §6G    §cR" + ENTER +
								"     §7I      I " + ENTER +
								"       §cRRR  " + ENTER +
								"§1L  §6G  §cR§0C§cR  §6G  §1L" + ENTER +
								"       §cRRR  " + ENTER +
								"     §7I      I " + ENTER +
								"   §cR    §6G    §cR" + ENTER +
								"                        " + ENTER +
								"         §1L"
								));
					}
					// Tier 6
					{
						QuestBuildAltar quest6 = QuestList.QUEST_BUILD_ALTAR_TIER_6;
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest6.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest6.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"
								));
						pages.appendTag(new NBTTagString(
								TextHelper.getEnters(1) +
								// How Altar should look
								"          §1L" + ENTER +
								"  §9D              D" + ENTER +
								"    §cR    §6G    §cR" + ENTER +
								"      §7I      I " + ENTER +
								"        §cRRR  " + ENTER +
								"§1L   §6G  §cR§0C§cR   §6G    §1L" + ENTER +
								"        §cRRR  " + ENTER +
								"      §7I      I " + ENTER +
								"    §cR    §6G    §cR" + ENTER +
								"  §9D              D" + ENTER +
								"          §1L"
								));
					}
					// Tier 7
					{
						QuestBuildAltar quest7 = QuestList.QUEST_BUILD_ALTAR_TIER_7;
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest7.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest7.uniqueName + ".desc") + TextHelper.getEnters(2) + "--->"
								));
						pages.appendTag(new NBTTagString(
								// How Altar should look
								"              §aE" + ENTER +
								"                       " + ENTER +
								"              §1L" + ENTER +
								"      §9D              D" + ENTER +
								"        §cR    §6G    §cR" + ENTER +
								"          §7I      I " + ENTER +
								"            §cRRR  " + ENTER +
								"§aE  §1L   §6G  §cR§0C§cR   §6G   §1L  §aE" + ENTER +
								"            §cRRR  " + ENTER +
								"          §7I      I " + ENTER +
								"        §cR    §6G    §cR" + ENTER +
								"      §9D              D" + ENTER +
								"              §1L" + ENTER +
								"                       " + ENTER +
								"              §aE"
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