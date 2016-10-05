package seia.vanillamagic.item.enchantedbucket;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.api.item.IEnchantedBucket;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.util.CauldronHelper;

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
	
	@Nullable
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
	@Nullable
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
						Fluid fluidInBucket = eb.getFluidInBucket();
						if(fluidInBucket != null)
						{
							FluidStack fluidStackInBucket = getFluidStack(fluidInBucket);
							if(fluidStackInBucket != null)
							{
								IFluidTankProperties prop0 = fh.getTankProperties()[0];
								if(prop0 != null)
								{
									FluidStack prop0Stack = prop0.getContents();
									if(prop0Stack != null)
									{
										if(prop0Stack.isFluidEqual(fluidStackInBucket))
										{
											return eb;
										}
									}
								}
							}
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
				public Fluid getFluidInBucket()
				{
					return fluid;
				}
			});
			VanillaMagic.LOGGER.log(Level.INFO, "Added Enchanted Bucket: " + fluid.getName());
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Registered Enchanted Buckets: " + VanillaMagicItems.INSTANCE.enchantedBuckets.size());
	}
}