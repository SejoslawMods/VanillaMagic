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
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.machine.TileMachine;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.NBTHelper;

public class WorldHandler
{
	public static final WorldHandler INSTANCE = new WorldHandler();
	
	private static String VM_DIRECTORY = "VanillaMagic";
	private static String FILE_NAME_TILES = "VanillaMagicTileEntities.dat";
	private static String FILE_NAME_TILES_OLD = "VanillaMagicTileEntities.dat_old";
	
	private static String TILES = "tiles";
	private static String TILE_QUARRY_PREFIX = "quarry";
	private static String TILE_CHUNKLOADER_PREFIX = "chunkLoader";
	private static String TILE_FARM_PREFIX = "farm";
	
	private WorldHandler()
	{
	}
	
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
		System.out.println("WorldHandler registered");
	}
	
	// TODO: Fix loading
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event)
	{
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
					System.out.println(dimFile.getName()); // TODO:
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
						TileEntity tileEntity = NBTHelper.getTileEntityFromNBT(world, tileEntityTag);
						System.out.println("Created TileEntity (" + tileEntity.getClass().getSimpleName() + ")");
						try
						{
							BlockPosHelper.printCoords("Setting pos at:", tileEntityPos);  // TODO:
							tileEntity.setPos(tileEntityPos);
							BlockPosHelper.printCoords("Pos saved at:", tileEntity.getPos());  // TODO:
						}
						catch(Exception e)
						{
							BlockPosHelper.printCoords("Can't set position for tile: " + tileEntity.getClass().getSimpleName(), tileEntityPos);
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
		//World world = event.getWorld();
		//int dimension = CustomTileEntityHandler.INSTANCE.getDimensionFromTickables(world);	
		for(Integer i : DimensionManager.getIDs())
		{
			int dimension = i.intValue();
			World world = DimensionManager.getWorld(dimension);
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
				System.out.println("Vanilla Magic TileEntities saved for Dimension: " + dimension);
			}
			catch(Exception e)
			{
				System.out.println("Error while saving Vanilla Magic TileEntities for World with id: " + dimension);
				if(fileTiles.exists())
				{
					try
					{
						fileTiles.delete();
					}
					catch(Exception ex)
					{
					}
				}
			}
		}
	}
	
	public static File getVanillaMagicRootDirectory()
	{
		return new File(DimensionManager.getCurrentSaveRootDirectory(), VM_DIRECTORY + "/");
	}
}