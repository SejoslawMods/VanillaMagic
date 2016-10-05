package seia.vanillamagic.api.item;

import net.minecraft.item.ItemStack;

/**
 * Instead of this, use {@link VanillaMagicItemsAPI}
 */
public interface IVanillaMagicItems
{
	void addCustomItem(ICustomItem item);
	
	/**
	 * Returns true ONLY if the given stack is a given custom item.
	 */
	boolean isCustomItem(ItemStack checkingStack, ICustomItem customItem);
	
	boolean isCustomBucket(ItemStack checkingStack, IEnchantedBucket customBucket);
}