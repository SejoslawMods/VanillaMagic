package com.github.sejoslaw.vanillamagic.tileentity.inventorybridge;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.event.EventInventoryBridge;
import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.IInventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.inventory.InventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.inventorybridge.IInventoryBridge;
import com.github.sejoslaw.vanillamagic.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.item.VanillaMagicItems;
import com.github.sejoslaw.vanillamagic.item.inventoryselector.ItemInventorySelector;
import com.github.sejoslaw.vanillamagic.tileentity.CustomTileEntity;
import com.github.sejoslaw.vanillamagic.util.BlockPosUtil;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.NBTUtil;
import com.github.sejoslaw.vanillamagic.util.WorldUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileInventoryBridge extends CustomTileEntity implements IInventoryBridge {
	public static final String REGISTRY_NAME = TileInventoryBridge.class.getName();

	/*
	 * Input Inventory.
	 */
	@Nullable
	protected IInventoryWrapper inputInvWrapper;

	/**
	 * Inventory under this Tile.
	 */
	@Nullable
	protected IInventoryWrapper outputInvWrapper;

	protected int inputInvX, inputInvY, inputInvZ, inputInvDim;
	protected int outputInvX, outputInvY, outputInvZ, outputInvDim;

	@Nullable
	public IInventoryWrapper getInputInventory() {
		return inputInvWrapper;
	}

	@Nullable
	public IInventoryWrapper getOutputInventory() {
		return outputInvWrapper;
	}

	public List<String> getAdditionalInfo() {
		List<String> list = super.getAdditionalInfo();
		list.add("Input:  X=" + inputInvX + ", Y=" + inputInvY + ", Z=" + inputInvZ + ", Dim=" + inputInvDim);
		list.add("Output: X=" + outputInvX + ", Y=" + outputInvY + ", Z=" + outputInvZ + ", Dim=" + outputInvDim);
		return list;
	}

	/**
	 * Try to override serializeNBT instead of this method.
	 */
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		super.writeToNBT(compound);
		compound = NBTUtil.writeToINBTSerializable(this, compound);
		return compound;
	}

	public CompoundNBT serializeNBT() {
		CompoundNBT compound = new CompoundNBT();
		compound.setInteger("inputInvX", inputInvX);
		compound.setInteger("inputInvY", inputInvY);
		compound.setInteger("inputInvZ", inputInvZ);
		compound.setInteger("inputInvDim", inputInvDim);

		compound.setInteger("outputInvX", outputInvX);
		compound.setInteger("outputInvY", outputInvY);
		compound.setInteger("outputInvZ", outputInvZ);
		compound.setInteger("outputInvDim", outputInvDim);
		return compound;
	}

	/**
	 * Try to override deserializeNBT instead of this method.
	 */
	public void readFromNBT(CompoundNBT compound) {
		super.readFromNBT(compound);
		NBTUtil.readFromINBTSerializable(this, compound);
	}

	public void deserializeNBT(CompoundNBT compound) {
		inputInvX = compound.getInteger("inputInvX");
		inputInvY = compound.getInteger("inputInvY");
		inputInvZ = compound.getInteger("inputInvZ");
		inputInvDim = compound.getInteger("inputInvDim");
		BlockPos inputPos = new BlockPos(inputInvX, inputInvY, inputInvZ);

		try {
			inputInvWrapper = new InventoryWrapper(WorldUtil.getWorld(inputInvDim), inputPos);
		} catch (NotInventoryException e) {
			BlockPosUtil.printCoords(Level.ERROR, "NotInventoryException at: ", inputPos);
			e.printStackTrace();
		}

		outputInvX = compound.getInteger("outputInvX");
		outputInvY = compound.getInteger("outputInvY");
		outputInvZ = compound.getInteger("outputInvZ");
		outputInvDim = compound.getInteger("outputInvDim");
		BlockPos outputPos = new BlockPos(outputInvX, outputInvY, outputInvZ);

		try {
			outputInvWrapper = new InventoryWrapper(WorldUtil.getWorld(outputInvDim), outputPos);
		} catch (NotInventoryException e) {
			BlockPosUtil.printCoords(Level.ERROR, "NotInventoryException at: ", outputPos);
			e.printStackTrace();
		}
	}

	/**
	 * Sets the input position based on the {@link ItemInventorySelector}'s NBT
	 * saved data.
	 */
	public void setPositionFromSelector(PlayerEntity player) throws NotInventoryException {
		setPositionFromSelector(player.inventory);
	}

	public void setPositionFromSelector(InventoryPlayer invPlayer) throws NotInventoryException {
		setPositionFromSelector(invPlayer.mainInventory);
	}

	public void setPositionFromSelector(NonNullList<ItemStack> mainInventory) throws NotInventoryException {
		for (ItemStack currentCheckingStack : mainInventory) {
			CompoundNBT currentCheckingStackTag = currentCheckingStack.getTagCompound();

			if (!VanillaMagicItems.isCustomItem(currentCheckingStack, VanillaMagicItems.INVENTORY_SELECTOR)
					|| (currentCheckingStackTag == null)) {
				continue;
			}

			BlockPos savedPosition = NBTUtil.getBlockPosDataFromNBT(currentCheckingStackTag);
			World world = WorldUtil.getWorld(NBTUtil.getDimensionFromNBT(currentCheckingStackTag));
			inputInvWrapper = new InventoryWrapper(world, savedPosition);

			inputInvX = savedPosition.getX();
			inputInvY = savedPosition.getY();
			inputInvZ = savedPosition.getZ();

			inputInvDim = WorldUtil.getDimensionID(world);
		}
	}

	public void setOutputInventory(World world, BlockPos pos) throws NotInventoryException {
		outputInvWrapper = new InventoryWrapper(world, pos);
		outputInvX = pos.getX();
		outputInvY = pos.getY();
		outputInvZ = pos.getZ();
		outputInvDim = WorldUtil.getDimensionID(world);
	}

	public Direction getInputFacing() {
		return Direction.UP;
	}

	/**
	 * Currently transporting slot
	 */
	int slotNumber = 0;

	public void update() {
		if (inputInvWrapper == null) {
			return;
		}

		IInventory inv = inputInvWrapper.getInventory();

		if (slotNumber >= inv.getSizeInventory()) {
			slotNumber = 0;
		}

		ItemStack transportingStack = inv.getStackInSlot(slotNumber);

		if (ItemStackUtil.isNullStack(transportingStack)) {
			slotNumber++;
			return;
		}

		ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(outputInvWrapper.getInventory(),
				transportingStack, getInputFacing());
		inv.setInventorySlotContents(slotNumber, leftItems);
		slotNumber++;
		EventUtil.postEvent(new EventInventoryBridge((IInventoryBridge) this, world, pos));
	}
}
