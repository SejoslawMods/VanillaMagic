package seia.vanillamagic.machine.farm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import seia.vanillamagic.machine.TileMachine;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.InventoryHelper;

public class TileFarm extends TileMachine
{
	public static final String REGISTRY_NAME = "TileFarm";
	
	public int farmSize;
	public BlockPos chestPos;
	
	public TileFarm(EntityPlayer player, BlockPos machinePos, int radius)
	{
		this.player = player;
		this.worldObj = player.worldObj;
		this.machinePos = machinePos;
		this.radius = radius;
		this.startPos = new BlockPos(machinePos.getX() + radius, machinePos.getY(), machinePos.getZ() + radius);
		this.workingPos = BlockPosHelper.copyPos(startPos);
		this.farmSize = (2 * radius) + 1;
		this.chestPos = this.machinePos.offset(EnumFacing.UP);
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
		return this.worldObj.getTileEntity(chestPos) instanceof IInventory;
	}
	
	public void doWork() 
	{
		for(int x = 0; x < farmSize; x++)
		{
			for(int z = 0; z < farmSize; z++)
			{
				try
				{
					workingPos = new BlockPos(workingPos.getX() - x, workingPos.getY(), workingPos.getZ() - z);
					IPlantable plantable = (IPlantable) worldObj.getBlockState(workingPos).getBlock();
					// Collect
					// Plant
				}
				catch(Exception e)
				{
				}
			}
		}
		workingPos = BlockPosHelper.copyPos(startPos);
	}
	
	public boolean inventoryOutputHasSpace() 
	{
		return !InventoryHelper.isInventoryFull(getOutputInventory(), EnumFacing.UP);
	}
}