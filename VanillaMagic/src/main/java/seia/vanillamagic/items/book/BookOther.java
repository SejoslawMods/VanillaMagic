package seia.vanillamagic.items.book;

import static seia.vanillamagic.utils.TextHelper.ENTER;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.chunkloader.QuestChunkLoader;
import seia.vanillamagic.machine.quarry.QuestQuarry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestBuildAltar;
import seia.vanillamagic.quest.QuestCraftOnAltar;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.quest.spell.QuestCastSpell;
import seia.vanillamagic.utils.TextHelper;

public class BookOther implements IBook
{
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getBook(), new Object[]{
				"   ",
				"   ",
				"BBB",
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
				pages.appendTag(new NBTTagString("\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " + TextHelper.translateToLocal("book.other.title") + " ====" + 
						TextHelper.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR));
				for(int i = 0; i < QuestList.QUESTS.size(); i++)
				{
					Quest quest = QuestList.QUESTS.get(i);
					if(quest instanceof QuestChunkLoader)
					{
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest.uniqueName + ".desc") + TextHelper.getEnters(2) +
								"--->"
								));
						pages.appendTag(new NBTTagString(
								"Layer 1:" + ENTER +
								"  §6T " + ENTER +
								"§6T§0E§6T" + ENTER +
								"  §6T " + TextHelper.getEnters(2) +
								"§0Layer 2:" + ENTER +
								"  §5O " + ENTER +
								"§5OOO" + ENTER +
								"  §5O "
								));
					}
					if(quest instanceof QuestQuarry)
					{
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest.uniqueName + ".desc"
										)));
						pages.appendTag(new NBTTagString(
								"Quarry from top:" + ENTER +
								"           §aN           " + ENTER +
								"" + ENTER +
								"            §cR" + ENTER +
								"§aW          §0C§9D          §aE" + ENTER +
								"" + ENTER +
								"           §aS"
								));
					}
					if(!(quest instanceof QuestCraftOnAltar) &&
							!(quest instanceof QuestCastSpell) &&
							!(quest instanceof QuestBuildAltar) &&
							!(quest instanceof QuestChunkLoader) &&
							!(quest instanceof QuestQuarry))
					{
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest.uniqueName) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest.uniqueName + ".desc"
										)));
					}
				}
			}
			data.setTag("pages", pages);
			data.setString("author", BookRegistry.AUTHOR);
			data.setString("title", BookRegistry.BOOK_NAME_OTHER);
		}
		infoBook.setTagCompound(data);
		infoBook.setStackDisplayName(BookRegistry.BOOK_NAME_OTHER);
		return infoBook.copy();
	}
}