package seia.vanillamagic.item.potionedcrystal;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import seia.vanillamagic.api.item.ICustomItem;

public interface IPotionedCrystal extends ICustomItem
{
	public static final String NBT_POTION_TYPE_NAME = "NBT_POTION_TYPE_NAME";
	
	PotionType getPotionType();
	
	default ItemStack getItem()
	{
		ItemStack stack = new ItemStack(Items.NETHER_STAR);
		stack.setStackDisplayName("Potioned Crystal: " + getPotionName());
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		stackTag.setString(NBT_POTION_TYPE_NAME, getPotionName());
		return stack;
	}
	
	default String getPotionName()
	{
		//return ForgeRegistries.POTION_TYPES.getKey(getPotionType()).getResourcePath();
		return PotionedCrystalHelper.getPotionTypeName(getPotionType());
	}
	
	default void registerRecipe()
	{
	}
}