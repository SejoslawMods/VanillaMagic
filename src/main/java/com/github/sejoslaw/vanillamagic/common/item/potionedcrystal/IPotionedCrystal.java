package com.github.sejoslaw.vanillamagic.common.item.potionedcrystal;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;
import net.minecraft.potion.Potion;
import net.minecraft.util.text.StringTextComponent;

/**
 * Interface used to dynamically create PotionedCrystals for each registered
 * Potion.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IPotionedCrystal extends ICustomItem {
	public static final String NBT_POTION_TYPE_NAME = "NBT_POTION_TYPE_NAME";

	/**
	 * @return Returns the type of this Potion.
	 */
	Potion getPotion();

	/**
	 * @return Returns the Crystal.
	 */
	default ItemStack getItem() {
		ItemStack stack = new ItemStack(Items.NETHER_STAR);
		stack.setDisplayName(new StringTextComponent("Potioned Crystal: " + TextUtil.translateToLocal(getPotionLocalizedName())));

		CompoundNBT stackTag = stack.getTag();
		stackTag.putString(NBT_UNIQUE_NAME, getUniqueNBTName());
		stackTag.putString(NBT_POTION_TYPE_NAME, getPotionUnlocalizedName());

		return stack;
	}

	/**
	 * For instance -> "water_breathing"
	 * 
	 * @return Returns the unlocalized name of the potion.
	 * 
	 * @see #getPotionLocalizedName()
	 */
	default String getPotionUnlocalizedName() {
		return PotionedCrystalHelper.getPotionTypeName(getPotion());
	}

	/**
	 * For instance -> "Potion of Water Breathing"
	 * 
	 * @return Returns the localized name of the potion.
	 * 
	 * @see #getPotionUnlocalizedName()
	 */
	default String getPotionLocalizedName() {
		return getPotion().getNamePrefixed("potion.effect.");
	}

	/**
	 * PotionedCrstals are created using outside technique (craft inside Cauldron).
	 */
	default void registerRecipe() {
	}
}
