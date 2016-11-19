package seia.vanillamagic.tileentity.machine.autocrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.IHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.util.ItemStackHelper;
import seia.vanillamagic.util.MatrixHelper;

public class ContainerAutocrafting extends Container
{
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	public World world;
	public ItemStack[][] stackMatrix;
	
	public ContainerAutocrafting(World world, ItemStack[][] stackMatrix)
	{
		this.world = world;
		this.stackMatrix = stackMatrix;
		this.reloadCraftMatrix(stackMatrix);
		this.onCraftMatrixChanged(this.craftMatrix);
	}
	
	public void reloadCraftMatrix(ItemStack[][] newStackMatrix)
	{
		int index = 0;
		int size = newStackMatrix.length;
		for(int i = 0; i < size; ++i)
		{
			for(int j = 0; j < size; ++j)
			{
				this.craftMatrix.setInventorySlotContents(index, newStackMatrix[i][j]);
				index++;
			}
		}
	}
	
	public void onCraftMatrixChanged(IInventory inv)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, world));
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return false;
	}

	public void rotateMatrix() 
	{
		this.stackMatrix = MatrixHelper.rotateMatrixRight(this.stackMatrix);
		reloadCraftMatrix(this.stackMatrix);
	}

	public boolean craft() 
	{
		this.onCraftMatrixChanged(this.craftMatrix);
		if(!ItemStackHelper.isNullStack(this.craftResult.getStackInSlot(0)))
		{
			return true;
		}
		return false;
	}

	public void removeStacks(IInventory[][] inventoryMatrix)
	{
		int size = inventoryMatrix.length;
		for(int i = 0; i < size; ++i)
		{
			for(int j = 0; j < size; ++j)
			{
				inventoryMatrix[i][j].decrStackSize(0, 1);
			}
		}
	}

	public void outputResult(IHopper iHopper)
	{
		ItemStack result = this.craftResult.getStackInSlot(0);
		InventoryHelper.putStackInInventoryAllSlots(iHopper, result.copy(), EnumFacing.DOWN);
		this.craftResult.removeStackFromSlot(0);
	}
}