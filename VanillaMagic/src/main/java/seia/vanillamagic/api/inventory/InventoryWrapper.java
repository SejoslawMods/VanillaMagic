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
	private IInventory _inventory;
	private World _world;
	private BlockPos _position;
	private IBlockState _state;
	
	public InventoryWrapper(World world, BlockPos position) 
			throws NotInventoryException
	{
		this.setNewInventory(world, position);
	}
	
	public void setNewInventory(World world, BlockPos position) 
			throws NotInventoryException
	{
		this._world = world;
		this._position = position;
		this._state = world.getBlockState(position);
		
		TileEntity tile = world.getTileEntity(position);
		if (tile instanceof IInventory) this._inventory = (IInventory) tile;
		else throw new NotInventoryException(world, position);
	}
	
	public IInventory getInventory() 
	{
		return _inventory;
	}
	
	public World getWorld() 
	{
		return _world;
	}
	
	public BlockPos getPos() 
	{
		return _position;
	}
	
	public IBlockState getBlockState() 
	{
		return _state;
	}
}