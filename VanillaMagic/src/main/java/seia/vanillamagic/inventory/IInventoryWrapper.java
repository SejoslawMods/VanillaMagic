package seia.vanillamagic.inventory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface is used to hold all the basic information about a single {@link IInventory} in {@link World}.
 */
public interface IInventoryWrapper
{
	/**
	 * Setting this wrapper for the new {@link IInventory} based on {@link World} and it's {@link BlockPos}.
	 */
	void setNewInventory(World world, BlockPos position);
	
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
	 * Returns the current state of the inventory block.
	 */
	IBlockState getBlockState();
}