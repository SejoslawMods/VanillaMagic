package seia.vanillamagic.machine.farm;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import seia.vanillamagic.machine.TileMachine;
import seia.vanillamagic.machine.farm.farmer.Farmers;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.InventoryHelper;
import seia.vanillamagic.utils.ItemStackHelper;

public class TileFarm extends TileMachine
{
	public static final String REGISTRY_NAME = "TileFarm";
	
	public int farmSize;
	public BlockPos chestPosInput;
	public BlockPos chestPosOutput;
	
	public TileFarm(EntityPlayer player, BlockPos machinePos, int radius)
	{
		this.player = player;
		this.worldObj = player.worldObj;
		this.machinePos = machinePos;
		this.radius = radius;
		this.startPos = new BlockPos(machinePos.getX() + radius, machinePos.getY(), machinePos.getZ() + radius);
		this.workingPos = BlockPosHelper.copyPos(startPos);
		this.farmSize = radius; //this.farmSize = (2 * radius) + 1;
		this.chestPosInput = this.machinePos.offset(EnumFacing.UP);
		this.chestPosOutput = this.machinePos.offset(EnumFacing.DOWN);
	}
	
	public IInventory getInputInventory() 
	{
		return ((IInventory) this.worldObj.getTileEntity(machinePos.offset(EnumFacing.UP)));
	}
	
	public IInventory getOutputInventory() 
	{
		return ((IInventory) this.worldObj.getTileEntity(machinePos.offset(EnumFacing.DOWN)));
	}
	
	public boolean checkSurroundings() 
	{
		return (this.worldObj.getTileEntity(chestPosInput) instanceof IInventory) &&
				(this.worldObj.getTileEntity(chestPosOutput) instanceof IInventory);
	}
	
	public void doWork() 
	{
		try
		{
			workingPos = getNextPos();
			IBlockState state = this.worldObj.getBlockState(workingPos);
			Block block = state.getBlock();
			Farmers.INSTANCE.prepareBlock(this, workingPos, block, state);
			state = this.worldObj.getBlockState(workingPos);
			block = state.getBlock();
			IHarvestResult harvest = Farmers.INSTANCE.harvestBlock(this, workingPos, block, state);
			if(harvest != null && harvest.getDrops() != null) 
			{
				for (EntityItem ei : harvest.getDrops()) 
				{
					if(ei != null) 
					{
						insertHarvestDrop(ei);
						if(!ei.isDead) 
						{
							worldObj.spawnEntityInWorld(ei);
						}
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	public boolean inventoryOutputHasSpace() 
	{
		return !InventoryHelper.isInventoryFull(getOutputInventory(), EnumFacing.UP);
	}
	
	public boolean tillBlock(BlockPos plantingLocation) 
	{
		BlockPos dirtLoc = plantingLocation.offset(EnumFacing.DOWN);
		Block dirtBlock = this.worldObj.getBlockState(dirtLoc).getBlock();
		if((dirtBlock == Blocks.DIRT || dirtBlock == Blocks.GRASS)) 
		{
			worldObj.setBlockState(dirtLoc, Blocks.FARMLAND.getDefaultState());
			worldObj.playSound(dirtLoc.getX() + 0.5F, dirtLoc.getY() + 0.5F, dirtLoc.getZ() + 0.5F, SoundEvents.BLOCK_GRASS_STEP, SoundCategory.BLOCKS,
					(Blocks.FARMLAND.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.FARMLAND.getSoundType().getPitch() * 0.8F, false);
			return true;
		} 
		else if(dirtBlock instanceof BlockFarmland)// == Blocks.FARMLAND) 
		{
			return true;
		}
		return false;
	}
	
	public ItemStack hasBonemeal() 
	{
		IInventory input = this.getInputInventory();
		for(int i = 0; i < input.getSizeInventory(); i++)
		{
			ItemStack currentStack = input.getStackInSlot(i);
			if(currentStack != null)
			{
				if(ItemStack.areItemsEqual(currentStack, ItemStackHelper.getBonemeal(1)))
				{
					return currentStack;
				}
			}
		}
		return null;
	}
	
	public boolean isOutputFull()
	{
		return InventoryHelper.isInventoryFull(getOutputInventory(), EnumFacing.DOWN);
	}
	
	public boolean hasSeed(ItemStack seeds)
	{
		IInventory input = getInputInventory();
		if(input != null)
		{
			for(int i = 0; i < input.getSizeInventory(); i++)
			{
				ItemStack currentStack = input.getStackInSlot(i);
				if(ItemStack.areItemsEqual(currentStack, seeds))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public void insertHarvestDrop(EntityItem drop)
	{
		if(!drop.isDead)
		{
			InventoryHelper.putDropInInventoryAllSlots(getOutputInventory(), drop);
		}
	}
	
	@Nonnull
	public BlockPos getNextPos()
	{
		int nextX = workingPos.getX() + 1;
		int nextZ = workingPos.getZ();
		if (nextX > machinePos.getX() + farmSize) 
		{
			nextX = machinePos.getX() - farmSize;
			nextZ += 1;
			if (nextZ > machinePos.getZ() + farmSize) 
			{
				nextX = machinePos.getX() - farmSize;
				nextZ = machinePos.getZ() - farmSize;
				workingPos = BlockPosHelper.copyPos(startPos);
			}
		}
		return workingPos = new BlockPos(nextX, machinePos.getY(), nextZ);
	}
	
	public ItemStack takeSeedFromInput()
	{
		IInventory input = getInputInventory();
		if(input == null)
		{
			return null;
		}
		for(int i = 0; i < input.getSizeInventory(); i++)
		{
			ItemStack stack = input.getStackInSlot(i);
			if(stack != null)
			{
				if(stack.getItem() instanceof IPlantable);
				{
					return stack;
				}
			}
		}
		return null;
	}

	public ItemStack getShearsFromInput()
	{
		IInventory input = getInputInventory();
		if(input == null)
		{
			return null;
		}
		for(int i = 0; i < input.getSizeInventory(); i++)
		{
			ItemStack stack = input.getStackInSlot(i);
			if(stack != null)
			{
				if(stack.getItem() instanceof ItemShears);
				{
					return stack;
				}
			}
		}
		return null;
	}
}