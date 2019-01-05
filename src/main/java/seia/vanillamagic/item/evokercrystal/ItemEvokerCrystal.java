package seia.vanillamagic.item.evokercrystal;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.item.CustomItemCrystal;

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
		stack.setStackDisplayName(getItemName() + ": " + EvokerSpellRegistry.getSpell(1).getSpellName());

		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		stackTag.setInteger(NBT_SPELL_ID, 1);

		return stack;
	}
}