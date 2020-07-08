package com.github.sejoslaw.vanillamagic.common.item.thecrystalofmothernature;

import com.github.sejoslaw.vanillamagic.common.item.CustomItemCrystal;
import com.github.sejoslaw.vanillamagic.common.item.CustomItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemMotherNatureCrystal extends CustomItemCrystal {
	public void registerRecipe() {
		CustomItemRegistry.addRecipe(this,
				new ItemStack(Items.MELON, 2),
				new ItemStack(Items.WHEAT_SEEDS, 4),
				new ItemStack(Items.NETHER_STAR),
				new ItemStack(Blocks.PUMPKIN, 2));
	}

	public ITextComponent getItemName() {
		return new StringTextComponent("The Crystal of Mother Nature");
	}
}
