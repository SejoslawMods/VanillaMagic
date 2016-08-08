package seia.vanillamagic.machine.farm.farmer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.machine.farm.IHarvestResult;
import seia.vanillamagic.machine.farm.TileFarm;

public class Farmers implements IFarmer
{
	public static final Farmers INSTANCE = new Farmers();
	
	public static final FarmerPlantable DEFAULT_FARMER = new FarmerPlantable();
	
	//====================================================================
	
	private List<IFarmer> farmers = new ArrayList<IFarmer>();
	
	private Farmers()
	{
		farmers.add(new FarmerStem(Blocks.REEDS, new ItemStack(Items.REEDS)));
		farmers.add(new FarmerStem(Blocks.CACTUS, new ItemStack(Blocks.CACTUS)));
		farmers.add(new FarmerTree(Blocks.SAPLING, Blocks.LOG));
		farmers.add(new FarmerTree(Blocks.SAPLING, Blocks.LOG2));
		farmers.add(new FarmerTree(true, Blocks.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK));
		farmers.add(new FarmerTree(true, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK));
		farmers.add(new FarmerMelon(Blocks.MELON_STEM, Blocks.MELON_BLOCK, new ItemStack(Items.MELON_SEEDS)));
		farmers.add(new FarmerMelon(Blocks.PUMPKIN_STEM, Blocks.PUMPKIN, new ItemStack(Items.PUMPKIN_SEEDS)));
		farmers.add(new FarmerNetherWart());
		farmers.add(new FarmerCocoa());
		farmers.add(DEFAULT_FARMER);
	}
	
	public void addFarmer(IFarmer farmer)
	{
		farmers.add(farmer);
	}
	
	public boolean canHarvest(TileFarm farm,  BlockPos pos, Block block, IBlockState meta) 
	{
		for(IFarmer farmer : farmers) 
		{
			if(farmer.canHarvest(farm, pos, block, meta)) 
			{
				return true;
			}
		}
		return false;
	}

	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) 
	{
		for(IFarmer farmer : farmers) 
		{
			if(farmer.canHarvest(farm, pos, block, meta)) 
			{
				return farmer.harvestBlock(farm, pos, block, meta);
			}
		}
		return null;
	}

	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) 
	{
		for(IFarmer farmer : farmers) 
		{
			if(farmer.prepareBlock(farm, pos, block, meta)) 
			{
				return true;
			}
		}
		return false;
	}

	public boolean canPlant(ItemStack stack) 
	{
		for(IFarmer farmer : farmers) 
		{
			if(farmer.canPlant(stack)) 
			{
				return true;
			}
		}
		return false;
	}
}