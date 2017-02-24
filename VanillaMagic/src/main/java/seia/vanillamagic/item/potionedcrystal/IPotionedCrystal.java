package seia.vanillamagic.item.potionedcrystal;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import seia.vanillamagic.api.item.ICustomItem;
import seia.vanillamagic.util.TextHelper;

public interface IPotionedCrystal extends ICustomItem
{
	public static final String NBT_POTION_TYPE_NAME = "NBT_POTION_TYPE_NAME";
	
	PotionType getPotionType();
	
	default ItemStack getItem()
	{
		ItemStack stack = new ItemStack(Items.NETHER_STAR);
		stack.setStackDisplayName("Potioned Crystal: " + 
									TextHelper.translateToLocal(getPotionLocalizedName()));
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		stackTag.setString(NBT_POTION_TYPE_NAME, getPotionUnlocalizedName());
		return stack;
	}
	
	/**
	 * For instance -> "water_breathing"
	 * 
	 * @return Returns the unlocalized name of the potion.
	 * @see #getPotionLocalizedName()
	 */
	default String getPotionUnlocalizedName()
	{
		//return ForgeRegistries.POTION_TYPES.getKey(getPotionType()).getResourcePath();
		return PotionedCrystalHelper.getPotionTypeName(getPotionType());
	}
	
	/**
	 * For instance -> "Potion of Water Breathing"
	 * 
	 * @return Returns the localized name of the potion.
	 * @see #getPotionUnlocalizedName()
	 */
	default String getPotionLocalizedName()
	{
		return getPotionType().getNamePrefixed("potion.effect.");
	}
	
	default void registerRecipe()
	{
	}
}