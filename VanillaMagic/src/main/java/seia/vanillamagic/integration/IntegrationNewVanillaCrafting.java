package seia.vanillamagic.integration;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IntegrationNewVanillaCrafting implements IIntegration
{
	public String getModName() 
	{
		return "VanillaMagic New Vanilla Crafting";
	}
	
	public boolean postInit()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(Items.BOOK), Items.ENCHANTED_BOOK);
		
		return true;
	}
}