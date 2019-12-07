package com.github.sejoslaw.vanillamagic.item.enchantedbucket;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.entity.item.ItemEntity;
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
import com.github.sejoslaw.vanillamagic.api.item.IEnchantedBucket;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.github.sejoslaw.vanillamagic.item.VanillaMagicItems;
import com.github.sejoslaw.vanillamagic.util.CauldronUtil;

/**
 * Class which contains methods connected with Enchanted Bucket.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EnchantedBucketUtil {
	private EnchantedBucketUtil() {
	}

	/**
	 * @return Returns the FluidStack from given Fluid.
	 */
	public static FluidStack getFluidStack(Fluid fluid) {
		return new FluidStack(fluid, Fluid.BUCKET_VOLUME);
	}

	/**
	 * @return Returns the given ItemStack filled with given Fluid.
	 */
	public static ItemStack getResult(ItemStack stack, Fluid fluid) {
		IFluidHandler fh = FluidUtil.getFluidHandler(stack);
		fh.fill(getFluidStack(fluid), true);
		return stack;
	}

	/**
	 * @return Returns the Bucket filled with given Fluid.
	 */
	public static ItemStack getResult(Fluid fluid) {
		return getResult(new ItemStack(Items.BUCKET), fluid);
	}

	/**
	 * @return Returns the EnchantedBucket from given ItemStack.
	 */
	@Nullable
	public static IEnchantedBucket getEnchantedBucket(ItemStack stack) {
		for (IEnchantedBucket enchantedBucket : VanillaMagicItems.ENCHANTED_BUCKETS) {
			if (VanillaMagicItems.isCustomBucket(stack, enchantedBucket)) {
				return enchantedBucket;
			}
		}
		return null;
	}

	/**
	 * @return Returns the first IEnchantedBucket from Cauldron at the given
	 *         position.
	 */
	@Nullable
	public static IEnchantedBucket getEnchantedBucketFromCauldron(World world, BlockPos cauldronPos) {
		List<ItemEntity> itemsInCauldron = CauldronUtil.getItemsInCauldron(world, cauldronPos);

		for (ItemEntity ei : itemsInCauldron) {
			ItemStack stackBucket = ei.getItem();
			IFluidHandler fh = FluidUtil.getFluidHandler(stackBucket);

			if ((fh == null) || (fh.getTankProperties() == null)) {
				continue;
			}

			IFluidTankProperties prop0 = fh.getTankProperties()[0];
			if (prop0 == null) {
				continue;
			}

			FluidStack prop0Stack = prop0.getContents();
			if (prop0Stack == null) {
				continue;
			}

			for (IEnchantedBucket eb : VanillaMagicItems.ENCHANTED_BUCKETS) {
				Fluid fluidInBucket = eb.getFluidInBucket();
				if (fluidInBucket == null) {
					continue;
				}

				FluidStack fluidStackInBucket = getFluidStack(fluidInBucket);
				if (fluidStackInBucket == null) {
					continue;
				}

				if (prop0Stack.isFluidEqual(fluidStackInBucket)) {
					return eb;
				}
			}
		}
		return null;
	}

	/**
	 * Register all EnchantedBuckets.
	 */
	public static void registerFluids() {
		Map<String, Fluid> registeredFluids = FluidRegistry.getRegisteredFluids();
		Collection<Fluid> fluids = registeredFluids.values();

		for (Fluid fluid : fluids) {
			VanillaMagicItems.ENCHANTED_BUCKETS.add(new IEnchantedBucket() {
				public Fluid getFluidInBucket() {
					return fluid;
				}
			});
			VanillaMagic.logInfo("Added Enchanted Bucket: " + fluid.getName());
		}

		VanillaMagic.logInfo("Registered Enchanted Buckets: " + VanillaMagicItems.ENCHANTED_BUCKETS.size());
	}
}