package seia.vanillamagic.machine.farm.farmer;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.machine.farm.HarvestResult;
import seia.vanillamagic.machine.farm.IHarvestResult;
import seia.vanillamagic.machine.farm.TileFarm;

public class FarmerPickable extends FarmerCustomSeed
{
	public FarmerPickable(Block plantedBlock, int plantedBlockMeta, int grownBlockMeta, ItemStack seeds) 
	{
		super(plantedBlock, plantedBlockMeta, grownBlockMeta, seeds);
	}

	public FarmerPickable(Block plantedBlock, int grownBlockMeta, ItemStack seeds) 
	{
		super(plantedBlock, grownBlockMeta, seeds);
	}

	public FarmerPickable(Block plantedBlock, ItemStack seeds) 
	{
		super(plantedBlock, seeds);
	}
	  
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		if(!canHarvest(farm, pos, block, state)) 
		{
			return null;
		}        
		return new HarvestResult(new ArrayList<EntityItem>(), pos);
	}
}