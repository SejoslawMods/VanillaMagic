package seia.vanillamagic.item.itemupgrade.upgrade;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry;

/**
 * This is the base of the Upgrade System.
 */
public interface IItemUpgrade
{
	public static final String NBT_ITEM_UPGRADE_NAME = "NBT_ITEM_UPGRADE_NAME";
	public static final String NBT_ITEM_CONTAINS_UPGRADE = "NBT_ITEM_CONTAINS_UPGRADE";
	
	/**
	 * This method is optional but You can do here 2 things. <br>
	 * For registering item: ItemUpgradeRegistry.INSTANCE.addItemToMapping("mapping_name", new YourItem()); <br>
	 * For registering upgrades: ItemUpgradeRegistry.INSTANCE.addUpgradeMapping("mapping_name", MyUpgrade.class); <br>
	 * If You did register new item and upgrade for YOUR item. Make sure that "mapping_name" in both is THE SAME.
	 * 
	 * @see {@link ItemUpgradeRegistry#addItemToMapping(String, Item)}
	 * @see {@link ItemUpgradeRegistry#addUpgradeMapping(String, Class)}
	 */
	void register();
	
	default ItemStack getResult(ItemStack base)
	{
		ItemStack result = base.copy();
		result.setStackDisplayName(result.getDisplayName() + " + Upgrade: " + getUpgradeName());
		NBTTagCompound stackTag = result.getTagCompound();
		stackTag.setString(NBT_ITEM_UPGRADE_NAME, getUniqueNBTTag());
		stackTag.setBoolean(NBT_ITEM_CONTAINS_UPGRADE, true);
		return result;
	}
	
	/**
	 * Ingredient which must be in Cauldron with the required Item.
	 */
	ItemStack getIngredient();
	
	/**
	 * This tag MUST BE UNIQUE !!! <br>
	 * It is used to recognize the {@link IItemUpgrade}. 
	 */
	String getUniqueNBTTag();
	
	/**
	 * Readable name for this upgrade. <br>
	 * It will be used as a part of new stack name. <br>
	 * For instance: "My Upgrade" or "Awesome Hyper World-Destroying Upgrade".
	 */
	String getUpgradeName();
}