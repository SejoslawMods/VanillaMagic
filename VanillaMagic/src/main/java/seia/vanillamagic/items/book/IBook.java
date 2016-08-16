package seia.vanillamagic.items.book;

import net.minecraft.item.ItemStack;

/**
 * Books should be added to BookRegistry before the PostInitialization.<br>
 * In PostInitialization books recipes will be registered.
 */
public interface IBook
{
	/**
	 * Returns the index of the book (for easier searching).
	 */
	public int getUID();
	
	/**
	 * Add recipe to the GameRegistry.
	 */
	public void registerRecipe();
	
	/**
	 * Get the Book as ItemStack with all the NBT data already written.
	 */
	public ItemStack getBook();
}