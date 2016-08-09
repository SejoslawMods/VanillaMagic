package seia.vanillamagic.items.book;

import net.minecraft.item.ItemStack;

/**
 * Books should added to BookRegistry before the PostInitialization.<br>
 * In PostInitialization books recipes will be registered.
 */
public interface IBook 
{
	/**
	 * Add recipe to the GameRegistry.
	 */
	public void registerRecipe();
	
	/**
	 * Get the Book as ItemStack with all the NBT data already written.
	 */
	public ItemStack getBook();
}