package seia.vanillamagic.item.thecrystalofmothernature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.api.item.ICustomItem;

public class ItemMotherNatureCrystal implements ICustomItem
{
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getItem(), new Object[]{
				"MSM",
				"SNS",
				"PSP",
				'M', Items.MELON,
				'S', Items.WHEAT_SEEDS,
				'N', Items.NETHER_STAR,
				'P', Blocks.PUMPKIN
		});
	}
	
	public ItemStack getItem()
	{
		ItemStack stack = new ItemStack(Items.NETHER_STAR);
		stack.setStackDisplayName("The Crystal of Mother Nature");
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		return stack.copy();
	}
}