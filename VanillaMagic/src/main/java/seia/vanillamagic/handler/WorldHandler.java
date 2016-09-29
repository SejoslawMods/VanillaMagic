package seia.vanillamagic.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Level;

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
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.BlockPosHelper;
import seia.vanillamagic.util.NBTHelper;
import seia.vanillamagic.util.WorldHelper;

/**
 * TODO: Fix Saving / Loading CustomTileEntities, Maybe remade to WorldSpecificSaveHandler ???
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
		VanillaMagic.logger.log(Level.INFO, "WorldHandler registered");
	}
	
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event)
	{
		File vmDirectory = getVanillaMagicRootDirectory();
		if(!vmDirectory.exists())
		{
			return;
		}
		World world = event.getWorld();
		int dimension = WorldHelper.getDimensionID(world);
		File folderDimension = new File(vmDirectory, String.valueOf(dimension) + "/");
		if(!folderDimension.exists())
		{
			return;
		}
		File[] dimFiles = folderDimension.listFiles();
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
						CustomTileEntity tileEntity = null;
						try
						{
							tileEntity = (CustomTileEntity) Class.forName(tileEntityClassName).newInstance();
							tileEntity.init(world, tileEntityPos);
						}
						catch(Exception e)
						{
							VanillaMagic.logger.log(Level.ERROR, "Error while reading class for CustomTileEntity");
						}
						VanillaMagic.logger.log(Level.INFO, "[World Load] Created TileEntity (" + tileEntity.getClass().getSimpleName() + ")");
						try
						{
							tileEntity.setPos(tileEntityPos);
							BlockPosHelper.printCoords(Level.INFO, "[World Load] Pos saved at:", tileEntity.getPos());
						}
						catch(Exception e)
						{
							BlockPosHelper.printCoords(Level.WARN, "[World Load] Can't set position for tile: " + tileEntity.getClass().getSimpleName(), tileEntityPos);
						}
						if(tileEntity != null)
						{
							NBTHelper.readFromINBTSerializable(tileEntity, tileEntityTag);
							//CustomTileEntityHandler.INSTANCE.addCustomTileEntity(tileEntity, dimension);
							CustomTileEntityHandler.INSTANCE.addReadedTile(tileEntity, dimension);
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
		int dimension = WorldHelper.getDimensionID(event);
		File folderDimension = new File(vmDirectory, String.valueOf(dimension) + "/");
		if(!folderDimension.exists())
		{
			folderDimension.mkdirs();
		}
		File fileTiles = new File(folderDimension, FILE_NAME_TILES);
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
		File fileTilesOld = new File(folderDimension, FILE_NAME_TILES_OLD);
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
			CustomTileEntityHandler.INSTANCE.moveTilesFromReadded(dimension);
			List<TileEntity> tickables = CustomTileEntityHandler.INSTANCE.getCustomEntitiesInDimension(dimension);
			for(int j = 0; j < tickables.size(); j++)
			{
				dataList.appendTag(tickables.get(j).writeToNBT(new NBTTagCompound()));
			}
			data.setTag(TILES, dataList);
			FileOutputStream fileOutputStream = new FileOutputStream(fileTiles);
			CompressedStreamTools.writeCompressed(data, fileOutputStream);
			fileOutputStream.close();
			VanillaMagic.logger.log(Level.INFO, "[World Save] Vanilla Magic TileEntities saved for Dimension: " + dimension);
		}
		catch(Exception e)
		{
		}
	}

	public static File getVanillaMagicRootDirectory()
	{
		return new File(DimensionManager.getCurrentSaveRootDirectory(), VM_DIRECTORY + "/");
	}
}