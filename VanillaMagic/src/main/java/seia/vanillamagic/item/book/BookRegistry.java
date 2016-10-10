package seia.vanillamagic.item.book;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.util.TextHelper;

public class BookRegistry 
{
	public static final Item BOOK_ITEM = Items.WRITABLE_BOOK;
	public static final String COLOR_TITLE = TextHelper.COLOR_BLUE;
	public static final String COLOR_HEADER = TextHelper.COLOR_RED;
	public static final String AUTHOR = "Seia";
	public static final String YEAR = "2016";
	public static final String BOOK_NAME_SPELLS = TextHelper.translateToLocal("book.spells.itemName");
	public static final String BOOK_NAME_ALTAR_CRAFTING = TextHelper.translateToLocal("book.altarCrafting.itemName");
	public static final String BOOK_NAME_BUILD_ALTAR = TextHelper.translateToLocal("book.altarBuilding.itemName");
	public static final String BOOK_NAME_OTHER = TextHelper.translateToLocal("book.other.itemName");
	public static final String BOOK_NAME_ITEM_UPGRADES = TextHelper.translateToLocal("book.itemUpgrades.itemName");
	public static final String BOOK_NAME_QUARRY_UPGRADES = TextHelper.translateToLocal("book.quarryUpgrades.itemName");
	public static final String BOOK_NBT_UID = "bookUID";
	
	public static final int BOOK_ALTAR_CRAFTING_UID = 1;
	public static final int BOOK_BUILD_ALTAR_UID = 2;
	public static final int BOOK_SPELLS_UID = 3;
	public static final int BOOK_OTHER_UID = 4;
	public static final int BOOK_ITEM_UPGRADES_UID = 5;
	public static final int BOOK_QUARRY_UPGRADES_UID = 6;
	
	public static final IBook BOOK_ALTAR_CRAFTING;
	public static final IBook BOOK_BUILD_ALTAR;
	public static final IBook BOOK_SPELLS;
	public static final IBook BOOK_OTHER;
	public static final IBook BOOK_ITEM_UPGRADES;
	public static final IBook BOOK_QUARRY_UPGRADES;
	
	private static List<IBook> books = new ArrayList<IBook>();
	
	static
	{
		BOOK_SPELLS = new BookSpells();
		books.add(BOOK_SPELLS);
		
		BOOK_ALTAR_CRAFTING = new BookAltarCrafting();
		books.add(BOOK_ALTAR_CRAFTING);
		
		BOOK_BUILD_ALTAR = new BookBuildAltar();
		books.add(BOOK_BUILD_ALTAR);
		
		BOOK_OTHER = new BookOther();
		books.add(BOOK_OTHER);
		
		BOOK_ITEM_UPGRADES = new BookItemUpgrade();
		books.add(BOOK_ITEM_UPGRADES);
		
		BOOK_QUARRY_UPGRADES = new BookQuarryUpgrades();
		books.add(BOOK_QUARRY_UPGRADES);
	}
	
	private BookRegistry()
	{
	}
	
	public static void postInit()
	{
		for(IBook book : books)
		{
			book.registerRecipe();
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Books registered (" + books.size() + ")");
	}
	
	/**
	 * bookUID: <br>
	 * 1 - Altar Crafting <br>
	 * 2 - Altar Building <br>
	 * 3 - Spells <br>
	 * 4 - Other <br>
	 * 5 - Item Upgrades <br>
	 * 6 - Quarry Upgrades <br>
	 */
	public static ItemStack getBookByUID(int bookUID)
	{
		for(int i = 0; i < books.size(); i++)
		{
			if(books.get(i).getUID() == bookUID)
			{
				return books.get(i).getItem();
			}
		}
		return null;
	}
	
	/**
	 * Checks if the given ItemStack is a book. 
	 */
	public static boolean isBook(ItemStack stack)
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
	public static int getUIDByBook(ItemStack stack)
	{
		if(isBook(stack))
		{
			return stack.getTagCompound().getInteger(BOOK_NBT_UID);
		}
		return -1;
	}
}