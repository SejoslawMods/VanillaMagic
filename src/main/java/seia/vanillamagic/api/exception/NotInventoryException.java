package seia.vanillamagic.api.exception;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.inventory.IInventoryWrapper;

/**
 * This Exception will be thrown if the {@link TileEntity} couldn't be convert
 * to {@link IInventory}
 * 
 * @see {@link IInventoryWrapper#setNewInventory(World, BlockPos)}
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class NotInventoryException extends Exception {
	private static final long serialVersionUID = 2586710499803361489L;

	/**
	 * {@link World} on which the {@link IInventory} should be.
	 */
	public final World world;

	/**
	 * Position on which the {@link IInventory} should be.
	 */
	public final BlockPos position;

	public NotInventoryException(World world, BlockPos position) {
		this.world = world;
		this.position = position;
	}
}