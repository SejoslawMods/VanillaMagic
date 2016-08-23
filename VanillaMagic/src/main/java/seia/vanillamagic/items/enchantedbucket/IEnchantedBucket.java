package seia.vanillamagic.items.enchantedbucket;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.items.ICustomItem;

/**
 * Interface implemented to any class that should be read as Enchanted Bucket.
 */
public interface IEnchantedBucket extends ICustomItem
{
	public static final List<IEnchantedBucket> enchantedBuckets = new ArrayList<IEnchantedBucket>();
	public static final String NBT_ENCHANTED_BUCKET = "NBT_EnchantedBucket";
	
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
		GameRegistry.addShapelessRecipe(getItem(), new Object[]{
				new ItemStack(Items.NETHER_STAR),
				getBucket()
		});
		enchantedBuckets.add(this);
	}
	
	default ItemStack getItem()
	{
		ItemStack stack = getBucket().copy();
		stack.setStackDisplayName("Enchanted Bucket: " + getFluidInBucket().getName());
		NBTTagCompound stackTag = stack.getTagCompound();
		stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
		stackTag.setString(NBT_ENCHANTED_BUCKET, getUniqueNBTName()); // to let Quest know that we want EnchantedBucket
		return stack.copy();
	}
}