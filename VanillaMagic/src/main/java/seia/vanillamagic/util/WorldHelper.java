package seia.vanillamagic.util;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import seia.vanillamagic.quest.QuestJumper;

/**
 * Class which store various methods connected with MC World.
 */
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
	
	public static Class<? extends WorldProvider>[] getVanillaWorldProviders()
	{
		return new Class[] 
		{
				WorldProviderHell.class,
				WorldProviderSurface.class,
				WorldProviderEnd.class
		};
	}
	
	public static Class<? extends WorldProvider> getRandomWorldProvider()
	{
		Class<? extends WorldProvider>[] providers = getVanillaWorldProviders();
		return providers[new Random().nextInt(providers.length)];
	}
	
	// TODO: Finish creating new Dimension
	public static void regiterNewDimension(int dimId, BlockPos spawnPos)
	{
		// world registration
		Class<? extends WorldProvider> newProvider = getRandomWorldProvider(); // null;
//		if(dimId == 666)
//		{
//			newProvider = WorldProviderHell.class;
//		}
//		else
//		{
//			newProvider = WorldProviderSurface.class;
//		}
		DimensionType newProviderType = DimensionType.register("Dim-" + dimId, "_Dim-" + dimId, dimId, newProvider, true);
		DimensionManager.registerDimension(dimId, newProviderType);
		DimensionManager.initDimension(dimId);
		
		// new world startup
		WorldServer newWorldServer = DimensionManager.getWorld(dimId);
		newWorldServer.setSpawnPoint(spawnPos);
		newWorldServer.getChunkProvider().loadChunk(spawnPos.getX() >> 4, spawnPos.getZ() >> 4);
	}
}