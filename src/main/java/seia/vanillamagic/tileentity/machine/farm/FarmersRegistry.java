package seia.vanillamagic.tileentity.machine.farm;

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
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerCocoa;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerFlowerPicker;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerMelon;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerNetherWart;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerOreDictionaryTree;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerPlantable;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerStem;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerTree;
import seia.vanillamagic.tileentity.machine.farm.farmer.IFarmer;

public class FarmersRegistry
{
	private static final List<IFarmer> _FARMERS;
	private static final List<ItemStack> _SAPLINGS = OreDictionary.getOres("treeSapling");
	private static final List<ItemStack> _WOODS = OreDictionary.getOres("logWood");
	private static final List<ItemStack> _FLOWERS = new ArrayList<ItemStack>();
	
	public static final FarmerPlantable DEFAULT_FARMER = new FarmerPlantable();
	
	private FarmersRegistry()
	{
	}
	
	static
	{
		_FLOWERS.add(new ItemStack(Blocks.YELLOW_FLOWER));
		_FLOWERS.add(new ItemStack(Blocks.RED_FLOWER));
		
		_FARMERS = new ArrayList<IFarmer>();
		_FARMERS.add(new FarmerFlowerPicker(_FLOWERS));
		_FARMERS.add(new FarmerStem(Blocks.REEDS, new ItemStack(Items.REEDS)));
	    _FARMERS.add(new FarmerStem(Blocks.CACTUS, new ItemStack(Blocks.CACTUS)));
	    _FARMERS.add(new FarmerOreDictionaryTree(_SAPLINGS, _WOODS));
	    _FARMERS.add(new FarmerTree(true, Blocks.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK));
	    _FARMERS.add(new FarmerTree(true, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK));
	    //special case of plantables to get spacing correct
	    _FARMERS.add(new FarmerMelon(Blocks.MELON_STEM, Blocks.MELON_BLOCK, new ItemStack(Items.MELON_SEEDS)));
	    _FARMERS.add(new FarmerMelon(Blocks.PUMPKIN_STEM, Blocks.PUMPKIN, new ItemStack(Items.PUMPKIN_SEEDS)));
	    //'BlockNetherWart' is not an IGrowable
	    _FARMERS.add(new FarmerNetherWart());
	    //Cocoa is odd
	    _FARMERS.add(new FarmerCocoa());
	    //Handles all 'Vanilla' style crops
	    _FARMERS.add(DEFAULT_FARMER);
	}
	
	public static void addFarmer(IFarmer farmer)
	{
		_FARMERS.add(farmer);
	}
	
	public static boolean canHarvest(TileFarm farm,  BlockPos pos, Block block, IBlockState meta) 
	{
		for (IFarmer farmer : _FARMERS) 
			if (farmer.canHarvest(farm, pos, block, meta)) 
				return true;
		return false;
	}

	@Nullable
	public static IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) 
	{
		for (IFarmer farmer : _FARMERS) 
			if (farmer.canHarvest(farm, pos, block, meta))
				return farmer.harvestBlock(farm, pos, block, meta);
		return null;
	}

	public static boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) 
	{
		for (IFarmer farmer : _FARMERS) 
			if (farmer.prepareBlock(farm, pos, block, meta))
				return true;
		return false;
	}

	public static boolean canPlant(ItemStack stack) 
	{
		for (IFarmer farmer : _FARMERS) 
			if (farmer.canPlant(stack)) 
				return true;
		return false;
	}
}