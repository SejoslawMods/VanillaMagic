package seia.vanillamagic.item.accelerationcrystal;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.api.item.ICustomItem;

public class ItemAccelerationCrystal implements ICustomItem
{
	public void registerRecipe()
	{
		GameRegistry.addRecipe(getItem(), new Object[]{
				" B ",
				"BNB",
				" B ",
				'B', Items.BOOK,
				'N', Items.NETHER_STAR
		});
	}
	
	public ItemStack getItem()
	{
		ItemStack stack = new ItemStack(Items.NETHER_STAR);
		stack.setStackDisplayName("Acceleration Crystal");
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		return stack;
	}
}