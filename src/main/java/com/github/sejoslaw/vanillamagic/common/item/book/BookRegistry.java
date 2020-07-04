package com.github.sejoslaw.vanillamagic.common.item.book;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.github.sejoslaw.vanillamagic.util.TextUtil;

/**
 * Class which holds all additional data for Books and Books themselves.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class BookRegistry {
	/**
	 * Basic Book Item
	 */
	public static final Item BOOK_ITEM = Items.WRITABLE_BOOK;
	/**
	 * Color of the book title.
	 */
	public static final String COLOR_TITLE = TextUtil.COLOR_BLUE;
	/**
	 * Color of the book header.
	 */
	public static final String COLOR_HEADER = TextUtil.COLOR_RED;
	/**
	 * Name of the Book author.
	 */
	public static final String AUTHOR = "Seia";
	/**
	 * Year displayed in book.
	 */
	public static final String YEAR = "2019";

	// Book Names
	public static final String BOOK_NAME_SPELLS = TextUtil.translateToLocal("book.spells.itemName");
	public static final String BOOK_NAME_ALTAR_CRAFTING = TextUtil.translateToLocal("book.altarCrafting.itemName");
	public static final String BOOK_NAME_BUILD_ALTAR = TextUtil.translateToLocal("book.altarBuilding.itemName");
	public static final String BOOK_NAME_OTHER = TextUtil.translateToLocal("book.other.itemName");
	public static final String BOOK_NAME_ITEM_UPGRADES = TextUtil.translateToLocal("book.itemUpgrades.itemName");
	public static final String BOOK_NAME_QUARRY_UPGRADES = TextUtil.translateToLocal("book.quarryUpgrades.itemName");

	/**
	 * Book unique ID.
	 */
	public static final String BOOK_NBT_UID = "bookUID";

	// Book unique IDs
	public static final int BOOK_ALTAR_CRAFTING_UID = 1;
	public static final int BOOK_BUILD_ALTAR_UID = 2;
	public static final int BOOK_SPELLS_UID = 3;
	public static final int BOOK_OTHER_UID = 4;
	public static final int BOOK_ITEM_UPGRADES_UID = 5;
	public static final int BOOK_QUARRY_UPGRADES_UID = 6;

	// Books
	public static final IBook BOOK_ALTAR_CRAFTING;
	public static final IBook BOOK_BUILD_ALTAR;
	public static final IBook BOOK_SPELLS;
	public static final IBook BOOK_OTHER;
	public static final IBook BOOK_ITEM_UPGRADES;
	public static final IBook BOOK_QUARRY_UPGRADES;

	/**
	 * List with all Books.
	 */
	private static List<IBook> BOOKS = new ArrayList<IBook>();

	static {
		BOOK_SPELLS = new BookSpells();
		BOOKS.add(BOOK_SPELLS);

		BOOK_ALTAR_CRAFTING = new BookAltarCrafting();
		BOOKS.add(BOOK_ALTAR_CRAFTING);

		BOOK_BUILD_ALTAR = new BookBuildAltar();
		BOOKS.add(BOOK_BUILD_ALTAR);

		BOOK_OTHER = new BookOther();
		BOOKS.add(BOOK_OTHER);

		BOOK_ITEM_UPGRADES = new BookItemUpgrade();
		BOOKS.add(BOOK_ITEM_UPGRADES);

		BOOK_QUARRY_UPGRADES = new BookQuarryUpgrades();
		BOOKS.add(BOOK_QUARRY_UPGRADES);
	}

	private BookRegistry() {
	}

	/**
	 * PostInitialization stage. Register all recipes.
	 */
	public static void postInit() {
		for (IBook book : BOOKS) {
			book.registerRecipe();
		}

		VanillaMagic.logInfo("Books registered (" + BOOKS.size() + ")");
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
	public static ItemStack getBookByUID(int bookUID) {
		for (int i = 0; i < BOOKS.size(); ++i) {
			if (BOOKS.get(i).getBookID() == bookUID) {
				return BOOKS.get(i).getItem();
			}
		}

		return null;
	}

	/**
	 * Checks if the given ItemStack is a book.
	 */
	public static boolean isBook(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			return stack.getTagCompound().hasKey(BOOK_NBT_UID);
		}

		return false;
	}

	/**
	 * This method will return the bookUID from the stack. <br>
	 * If it returns -1, than it means that the given stack is not a book.
	 */
	public static int getUIDByBook(ItemStack stack) {
		if (isBook(stack)) {
			return stack.getTagCompound().getInteger(BOOK_NBT_UID);
		}

		return -1;
	}

	/**
	 * @return The list of all registered IBooks.
	 */
	public static List<IBook> getBooks() {
		return BOOKS;
	}

	/**
	 * Add all Books to list.
	 */
	public static NonNullList<ItemStack> fillList(NonNullList<ItemStack> list) {
		for (IBook book : BOOKS) {
			list.add(book.getItem());
		}

		return list;
	}
}