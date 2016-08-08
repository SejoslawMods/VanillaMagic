package seia.vanillamagic.machine.farm.farmer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import seia.vanillamagic.machine.farm.HarvestResult;
import seia.vanillamagic.machine.farm.IHarvestResult;
import seia.vanillamagic.machine.farm.TileFarm;

public class FarmerPlantable implements IFarmer
{
	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state)
	{
		if(block == null) 
		{
			return false;
		}
		ItemStack seedStack = null;
		IInventory input = farm.getInputInventory();
		for(int i = 0; i < input.getSizeInventory(); i++)
		{
			seedStack = input.getStackInSlot(i);
			if(seedStack.getItem() instanceof IPlantable)
			{
				IPlantable plantable = (IPlantable) seedStack.getItem();
				EnumPlantType type = plantable.getPlantType(farm.getWorld(), pos);
				if(type == null) 
				{
					return false;
				}
				BlockPos groundPos = farm.getWorkingPos().offset(EnumFacing.DOWN);
				Block ground = farm.getWorld().getBlockState(groundPos).getBlock();
				if(type == EnumPlantType.Nether) 
				{
					if(ground != Blocks.SOUL_SAND) 
					{
						return false;
					}
					return plantFromInventory(farm, pos, plantable, seedStack, input, i);
				}
				if(type == EnumPlantType.Crop) 
				{
					farm.tillBlock(pos);        
					return plantFromInventory(farm, pos, plantable, seedStack, input, i);
				}
				if(type == EnumPlantType.Water) 
				{
					return plantFromInventory(farm, pos, plantable, seedStack, input, i);
				}
			}
		}
		return false;
	}
	
	public boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		if(block instanceof IGrowable && 
				!(block instanceof BlockStem)) 
		{
			return !((IGrowable) block).canGrow(farm.getWorld(), pos, state, true);
		}
		return false;
	}
	
	public boolean canPlant(ItemStack stack) 
	{
		if(stack == null) 
		{
			return false;
		}
		return stack.getItem() instanceof IPlantable;
	}
	
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		if(!canHarvest(farm, pos, block, state)) 
		{
			return null;
		}
		World worldObj = farm.getWorld();
		List<EntityItem> result = new ArrayList<EntityItem>();
		ItemStack removedPlantable = null;
		List<ItemStack> drops = block.getDrops(worldObj, pos, state, 0);
		worldObj.setBlockToAir(pos);
		boolean removed = false;
		if(drops != null) 
		{
			for(ItemStack stack : drops) 
			{
				if(stack != null && stack.stackSize > 0) 
				{
					if(!removed && isPlantableForBlock(stack, block)) 
					{
						removed = true;
						removedPlantable = stack.copy();
						removedPlantable.stackSize = 1;
						stack.stackSize--;
						if(stack.stackSize > 0) 
						{
							result.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
						}
					} 
					else 
					{
						result.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
					}
				}
			}
		}
		
		if(removed) 
		{
			if(!plant(farm, worldObj, pos, (IPlantable) removedPlantable.getItem(), removedPlantable, null, -1)) 
			{
				result.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, removedPlantable.copy()));
				worldObj.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
			}
		} 
		else 
		{
			worldObj.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
		}
		return new HarvestResult(result, pos);
	}
	
	public boolean canPlant(World worldObj, BlockPos pos, IPlantable plantable, ItemStack seedStack)
	{
		IBlockState target = plantable.getPlant(null, new BlockPos(0, 0, 0));
		BlockPos groundPos = pos.down();
		IBlockState groundBS = worldObj.getBlockState(groundPos);
		Block ground = groundBS.getBlock();
		if(target != null && target.getBlock().canPlaceBlockAt(worldObj, pos) &&        
				ground.canSustainPlant(groundBS, worldObj, groundPos, EnumFacing.UP, plantable)) 
		{
			return true;
		}
		return false;
	}
	
	public boolean plant(TileFarm farm, World worldObj, BlockPos pos, IPlantable plantable, ItemStack seedStack, IInventory inv, int index) 
	{
		worldObj.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
		IBlockState target = plantable.getPlant(null, new BlockPos(0, 0, 0));    
		worldObj.setBlockState(pos, target, 1 | 2);
		farm.getInputInventory().decrStackSize(index, 1);
		return true;
	}
	
	public boolean plantFromInventory(TileFarm farm, BlockPos pos, IPlantable plantable, ItemStack seedStack, IInventory inv, int index) 
	{
	    World worldObj = farm.getWorld();
	    if(canPlant(worldObj, pos, plantable, seedStack) && seedStack != null) 
	    {
	    	return plant(farm, worldObj, pos, plantable, seedStack, inv, index);
	    }
	    return false;
	}
	
	public boolean isPlantableForBlock(ItemStack stack, Block block) 
	{
		if(!(stack.getItem() instanceof IPlantable)) 
		{
			return false;
		}
		IPlantable plantable = (IPlantable) stack.getItem();
		IBlockState b = plantable.getPlant(null, new BlockPos(0, 0, 0));
		return b != null && b.getBlock() == block;
	}
}