package seia.vanillamagic.api.inventory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.exception.NotInventoryException;

/**
 * This interface is used to hold all the basic information about a single {@link IInventory} in {@link World}.
 */
public interface IInventoryWrapper
{
	/**
	 * Setting this wrapper for the new {@link IInventory} based on {@link World} and it's {@link BlockPos}.
	 * 
	 * @throws NotInventoryException thrown if the {@link TileEntity} at given {@link BlockPos} couldn't be cast to {@link IInventory}
	 */
	void setNewInventory(World world, BlockPos position) 
			throws NotInventoryException;
	
	/**
	 * Returns the holding {@link IInventory}.
	 */
	IInventory getInventory();
	
	/**
	 * Returns the {@link World} with this {@link IInventory}.
	 */
	World getWorld();
	
	/**
	 * Returns the position of this {@link IInventory}.
	 */
	BlockPos getPos();
	
	/**
	 * Returns the current state of the {@link IInventory} block.
	 */
	IBlockState getBlockState();
}