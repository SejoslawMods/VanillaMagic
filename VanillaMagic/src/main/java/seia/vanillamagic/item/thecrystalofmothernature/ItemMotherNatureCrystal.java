package seia.vanillamagic.item.thecrystalofmothernature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.item.CustomItemCrystal;

public class ItemMotherNatureCrystal extends CustomItemCrystal
{
	public void registerRecipe() 
	{
		GameRegistry.addRecipe(getItem(), new Object[]{
				"MSM",
				"SNS",
				"PSP",
				'M', Items.MELON,
				'S', Items.WHEAT_SEEDS,
				'N', Items.NETHER_STAR,
				'P', Blocks.PUMPKIN
		});
	}
	
	public String getItemName() 
	{
		return "The Crystal of Mother Nature";
	}
}