package com.github.sejoslaw.vanillamagic.common.item.evokercrystal;

import com.github.sejoslaw.vanillamagic.common.item.CustomItemCrystal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

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

	public ITextComponent getItemName() {
		return new StringTextComponent("Evoker Crystal");
	}

	public ItemStack getItem() {
		ItemStack stack = new ItemStack(getBaseItem());
		stack.setDisplayName(new StringTextComponent(getItemName() + ": " + EvokerSpellRegistry.getSpell(1).getSpellName()));

		CompoundNBT stackTag = stack.getTag();
		stackTag.putString(NBT_UNIQUE_NAME, getUniqueNBTName());
		stackTag.putInt(NBT_SPELL_ID, 1);

		return stack;
	}
}
