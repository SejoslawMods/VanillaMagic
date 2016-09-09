package seia.vanillamagic.items.book;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestCraftOnAltar;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.utils.TextHelper;

public class BookAltarCrafting implements IBook
{
	public int getUID() 
	{
		return 1;
	}
	
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getItem(), new Object[]{
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
						"\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " + TextHelper.translateToLocal("book.altarCrafting.title") + " ====" + 
						TextHelper.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR
						));
				for(int i = 0; i < QuestList.size(); i++)
				{
					Quest quest = QuestList.get(i);
					if(quest instanceof QuestCraftOnAltar)
					{
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest.uniqueName) + 
								TextHelper.getEnters(2) + 
								"�0" +
								TextHelper.translateToLocal("achievement." + quest.uniqueName + ".desc"
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
		return infoBook.copy();
	}
	
	public String getUniqueNBTName() 
	{
		return BookRegistry.BOOK_NBT_UID;
	}
}