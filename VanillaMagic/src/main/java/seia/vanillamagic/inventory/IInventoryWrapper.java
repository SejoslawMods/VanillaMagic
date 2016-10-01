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
	 * Returns the holding {@link IInventory}.
	 */
	IInventory getInventory();
	
	/**
	 * Set new {@link IInventory} for this wrapper.
	 */
	void setInventory(IInventory inv);
	
	/**
	 * Returns the {@link World} with this {@link IInventory}.
	 */
	World getWorld();
	
	/**
	 * Set new {@link World} for this wrapper
	 */
	void setWorld(World world);
	
	/**
	 * Returns the position of this {@link IInventory}.
	 */
	BlockPos getPos();
	
	/**
	 * Set new position.
	 */
	void setPos(BlockPos pos);
	
	/**
	 * Returns the current state of the inventory block.
	 */
	IBlockState getBlockState();
	
	/**
	 * Set new state for this inventory block.
	 */
	void setBlockState(IBlockState state);
}