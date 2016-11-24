package seia.vanillamagic.creativetab;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabMobSpawner extends CreativeTabs
{
	private final Block SPAWNER = Blocks.MOB_SPAWNER;
	
	public CreativeTabMobSpawner()
	{
		super("mobSpawner");
		SPAWNER.setCreativeTab(this);
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
	
	// TODO: Add all custom spawners for all mobs
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(NonNullList<ItemStack> list)
	{
		List<EntityEntry> entries = ForgeRegistries.ENTITIES.getValues(); // List of all entities
		for(EntityEntry ee : entries)
		{
			ItemStack stack = new ItemStack(SPAWNER);
			stack.setStackDisplayName("Mob Spawner: " + ee.getName());
		}
		super.displayAllRelevantItems(list); // This must be at the end
	}
}