package seia.vanillamagic.tileentity.machine.quarry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import seia.vanillamagic.api.exception.MappingExistsException;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgrade;
import seia.vanillamagic.api.tileentity.machine.QuarryUpgradeAPI;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.tileentity.machine.quarry.upgrade.QuarryUpgradeAutoInventoryOutputPlacer;
import seia.vanillamagic.tileentity.machine.quarry.upgrade.QuarryUpgradeFortune;
import seia.vanillamagic.tileentity.machine.quarry.upgrade.QuarryUpgradeSilkTouch;

/**
 * This class is used for holding the single instances of the upgrades.<br>
 * This is the main registry for ALL the Quarry upgrades.
 */
public class QuarryUpgradeRegistry
{
	private static List<Block> LIST_BLOCK = new ArrayList<Block>();
	private static List<IQuarryUpgrade> LIST_UPGRADE = new ArrayList<IQuarryUpgrade>();
	private static Map<Block, IQuarryUpgrade> MAP_BLOCK_UPGRADE = new HashMap<Block, IQuarryUpgrade>();
	private static Map<IQuarryUpgrade, Block> MAP_UPGRADE_BLOCK = new HashMap<IQuarryUpgrade, Block>();
	private static Map<Block, Class<? extends IQuarryUpgrade>> MAP_BLOCK_CLASS = new HashMap<Block, Class<? extends IQuarryUpgrade>>();
	private static Map<Class<? extends IQuarryUpgrade>, IQuarryUpgrade> MAP_CLASS_UPGRADE = new HashMap<Class<? extends IQuarryUpgrade>, IQuarryUpgrade>();
	
	private QuarryUpgradeRegistry()
	{
	}
	
	static
	{
		// Register Vanilla Magic Quarry Upgrades
		try
		{
			addUpgrade(QuarryUpgradeSilkTouch.class);
			addUpgrade(QuarryUpgradeFortune.One.class);
			addUpgrade(QuarryUpgradeFortune.Two.class);
			addUpgrade(QuarryUpgradeFortune.Three.class);
			addUpgrade(QuarryUpgradeAutoInventoryOutputPlacer.class);
		}
		catch(MappingExistsException e)
		{
			e.printStackTrace();
			VanillaMagic.LOGGER.log(Level.ERROR, "Error while registering QuarryUpgrade for block: " + e.checkingKey);
		}
	}
	
	/**
	 * @see QuarryUpgradeAPI#addUpgrade(Class)
	 */
	public static boolean addUpgrade(Class<? extends IQuarryUpgrade> quarryUpgradeClass) throws MappingExistsException
	{
		try
		{
			IQuarryUpgrade instance = quarryUpgradeClass.newInstance();
			if(MAP_BLOCK_UPGRADE.get(instance.getBlock()) != null) // there is already mapping for this Block
			{
				throw new MappingExistsException(instance.getBlock(), MAP_BLOCK_UPGRADE.get(instance.getBlock()));
			}
			LIST_BLOCK.add(instance.getBlock());
			LIST_UPGRADE.add(instance);
			MAP_BLOCK_UPGRADE.put(instance.getBlock(), instance);
			MAP_UPGRADE_BLOCK.put(instance, instance.getBlock());
			MAP_BLOCK_CLASS.put(instance.getBlock(), quarryUpgradeClass);
			MAP_CLASS_UPGRADE.put(quarryUpgradeClass, instance);
			return true;
		}
		catch(Exception e)
		{
			VanillaMagic.LOGGER.log(Level.WARN, "Couldn't register Quarry upgrade: " + quarryUpgradeClass.getSimpleName());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @see QuarryUpgradeAPI#isUpgradeBlock(Block)
	 */
	public static boolean isUpgradeBlock(Block block)
	{
		for(Block b : LIST_BLOCK)
		{
			if(Block.isEqualTo(b, block))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @see QuarryUpgradeAPI#getUpgradeFromBlock(Block)
	 */
	@Nullable
	public static IQuarryUpgrade getUpgradeFromBlock(Block block) 
	{
		for(Entry<Block, IQuarryUpgrade> entry : MAP_BLOCK_UPGRADE.entrySet())
		{
			if(Block.isEqualTo(block, entry.getKey()))
			{
				return entry.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Returns the number of currently registered upgrades.
	 */
	public static int countUpgrades()
	{
		return MAP_CLASS_UPGRADE.size();
	}

	/**
	 * @see QuarryUpgradeAPI#getReguiredUpgrade(IQuarryUpgrade)
	 */
	@Nullable
	public static IQuarryUpgrade getReguiredUpgrade(IQuarryUpgrade iqu) 
	{
		return MAP_CLASS_UPGRADE.get(iqu.requiredUpgrade());
	}

	/**
	 * @see QuarryUpgradeAPI#isTheSameUpgrade(IQuarryUpgrade, IQuarryUpgrade)
	 */
	public static boolean isTheSameUpgrade(IQuarryUpgrade iqu1, IQuarryUpgrade iqu2)
	{
		return Block.isEqualTo(iqu1.getBlock(), iqu2.getBlock());
	}
	
	/**
	 * @see QuarryUpgradeAPI#getUpgrades()
	 */
	public static List<IQuarryUpgrade> getUpgrades()
	{
		List<IQuarryUpgrade> upgrades = new ArrayList<IQuarryUpgrade>();
		for(IQuarryUpgrade iqu : MAP_CLASS_UPGRADE.values())
		{
			upgrades.add(iqu);
		}
		return upgrades;
	}
}