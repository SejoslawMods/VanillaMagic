package seia.vanillamagic.api.tileentity.machine;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import seia.vanillamagic.api.exception.MappingExistsException;

/**
 * This is the connection to QuarryUpgrade system.
 */
public class QuarryUpgradeAPI 
{
	/**
	 * QuarryUpgradeRegistry Class
	 */
	private static final String REGISTRY_CLASS = "seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry";
	
	private QuarryUpgradeAPI()
	{
	}
	
	/**
	 * Main method to register new IQuarryUpgrade.
	 * 
	 * @throws MappingExistsException this {@link Exception} will be thrown if there is already registered upgrade with this Block 
	 * (solve: change this Upgrade's Block in "getBlock" method)
	 * 
	 * @return Returns TRUE if the upgrade was successfully registered.
	 */
	public static boolean addUpgrade(Class<? extends IQuarryUpgrade> quarryUpgradeClass) 
			throws MappingExistsException
	{
		try 
		{
			Class<?> clazz = Class.forName(REGISTRY_CLASS);
			Method method = clazz.getMethod("addUpgrade", Class.class);
			Boolean b = (Boolean) method.invoke(null, quarryUpgradeClass);
			return b.booleanValue();
		} 
		catch(ReflectiveOperationException e) 
		{
			e.printStackTrace();
		} 
		catch(RuntimeException e) 
		{
			e.printStackTrace();
		} 
		return false;
	}
	
	/**
	 * @return Returns TRUE if the given block is an Block connected to any IQuarryUpgrade
	 */
	public static boolean isUpgradeBlock(Block block)
	{
		try
		{
			Class<?> clazz = Class.forName(REGISTRY_CLASS);
			Method method = clazz.getMethod("isUpgradeBlock", Block.class);
			Boolean b = (Boolean) method.invoke(null, block);
			return b.booleanValue();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return Returns the QuarryUpgrade from given Block if the Upgrade is registered, otherwise NULL.
	 */
	@Nullable
	public static IQuarryUpgrade getUpgradeFromBlock(Block block)
	{
		try
		{
			Class<?> clazz = Class.forName(REGISTRY_CLASS);
			Method method = clazz.getMethod("getUpgradeFromBlock", Block.class);
			return (IQuarryUpgrade) method.invoke(null, block);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return Returns the required upgrade for the given upgrade.
	 */
	@Nullable
	public static IQuarryUpgrade getReguiredUpgrade(IQuarryUpgrade iqu) 
	{
		try
		{
			Class<?> clazz = Class.forName(REGISTRY_CLASS);
			Method method = clazz.getMethod("getReguiredUpgrade", IQuarryUpgrade.class);
			return (IQuarryUpgrade) method.invoke(null, iqu);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @return Returns TRUE if the given upgrades are the same upgrade.
	 */
	public static boolean isTheSameUpgrade(IQuarryUpgrade iqu1, IQuarryUpgrade iqu2)
	{
		try
		{
			Class<?> clazz = Class.forName(REGISTRY_CLASS);
			Method method = clazz.getMethod("isTheSameUpgrade", IQuarryUpgrade.class, IQuarryUpgrade.class);
			Boolean b = (Boolean) method.invoke(null, iqu1, iqu2);
			return b.booleanValue();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @return Returns the new {@link List}, so changes will no affect the registry.
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public static List<IQuarryUpgrade> getUpgrades()
	{
		try
		{
			Class<?> clazz = Class.forName(REGISTRY_CLASS);
			Method method = clazz.getMethod("getUpgrades");
			return (List<IQuarryUpgrade>) method.invoke(null);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}