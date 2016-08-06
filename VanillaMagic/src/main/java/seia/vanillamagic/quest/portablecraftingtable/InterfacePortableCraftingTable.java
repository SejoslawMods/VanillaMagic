package seia.vanillamagic.quest.portablecraftingtable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class InterfacePortableCraftingTable implements IInteractionObject
{
	public EntityPlayer player;
	
	public InterfacePortableCraftingTable(EntityPlayer player)
	{
		this.player = player;
	}
	
	public String getName() 
	{
		return null;
	}
	
	public boolean hasCustomName() 
	{
		return false;
	}
	
	public ITextComponent getDisplayName() 
	{
		return new TextComponentTranslation(Blocks.CRAFTING_TABLE.getUnlocalizedName() + ".name", new Object[0]);
	}
	
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) 
	{
		return new ContainerPortableCraftingTable(player);
	}
	
	public String getGuiID() 
	{
		return "minecraft:crafting_table";
	}
}