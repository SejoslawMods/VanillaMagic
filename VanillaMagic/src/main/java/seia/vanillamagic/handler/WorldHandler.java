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
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.tileentity.blockabsorber.TileBlockAbsorber;
import seia.vanillamagic.tileentity.inventorybridge.TileInventoryBridge;
import seia.vanillamagic.tileentity.machine.farm.QuestMachineFarm.FarmRadiusReader;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;
import seia.vanillamagic.tileentity.machine.quarry.TileQuarry;
import seia.vanillamagic.tileentity.speedy.TileSpeedy;
import seia.vanillamagic.util.BlockPosHelper;
import seia.vanillamagic.util.NBTHelper;
import seia.vanillamagic.util.WorldHelper;

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
		VanillaMagic.LOGGER.log(Level.INFO, "WorldHandler registered");
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
					FileInputStream fis = new FileInputStream(dimFile);
					NBTTagCompound data = null;
					try
					{
						data = CompressedStreamTools.readCompressed(fis);
					}
					catch(EOFException eof)
					{
						VanillaMagic.LOGGER.log(Level.ERROR, 
								"[World Load] Error while reading CustomTileEntities data from file for Dimension: " + dimension);
						return;
					}
					fis.close();
					NBTTagList tagList = data.getTagList(TILES, 10);
					boolean canAdd = true;
					for(int i = 0; i < tagList.tagCount(); i++)
					{
						NBTTagCompound tileEntityTag = tagList.getCompoundTagAt(i);
						String tileEntityClassName = tileEntityTag.getString("id");
						int tileEntityPosX = tileEntityTag.getInteger("x");
						int tileEntityPosY = tileEntityTag.getInteger("y");
						int tileEntityPosZ = tileEntityTag.getInteger("z");
						BlockPos tileEntityPos = new BlockPos(tileEntityPosX, tileEntityPosY, tileEntityPosZ);
						ICustomTileEntity tileEntity = null;
						try
						{
							tileEntity = (ICustomTileEntity) Class.forName(tileEntityClassName).newInstance();
							tileEntity.init(world, tileEntityPos);
							tileEntity.getTileEntity().func_190200_a(world, tileEntityTag);
							// Additional parameters for different CustomTileEntities
							if(tileEntity instanceof TileBlockAbsorber)
							{
								// TODO: Currently Disabled
//								canAdd = false;
							}
							else if(tileEntity instanceof TileInventoryBridge)
							{
								// TODO: Currently Disabled
								canAdd = false;
							}
							else if(tileEntity instanceof TileFarm)
							{
								TileFarm tileFarm = (TileFarm) tileEntity;
								tileFarm.radius = FarmRadiusReader.getRadius();
							}
							else if(tileEntity instanceof TileQuarry)
							{
								TileQuarry tileQuarry = (TileQuarry) tileEntity;
								if(!tileQuarry.checkSurroundings())
								{
									canAdd = false;
								}
							}
							else if(tileEntity instanceof TileSpeedy)
							{
								TileSpeedy speedy = (TileSpeedy) tileEntity;
								if(!speedy.containsCrystal())
								{
									canAdd = false;
								}
							}
						}
						catch(Exception e)
						{
							VanillaMagic.LOGGER.log(Level.ERROR, 
									"[World Load] Error while reading class for CustomTileEntity: " + tileEntityClassName);
						}
						VanillaMagic.LOGGER.log(Level.INFO, 
								"[World Load] Created CustomTileEntity (" + tileEntity.getClass().getSimpleName() + ")");
						if(tileEntity != null)
						{
							if(canAdd)
							{
								if(!BlockPosHelper.isSameBlockPos(tileEntityPos, CustomTileEntityHandler.EMPTY_SPACE))
								{
									NBTHelper.readFromINBTSerializable(tileEntity, tileEntityTag);
									CustomTileEntityHandler.addCustomTileEntity(tileEntity, dimension);
								}
							}
							canAdd = true;
						}
					}
					CustomTileEntityHandler.removeCustomTileEntityAtPos(world, CustomTileEntityHandler.EMPTY_SPACE);
				}
			}
		}
		catch(Exception e)
		{
			VanillaMagic.LOGGER.log(Level.INFO, "[World Save] Error while loading CustomTileEntities for Dimension: " + dimension);
			e.printStackTrace();
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
			List<ICustomTileEntity> tickables = CustomTileEntityHandler.getCustomEntitiesInDimension(dimension);
			for(int j = 0; j < tickables.size(); j++)
			{
				dataList.appendTag(tickables.get(j).getTileEntity().writeToNBT(new NBTTagCompound()));
			}
			data.setTag(TILES, dataList);
			FileOutputStream fileOutputStream = new FileOutputStream(fileTiles);
			CompressedStreamTools.writeCompressed(data, fileOutputStream);
			fileOutputStream.close();
			VanillaMagic.LOGGER.log(Level.INFO, "[World Save] VanillaMagic CustomTileEntities saved for Dimension: " + dimension);
		}
		catch(Exception e)
		{
			VanillaMagic.LOGGER.log(Level.INFO, "[World Save] Error while saving CustomTileEntities for Dimension: " + dimension);
			e.printStackTrace();
		}
	}

	public static File getVanillaMagicRootDirectory()
	{
		File file = new File(DimensionManager.getCurrentSaveRootDirectory(), VM_DIRECTORY + "/");
		if(!file.exists())
		{
			file.mkdirs();
		}
		return file;
	}
}