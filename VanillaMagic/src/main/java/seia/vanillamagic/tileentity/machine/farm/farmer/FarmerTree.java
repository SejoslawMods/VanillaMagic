package seia.vanillamagic.tileentity.machine.farm.farmer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;
import seia.vanillamagic.tileentity.machine.farm.HarvestResult;
import seia.vanillamagic.tileentity.machine.farm.IHarvestResult;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;
import seia.vanillamagic.tileentity.machine.farm.TreeHarvestUtils;
import seia.vanillamagic.util.ItemStackUtil;

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
		if (sapling != null) saplingItem = new ItemStack(sapling);
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
		for (Block wood : woods) 
			if (block == wood) 
				return true;
		return false;
	}
	
	public boolean canPlant(ItemStack stack) 
	{
		return !ItemStackUtil.isNullStack(stack) && stack.getItem() == saplingItem.getItem();
	}
	
	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		if (block == sapling) return true;
		return plantFromInventory(farm, pos, block, state);
	}

	protected boolean plantFromInventory(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		World worldObj = farm.getWorld();
		if (canPlant(worldObj, pos)) 
		{
			ItemStack seed = farm.takeSeedFromSupplies(saplingItem, pos, false);
			if (!ItemStackUtil.isNullStack(seed)) return plant(farm, worldObj, pos, seed);
		}
		return false;
	}

	protected boolean canPlant(World worldObj, BlockPos pos) 
	{
		BlockPos grnPos = pos.down();
		IBlockState bs = worldObj.getBlockState(grnPos);
		Block ground = bs.getBlock(); 
		IPlantable plantable = (IPlantable) sapling;
		if (sapling.canPlaceBlockAt(worldObj, pos) && ground.canSustainPlant(bs, worldObj, grnPos, EnumFacing.UP, plantable)) return true;
		return false;
	}

	protected boolean plant(TileFarm farm, World worldObj, BlockPos pos, ItemStack seed) 
	{    
		worldObj.setBlockToAir(pos);
		if (canPlant(worldObj, pos)) 
		{            
			worldObj.setBlockState(pos, sapling.getStateFromMeta(seed.getItemDamage()), 1 | 2);
			return true;
		}
		return false;
	}
	
	@Nullable
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		boolean hasAxe = farm.hasAxe();
		if (!hasAxe) return null;
		
		World worldObj = farm.getWorld();
		final int fortune = farm.getMaxLootingValue();
		HarvestResult res = new HarvestResult();
		harvester.harvest(farm, this, pos, res);
		Collections.sort(res.harvestedBlocks, comp);
		List<BlockPos> actualHarvests = new ArrayList<BlockPos>();
		// avoid calling this in a loop
		boolean hasShears = farm.hasShears();
		int noShearingPercentage = farm.isLowOnSaplings(pos);
		int shearCount = 0;
		for (int i = 0; i < res.harvestedBlocks.size() && hasAxe; ++i) 
		{
			BlockPos coord = res.harvestedBlocks.get(i);
			Block blk = farm.getBlock(coord);
			NonNullList<ItemStack> drops = NonNullList.create();
			boolean wasSheared = false;
			boolean wasAxed = false;
			boolean wasWood = isWood(blk);
			float chance = 1.0F;
			if (blk instanceof IShearable && hasShears && ((shearCount / res.harvestedBlocks.size() + noShearingPercentage) < 100)) 
			{
				drops = (NonNullList<ItemStack>) ((IShearable) blk).onSheared(null, worldObj, coord, 0);
				wasSheared = true;
				shearCount += 100;
			} 
			else 
			{
				blk.getDrops(drops, worldObj, coord, farm.getBlockState(coord), fortune);
				chance = ForgeEventFactory.fireBlockHarvesting(drops, worldObj, coord, farm.getBlockState(coord), fortune, chance, false, null/*fakePlayer*/);
				wasAxed = true;
			}
			
			if (drops != null) 
				for (ItemStack drop : drops) 
					if (worldObj.rand.nextFloat() <= chance) 
						res.drops.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop.copy()));
			
			if (wasAxed && !wasWood) wasAxed = true;
			
			if (wasAxed) 
			{
				farm.damageAxe(blk, new BlockPos(coord));
				hasAxe = farm.hasAxe();
			} 
			else if (wasSheared) 
			{
				farm.damageShears(blk, new BlockPos(coord));
				hasShears = farm.hasShears();
			}
			farm.getWorld().setBlockToAir(coord);
			actualHarvests.add(coord);
		}
		res.harvestedBlocks.clear();
		res.harvestedBlocks.addAll(actualHarvests);
		//try replant    
		for (EntityItem drop : res.drops) 
		{
			if (canPlant(drop.getItem()) && plant(farm, worldObj, pos, drop.getItem())) 
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