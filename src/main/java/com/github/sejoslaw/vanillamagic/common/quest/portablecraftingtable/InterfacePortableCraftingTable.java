package com.github.sejoslaw.vanillamagic.quest.portablecraftingtable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

/**
 * Class which represents Interface of Portable Crafting Table (PCT). Works
 * similar to vanilla Crafting Table.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class InterfacePortableCraftingTable implements IInteractionObject {
	public final PlayerEntity player;

	public InterfacePortableCraftingTable(PlayerEntity player) {
		this.player = player;
	}

	public String getName() {
		return null;
	}

	public boolean hasCustomName() {
		return false;
	}

	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(Blocks.CRAFTING_TABLE.getUnlocalizedName() + ".name", new Object[0]);
	}

	public Container createContainer(InventoryPlayer playerInventory, PlayerEntity playerIn) {
		return new ContainerPortableCraftingTable(player);
	}

	public String getGuiID() {
		return "minecraft:crafting_table";
	}
}