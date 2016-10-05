package seia.vanillamagic.api.item;

import net.minecraft.item.ItemStack;

/**
 * Interface implemented to any class that should support custom items.
 */
public interface ICustomItem
{
	public static final String NBT_UNIQUE_NAME = "NBT_UNIQUE_NAME";
	
	/**
	 * Add recipe to the GameRegistry.
	 */
	void registerRecipe();
	
	/**
	 * Get the Item as ItemStack with all the NBT data already written.
	 */
	ItemStack getItem();
	
	/**
	 * Returns the unique tag of this "item".
	 */
	default String getUniqueNBTName()
	{
		return "NBT_" + this.getClass().getSimpleName();
	}
}