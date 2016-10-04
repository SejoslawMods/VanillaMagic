package seia.vanillamagic.inventory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryWrapper implements IInventoryWrapper
{
	private IInventory inventory;
	private World world;
	private BlockPos position;
	private IBlockState state;
	
	public InventoryWrapper(World world, BlockPos position)
	{
		this.setNewInventory(world, position);
	}
	
	public void setNewInventory(World world, BlockPos position)
	{
		this.world = world;
		this.position = position;
		this.state = world.getBlockState(position);
		this.inventory = ((IInventory) world.getTileEntity(position));
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