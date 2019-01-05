package seia.vanillamagic.tileentity.machine.autocrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.MatrixUtil;

public class ContainerAutocrafting extends Container
{
	private InventoryCrafting _craftMatrix = new InventoryCrafting(this, 3, 3);
	private IInventory _craftResult = new InventoryCraftResult();
	private World _world;
	private ItemStack[][] _stackMatrix;
	private TileAutocrafting _tile;
	
	public ContainerAutocrafting(World world, ItemStack[][] stackMatrix, TileAutocrafting tile)
	{
		this._world = world;
		this._stackMatrix = stackMatrix;
		this.reloadCraftMatrix(stackMatrix);
		this.onCraftMatrixChanged(this._craftMatrix);
		this._tile = tile;
	}
	
	public void reloadCraftMatrix(ItemStack[][] newStackMatrix)
	{
		int index = 0;
		int size = newStackMatrix.length;
		for (int i = 0; i < size; ++i)
		{
			for (int j = 0; j < size; ++j)
			{
				this._craftMatrix.setInventorySlotContents(index, newStackMatrix[i][j]);
				index++;
			}
		}
	}
	
	public void onCraftMatrixChanged(IInventory inv)
	{
		this._craftResult.setInventorySlotContents(0, CraftingManager.findMatchingRecipe(this._craftMatrix, _world).getRecipeOutput());
	}
	
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return false;
	}

	public void rotateMatrix() 
	{
		this._stackMatrix = MatrixUtil.rotateMatrixRight(this._stackMatrix);
		reloadCraftMatrix(this._stackMatrix);
	}

	public boolean craft() 
	{
		this.onCraftMatrixChanged(this._craftMatrix);
		if (!ItemStackUtil.isNullStack(this._craftResult.getStackInSlot(0))) return true;
		return false;
	}

	public void removeStacks(IInventory[][] inventoryMatrix)
	{
		int size = inventoryMatrix.length;
		for (int i = 0; i < size; ++i)
			for (int j = 0; j < size; ++j)
				inventoryMatrix[i][j].decrStackSize(_tile.getCurrentCraftingSlot(), 1);
	}

	public void outputResult(IInventory inv)
	{
		ItemStack result = this._craftResult.getStackInSlot(0);
		InventoryHelper.putStackInInventoryAllSlots(inv, result.copy(), EnumFacing.DOWN);
		this._craftResult.removeStackFromSlot(0);
	}
}