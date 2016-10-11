package seia.vanillamagic.api.inventory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.exception.NotInventoryException;

/**
 * Basic implementation of the {@link IInventoryWrapper}
 */
public class InventoryWrapper implements IInventoryWrapper
{
	private IInventory inventory;
	private World world;
	private BlockPos position;
	private IBlockState state;
	
	public InventoryWrapper(World world, BlockPos position) throws NotInventoryException
	{
		this.setNewInventory(world, position);
	}
	
	public void setNewInventory(World world, BlockPos position) throws NotInventoryException
	{
		this.world = world;
		this.position = position;
		this.state = world.getBlockState(position);
		
		TileEntity tile = world.getTileEntity(position);
		if(tile instanceof IInventory)
		{
			this.inventory = (IInventory) tile;
		}
		else
		{
			throw new NotInventoryException(world, position);
		}
	}
	
	public IInventory getInventory() 
	{
		return inventory;
	}
	
	public World getWorld() 
	{
		return world;
	}
	
	public BlockPos getPos() 
	{
		return position;
	}
	
	public IBlockState getBlockState() 
	{
		return state;
	}
}