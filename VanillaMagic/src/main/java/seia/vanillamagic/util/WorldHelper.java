package seia.vanillamagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import seia.vanillamagic.quest.QuestJumper;

public class WorldHelper
{
	private WorldHelper()
	{
	}
	
	public static int getDimensionID(World world)
	{
		return world.provider.getDimension();
	}
	
	public static int getDimensionID(Entity entity)
	{
		return entity.dimension;
	}
	
	public static int getDimensionID(TileEntity tile)
	{
		return getDimensionID(tile.getWorld());
	}
	
	public static int getDimensionID(WorldEvent event)
	{
		return getDimensionID(event.getWorld());
	}
	
	public static NonNullList<ItemStack> fillList(NonNullList<ItemStack> list) 
	{
		BlockPos pos = new BlockPos(100, 100, 100); // Default position
		for(Integer i : DimensionManager.getIDs())
		{
			list.add(QuestJumper.writeDataToBook(DimensionManager.getWorld(i.intValue()), pos));
		}
		return list;
	}
}