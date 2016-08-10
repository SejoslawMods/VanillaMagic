package seia.vanillamagic.items.book;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.utils.TextHelper;

public class BookRegistry 
{
	public static final BookRegistry INSTANCE = new BookRegistry();
	
	public static final Item BOOK_ITEM = Items.WRITABLE_BOOK;
	public static final String COLOR_TITLE = TextHelper.COLOR_BLUE;
	public static final String COLOR_HEADER = TextHelper.COLOR_RED;
	public static final String AUTHOR = "Seia";
	public static final String YEAR = "2016";
	public static final String BOOK_NAME_SPELLS = TextHelper.translateToLocal("book.spells.itemName");
	public static final String BOOK_NAME_ALTAR_CRAFTING = TextHelper.translateToLocal("book.altarCrafting.itemName");
	public static final String BOOK_NAME_BUILD_ALTAR = TextHelper.translateToLocal("book.altarBuilding.itemName");
	public static final String BOOK_NAME_OTHER = TextHelper.translateToLocal("book.other.itemName");
	public static final String BOOK_NBT_UID = "bookUID";
	
	private List<IBook> books = new ArrayList<IBook>();
	
	private BookRegistry()
	{
		books.add(new BookSpells());
		books.add(new BookAltarCrafting());
		books.add(new BookBuildAltar());
		books.add(new BookOther());
	}
	
	public void postInit()
	{
		for(IBook book : books)
		{
			book.registerRecipe();
		}
		System.out.println("Books registered");
	}
	
	/**
	 * bookUID: <br>
	 * 1 - Altar Crafting <br>
	 * 2 - Altar Building <br>
	 * 3 - Spells <br>
	 * 4 - Other <br>
	 */
	public ItemStack getBookByUID(int bookUID)
	{
		for(int i = 0; i < books.size(); i++)
		{
			if(books.get(i).getUID() == bookUID)
			{
				return books.get(i).getBook();
			}
		}
		return null;
	}
	
	/**
	 * Checks if the given ItemStack is a book. 
	 */
	public boolean isBook(ItemStack stack)
	{
		if(stack.getTagCompound() != null)
		{
			return stack.getTagCompound().hasKey(BOOK_NBT_UID);
		}
		return false;
	}
	
	/**
	 * This method will return the bookUID from the stack. <br>
	 * If it returns -1, than it means that the given stack is not a book.
	 */
	public int getUIDByBook(ItemStack stack)
	{
		if(isBook(stack))
		{
			return stack.getTagCompound().getInteger(BOOK_NBT_UID);
		}
		return -1;
	}
}