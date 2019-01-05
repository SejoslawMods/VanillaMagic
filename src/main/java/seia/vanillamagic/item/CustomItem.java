package seia.vanillamagic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.api.item.ICustomItem;

/**
 * Basic ICustomItem implementation.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class CustomItem implements ICustomItem {
	public ItemStack getItem() {
		ItemStack stack = new ItemStack(this.getBaseItem());
		stack.setStackDisplayName(this.getItemName());

		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());

		return stack;
	}

	/**
	 * @return Returns the name on this item to be displayed.
	 */
	public abstract String getItemName();

	/**
	 * @return Returns item connected with this CustomItem.
	 */
	public abstract Item getBaseItem();
}