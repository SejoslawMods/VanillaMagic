package seia.vanillamagic.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.io.Files;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.chunkloader.TileChunkLoader;
import seia.vanillamagic.machine.farm.TileFarm;
import seia.vanillamagic.machine.quarry.TileQuarry;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.NBTHelper;

public class WorldHandler 
{
	public static final WorldHandler INSTANCE = new WorldHandler();
	
	private String VM_DIRECTORY = "VanillaMagic";
	private String FILE_NAME_TILES = "VanillaMagicTileEntities.dat";
	private String FILE_NAME_TILES_OLD = "VanillaMagicTileEntities.dat_old";
	
	private String TILE_QUARRY_PREFIX = "quarry";
	private String TILE_CHUNKLOADER_PREFIX = "chunkLoader";
	private String TILE_FARM_PREFIX = "farm";
	
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
		File worldDirectory = DimensionManager.getCurrentSaveRootDirectory();
		File vmDirectory = new File(worldDirectory, VM_DIRECTORY + "/");
		if(!vmDirectory.exists())
		{
			vmDirectory.mkdirs();
		}
		File[] dimDirs = vmDirectory.listFiles();
		try
		{
			for(File dimDir : dimDirs)
			{
				System.out.println(dimDir.getName()); // TODO:
				try
				{
					File[] dimFiles = dimDir.listFiles();
					for(File dimFile : dimFiles)
					{
						String fileExtension = Files.getFileExtension(dimFile.getAbsolutePath());
						if(fileExtension.equals("dat"))
						{
							System.out.println(dimFile.getName()); // TODO:
							FileInputStream fileInputStream = new FileInputStream(dimFile);
							NBTTagCompound data = CompressedStreamTools.readCompressed(fileInputStream);
							fileInputStream.close();
							int dimID = Integer.parseInt(dimDir.getName());
							World world = DimensionManager.getWorld(dimID);
							Set<String> dataTileName = data.getKeySet();
							if(dataTileName.size() > 0)
							{
								Iterator<String> dataTileNameIterator = dataTileName.iterator();
								String s = "";
								while(dataTileNameIterator.hasNext());
								{
									s = dataTileNameIterator.next();
									NBTTagCompound machineTag = data.getCompoundTag(s);
									TileEntity tileMachine = NBTHelper.getTileEntityFromNBT(world, machineTag);
									if(tileMachine != null)
									{
										if(world.getTileEntity(tileMachine.getPos()) == null)
										{
											world.addTileEntity(tileMachine);
											System.out.println("Machine added at pos:");
											BlockPosHelper.printCoords(tileMachine.getPos());
										}
									}
								}
							}
						}
					}
				}
				catch(Exception e)
				{
					System.out.println("Error while loading Vanilla Magic TileEntities for World with id: " + dimDir.getName());
					e.printStackTrace();
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
		File worldDirectory = DimensionManager.getCurrentSaveRootDirectory();
		File vmDirectory = new File(worldDirectory, VM_DIRECTORY + "/");
		if(!vmDirectory.exists())
		{
			vmDirectory.mkdirs();
		}
		Integer[] dimensionIDs = DimensionManager.getIDs();
		for(Integer integerDimID : dimensionIDs)
		{
			int dimID = integerDimID.intValue();
			File dimDir = new File(vmDirectory, String.valueOf(dimID) + "/");
			if(!dimDir.exists())
			{
				dimDir.mkdirs();
			}
			File fileTiles = new File(dimDir, FILE_NAME_TILES);
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
			File fileTilesOld = new File(dimDir, FILE_NAME_TILES_OLD);
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
				int count = 0;
				NBTTagCompound data = new NBTTagCompound();
				List<TileEntity> tickables = DimensionManager.getWorld(dimID).tickableTileEntities;
				List<TileEntity> saved = new ArrayList<TileEntity>();
				for(int j = 0; j < tickables.size(); j++)
				{
					if(tickables.get(j) instanceof TileQuarry)
					{
						TileQuarry tile = (TileQuarry) tickables.get(j);
						data.setTag(TILE_QUARRY_PREFIX + "_" + count, tile.writeToNBT(new NBTTagCompound()));
						count++;
					}
					else if(tickables.get(j) instanceof TileChunkLoader)
					{
						TileChunkLoader tile = (TileChunkLoader) tickables.get(j);
						data.setTag(TILE_CHUNKLOADER_PREFIX + "_" + count, tile.writeToNBT(new NBTTagCompound()));
						count++;
					}
					else if(tickables.get(j) instanceof TileFarm)
					{
						TileFarm tile = (TileFarm) tickables.get(j);
						data.setTag(TILE_FARM_PREFIX + "_" + count, tile.writeToNBT(new NBTTagCompound()));
						count++;
					}
				}
				FileOutputStream fileOutputStream = new FileOutputStream(fileTiles);
				CompressedStreamTools.writeCompressed(data, fileOutputStream);
				fileOutputStream.close();
				//System.out.println("Vanilla Magic TileEntities saved (amount: " + count + ")"); // TODO:
			}
			catch(Exception e)
			{
				System.out.println("Error while saving Vanilla Magic TileEntities for World with id: " + dimID);
				e.printStackTrace();
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
}