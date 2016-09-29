package seia.vanillamagic.item.liquidsuppressioncrystal;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.item.ICustomItem;

public class ItemLiquidSuppressionCrystal implements ICustomItem
{
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getItem(), new Object[]{
				"BBB",
				"BNB",
				"BBB",
				'B', Items.BUCKET,
				'N', Items.NETHER_STAR
		});
	}
	
	public ItemStack getItem() 
	{
		ItemStack stack = new ItemStack(Items.NETHER_STAR);
		stack.setStackDisplayName("Liquid Suppression Crystal");
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		return stack.copy();
	}
}