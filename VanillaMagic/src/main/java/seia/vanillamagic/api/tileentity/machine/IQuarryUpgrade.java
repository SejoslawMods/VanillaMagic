package seia.vanillamagic.api.tileentity.machine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Every method except getBlock() is fired once a tick. <br>
 * <br>
 * To create a working Quarry upgrade You must: <br>
 * 1 - Create a class which implements this interface. <br>
 * 2 - Register (in FMLInitializationEvent phase) Your class using QuarryUpgradeAPI.addUpgrade(YourClass.class); <br>
 * 3 - Place a block which You declared in getBlock() method next to the Quarry. <br>
 * If You want to check if Quarry can see Your upgrade, right-click Quarry with Clock and look at "Upgrades" section. <br>
 * Scroll down for an example.
 */
public interface IQuarryUpgrade
{
	/**
	 * Readable upgrade name like: "My upgrade" or "Silk-Touch".
	 */
	public String getUpgradeName();
	
	/**
	 * Returns the Block to which this upgrade is connected. <br>
	 * <br>
	 * Each upgrade MUST HAVE a different block !!!
	 */
	public Block getBlock();
	
	/**
	 * Returns the list of the stacks which will be dropped from the given "blockToDig". <br>
	 * Here is where You should do Your stuff like silk-touch, fortune, etc.
	 */
	default public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, 
			BlockPos workingPos, IBlockState workingPosState)
	{
		return new ArrayList<ItemStack>();
	}
	
	/**
	 * Here is where You should modify the Quarry itself.
	 */
	default public void modifyQuarry(IQuarry quarry)
	{
	}
	
	/**
	 * TODO: Quarry Required Upgrade W.I.P. <br><br>
	 * Returns the upgrade that must be placed BEFORE this upgrade is placed. (talking about blocks) <br>
	 * For instance: block with fortune 1 must be placed before block with fortune 2. <br>
	 * If null than this will be skipped. <br>
	 */
	default public Class<? extends IQuarryUpgrade> requiredUpgrade()
	{
		return null;
	}
}
/*

=============================================================================
For instance:
=============================================================================

@Mod(...)
public class MyMod
{
	...
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		...
		MinecraftForge.EVENT_BUS.register(new DoSomethingWhenMyUpgradeIsRegistered()); // Register Your Event connected with Your upgrade
		...
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		...
		try 
		{
			...
			QuarryUpgradeAPI.addUpgrade(MyNewUpgrade.class); // Register Your new Upgrade
			...
		} 
		// This Exception will be thrown if this upgrade is already registered OR other upgrade with the same block is already registered.
		catch(MappingExistsException e)
		{
			e.printStackTrace();
		}
		...
	}
	...
}
=============================================================================

public class MyNewUpgrade implements IQuarryUpgrade
{
	...
	public String getUpgradeName() 
	{
		return "My New Upgrade Full Name";
	}
	
	public Block getBlock() 
	{
		return MyBlocks.MY_BLOCK; // This should be an block used for Your upgrade. For instance: Blocks.DIRT.
	}
	...
}
=============================================================================

public class DoSomethingWhenMyUpgradeIsRegistered
{
	...
	@SubscribeEvent
	public void addQuarryTileBattery(EventQuarry.AddUpgrade event) // Event which is fired when Quarry check block if it is an upgrade
	{
		IQuarryUpgrade myUpgrade = QuarryUpgradeAPI.getUpgradeFromBlock(MyBlocks.MY_BLOCK); // Get upgrade for Your block.
		IQuarryUpgrade eventUpgrade = event.getUpgrade(); // Get upgrade from event
		if(QuarryUpgradeAPI.isTheSameUpgrade(powerUpgrade, eventUpgrade)) // Check if Quarry is adding my upgrade.
		{
			// If it reached here it means that this is Your upgrade and here should be stuff which should happen when Your upgrade is added.
			System.out.println("My upgrade was added");
		}
	}
	...
}

 */