package com.github.sejoslaw.vanillamagic.common.item.book;

import com.github.sejoslaw.vanillamagic.api.VanillaMagicAPI;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

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
	public static final String YEAR = VanillaMagicAPI.VERSION;

	/**
	 * Book unique ID.
	 */
	public static final String BOOK_NBT_UID = "bookUID";

	// Books
	public static final IBook BOOK_ALTAR_CRAFTING;
	public static final IBook BOOK_BUILD_ALTAR;
	public static final IBook BOOK_SPELLS;
	public static final IBook BOOK_OTHER;
	public static final IBook BOOK_ITEM_UPGRADES;
	public static final IBook BOOK_QUARRY_UPGRADES;
	public static final IBook BOOK_ON_GROUND_CRAFTING_RECIPES;

	/**
	 * List with all Books.
	 */
	private static final List<IBook> BOOKS = new ArrayList<>();

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

		BOOK_ON_GROUND_CRAFTING_RECIPES = new BookOnGroundCraftingRecipes();
		BOOKS.add(BOOK_ON_GROUND_CRAFTING_RECIPES);
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

		VMLogger.logInfo("Books registered (" + BOOKS.size() + ")");
	}

	/**
	 * Checks if the given ItemStack is a Vanilla Magic Book.
	 */
	public static boolean isBook(ItemStack stack) {
		if (stack.getTag() != null) {
			return stack.getTag().hasUniqueId(BOOK_NBT_UID);
		}

		return false;
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
