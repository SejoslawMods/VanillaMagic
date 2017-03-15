package seia.vanillamagic.creativetab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.item.book.BookRegistry;
import seia.vanillamagic.quest.mobspawnerdrop.MobSpawnerRegistry;
import seia.vanillamagic.util.WorldHelper;

public class VanillaMagicCreativeTab extends CreativeTabs
{
	private final Block SPAWNER = Blocks.MOB_SPAWNER;
	
	public VanillaMagicCreativeTab()
	{
		super("vm");
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
		list = VanillaMagicItems.fillList(list);
		list = BookRegistry.fillList(list);
		list = WorldHelper.fillList(list);
		super.displayAllRelevantItems(list);
	}
}