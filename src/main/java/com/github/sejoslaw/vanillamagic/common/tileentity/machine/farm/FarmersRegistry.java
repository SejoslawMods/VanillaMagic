package com.github.sejoslaw.vanillamagic.tileentity.machine.farm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.FarmerCocoa;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.FarmerFlowerPicker;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.FarmerMelon;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.FarmerNetherWart;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.FarmerOreDictionaryTree;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.FarmerPlantable;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.FarmerStem;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.FarmerTree;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer.IFarmer;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class FarmersRegistry {
	private static final List<IFarmer> FARMERS;
	private static final List<ItemStack> SAPLINGS = OreDictionary.getOres("treeSapling");
	private static final List<ItemStack> WOODS = OreDictionary.getOres("logWood");
	private static final List<ItemStack> FLOWERS = new ArrayList<ItemStack>();

	public static final FarmerPlantable DEFAULT_FARMER = new FarmerPlantable();

	private FarmersRegistry() {
	}

	static {
		FLOWERS.add(new ItemStack(Blocks.YELLOW_FLOWER));
		FLOWERS.add(new ItemStack(Blocks.RED_FLOWER));

		FARMERS = new ArrayList<IFarmer>();
		FARMERS.add(new FarmerFlowerPicker(FLOWERS));
		FARMERS.add(new FarmerStem(Blocks.REEDS, new ItemStack(Items.REEDS)));
		FARMERS.add(new FarmerStem(Blocks.CACTUS, new ItemStack(Blocks.CACTUS)));
		FARMERS.add(new FarmerOreDictionaryTree(SAPLINGS, WOODS));
		FARMERS.add(new FarmerTree(true, Blocks.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK));
		FARMERS.add(new FarmerTree(true, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK));

		// special case of plantables to get spacing correct
		FARMERS.add(new FarmerMelon(Blocks.MELON_STEM, Blocks.MELON_BLOCK, new ItemStack(Items.MELON_SEEDS)));
		FARMERS.add(new FarmerMelon(Blocks.PUMPKIN_STEM, Blocks.PUMPKIN, new ItemStack(Items.PUMPKIN_SEEDS)));

		// 'BlockNetherWart' is not an IGrowable
		FARMERS.add(new FarmerNetherWart());

		// Cocoa is odd
		FARMERS.add(new FarmerCocoa());

		// Handles all 'Vanilla' style crops
		FARMERS.add(DEFAULT_FARMER);
	}

	public static void addFarmer(IFarmer farmer) {
		FARMERS.add(farmer);
	}

	public static boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState meta) {
		for (IFarmer farmer : FARMERS) {
			if (farmer.canHarvest(farm, pos, block, meta)) {
				return true;
			}
		}

		return false;
	}

	@Nullable
	public static IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) {
		for (IFarmer farmer : FARMERS) {
			if (farmer.canHarvest(farm, pos, block, meta)) {
				return farmer.harvestBlock(farm, pos, block, meta);
			}
		}

		return null;
	}

	public static boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) {
		for (IFarmer farmer : FARMERS) {
			if (farmer.prepareBlock(farm, pos, block, meta)) {
				return true;
			}
		}

		return false;
	}

	public static boolean canPlant(ItemStack stack) {
		for (IFarmer farmer : FARMERS) {
			if (farmer.canPlant(stack)) {
				return true;
			}
		}

		return false;
	}
}