package seia.vanillamagic.tileentity.machine.farm.farmer;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;

public class FarmerOreDictionaryTree extends FarmerTree
{
	protected List<ItemStack> saplings;
	protected List<ItemStack> woodBlocks;
	
	public FarmerOreDictionaryTree(List<ItemStack> saplings, List<ItemStack> woods) 
	{
	    super(null);
	    this.saplings = saplings;
	    this.woodBlocks = woods;
	}
	
	protected boolean isWood(Block block) 
	{
		return woodBlocks.contains(block);
	}
	
	public boolean canPlant(ItemStack stack) 
	{
		return stack != null && saplings.contains(stack) && Block.getBlockFromItem(stack.getItem()) != null;
	}
	
	public boolean prepareBlock(TileFarm farm, BlockPos bc, Block block, IBlockState meta) 
	{
		if(saplings.contains(block)) 
		{
			return true;
		}
		return plantFromInventory(farm, bc, block, meta);
	}
	
	protected boolean plantFromInventory(TileFarm farm, BlockPos bc, Block block, IBlockState meta) 
	{
		World worldObj = farm.getWorld();
		ItemStack sapling = null; //farm.getSeedTypeInSuppliesFor(bc); // TODO:
		
		IInventory inv = farm.getInputInventory().getInventory();
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack invStack = inv.getStackInSlot(i);
			if((invStack != null) && saplings.contains(invStack))
			{
				sapling = invStack.copy();
			}
		}
		
		if(canPlant(worldObj, bc, sapling)) 
		{
			ItemStack seed = farm.takeSeedFromSupplies(sapling, bc, false);
			if(seed != null) 
			{
				return plant(farm, worldObj, bc, seed);
			}
		}
		return false;
	}

	protected boolean canPlant(World worldObj, BlockPos bc, ItemStack sapling) 
	{
		if(!saplings.contains(sapling)) 
		{
			return false;
		}
		BlockPos grnPos = bc.down();
		IBlockState bs = worldObj.getBlockState(grnPos);
		Block ground = bs.getBlock();
		Block saplingBlock = Block.getBlockFromItem(sapling.getItem());
		if(saplingBlock == null) 
		{
			return false;
		}
		if(saplingBlock.canPlaceBlockAt(worldObj, bc)) 
		{
			if(saplingBlock instanceof IPlantable) 
			{
				return ground.canSustainPlant(bs, worldObj, grnPos, EnumFacing.UP, (IPlantable) saplingBlock);
			}
			return true;
		}
		return false;
	}
	
	protected boolean plant(TileFarm farm, World worldObj, BlockPos bc, ItemStack seed) 
	{    
		worldObj.setBlockToAir(bc);
		Item item = seed.getItem();
		worldObj.setBlockState(bc, Block.getBlockFromItem(item).getStateFromMeta(item.getMetadata(seed.getMetadata())), 1 | 2);
		if(seed != null)
		{
			seed.stackSize--;
		}
		return true;
	}
}