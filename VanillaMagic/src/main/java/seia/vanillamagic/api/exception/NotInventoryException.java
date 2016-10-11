package seia.vanillamagic.api.exception;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.inventory.IInventoryWrapper;

/**
 * This Exception will be thrown if the {@link IInventoryWrapper} 
 * couldn't convert the {@link TileEntity}, at given {@link BlockPos}, to {@link IInventory}
 * 
 * @see {@link IInventoryWrapper#setNewInventory(World, BlockPos)}
 */
public class NotInventoryException extends Exception
{
	/**
	 * {@link World} on which the {@link IInventory} should be.
	 */
	public final World world;
	/**
	 * Position on which the {@link IInventory} should be.
	 */
	public final BlockPos position;
	
	public NotInventoryException(World world, BlockPos position) 
	{
		this.world = world;
		this.position = position;
	}
}