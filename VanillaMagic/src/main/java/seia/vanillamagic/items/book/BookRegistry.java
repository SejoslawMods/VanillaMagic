package seia.vanillamagic.items.book;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import seia.vanillamagic.utils.TextHelper;

public class BookRegistry 
{
	public static final BookRegistry INSTANCE = new BookRegistry();
	
	public static final Item BOOK_ITEM = Items.WRITABLE_BOOK;
	public static final String COLOR_TITLE = TextHelper.COLOR_BLUE;
	public static final String COLOR_HEADER = TextHelper.COLOR_RED;
	public static final String AUTHOR = "Seia";
	public static final String YEAR = "2016";
	public static final String BOOK_NAME_SPELLS = "Vanilla Magic Spells";
	public static final String BOOK_NAME_ALTAR_CRAFTING = "Vanilla Magic Altar Crafting";
	public static final String BOOK_NAME_BUILD_ALTAR = "Vanilla Magic Altar Building";
	public static final String BOOK_NAME_OTHER = "Vanilla Magic Other Stuff";
	
	public List<IBook> books = new ArrayList<IBook>();
	
	private BookRegistry()
	{
		books.add(new BookSpells());
		books.add(new BookAltarCrafting());
		books.add(new BookBuildAltair());
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
}