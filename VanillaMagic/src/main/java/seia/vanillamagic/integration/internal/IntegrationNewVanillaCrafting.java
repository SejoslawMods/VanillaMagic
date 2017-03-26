package seia.vanillamagic.integration.internal;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.integration.IIntegration;

public class IntegrationNewVanillaCrafting implements IIntegration
{
	public String getModName() 
	{
		return "VM New Vanilla Crafting";
	}
	
	public void postInit()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(Items.BOOK), Items.ENCHANTED_BOOK);
	}
}