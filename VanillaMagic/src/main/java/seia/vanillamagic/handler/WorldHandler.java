package seia.vanillamagic.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.google.common.io.Files;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.chunkloader.TileChunkLoader;
import seia.vanillamagic.machine.TileMachine;
import seia.vanillamagic.machine.farm.TileFarm;
import seia.vanillamagic.machine.quarry.TileQuarry;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.NBTHelper;

/**
 * TODO: Fix Saving / Loading CustomTileEntities
 */
public class WorldHandler
{
	public static final WorldHandler INSTANCE = new WorldHandler();
	
	private static String VM_DIRECTORY = "VanillaMagic";
	private static String FILE_NAME_TILES = "VanillaMagicTileEntities.dat";
	private static String FILE_NAME_TILES_OLD = "VanillaMagicTileEntities.dat_old";
	
	private static String TILES = "tiles";
	
	private WorldHandler()
	{
	}
	
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
		System.out.println("WorldHandler registered");
	}
	
//	@SubscribeEvent
//	public void worldUnload(WorldEvent.Unload event)
//	{
//		try
//		{
//			World world = event.getWorld();
//			if(world.isRemote)
//			{
//				return;
//			}
//			int dimension = CustomTileEntityHandler.INSTANCE.getDimensionFromTickables(world);
//			if(dimension == CustomTileEntityHandler.ERROR_DIMENSION_ID)
//			{
//				System.out.println("[World Unload] There is no CustomTileEntities for this World.");
//				return;
//			}
//			else
//			{
//				worldSave(new WorldEvent.Save(world));
//				CustomTileEntityHandler.INSTANCE.clearTileEntitiesForDimension(dimension);
//			}
//		}
//		catch(Exception e)
//		{
//		}
//	}
	
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event)
	{
		if(event.getWorld().isRemote)
		{
			return;
		}
		File vmDirectory = getVanillaMagicRootDirectory();
		if(!vmDirectory.exists())
		{
			vmDirectory.mkdirs();
			return;
		}
		File[] dimFiles = vmDirectory.listFiles();
		try
		{
			for(File dimFile : dimFiles)
			{
				String fileExtension = Files.getFileExtension(dimFile.getAbsolutePath());
				if(fileExtension.equals("dat"))
				{
					FileInputStream fileInputStream = new FileInputStream(dimFile);
					NBTTagCompound data = CompressedStreamTools.readCompressed(fileInputStream);
					fileInputStream.close();
					NBTTagList tagList = data.getTagList(TILES, 10);
					for(int i = 0; i < tagList.tagCount(); i++)
					{
						NBTTagCompound tileEntityTag = tagList.getCompoundTagAt(i);
						String tileEntityClassName = tileEntityTag.getString("id");
						int tileEntityPosX = tileEntityTag.getInteger("x");
						int tileEntityPosY = tileEntityTag.getInteger("y");
						int tileEntityPosZ = tileEntityTag.getInteger("z");
						BlockPos tileEntityPos = new BlockPos(tileEntityPosX, tileEntityPosY, tileEntityPosZ);
						int dimension = tileEntityTag.getCompoundTag(NBTHelper.NBT_SERIALIZABLE).getInteger(TileMachine.NBT_DIMENSION);
						World world = DimensionManager.getWorld(dimension);
						//TileEntity tileEntity = NBTHelper.getTileEntityFromNBT(world, tileEntityTag);
						TileEntity tileEntity = null;
						if(tileEntityClassName.equals(TileQuarry.class.getSimpleName()))
						{
							tileEntity = new TileQuarry();
							((TileQuarry) tileEntity).init(tileEntityPos, world);
						}
						else if(tileEntityClassName.equals(TileFarm.class.getSimpleName()))
						{
							tileEntity = new TileFarm();
							((TileFarm) tileEntity).init(world, tileEntityPos);
						}
						else if(tileEntityClassName.equals(TileChunkLoader.class.getSimpleName()))
						{
							tileEntity = new TileChunkLoader();
							((TileChunkLoader) tileEntity).init(tileEntityPos, world);
						}
						System.out.println("[World Load] Created TileEntity (" + tileEntity.getClass().getSimpleName() + ")");
						try
						{
							BlockPosHelper.printCoords("[World Load] Setting pos at:", tileEntityPos);  // TODO:
							tileEntity.setPos(tileEntityPos);
							BlockPosHelper.printCoords("[World Load] Pos saved at:", tileEntity.getPos());  // TODO:
						}
						catch(Exception e)
						{
							BlockPosHelper.printCoords("[World Load] Can't set position for tile: " + tileEntity.getClass().getSimpleName(), tileEntityPos);
						}
						if(tileEntity != null)
						{
							NBTHelper.readFromINBTSerializable(tileEntity, tileEntityTag);
							CustomTileEntityHandler.INSTANCE.addCustomTileEntity(tileEntity);
						}
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	@SubscribeEvent
	public void worldSave(WorldEvent.Save event)
	{
		if(event.getWorld().isRemote)
		{
			return;
		}
		File vmDirectory = getVanillaMagicRootDirectory();
		if(!vmDirectory.exists())
		{
			vmDirectory.mkdirs();
		}
		File fileTiles = new File(vmDirectory, FILE_NAME_TILES);
		if(!fileTiles.exists())
		{
			try 
			{
				fileTiles.createNewFile();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		File fileTilesOld = new File(vmDirectory, FILE_NAME_TILES_OLD);
		if(!fileTilesOld.exists())
		{
			try 
			{
				fileTilesOld.createNewFile();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		//for(Integer i : DimensionManager.getIDs())
		{
			//int dimension = i.intValue();
			//World world = DimensionManager.getWorld(dimension);
			//if(!world.isRemote)
			World world = event.getWorld();
			int dimension = CustomTileEntityHandler.INSTANCE.getDimensionFromTickables(world);
			if(dimension == CustomTileEntityHandler.ERROR_DIMENSION_ID)
			{
				System.out.println("[World Save] There is no CustomTileEntities for this World.");
				return;
			}
			try
			{
				try
				{
					Files.copy(fileTiles, fileTilesOld);
				}
				catch(Exception e)
				{
				}
				NBTTagCompound data = new NBTTagCompound();
				NBTTagList dataList = new NBTTagList();
				List<TileEntity> tickables = CustomTileEntityHandler.INSTANCE.getCustomEntitiesInDimension(dimension);
				for(int j = 0; j < tickables.size(); j++)
				{
					dataList.appendTag(tickables.get(j).writeToNBT(new NBTTagCompound()));
				}
				data.setTag(TILES, dataList);
				FileOutputStream fileOutputStream = new FileOutputStream(fileTiles);
				CompressedStreamTools.writeCompressed(data, fileOutputStream);
				fileOutputStream.close();
				System.out.println("[World Save] Vanilla Magic TileEntities saved for Dimension: " + dimension);
			}
			catch(Exception e)
			{
//				System.out.println("Error while saving Vanilla Magic TileEntities for World with id: " + dimension);
//				if(fileTiles.exists())
//				{
//					try
//					{
//						fileTiles.delete();
//						
//					}
//					catch(Exception ex)
//					{
//					}
//				}
			}
		}
	}

	public static File getVanillaMagicRootDirectory()
	{
		return new File(DimensionManager.getCurrentSaveRootDirectory(), VM_DIRECTORY + "/");
	}
}