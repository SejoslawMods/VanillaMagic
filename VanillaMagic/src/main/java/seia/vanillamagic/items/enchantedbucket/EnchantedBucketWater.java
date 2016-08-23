package seia.vanillamagic.items.enchantedbucket;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class EnchantedBucketWater implements IEnchantedBucket
{
	public Fluid getFluidInBucket()
	{
		return FluidRegistry.WATER;
	}
	
	public ItemStack getBucket() 
	{
		return new ItemStack(Items.WATER_BUCKET);
	}
}