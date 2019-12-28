package com.github.sejoslaw.vanillamagic.common.tileentity.machine.autocrafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.MatrixUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ContainerAutocrafting extends Container {
	private InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	private IInventory craftResult = new InventoryCraftResult();
	private World world;
	private ItemStack[][] stackMatrix;
	private TileAutocrafting tile;

	public ContainerAutocrafting(World world, ItemStack[][] stackMatrix, TileAutocrafting tile) {
		this.world = world;
		this.stackMatrix = stackMatrix;
		this.reloadCraftMatrix(stackMatrix);
		this.onCraftMatrixChanged(this.craftMatrix);
		this.tile = tile;
	}

	public void reloadCraftMatrix(ItemStack[][] newStackMatrix) {
		int index = 0;
		int size = newStackMatrix.length;

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				this.craftMatrix.setInventorySlotContents(index, newStackMatrix[i][j]);
				index++;
			}
		}
	}

	public void onCraftMatrixChanged(IInventory inv) {
		this.craftResult.setInventorySlotContents(0,
				CraftingManager.findMatchingRecipe(this.craftMatrix, world).getRecipeOutput());
	}

	public boolean canInteractWith(PlayerEntity playerIn) {
		return false;
	}

	public void rotateMatrix() {
		this.stackMatrix = MatrixUtil.rotateMatrixRight(this.stackMatrix);
		reloadCraftMatrix(this.stackMatrix);
	}

	public boolean craft() {
		this.onCraftMatrixChanged(this.craftMatrix);

		if (!ItemStackUtil.isNullStack(this.craftResult.getStackInSlot(0))) {
			return true;
		}

		return false;
	}

	public void removeStacks(IInventory[][] inventoryMatrix) {
		int size = inventoryMatrix.length;

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				inventoryMatrix[i][j].decrStackSize(tile.getCurrentCraftingSlot(), 1);
			}
		}
	}

	public void outputResult(IInventory inv) {
		ItemStack result = this.craftResult.getStackInSlot(0);
		InventoryHelper.putStackInInventoryAllSlots(inv, result.copy(), Direction.DOWN);
		this.craftResult.removeStackFromSlot(0);
	}
}