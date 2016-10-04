package seia.vanillamagic.machine.quarry.upgrade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import seia.vanillamagic.machine.quarry.IQuarry;

/**
 * Every method except getBlock() is fired once a tick. <br>
 * <br>
 * To create a working Quarry upgrade You must: <br>
 * 1 - Create a class which implements this interface. <br>
 * 2 - Register Your class in QuarryUpgradeRegistry using QuarryUpgradeRegistry.addUpgrade(YourClass.class); <br>
 * 3 - Place a block which You declared in getBlock() method next to the Quarry. <br>
 * If You want to check if Quarry can see Your upgrade, right-click Quarry with Clock and look at "Upgrades:" section.
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
	default public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState)
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
	 * Returns the upgrade that must be placed BEFORE this upgrade is placed. (talking about blocks) <br>
	 * For instance: block with fortune 1 must be placed before block with fortune 2. <br>
	 * If null than this will be skipped.
	 */
	default public Class<? extends IQuarryUpgrade> requiredUpgrade()
	{
		return null;
	}
}