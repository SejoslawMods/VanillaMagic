package seia.vanillamagic.handler;

import java.io.EOFException;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.tileentity.machine.quarry.TileQuarry;
import seia.vanillamagic.tileentity.speedy.TileSpeedy;
import seia.vanillamagic.util.BlockPosUtil;
import seia.vanillamagic.util.NBTUtil;
import seia.vanillamagic.util.WorldUtil;

/**
 * Class which is responsible for loading / saving CustomTileEntities.
 */
public class WorldHandler
{
	public static final WorldHandler INSTANCE = new WorldHandler();
	
	private static String _VM_DIRECTORY = "VanillaMagic";
	private static String _FILE_NAME_TILES = "VanillaMagicTileEntities.dat";
	private static String _FILE_NAME_TILES_OLD = "VanillaMagicTileEntities.dat_old";
	private static String _TILES = "tiles";
	
	private WorldHandler()
	{
	}
	
	/**
	 * PreInitialization stage. Register Events from this class.
	 */
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
		VanillaMagic.LOGGER.log(Level.INFO, "WorldHandler registered");
	}
	
	/**
	 * Show additional console info if  enabled in config,
	 */
	public void log(Level level, String message)
	{
		if (VMConfig.SHOW_CUSTOM_TILE_ENTITY_SAVING) VanillaMagic.LOGGER.log(level, message);
	}
	
	/**
	 * Event which loads all CustomTileEntities to for World.
	 */
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event)
	{
		File vmDirectory = getVanillaMagicRootDirectory();
		if (!vmDirectory.exists()) return;
		
		World world = event.getWorld();
		int dimension = WorldUtil.getDimensionID(world);
		File folderDimension = new File(vmDirectory, String.valueOf(dimension) + "/");
		if (!folderDimension.exists()) return;
		
		File[] dimFiles = folderDimension.listFiles();
		try
		{
			for (File dimFile : dimFiles)
			{
				String fileExtension = Files.getFileExtension(dimFile.getAbsolutePath());
				if (fileExtension.equals("dat"))
				{
					FileInputStream fis = new FileInputStream(dimFile);
					NBTTagCompound data = null;
					try
					{
						data = CompressedStreamTools.readCompressed(fis);
					}
					catch (EOFException eof)
					{
						log(Level.ERROR, "[World Load] Error while reading CustomTileEntities data from file for Dimension: " + dimension);
						return;
					}
					fis.close();
					NBTTagList tagList = data.getTagList(_TILES, 10);
					boolean canAdd = true;
					for (int i = 0; i < tagList.tagCount(); ++i)
					{
						NBTTagCompound tileEntityTag = tagList.getCompoundTagAt(i);
						//String tileEntityClassName = tileEntityTag.getString("id"); // This must be the class -> class.getName();
						String tileEntityClassName = tileEntityTag.getString(NBTUtil.NBT_CLASS);
						int tileEntityPosX = tileEntityTag.getInteger("x");
						int tileEntityPosY = tileEntityTag.getInteger("y");
						int tileEntityPosZ = tileEntityTag.getInteger("z");
						BlockPos tileEntityPos = new BlockPos(tileEntityPosX, tileEntityPosY, tileEntityPosZ);
						ICustomTileEntity tileEntity = null;
						try
						{
							tileEntity = (ICustomTileEntity) Class.forName(tileEntityClassName).newInstance();
							tileEntity.getTileEntity().setWorld(world);
							tileEntity.getTileEntity().setPos(tileEntityPos);
							tileEntity.getTileEntity().create(world, tileEntityTag);
							
							// Additional parameters for different CustomTileEntities (only if  MUST be)
							if (tileEntity instanceof TileQuarry)
							{
								TileQuarry tileQuarry = (TileQuarry) tileEntity;
								if (!tileQuarry.checkSurroundings()) canAdd = false;
							}
							else if (tileEntity instanceof TileSpeedy)
							{
								TileSpeedy speedy = (TileSpeedy) tileEntity;
								if (!speedy.containsCrystal()) canAdd = false;
							}
							log(Level.INFO, "[World Load] Created CustomTileEntity (" + tileEntity.getClass().getSimpleName() + ")");
						}
						catch (Exception e)
						{
							log(Level.ERROR, "[World Load] Error while reading class for CustomTileEntity: " + tileEntityClassName);
						}
						if (tileEntity != null)
						{
							if (canAdd)
							{
								if (!BlockPosUtil.isSameBlockPos(tileEntityPos, CustomTileEntityHandler.EMPTY_SPACE))
								{
									NBTUtil.readFromINBTSerializable(tileEntity, tileEntityTag);
									CustomTileEntityHandler.addCustomTileEntity(tileEntity, dimension);
								}
							}
							canAdd = true;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			log(Level.INFO, "[World Load] Error while loading CustomTileEntities for Dimension: " + dimension);
			e.printStackTrace();
		}
	}
	
	/**
	 * Event which saves all CustomTileEntities for given World.
	 */
	@SubscribeEvent
	public void worldSave(WorldEvent.Save event)
	{
		File vmDirectory = getVanillaMagicRootDirectory();
		if (!vmDirectory.exists()) vmDirectory.mkdirs();
		
		int dimension = WorldUtil.getDimensionID(event);
		File folderDimension = new File(vmDirectory, String.valueOf(dimension) + "/");
		if (!folderDimension.exists()) folderDimension.mkdirs();
		
		File fileTiles = new File(folderDimension, _FILE_NAME_TILES);
		if (!fileTiles.exists())
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
		File fileTilesOld = new File(folderDimension, _FILE_NAME_TILES_OLD);
		if (!fileTilesOld.exists())
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
			catch (Exception e)
			{
			}
			NBTTagCompound data = new NBTTagCompound();
			NBTTagList dataList = new NBTTagList();
			List<ICustomTileEntity> tickables = CustomTileEntityHandler.getCustomEntitiesInDimension(dimension);
			for (int j = 0; j < tickables.size(); ++j) 
				dataList.appendTag(tickables.get(j).getTileEntity().writeToNBT(new NBTTagCompound()));
			
			data.setTag(_TILES, dataList);
			FileOutputStream fileOutputStream = new FileOutputStream(fileTiles);
			CompressedStreamTools.writeCompressed(data, fileOutputStream);
			fileOutputStream.close();
			log(Level.INFO, "[World Save] CustomTileEntities saved for Dimension: " + dimension);
		}
		catch (Exception e)
		{
			log(Level.INFO, "[World Save] Error while saving CustomTileEntities for Dimension: " + dimension);
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns VM root directory for CustomTileEntities.
	 */
	public static File getVanillaMagicRootDirectory()
	{
		File file = new File(DimensionManager.getCurrentSaveRootDirectory(), _VM_DIRECTORY + "/");
		if (!file.exists()) file.mkdirs();
		return file;
	}
}