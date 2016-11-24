package seia.vanillamagic.creativetab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CreativeTabMobSpawner extends CreativeTabs
{
	private final Block SPAWNER = Blocks.MOB_SPAWNER;
	
	public CreativeTabMobSpawner() 
	{
		super("mobSpawner");
		SPAWNER.setCreativeTab(this);
	}

	@Override
	public ItemStack getTabIconItem() 
	{
		return new ItemStack(SPAWNER);
	}
}