package seia.vanillamagic.item.book;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.quest.spell.QuestCastSpell;
import seia.vanillamagic.util.TextHelper;

public class BookSpells implements IBook
{
	public int getUID() 
	{
		return BookRegistry.BOOK_SPELLS_UID;
	}
	
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getItem(), new Object[]{
				" B ",
				" B ",
				" B ",
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
						"\n\n\n\n" + BookRegistry.COLOR_TITLE + "==== " + TextHelper.translateToLocal("book.spells.title") + " ====" + 
						TextHelper.getEnters(4) + "-" + BookRegistry.AUTHOR + " " + BookRegistry.YEAR
						));
				for(int i = 0; i < QuestList.size(); i++)
				{
					IQuest quest = QuestList.get(i);
					if(quest instanceof QuestCastSpell)
					{
						pages.appendTag(new NBTTagString(
								BookRegistry.COLOR_HEADER + 
								TextHelper.translateToLocal("achievement." + quest.getUniqueName()) + 
								TextHelper.getEnters(2) + 
								"§0" +
								TextHelper.translateToLocal("achievement." + quest.getUniqueName() + ".desc")
								));
					}
				}
			}
			data.setTag("pages", pages);
			data.setString("author", BookRegistry.AUTHOR);
			data.setString("title", BookRegistry.BOOK_NAME_SPELLS);
			data.setInteger(BookRegistry.BOOK_NBT_UID, getUID());
		}
		infoBook.setTagCompound(data);
		infoBook.setStackDisplayName(BookRegistry.BOOK_NAME_SPELLS);
		return infoBook;
	}
}