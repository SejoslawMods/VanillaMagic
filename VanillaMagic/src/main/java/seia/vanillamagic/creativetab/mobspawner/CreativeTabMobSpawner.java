package seia.vanillamagic.creativetab.mobspawner;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import seia.vanillamagic.quest.mobspawnerdrop.MobSpawnerRegistry;

public class CreativeTabMobSpawner extends CreativeTabs
{
	private final Block SPAWNER = Blocks.MOB_SPAWNER;
	
	public CreativeTabMobSpawner()
	{
		super("mobSpawner");
		SPAWNER.setCreativeTab(this);
		Blocks.COMMAND_BLOCK.setCreativeTab(this);
	}
	
	@SideOnly(Side.CLIENT)
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(SPAWNER);
	}
	
	public boolean hasSearchBar()
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> list)
	{
		list = MobSpawnerRegistry.fillList(list);
		super.displayAllRelevantItems(list);
	}
}