package seia.vanillamagic.tileentity.machine.quarry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import seia.vanillamagic.api.tileentity.machine.IQuarry;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgrade;
import seia.vanillamagic.util.ListHelper;

/**
 * Each Quarry has it's own helper to handler the upgrades.
 */
public class QuarryUpgradeHelper
{
	private final IQuarry quarry;
	private List<IQuarryUpgrade> upgrades = new ArrayList<IQuarryUpgrade>();
	
	public QuarryUpgradeHelper(IQuarry quarry)
	{
		this.quarry = quarry;
	}

	/**
	 * Add the {@link IQuarryUpgrade} to the list of upgrades.
	 */
	public void addUpgradeFromBlock(Block block)
	{
		IQuarryUpgrade iqu = QuarryUpgradeRegistry.getUpgradeFromBlock(block);
		if(!hasRegisteredRequireUpgrade(iqu))
		{
			return;
		}
		if(canAddUpgrade(iqu))
		{
			upgrades.add(iqu);
		}
	}

	/**
	 * Returns TRUE if the required upgrade for the given upgrade is registered.
	 */
	private boolean hasRegisteredRequireUpgrade(IQuarryUpgrade iqu) 
	{
		if(iqu.requiredUpgrade() == null)
		{
			return true;
		}
		IQuarryUpgrade required = null;
		for(IQuarryUpgrade registeredUpgrade : upgrades)
		{
			required = QuarryUpgradeRegistry.getReguiredUpgrade(iqu);
			if(QuarryUpgradeRegistry.isTheSameUpgrade(registeredUpgrade, required))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * If the given upgrade is already on the list, return FALSE. <br>
	 * Otherwise return TRUE.
	 */
	private boolean canAddUpgrade(IQuarryUpgrade iqu) 
	{
		for(IQuarryUpgrade up : upgrades)
		{
			if(Block.isEqualTo(up.getBlock(), iqu.getBlock()))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the drops list from all upgrades.
	 */
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos,IBlockState workingPosState) 
	{
		List<ItemStack> drops = new ArrayList<ItemStack>();
		for(IQuarryUpgrade upgrade : upgrades)
		{
			//drops.addAll(upgrade.getDrops(blockToDig, world, workingPos, workingPosState));
			drops = ListHelper.<ItemStack>combineLists(drops, upgrade.getDrops(blockToDig, world, workingPos, workingPosState));
		}
		// If there is no upgrades mine the old-fashion way.
		if(drops.isEmpty())
		{
			return blockToDig.getDrops(world, workingPos, workingPosState, 0);
		}
		return drops;
	}

	/**
	 * Clears the upgrade list.
	 */
	public void clearUpgrades() 
	{
		upgrades.clear();
	}

	/**
	 * This method will perform upgrades once a tick.
	 */
	public void modifyQuarry(IQuarry quarry)
	{
		for(IQuarryUpgrade upgrade : upgrades)
		{
			upgrade.modifyQuarry(quarry);
		}
	}

	/**
	 * Method in which all upgrades info will be added.
	 */
	public List<String> addAdditionalInfo(List<String> baseInfo)
	{
		baseInfo.add("Upgrades:");
		for(int i = 0; i < upgrades.size(); i++)
		{
			baseInfo.add("   " + (i+1) + ") " + upgrades.get(i).getUpgradeName());
		}
		return baseInfo;
	}
}