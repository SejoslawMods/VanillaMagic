package seia.vanillamagic.quest.portablecraftingtable;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Class which represents Container for Portable Crafting Table (PCT).
 * Works similar to vanilla Crafting Table.
 */
public class ContainerPortableCraftingTable extends Container
{
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	public EntityPlayer player;
	
	public ContainerPortableCraftingTable(EntityPlayer player)
	{
		this.player = player;
		this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 124, 35));
		for (int x = 0; x < 3; ++x)
			for (int y = 0; y < 3; ++y)
				this.addSlotToContainer(new Slot(this.craftMatrix, y + x * 3, 30 + y * 18, 17 + x * 18));
		
		for (int x = 0; x < 3; ++x)
			for (int y = 0; y < 9; ++y)
				this.addSlotToContainer(new Slot(player.inventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
		
		for (int x = 0; x < 9; ++x)
			this.addSlotToContainer(new Slot(player.inventory, x, 8 + x * 18, 142));
		
		this.onCraftMatrixChanged(this.craftMatrix);
	}
	
	public void onCraftMatrixChanged(IInventory inv)
	{
		IRecipe recipe = CraftingManager.findMatchingRecipe(this.craftMatrix, player.world);
		if (recipe == null) this.craftResult.setInventorySlotContents(0, ItemStackUtil.NULL_STACK);
		else this.craftResult.setInventorySlotContents(0, recipe.getCraftingResult(craftMatrix));
	}
	
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		if (!player.world.isRemote)
		{
			for (int i = 0; i < 9; ++i)
			{
				ItemStack stack = this.craftMatrix.removeStackFromSlot(i);
				if (!ItemStackUtil.isNullStack(stack)) player.dropItem(stack, false);
			}
		}
	}
    
	public boolean canInteractWith(EntityPlayer entityPlayer) 
	{
		return player.isEntityEqual(entityPlayer);
	}
	
	@Nullable
	public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int index)
	{
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 0)
			{
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) return ItemStackUtil.NULL_STACK;
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index >= 10 && index < 37)
			{
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) return ItemStackUtil.NULL_STACK;
			}
			else if (index >= 37 && index < 46)
			{
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) return ItemStackUtil.NULL_STACK;
			}
			else if (!this.mergeItemStack(itemstack1, 10, 46, false)) return ItemStackUtil.NULL_STACK;
			
			if (ItemStackUtil.getStackSize(itemstack1) == 0) slot.putStack(ItemStackUtil.NULL_STACK);
			else slot.onSlotChanged();
			
			if (ItemStackUtil.getStackSize(itemstack1) == ItemStackUtil.getStackSize(itemstack)) return ItemStackUtil.NULL_STACK;
			slot.onTake(entityPlayer, itemstack1);
		}
		return itemstack;
	}
	
	public boolean canMergeSlot(ItemStack stack, Slot slot)
	{
		return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
	}
}