package seia.vanillamagic.machine.quarry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.machine.quarry.upgrade.IQuarryUpgrade;
import seia.vanillamagic.machine.quarry.upgrade.QuarryUpgradeFortune;
import seia.vanillamagic.machine.quarry.upgrade.QuarryUpgradeSilkTouch;

/**
 * This class is used for holding the single instances of the upgrades.
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
		addUpgrade(QuarryUpgradeSilkTouch.class);
		addUpgrade(QuarryUpgradeFortune.One.class);
		addUpgrade(QuarryUpgradeFortune.Two.class);
		addUpgrade(QuarryUpgradeFortune.Three.class);
	}
	
	/**
	 * Main method to register new IQuarryUpgrade. <br>
	 * Returns TRUE if the upgrade was successfully registered.
	 */
	public static boolean addUpgrade(Class<? extends IQuarryUpgrade> quarryUpgradeClass)
	{
		try
		{
			IQuarryUpgrade instance = quarryUpgradeClass.newInstance();
			LIST_BLOCK.add(instance.getBlock());
			LIST_UPGRADE.add(instance);
			MAP_BLOCK_UPGRADE.put(instance.getBlock(), instance);
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
	 * Returns TRUE if the given block is an Block connected to any IQuarryUpgrade
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
	 * Returns the required upgrade for the given upgrade.
	 */
	@Nullable
	public static IQuarryUpgrade getReguiredUpgrade(IQuarryUpgrade iqu) 
	{
		return MAP_CLASS_UPGRADE.get(iqu.requiredUpgrade());
	}

	/**
	 * Returns TRUE if the given upgrades are the same upgrade.
	 */
	public static boolean isTheSameUpgrade(IQuarryUpgrade iqu1, IQuarryUpgrade iqu2)
	{
		return Block.isEqualTo(iqu1.getBlock(), iqu2.getBlock());
	}
}