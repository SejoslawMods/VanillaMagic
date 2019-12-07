package com.github.sejoslaw.vanillamagic.integration.internal;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.github.sejoslaw.vanillamagic.integration.IIntegration;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class IntegrationNewVanillaCrafting implements IIntegration {
	public String getModName() {
		return "VM New Vanilla Crafting";
	}

	public void postInit() {
		GameRegistry.addShapelessRecipe(new ResourceLocation(""), null, new ItemStack(Items.BOOK),
				Ingredient.fromItem(Items.ENCHANTED_BOOK));
	}
}