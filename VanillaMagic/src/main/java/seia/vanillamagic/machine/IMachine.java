package seia.vanillamagic.machine;

import java.io.Serializable;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import seia.vanillamagic.inventory.IInventoryWrapper;
import seia.vanillamagic.util.IAdditionalInfoProvider;

/**
 * Machine is a TileEntity that perform some work on World.
 * This interface is made to unify some of the Machine work.
 * 
 * @author Seia
 */
public interface IMachine extends ITickable, INBTSerializable<NBTTagCompound>, Serializable, IAdditionalInfoProvider
{
	/**
	 * Returns the World on which Machine is.
	 */
	World getWorld();
	
	/**
	 * Set the Machine's World.
	 */
	void setWorld(World world);
	
	/**
	 * Get TileEntity to which this interface is implemented into.
	 */
	TileEntity getTileEntity();
	
	/**
	 * Get the actual position of the machine block.
	 * By MachinePosition we understand the core of the Machine.
	 * In Vanilla Magic it usually will return Cauldron position.
	 */
	BlockPos getMachinePos();
	
	/**
	 * Returns the state of this Machine.
	 */
	IBlockState getMachineState();
	
	/**
	 * Returns the Machine block.
	 */
	Block getMachineBlock();
	
	/**
	 * Set Machine position.
	 */
	void setMachinePos(BlockPos newPos);
	
	/**
	 * Get the current Machine working position.
	 * In Quarry it's a digging position.
	 * It should return the position on which Machine is performing work.
	 */
	BlockPos getWorkingPos();
	
	/**
	 * Set the position on which Machine will work.
	 * It can be make directly in 'onUpdate' method.
	 */
	void setWorkingPos(BlockPos newPos);
	
	/**
	 * Returns the starting position at which the Machine will start to operate. 
	 */
	BlockPos getStartPos();
	
	/**
	 * Set new Machine starting position. 
	 * After this being set, the Machine should restart the work.
	 */
	void setNewStartPos(BlockPos newStartPos);
	
	/**
	 * Get the Machine work radius (in blocks).
	 */
	int getWorkRadius();
	
	/**
	 * Sets the new radius for this Machine.
	 */
	void setWorkRadius(int newRadius);
	
	/**
	 * It will return the cost of performing one operation.
	 */
	int getOneOperationCost();
	
	/**
	 * Each Machine uses ticks.
	 * Ticks works like Furnace smelting ticks.
	 * For instance Coal as fuel will return 1600 ticks.
	 * It will return the current ticks in machine.
	 * Dividing this by getOneOperationCost() will give You the amount of operations the Machine
	 * can handle.
	 */
	int getCurrentTicks();
	
	/**
	 * It should return the max number of ticks that Machine can contain.
	 * It's efficient to make it: 10 * getOneOperationCost().
	 * If that made, the Machine won't use the infinite amount of resources.
	 */
	int getMaxTicks();
	
	/**
	 * Method to force the Machine for fuel-check.
	 */
	void checkFuel();
	
	/**
	 * Method to force the Machine to end it's work.
	 */
	void endWork();
	
	/**
	 * Method to determine if a Machine has finished work.
	 */
	boolean finishedWork();
	
	/**
	 * What Player should hold in left hand (off hand) to activate this Machine.
	 * This usually should be set in Quest.
	 */
	@Nullable
	ItemStack getActivationStackLeftHand();
	
	/**
	 * Set the activation stack that should be in left hand (off hand).
	 */
	void setActivationStackLeftHand(ItemStack stack);
	
	/**
	 * What Player should hold in right hand (main hand) to activate this Machine.
	 * This usually should be set in Quest.
	 */
	@Nullable
	ItemStack getActivationStackRightHand();
	
	/**
	 * Set the activation stack that should be in right hand (main hand).
	 */
	void setActivationStackRightHand(ItemStack stack);
	
	/**
	 * Simple method to detect if the Machine is active.
	 */
	boolean isActive();
	
	/**
	 * Returns the inventory from which Machine can take resources to work (fuel, etc.).
	 */
	@Nullable
	IInventoryWrapper getInputInventory();
	
	/**
	 * Returns the inventory to which Machine should output.
	 */
	@Nullable
	IInventoryWrapper getOutputInventory();
	
	//=========================== NEIGHBOR =====================================================
	
	/**
	 * Get neighbor TileEntity at given face.
	 */
	@Nullable
	TileEntity getNeighborTile(EnumFacing face);
	
	/**
	 * Get the Machine that is next to this Machine.
	 */
	@Nullable
	IMachine getNeighborMachine(EnumFacing face);
	
	/**
	 * Returns the neighbor position.
	 */
	BlockPos getNeighborPos(EnumFacing face);
	
	/**
	 * Returns the state of the neighbor block.
	 */
	IBlockState getNeighborState(EnumFacing face);
	
	/**
	 * Returns the neighbor block.
	 */
	Block getNeighborBlock(EnumFacing face);
}