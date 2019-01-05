package seia.vanillamagic.api.item;

import net.minecraft.item.ItemStack;

/**
 * Interface implemented to any class that should be a Custom Item.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface ICustomItem {
	/**
	 * Unique NBT Tag. This Tag represents this CustomItem unique name.
	 */
	public static final String NBT_UNIQUE_NAME = "NBT_UNIQUE_NAME";

	/**
	 * Add recipe to the GameRegistry.
	 */
	void registerRecipe();

	/**
	 * @return Returns the Item as ItemStack with all the NBT data already written.
	 */
	ItemStack getItem();

	/**
	 * @return Returns the unique tag of this "item".
	 */
	default String getUniqueNBTName() {
		return "NBT_" + this.getClass().getSimpleName();
	}
}