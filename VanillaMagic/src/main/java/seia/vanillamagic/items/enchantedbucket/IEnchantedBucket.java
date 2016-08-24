package seia.vanillamagic.items.enchantedbucket;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import seia.vanillamagic.items.ICustomItem;

/**
 * Interface implemented to any class that should be read as Enchanted Bucket.
 */
public interface IEnchantedBucket extends ICustomItem
{
	public static final String NBT_ENCHANTED_BUCKET = "NBT_ENCHANTED_BUCKET";
	public static final String NBT_FLUID_NAME = "NBT_FLUID_NAME";
	
	/**
	 * Fluid which this bucket contains.
	 */
	Fluid getFluidInBucket();
	
	/**
	 * Crafting ingredient bucket with fluid.
	 */
	ItemStack getBucket();
	
	default void registerRecipe()
	{
	}
}