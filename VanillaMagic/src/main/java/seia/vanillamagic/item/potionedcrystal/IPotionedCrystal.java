package seia.vanillamagic.item.potionedcrystal;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.item.ICustomItem;

public interface IPotionedCrystal extends ICustomItem
{
	public static final String NBT_POTION_TYPE_ID = "NBT_POTION_TYPE_ID";
	
	PotionType getPotionType();
	
	default ItemStack getItem()
	{
		ItemStack stack = new ItemStack(Items.NETHER_STAR);
		stack.setStackDisplayName("Potioned Crystal: " + getPotionName());
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		stackTag.setInteger(NBT_POTION_TYPE_ID, getPotionTypeID());
		return stack;
	}
	
	default String getPotionName()
	{
		return ForgeRegistries.POTION_TYPES.getKey(getPotionType()).getResourcePath();
	}
	
	default int getPotionTypeID()
	{
		return PotionType.getID(getPotionType());
	}
	
	default void registerRecipe()
	{
	}
}