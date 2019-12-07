package com.github.sejoslaw.vanillamagic.item.evokercrystal;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import com.github.sejoslaw.vanillamagic.item.CustomItemCrystal;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemEvokerCrystal extends CustomItemCrystal {
	public static final String NBT_SPELL_ID = "NBT_SPELL_ID";

	/**
	 * Evoker Crystal cannot be crafted !!! They only drop from Evokers.
	 */
	public void registerRecipe() {
	}

	public String getItemName() {
		return "Evoker Crystal";
	}

	public ItemStack getItem() {
		ItemStack stack = new ItemStack(getBaseItem());
		stack.setDisplayName(getItemName() + ": " + EvokerSpellRegistry.getSpell(1).getSpellName());

		CompoundNBT stackTag = stack.getTagCompound();
		stackTag.putString(NBT_UNIQUE_NAME, getUniqueNBTName());
		stackTag.setInteger(NBT_SPELL_ID, 1);

		return stack;
	}
}