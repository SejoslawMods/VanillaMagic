package seia.vanillamagic.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.api.item.ICustomItem;

public abstract class CustomItem implements ICustomItem
{
	public ItemStack getItem()
	{
		ItemStack stack = new ItemStack(getBaseItem());
		stack.setStackDisplayName(getItemName());
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		return stack;
	}
	
	public abstract String getItemName();
	
	public abstract Item getBaseItem();
}