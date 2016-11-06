package seia.vanillamagic.tileentity.machine.farm.farmer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;

public class FarmerMelon extends FarmerCustomSeed
{
	private Block grownBlock;

	public FarmerMelon(Block plantedBlock, Block grownBlock, ItemStack seeds)
	{
		super(plantedBlock, seeds);
		this.grownBlock = grownBlock;
	}
	
	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		int xVal = farm.getMachinePos().getX() & 1;
		int zVal = farm.getMachinePos().getZ() & 1;
		if((pos.getX() & 1) != xVal || (pos.getZ() & 1) != zVal) 
		{
			// if we have melon seeds, we still want to return true here so they are not planted by the default plantable handlers
			// return canPlant(farm.getSeedTypeInSuppliesFor(pos)); // TODO:
			
			// check for melon seeds
			IInventory inv = farm.getInputInventory().getInventory();
			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack invSeeds = inv.getStackInSlot(i);
				if((invSeeds != null) && canPlant(invSeeds))
				{
					return true;
				}
			}
		}
		return super.prepareBlock(farm, pos, block, state);
	}
	  
	public boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		return block == grownBlock;
	}
}