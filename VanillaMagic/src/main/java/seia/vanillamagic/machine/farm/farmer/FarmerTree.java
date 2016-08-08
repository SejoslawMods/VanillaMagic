package seia.vanillamagic.machine.farm.farmer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import seia.vanillamagic.machine.farm.HarvestResult;
import seia.vanillamagic.machine.farm.IHarvestResult;
import seia.vanillamagic.machine.farm.TileFarm;
import seia.vanillamagic.machine.farm.TreeHarvestUtils;

public class FarmerTree implements IFarmer
{
	private static final HeightComparator comp = new HeightComparator();
	
	protected Block sapling;
	protected ItemStack saplingItem;
	protected Block[] woods;
	protected TreeHarvestUtils harvester = new TreeHarvestUtils();
	  
	private boolean ignoreMeta;

	public FarmerTree(Block sapling, Block... wood) 
	{
		this.sapling = sapling;
		if(sapling != null) 
		{
			saplingItem = new ItemStack(sapling);
		}
		woods = wood;
	}

	public FarmerTree(boolean ignoreMeta, Block sapling, Block... wood) 
	{
		this(sapling,wood);
		this.ignoreMeta = ignoreMeta;
	}

	public FarmerTree(ItemStack sapling, ItemStack wood) 
	{
		this(Block.getBlockFromItem(sapling.getItem()), Block.getBlockFromItem(wood.getItem()));
	}
	
	public boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		return isWood(block);
	}

	protected boolean isWood(Block block) 
	{
		for(Block wood : woods) 
		{
			if(block == wood) 
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean canPlant(ItemStack stack) 
	{
		return stack != null && stack.getItem() == saplingItem.getItem();
	}
	
	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		if(block == sapling) 
		{
			return true;
		}
		return plantFromInventory(farm, pos, block, state);
	}

	protected boolean plantFromInventory(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		World worldObj = farm.getWorld();
		if(canPlant(worldObj, pos)) 
		{
			ItemStack seed = farm.takeSeedFromInput();
			if(seed != null) 
			{
				return plant(farm, worldObj, pos, seed);
			}
		}
		return false;
	}

	protected boolean canPlant(World worldObj, BlockPos pos) 
	{
		BlockPos grnPos = pos.down();
		IBlockState bs = worldObj.getBlockState(grnPos);
		Block ground =bs.getBlock(); 
		IPlantable plantable = (IPlantable) sapling;
		if(sapling.canPlaceBlockAt(worldObj, pos) &&        
				ground.canSustainPlant(bs, worldObj, grnPos, EnumFacing.UP, plantable)) 
		{
			return true;
		}
		return false;
	}

	protected boolean plant(TileFarm farm, World worldObj, BlockPos pos, ItemStack seed) 
	{    
		worldObj.setBlockToAir(pos);
		if(canPlant(worldObj, pos)) 
		{            
			worldObj.setBlockState(pos, sapling.getStateFromMeta(seed.getItemDamage()), 1 | 2);
			return true;
		}
		return false;
	}
	
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		World worldObj = farm.getWorld();
		HarvestResult res = new HarvestResult();
		harvester.harvest(farm, this, pos, res);
		Collections.sort(res.harvestedBlocks, comp);
		List<BlockPos> actualHarvests = new ArrayList<BlockPos>();
		// avoid calling this in a loop
		boolean hasShears = (farm.getShearsFromInput() == null);
		int shearCount = 0;
		for (int i = 0; i < res.harvestedBlocks.size(); i++) 
		{
			BlockPos coord = res.harvestedBlocks.get(i);
			Block blk = worldObj.getBlockState(coord).getBlock();
			List<ItemStack> drops;
			boolean wasSheared = false;
			boolean wasAxed = false;
			boolean wasWood = isWood(blk);
			float chance = 1.0F;
			if (blk instanceof IShearable && hasShears && ((shearCount / res.harvestedBlocks.size()) < 100)) 
			{
				drops = ((IShearable) blk).onSheared(null, worldObj, coord, 0);
				wasSheared = true;
				shearCount += 100;
			} 
			else 
			{
				drops = blk.getDrops(worldObj, coord, worldObj.getBlockState(coord), 0);
				wasAxed = true;
			}
			
			if(drops != null) 
			{
				for (ItemStack drop : drops) 
				{
					res.drops.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop.copy()));
				}
			}
			farm.getWorld().setBlockToAir(coord);
			actualHarvests.add(coord);
		}
		res.harvestedBlocks.clear();
		res.harvestedBlocks.addAll(actualHarvests);
		//try replant    
		for(EntityItem drop : res.drops) 
		{
			if(canPlant(drop.getEntityItem()) && plant(farm, worldObj, pos, drop.getEntityItem())) 
			{     
				res.drops.remove(drop);
				break;
			}
		}    
		return res;
	}

	public boolean getIgnoreMeta()
	{
		return ignoreMeta;
	}

	private static class HeightComparator implements Comparator<BlockPos> 
	{
		public int compare(BlockPos o1, BlockPos o2) 
		{
			return compare(o2.getY(), o1.getY()); //reverse order
		}
			
		//same as 1.7 Integer.compare
		public static int compare(int x, int y) 
		{
			return (x < y) ? -1 : ((x == y) ? 0 : 1);
		}
	}
}