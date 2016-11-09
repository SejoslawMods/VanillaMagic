package seia.vanillamagic.tileentity.machine.farm.farmer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import seia.vanillamagic.tileentity.machine.farm.HarvestResult;
import seia.vanillamagic.tileentity.machine.farm.IHarvestResult;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;

public class FarmerStem extends FarmerCustomSeed
{
	private static final HeightCompatator COMP = new HeightCompatator();

	public FarmerStem(Block plantedBlock, ItemStack seeds) 
	{
		super(plantedBlock, seeds);
	}
	  
	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) 
	{
		if(plantedBlock == block) 
		{
			return true;
		}
		return plantFromInventory(farm, pos);
	}
	  
	public boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		BlockPos up = pos.offset(EnumFacing.UP);
		Block upBlock = farm.getWorld().getBlockState(up).getBlock();
		return upBlock == plantedBlock;
	}
	  
	public boolean canPlant(ItemStack stack) 
	{
		return seeds.isItemEqual(stack);
	}
	
	@Nullable
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		World worldObj = farm.getWorld();
//		final EntityPlayerMP fakePlayer = farm.getFarmer();
		final int fortune = farm.getMaxLootingValue();
		HarvestResult result = new HarvestResult();
		BlockPos harvestCoord = pos;
		boolean done = false;
		do
		{
			harvestCoord = harvestCoord.offset(EnumFacing.UP);
			boolean hasHoe = farm.hasHoe();
			if(plantedBlock == farm.getBlock(harvestCoord) && hasHoe) 
			{
				result.harvestedBlocks.add(harvestCoord);
				List<ItemStack> drops = plantedBlock.getDrops(worldObj, harvestCoord, state, fortune);
				float chance = ForgeEventFactory.fireBlockHarvesting(drops, worldObj, harvestCoord, state, fortune, 1.0F, false, null/*fakePlayer*/);
				if(drops != null) 
				{
					for(ItemStack drop : drops) 
					{
						if(worldObj.rand.nextFloat() <= chance) 
						{
							result.drops.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop.copy()));
						}
					}
				}
				farm.damageHoe(1, new BlockPos(harvestCoord));
//				ItemStack[] inv = fakePlayer.inventory.mainInventory;
//				for(int slot = 0; slot < inv.length; slot++) 
//				{
//					ItemStack stack = inv[slot];
//					if(stack != null) 
//					{
//						inv[slot] = null;
//						EntityItem entityitem = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
//						result.drops.add(entityitem);
//					}
//				}
			} 
			else 
			{
				done = true;
			}
		} 
		while(!done);
		List<BlockPos> toClear = new ArrayList<BlockPos>(result.getHarvestedBlocks());
		Collections.sort(toClear, COMP);
		for(BlockPos coord : toClear) 
		{
			farm.getWorld().setBlockToAir(coord);
		}
		return result;
	}
	  
	protected boolean plantFromInventory(TileFarm farm, BlockPos pos) 
	{
		World worldObj = farm.getWorld();
		ItemStack seed = farm.takeSeedFromSupplies(seeds, pos);
		if(canPlant(farm, worldObj, pos) && seed != null)
		{
			return plant(farm, worldObj, pos, seed);
		}
		return false;
	}

	private static class HeightCompatator implements Comparator<BlockPos> 
	{
		public int compare(BlockPos pos1, BlockPos pos2) 
		{
			return -compare(pos1.getY(), pos2.getY());
		}
		
		public static int compare(int x, int y) 
		{
			return (x < y) ? -1 : ((x == y) ? 0 : 1);
		}
	}
}