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
import seia.vanillamagic.tileentity.machine.quarry.upgrade.QuarryUpgradeAutosmeltDigged;
import seia.vanillamagic.tileentity.machine.quarry.upgrade.QuarryUpgradeFortune;
import seia.vanillamagic.tileentity.machine.quarry.upgrade.QuarryUpgradeSilkTouch;

/**
 * This class is used for holding the single instances of the upgrades.<br>
 * This is the main registry for ALL the Quarry upgrades.
 */
public class QuarryUpgradeRegistry
{
	private static List<Block> _LIST_BLOCK = new ArrayList<>();
	private static Map<Block, IQuarryUpgrade> _MAP_BLOCK_UPGRADE = new HashMap<>();
	private static Map<Class<? extends IQuarryUpgrade>, IQuarryUpgrade> _MAP_CLASS_UPGRADE = new HashMap<>();
	
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
			addUpgrade(QuarryUpgradeAutosmeltDigged.class);
		}
		catch (MappingExistsException e)
		{
			e.printStackTrace();
			VanillaMagic.LOGGER.log(Level.ERROR, "Error while registering QuarryUpgrade for block: " + e.checkingKey);
		}
	}
	
	/**
	 * @see QuarryUpgradeAPI#addUpgrade(Class)
	 */
	public static boolean addUpgrade(Class<? extends IQuarryUpgrade> quarryUpgradeClass) 
			throws MappingExistsException
	{
		try
		{
			IQuarryUpgrade instance = quarryUpgradeClass.newInstance();
			if (_MAP_BLOCK_UPGRADE.get(instance.getBlock()) != null) // there is already mapping for this Block
				throw new MappingExistsException("Mapping already exists: (Block, Upgrade)", instance.getBlock(), _MAP_BLOCK_UPGRADE.get(instance.getBlock()));
			
			_LIST_BLOCK.add(instance.getBlock());
			_MAP_BLOCK_UPGRADE.put(instance.getBlock(), instance);
			_MAP_CLASS_UPGRADE.put(quarryUpgradeClass, instance);
			VanillaMagic.LOGGER.log(Level.INFO, "Registered QuarryUpgrade: " + instance);
			return true;
		}
		catch (Exception e)
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
		for (Block b : _LIST_BLOCK)
			if (Block.isEqualTo(b, block))
				return true;
		return false;
	}

	/**
	 * @see QuarryUpgradeAPI#getUpgradeFromBlock(Block)
	 */
	@Nullable
	public static IQuarryUpgrade getUpgradeFromBlock(Block block) 
	{
		for (Entry<Block, IQuarryUpgrade> entry : _MAP_BLOCK_UPGRADE.entrySet())
			if (Block.isEqualTo(block, entry.getKey()))
				return entry.getValue();
		return null;
	}
	
	/**
	 * Returns the number of currently registered upgrades.
	 */
	public static int countUpgrades()
	{
		return _MAP_CLASS_UPGRADE.size();
	}

	/**
	 * @see QuarryUpgradeAPI#getReguiredUpgrade(IQuarryUpgrade)
	 */
	@Nullable
	public static IQuarryUpgrade getReguiredUpgrade(IQuarryUpgrade iqu) 
	{
		return _MAP_CLASS_UPGRADE.get(iqu.requiredUpgrade());
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
		for (IQuarryUpgrade iqu : _MAP_CLASS_UPGRADE.values()) upgrades.add(iqu);
		return upgrades;
	}
}