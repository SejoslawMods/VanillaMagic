package seia.vanillamagic.quest.mobspawnerdrop;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Registry which store all data related with Mob Spawner.
 */
public class MobSpawnerRegistry 
{
	// NBT Tags
	public static final String NBT_MOB_SPAWNER_DATA = "NBT_MOB_SPAWNER_DATA";
	public static final String NBT_ENTITY_CLASS = "NBT_ENTITY_CLASS";
	public static final String NBT_ID = "NBT_ID";
	
	/**
	 * Key: Entity name
	 * Value: ItemStack (Enchanted Book) with data written (Entity class, Entity name)
	 */
	private static final Map<String, ItemStack> _MAP_NAME_STACK = new HashMap<>();
	/**
	 * Key: ResourceLocation connected with each Entity
	 * Value: Entity name
	 */
	private static final Map<ResourceLocation, String> _MAP_RESOURCE_LOCATION_NAME = new HashMap<>();
	/**
	 * Key: Entity name
	 * Value: ResourceLocation connected with each Entity
	 */
	private static final Map<String, ResourceLocation> _MAP_NAME_RESOURCE_LOCATION = new HashMap<>();
	
	private MobSpawnerRegistry()
	{
	}
	
	/**
	 * PostInitialization stage.
	 * Register all Entities for Mob Spawner.
	 */
	public static void postInit()
	{
		VanillaMagic.LOGGER.log(Level.INFO, "Started Mob Spawner Registry...");
		for (EntityEntry ee : ForgeRegistries.ENTITIES)
		{
			String entityName = ee.getName();
			
			ItemStack entryStack = new ItemStack(Items.ENCHANTED_BOOK);
			entryStack.setStackDisplayName("Mob Spawner Entity Data: " + entityName);
			entryStack.getTagCompound().setString(NBT_ENTITY_CLASS, ee.getEntityClass().getName());
			entryStack.getTagCompound().setString(NBT_ID, entityName);
			entryStack.getTagCompound().setString(NBT_MOB_SPAWNER_DATA, NBT_MOB_SPAWNER_DATA);
			
			_MAP_NAME_STACK.put(entityName, entryStack);
			_MAP_RESOURCE_LOCATION_NAME.put(EntityList.getKey(ee.getEntityClass()), entityName);
			_MAP_NAME_RESOURCE_LOCATION.put(entityName, EntityList.getKey(ee.getEntityClass()));
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Mob Spawner Registry: registered " + _MAP_NAME_STACK.size() + " entities for Mob Spawner.");
	}
	
	/**
	 * @return Returns ResourceLocation of Entity from given MobSpawner.
	 */
	public static ResourceLocation getEntityId(TileEntityMobSpawner tileMobSpawner)
	{
		return getEntityId(tileMobSpawner.getSpawnerBaseLogic());
	}
	
	/**
	 * @return Returns ResourceLocation of Entity from given MobSpawner.
	 */
	public static ResourceLocation getEntityId(MobSpawnerBaseLogic spawnerBaseLogic)
	{
		NBTTagCompound tag = spawnerBaseLogic.writeToNBT(new NBTTagCompound());
		String id = (((NBTTagCompound) tag.getTag("SpawnData"))).getString("id");
		return new ResourceLocation(id);
	}
	
	/**
	 * @return Returns ItemStack with Entity info.
	 */
	public static ItemStack getStackFromTile(TileEntityMobSpawner tileMobSpawner)
	{
		String name = _MAP_RESOURCE_LOCATION_NAME.get(getEntityId(tileMobSpawner));
		return getStackFromName(name);
	}
	
	/**
	 * Can't do like so -> return Map.get(name);<br>
	 * Map build in method can return null and we don't want null.<br>
	 * In case of null we want it to return ItemStackUtil.NULL_STACK.<br>
	 * Like below.
	 * 
	 * @param name
	 * 
	 * @return Returns the ItemStack with Entity info.
	 */
	public static ItemStack getStackFromName(String name) 
	{
		for (Entry<String, ItemStack> entry : _MAP_NAME_STACK.entrySet())
		{
			String entryName = entry.getKey();
			if (entryName.equals(name)) return entry.getValue().copy();
		}
		return ItemStackUtil.NULL_STACK;
	}
	
	/**
	 * @return Returns ResourceLocation of Entity from given MobSpawner.
	 */
	public static String getNameFromBaseLogic(MobSpawnerBaseLogic spawnerBaseLogic) 
	{
		return _MAP_RESOURCE_LOCATION_NAME.get(getEntityId(spawnerBaseLogic));
	}
	
	/**
	 * Fill list with ItemStacks which contains Entity info.
	 */
	public static NonNullList<ItemStack> fillList(NonNullList<ItemStack> list) 
	{
		for (Entry<String, ItemStack> entry : _MAP_NAME_STACK.entrySet())
		{
			ItemStack bookWithData = entry.getValue();
			list.add(bookWithData);
		}
		return list;
	}
	
	/**
	 * Change MobSpawner mob ID.
	 */
	public static void setID(TileEntityMobSpawner tileMS, String id, World world, BlockPos spawnerPos)
	{
		MobSpawnerBaseLogic msLogic = ((TileEntityMobSpawner)tileMS).getSpawnerBaseLogic();
		msLogic.setEntityId(_MAP_NAME_RESOURCE_LOCATION.get(id));
		tileMS.markDirty();
		IBlockState spawnerState = world.getBlockState(spawnerPos);
		world.notifyBlockUpdate(spawnerPos, spawnerState, spawnerState, 3);
	}
}