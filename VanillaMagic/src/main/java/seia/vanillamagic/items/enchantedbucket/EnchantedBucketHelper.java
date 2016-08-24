package seia.vanillamagic.items.enchantedbucket;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.items.VanillaMagicItems;
import seia.vanillamagic.utils.CauldronHelper;

public class EnchantedBucketHelper 
{
	private EnchantedBucketHelper()
	{
	}
	
	public static FluidStack getFluidStack(Fluid fluid)
	{
		return new FluidStack(fluid, Fluid.BUCKET_VOLUME);
	}
	
	public static ItemStack getResult(ItemStack stack, Fluid fluid)
	{
		IFluidHandler fh = FluidUtil.getFluidHandler(stack);
		fh.fill(getFluidStack(fluid), true);
		return stack;
	}
	
	public static ItemStack getResult(Fluid fluid)
	{
		return getResult(new ItemStack(Items.BUCKET), fluid);
	}
	
	public static IEnchantedBucket getEnchantedBucket(ItemStack stack) // ItemBucket
	{
		for(IEnchantedBucket enchantedBucket : VanillaMagicItems.INSTANCE.enchantedBuckets)
		{
			if(VanillaMagicItems.INSTANCE.isCustomBucket(stack, enchantedBucket))
			{
				return enchantedBucket;
			}
		}
		return null;
	}
	
	/**
	 * Returns the first IEnchantedBucket from Cauldron at the given position.
	 */
	public static IEnchantedBucket getEnchantedBucketFromCauldron(World world, BlockPos cauldronPos)
	{
		List<EntityItem> itemsInCauldron = CauldronHelper.getItemsInCauldron(world, cauldronPos);
		for(EntityItem ei : itemsInCauldron)
		{
			ItemStack stackBucket = ei.getEntityItem();
			IFluidHandler fh = FluidUtil.getFluidHandler(stackBucket);
			if(fh != null)
			{
				for(IEnchantedBucket eb : VanillaMagicItems.INSTANCE.enchantedBuckets)
				{
					if(fh.getTankProperties() != null)
					{
						if(fh.getTankProperties()[0].getContents().isFluidEqual(getFluidStack(eb.getFluidInBucket())))
						{
							return eb;
						}
					}
				}
			}
		}
		return null;
	}
	
	public static void registerFluids()
	{
		Map<String, Fluid> registeredFluids = FluidRegistry.getRegisteredFluids();
		Collection<Fluid> fluids = registeredFluids.values();
		for(Fluid fluid : fluids)
		{
			VanillaMagicItems.INSTANCE.enchantedBuckets.add(new IEnchantedBucket()
			{
				public ItemStack getItem()
				{
					ItemStack stack = getBucket().copy();
					stack.setStackDisplayName("Enchanted Bucket: " + getFluidInBucket().getName());
					NBTTagCompound stackTag = stack.getTagCompound();
					stackTag.setString(NBT_UNIQUE_NAME, getUniqueNBTName());
					stackTag.setString(NBT_ENCHANTED_BUCKET, getUniqueNBTName()); // to let Quest know that we want EnchantedBucket
					stackTag.setString(NBT_FLUID_NAME, getFluidInBucket().getName());
					return stack;
				}

				public Fluid getFluidInBucket()
				{
					return fluid;
				}
				
				public ItemStack getBucket()
				{
					return getResult(getFluidInBucket());
				}
			});
			VanillaMagic.logger.log(Level.INFO, "Added Enchanted Bucket: " + fluid.getName());
		}
		VanillaMagic.logger.log(Level.INFO, "Registered Enchanted Buckets: " + VanillaMagicItems.INSTANCE.enchantedBuckets.size());
	}
}