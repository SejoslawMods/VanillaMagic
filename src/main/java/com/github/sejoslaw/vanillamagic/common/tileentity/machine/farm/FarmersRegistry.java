package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarmer;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IHarvestResult;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class FarmersRegistry {
    private static final List<IFarmer> FARMERS = new ArrayList<>();
    private static final List<ItemStack> FLOWERS = new ArrayList<>();

    public static final FarmerPlantable DEFAULT_FARMER = new FarmerPlantable();

    private FarmersRegistry() {
    }

    static {
        List<ItemStack> saplings = filterBlocks("sapling");
        List<ItemStack> woods = new ArrayList<>();

        woods.addAll(filterBlocks("log"));
        woods.addAll(filterBlocks("wood"));

        FARMERS.add(new FarmerOreDictionaryTree(saplings, woods));

        FLOWERS.add(new ItemStack(Blocks.CHORUS_FLOWER));
        FLOWERS.add(new ItemStack(Blocks.POTTED_CORNFLOWER));

        FLOWERS.add(new ItemStack(Blocks.DANDELION));
        FLOWERS.add(new ItemStack(Blocks.POPPY));
        FLOWERS.add(new ItemStack(Blocks.BLUE_ORCHID));
        FLOWERS.add(new ItemStack(Blocks.ALLIUM));
        FLOWERS.add(new ItemStack(Blocks.AZURE_BLUET));
        FLOWERS.add(new ItemStack(Blocks.ORANGE_TULIP));
        FLOWERS.add(new ItemStack(Blocks.PINK_TULIP));
        FLOWERS.add(new ItemStack(Blocks.RED_TULIP));
        FLOWERS.add(new ItemStack(Blocks.WHITE_TULIP));
        FLOWERS.add(new ItemStack(Blocks.OXEYE_DAISY));
        FLOWERS.add(new ItemStack(Blocks.CORNFLOWER));
        FLOWERS.add(new ItemStack(Blocks.LILY_OF_THE_VALLEY));
        FLOWERS.add(new ItemStack(Blocks.WITHER_ROSE));
        FLOWERS.add(new ItemStack(Blocks.SUNFLOWER));
        FLOWERS.add(new ItemStack(Blocks.LILAC));
        FLOWERS.add(new ItemStack(Blocks.ROSE_BUSH));
        FLOWERS.add(new ItemStack(Blocks.PEONY));

        FARMERS.add(new FarmerFlowerPicker(FLOWERS));

        FARMERS.add(new FarmerStem(Blocks.SUGAR_CANE, new ItemStack(Items.SUGAR_CANE)));
        FARMERS.add(new FarmerStem(Blocks.CACTUS, new ItemStack(Blocks.CACTUS)));

        FARMERS.add(new FarmerTree(Blocks.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK));
        FARMERS.add(new FarmerTree(Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK));

        FARMERS.add(new FarmerMelon(Blocks.MELON_STEM, Blocks.MELON, new ItemStack(Items.MELON_SEEDS)));
        FARMERS.add(new FarmerMelon(Blocks.PUMPKIN_STEM, Blocks.PUMPKIN, new ItemStack(Items.PUMPKIN_SEEDS)));

        FARMERS.add(new FarmerNetherWart());
        FARMERS.add(new FarmerCocoa());

        FARMERS.add(DEFAULT_FARMER);
    }

    public static void addFarmer(IFarmer farmer) {
        FARMERS.add(farmer);
    }

    public static boolean canHarvest(TileFarm farm, BlockPos pos, Block block, BlockState state) {
        for (IFarmer farmer : FARMERS) {
            if (farmer.canHarvest(farm, pos, block, state)) {
                return true;
            }
        }

        return false;
    }

    public static IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, BlockState state) {
        for (IFarmer farmer : FARMERS) {
            if (farmer.canHarvest(farm, pos, block, state)) {
                return farmer.harvestBlock(farm, pos, block, state);
            }
        }

        return null;
    }

    public static boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, BlockState state) {
        for (IFarmer farmer : FARMERS) {
            if (farmer.prepareBlock(farm, pos, block, state)) {
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

    private static List<ItemStack> filterBlocks(String key) {
        return ForgeRegistries.BLOCKS.getEntries().stream().filter(entry -> entry.getKey().toString().contains(key)).map(entry -> new ItemStack(entry.getValue())).collect(Collectors.toList());
    }
}