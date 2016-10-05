package seia.vanillamagic.api.tileentity.machine;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * This is the connection to the TileQuarry.
 */
public interface IQuarry extends IMachine
{
	/**
	 * Returns Quarry size in blocks.
	 */
	int getQuarrySize();
	
	/**
	 * Returns the Quarry mining direction. Facing from Cauldron to the start mining position. <br>
	 * <br>
	 * rotateY(getStartPosFacing(), 1) - returns the direction of {@link IInventory} on right from Cauldron<br>
	 * rotateY(getStartPosFacing(), 2) - returns the direction of RedstoneBlock<br>
	 * rotateY(getStartPosFacing(), 3) - returns the direction of DiamondBock
	 * 
	 * @see {@link #rotateY(EnumFacing, int)}
	 */
	EnumFacing getStartPosFacing();
	
	/**
	 * In short, this will restart the Quarry mining position.
	 */
	void restartDefaultStartPos();
	
	/**
	 * Facings around the Quarry;
	 */
	EnumFacing[] facings();
	
	/**
	 * Returns TRUE if the Quarry can mine next block.
	 */
	boolean canDig();
	
	/**
	 * Spawns the given ItemStack.
	 */
	void spawnDigged(ItemStack digged);
	
	/**
	 * Moves the working position to the next position and returns it.
	 */
	BlockPos moveWorkingPosToNextPos();
	
	/**
	 * One Quarry operation. <br>
	 * Check if Air Block OR check if Bedrock OR mine block, etc.
	 */
	void performOneOperation();
	
	/**
	 * Rotates the given facing the number of given times and returns this facing after rotation. <br>
	 * This will only rotate Horizontally.
	 * 
	 * @see {@link net.minecraft.util.EnumFacing.Plane#HORIZONTAL}
	 */
	EnumFacing rotateY(EnumFacing startFace, int times);
	
	/**
	 * Forces Quarry to stop it's work. <br>
	 * Quarry won't start until You use {@link #forceQuarryStart()}.
	 */
	void forceQuarryStop();
	
	/**
	 * Forces Quarry to start it's work.
	 */
	void forceQuarryStart();
}