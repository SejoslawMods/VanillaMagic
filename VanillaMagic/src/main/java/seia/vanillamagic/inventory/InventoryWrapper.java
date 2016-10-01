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
		this.world = world;
		this.position = position;
		this.state = world.getBlockState(position);
		this.inventory = ((IInventory) world.getTileEntity(position));
	}
	
	public InventoryWrapper(World world, BlockPos position, IInventory inventory)
	{
		this(world, position);
		this.inventory = inventory;
	}
	
	public IInventory getInventory() 
	{
		return inventory;
	}
	
	public void setInventory(IInventory inv) 
	{
		this.inventory = inv;
	}
	
	public World getWorld() 
	{
		return world;
	}
	
	public void setWorld(World world) 
	{
		this.world = world;
	}
	
	public BlockPos getPos() 
	{
		return position;
	}
	
	public void setPos(BlockPos pos) 
	{
		this.position = pos;
	}
	
	public IBlockState getBlockState() 
	{
		return state;
	}
	
	public void setBlockState(IBlockState state) 
	{
		this.state = state;
	}
}