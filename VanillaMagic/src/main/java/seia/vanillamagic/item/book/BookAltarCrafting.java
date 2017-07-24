package seia.vanillamagic.item.book;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.api.quest.QuestList;
import seia.vanillamagic.quest.QuestCraftOnAltar;
import seia.vanillamagic.util.CraftingUtil;
import seia.vanillamagic.util.TextUtil;

public class BookAltarCrafting implements IBook
{
	public int getUID() 
	{
		return BookRegistry.BOOK_ALTAR_CRAFTING_UID;
	}
	
	public void registerRecipe() 
	{
		CraftingUtil.addShapedRecipe(getItem(), new Object[]{
				"B  ",
				"B  ",
				"B  ",
				'B', Items.BOOK
		});
	}
	
	public ItemStack getItem() 
	{
		ItemStack infoBook = new ItemStack(BookRegistry.BOOK_ITEM);
		NBTTagCompound data = new NBTTagCompound();
		{
			// Constructing TagCompound
			NBTTagList pages = new NBTTagList();
			{
				// Pages
				pages.appendTag(new NBTTagString(
						"\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " + TextUtil.translateToLocal("book.altarCrafting.title") + " ====" + 
						TextUtil.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR
						));
				for (int i = 0; i < QuestList.size(); ++i)
				{
					IQuest quest = QuestList.get(i);
					if (quest instanceof QuestCraftOnAltar)
					{
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextUtil.translateToLocal("quest." + quest.getUniqueName()) + 
								TextUtil.getEnters(2) + 
								"§0" +
								TextUtil.translateToLocal("quest." + quest.getUniqueName() + ".desc"
										)));
					}
				}
			}
			data.setTag("pages", pages);
			data.setString("author", BookRegistry.AUTHOR);
			data.setString("title", BookRegistry.BOOK_NAME_ALTAR_CRAFTING);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getUID());
		}
		infoBook.setTagCompound(data);
		infoBook.setStackDisplayName(BookRegistry.BOOK_NAME_ALTAR_CRAFTING);
		return infoBook;
	}
}