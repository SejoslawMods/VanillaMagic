package seia.vanillamagic.api.tileentity.machine;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import net.minecraft.block.Block;

public class QuarryUpgradeAPI 
{
	private QuarryUpgradeAPI()
	{
	}
	
	/**
	 * Main method to register new IQuarryUpgrade. <br>
	 * Returns TRUE if the upgrade was successfully registered.
	 */
	public static boolean addUpgrade(Class<? extends IQuarryUpgrade> quarryUpgradeClass)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry");
			Method method = clazz.getMethod("addUpgrade", IQuarryUpgrade.class);
			Boolean b = (Boolean) method.invoke(null, quarryUpgradeClass);
			return b.booleanValue();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns TRUE if the given block is an Block connected to any IQuarryUpgrade
	 */
	public static boolean isUpgradeBlock(Block block)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry");
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
	
	@Nullable
	public static IQuarryUpgrade getUpgradeFromBlock(Block block)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry");
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
	 * Returns the required upgrade for the given upgrade.
	 */
	@Nullable
	public static IQuarryUpgrade getReguiredUpgrade(IQuarryUpgrade iqu) 
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry");
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
	 * Returns TRUE if the given upgrades are the same upgrade.
	 */
	public static boolean isTheSameUpgrade(IQuarryUpgrade iqu1, IQuarryUpgrade iqu2)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry");
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
}