package seia.vanillamagic.machine.quarry;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.machine.IMachine;

/**
 * This is connection to the TileQuarry.
 */
public interface IQuarry extends IMachine
{
	/**
	 * Returns Quarry size in blocks.
	 */
	public int getQuarrySize();
	
	/**
	 * Returns the Quarry mining direction. Facing from Cauldron to the start mining position. <br>
	 * <br>
	 * rotateY(getStartPosFacing(), 1) - returns the direction of {@link IInventory} on right from Cauldron<br>
	 * rotateY(getStartPosFacing(), 2) - returns the direction of RedstoneBlock<br>
	 * rotateY(getStartPosFacing(), 3) - returns the direction of DiamondBock
	 * 
	 * @see {@link #rotateY(EnumFacing, int)}
	 */
	public EnumFacing getStartPosFacing();
	
	/**
	 * In short, this will restart the Quarry mining position.
	 */
	public void restartDefaultStartPos();
	
	/**
	 * Facings around the Quarry;
	 */
	public EnumFacing[] facings();
	
	/**
	 * Returns TRUE if the Quarry can mine next block.
	 */
	public boolean canDig();
	
	/**
	 * Spawns the given ItemStack.
	 */
	public void spawnDigged(ItemStack digged);
	
	/**
	 * Moves the working position to the next position and returns it.
	 */
	public BlockPos moveWorkingPosToNextPos();
	
	/**
	 * One Quarry operation. <br>
	 * Check if Air Block OR check if Bedrock OR mine block, etc.
	 */
	public void performOneOperation();
	
	/**
	 * Rotates the given facing the number of given times and returns this facing after rotation. <br>
	 * This will only rotate Horizontally.
	 * 
	 * @see {@link net.minecraft.util.EnumFacing.Plane#HORIZONTAL}
	 */
	public EnumFacing rotateY(EnumFacing startFace, int times);
}