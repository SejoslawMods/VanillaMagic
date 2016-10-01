package seia.vanillamagic.item.book;

import seia.vanillamagic.item.ICustomItem;

/**
 * Books should be added to BookRegistry before the PostInitialization.<br>
 * In PostInitialization books recipes will be registered.
 */
public interface IBook extends ICustomItem
{
	/**
	 * Returns the index of the book (for easier searching).
	 */
	int getUID();
	
	default public String getUniqueNBTName() 
	{
		return BookRegistry.BOOK_NBT_UID;
	}
}