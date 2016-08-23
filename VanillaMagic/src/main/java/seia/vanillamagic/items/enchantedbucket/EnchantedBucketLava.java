package seia.vanillamagic.items.enchantedbucket;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class EnchantedBucketLava implements IEnchantedBucket
{
	public Fluid getFluidInBucket()
	{
		return FluidRegistry.LAVA;
	}
	
	public ItemStack getBucket()
	{
		return new ItemStack(Items.LAVA_BUCKET);
	}
}