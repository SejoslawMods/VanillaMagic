package seia.vanillamagic.tileentity.machine.quarry.upgrade;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.IInventoryWrapper;
import seia.vanillamagic.api.tileentity.machine.IQuarry;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgrade;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.util.BlockHelper;

public class QuarryUpgradeAutoInventoryOutputPlacer implements IQuarryUpgrade
{
	public String getUpgradeName() 
	{
		return "Auto Inventory Output Placer";
	}
	
	public Block getBlock() 
	{
		return Blocks.STAINED_HARDENED_CLAY;
	}
	
	public void modifyQuarry(IQuarry quarry)
	{
		IInventoryWrapper invOutputWrapper = quarry.getOutputInventory();
		IInventory invOutput = invOutputWrapper.getInventory();
		EnumFacing invFacing = quarry.getOutputFacing();
		if(!InventoryHelper.hasInventoryFreeSpace(invOutput, invFacing))
		{
			IInventoryWrapper invInputWrapper = quarry.getInputInventory();
			int slotWithInventory = InventoryHelper.containsAnotherInventoryBlock(invInputWrapper);
			if(slotWithInventory != -1)
			{
				World worldOutput = invOutputWrapper.getWorld();
				BlockPos nextPos = invOutputWrapper.getPos().offset(invFacing, 2);
				IInventory invInput = invInputWrapper.getInventory();
				ItemStack stackWithInventory = invInput.getStackInSlot(slotWithInventory);
				BlockHelper.placeBlockFromStack(worldOutput, nextPos, stackWithInventory);
				invInput.decrStackSize(slotWithInventory, 1);
				IBlockState placedBlockState = worldOutput.getBlockState(nextPos);
				Block placedBlock = placedBlockState.getBlock();
				if(placedBlock instanceof ITileEntityProvider) // this should always be true ???
				{
					TileEntity createdTileEntity = placedBlock.createTileEntity(worldOutput, placedBlockState);
					worldOutput.setTileEntity(nextPos, createdTileEntity);
					try 
					{
						invOutputWrapper.setNewInventory(worldOutput, nextPos);
					} 
					catch(NotInventoryException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}