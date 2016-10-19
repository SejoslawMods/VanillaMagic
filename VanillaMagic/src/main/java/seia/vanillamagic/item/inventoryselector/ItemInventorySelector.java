package seia.vanillamagic.item.inventoryselector;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.api.item.ICustomItem;

public class ItemInventorySelector implements ICustomItem
{
	public void registerRecipe() 
	{
		GameRegistry.addShapelessRecipe(getItem(), Items.BLAZE_ROD, Blocks.CHEST);
	}
	
	public ItemStack getItem() 
	{
		ItemStack stack = new ItemStack(Items.BLAZE_ROD);
		stack.setStackDisplayName("Inventory Selector");
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		return stack;
	}
}