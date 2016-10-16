package seia.vanillamagic.tileentity.blockabsorber;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import seia.vanillamagic.api.tileentity.blockabsorber.IBlockAbsorber;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.tileentity.CustomTileEntity;

public class TileBlockAbsorber extends CustomTileEntity implements IBlockAbsorber
{
	public static final String REGISTRY_NAME = TileBlockAbsorber.class.getSimpleName();
	
	protected TileEntityHopper connectedHopper;
	
	/**
	 * On each update this {@link TileEntity} will check for block which is on this position and try to place it in bottom {@link IHopper}
	 * 
	 * @see seia.vanillamagic.tileentity.CustomTileEntity#update()
	 */
	public void update()
	{
		IBlockState thisState = worldObj.getBlockState(pos);
		if(Block.isEqualTo(thisState.getBlock(), Blocks.AIR)) // We don't want to put Air into Hopper
		{
			return;
		}
		// For IInventory
		TileEntity tileAtThisPos = worldObj.getTileEntity(pos);
		if(tileAtThisPos != null)
		{
			// If we have a Tile which is Inventory we want to absorb it's items first
			if(tileAtThisPos instanceof IInventory)
			{
				IInventory inv = (IInventory) tileAtThisPos;
				if(!InventoryHelper.isInventoryEmpty(inv, EnumFacing.DOWN)) // First call to absorb items from Inventory
				{
					for(int i = 0; i < inv.getSizeInventory(); i++)
					{
						ItemStack stackInSlot = inv.getStackInSlot(i);
						ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(connectedHopper, stackInSlot, getInputFacing());
						inv.setInventorySlotContents(i, leftItems);
					}
				}
				if(!InventoryHelper.isInventoryEmpty(inv, EnumFacing.DOWN)) // Second call to check if all items were absorbed after first call
				{
					return; // If it's not possible to absorb all, return
				}
			}
		}
		// If it's not an Inventory than it must be a Block
		// For normal Block
		ItemStack thisBlock = new ItemStack(thisState.getBlock());
		ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(connectedHopper, thisBlock, getInputFacing());
		if(leftItems == null)
		{
			worldObj.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}
	
	public EnumFacing getInputFacing()
	{
		return EnumFacing.UP;
	}

	public void setConnectedHopper(TileEntityHopper clickedHopper) 
	{
		this.connectedHopper = clickedHopper;
	}
	
	public TileEntityHopper getConnectedHopper()
	{
		return connectedHopper;
	}
}