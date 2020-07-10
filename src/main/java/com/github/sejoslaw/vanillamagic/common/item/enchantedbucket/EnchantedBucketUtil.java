package com.github.sejoslaw.vanillamagic.common.item.enchantedbucket;

import com.github.sejoslaw.vanillamagic.api.item.IEnchantedBucket;
import com.github.sejoslaw.vanillamagic.common.util.CauldronUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		return new FluidStack(fluid, 1000);
	}

	/**
	 * @return Returns the given ItemStack filled with given Fluid.
	 */
	public static ItemStack getResult(ItemStack stack, Fluid fluid) {
		IFluidHandlerItem fh = FluidUtil.getFluidHandler(stack).orElse(null);
		fh.fill(getFluidStack(fluid), IFluidHandler.FluidAction.EXECUTE);
		return fh.getContainer();
	}

	/**
	 * @return Returns the Bucket filled with given Fluid.
	 */
	public static ItemStack getResult(Fluid fluid) {
		return getResult(new ItemStack(Items.BUCKET), fluid);
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
			IFluidHandlerItem fh = FluidUtil.getFluidHandler(stackBucket).orElse(null);

			FluidStack fluidStack = fh.getFluidInTank(0);

			for (IEnchantedBucket eb : VMItems.ENCHANTED_BUCKETS) {
				Fluid fluidInBucket = eb.getFluidInBucket();
				if (fluidInBucket == null) {
					continue;
				}

				FluidStack fluidStackInBucket = getFluidStack(fluidInBucket);

				if (fluidStack.isFluidEqual(fluidStackInBucket)) {
					return eb;
				}
			}
		}
		return null;
	}

	public static void initialize() {
		Set<Map.Entry<ResourceLocation, Fluid>> registeredFluids = ForgeRegistries.FLUIDS.getEntries();

		for (Map.Entry<ResourceLocation, Fluid> entry : registeredFluids) {
			Fluid fluid = entry.getValue();
			VMItems.ENCHANTED_BUCKETS.add(() -> fluid);
			VMLogger.logInfo("Added Enchanted Bucket: " + fluid.getRegistryName());
		}

		VMLogger.logInfo("Registered Enchanted Buckets: " + VMItems.ENCHANTED_BUCKETS.size());
	}
}
