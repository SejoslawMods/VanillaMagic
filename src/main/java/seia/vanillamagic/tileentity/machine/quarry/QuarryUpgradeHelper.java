package seia.vanillamagic.tileentity.machine.quarry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventQuarry;
import seia.vanillamagic.api.tileentity.machine.IQuarry;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgrade;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgradeHelper;
import seia.vanillamagic.util.ListUtil;

/**
 * Each Quarry has it's own helper to handler the upgrades.
 */
public class QuarryUpgradeHelper implements IQuarryUpgradeHelper
{
	private final IQuarry _quarry;
	private final List<IQuarryUpgrade> _upgrades = new ArrayList<>();
	private final Map<BlockPos, IQuarryUpgrade> _upgradeOnPos = new HashMap<>();
	
	public QuarryUpgradeHelper(IQuarry quarry)
	{
		this._quarry = quarry;
	}
	
	public IQuarry getQuarry()
	{
		return _quarry;
	}
	
	public List<IQuarryUpgrade> getUpgradesList()
	{
		return _upgrades;
	}
	
	public Map<BlockPos, IQuarryUpgrade> getUpgradesMap()
	{
		return _upgradeOnPos;
	}
	
	public void addUpgradeFromBlock(Block block, BlockPos upgradePos)
	{
		IQuarryUpgrade iqu = QuarryUpgradeRegistry.getUpgradeFromBlock(block);
		if (!hasRegisteredRequireUpgrade(iqu)) return;
		
		if (canAddUpgrade(iqu))
		{
			_upgrades.add(iqu);
			_upgradeOnPos.put(upgradePos, iqu);
			MinecraftForge.EVENT_BUS.post(new EventQuarry.AddUpgrade(_quarry, _quarry.getTileEntity().getWorld(), _quarry.getMachinePos(), iqu, upgradePos));
		}
	}

	/**
	 * Returns TRUE if  the required upgrade for the given upgrade is registered.
	 */
	private boolean hasRegisteredRequireUpgrade(IQuarryUpgrade iqu) 
	{
		if (iqu.requiredUpgrade() == null) return true;
		
		IQuarryUpgrade required = null;
		for (IQuarryUpgrade registeredUpgrade : _upgrades)
		{
			required = QuarryUpgradeRegistry.getReguiredUpgrade(iqu);
			if (QuarryUpgradeRegistry.isTheSameUpgrade(registeredUpgrade, required)) return true;
		}
		return false;
	}

	/**
	 * if  the given upgrade is already on the list, return FALSE. <br>
	 * Otherwise return TRUE.
	 */
	private boolean canAddUpgrade(IQuarryUpgrade iqu) 
	{
		for (IQuarryUpgrade up : _upgrades)
			if (Block.isEqualTo(up.getBlock(), iqu.getBlock()))
				return false;
		return true;
	}
	
	@Nullable
	public BlockPos getUpgradePos(IQuarryUpgrade upgrade)
	{
		for (Entry<BlockPos, IQuarryUpgrade> entry : _upgradeOnPos.entrySet())
		{
			IQuarryUpgrade iqu = entry.getValue();
			if (QuarryUpgradeRegistry.isTheSameUpgrade(upgrade, iqu))
				return entry.getKey();
		}
		return null;
	}
	
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos,IBlockState workingPosState)
	{
		List<ItemStack> drops = new ArrayList<ItemStack>();
		for (IQuarryUpgrade upgrade : _upgrades)
			drops = ListUtil.<ItemStack>combineLists(drops, upgrade.getDrops(blockToDig, world, workingPos, workingPosState));
		
		// if  there is no upgrades mine the old-fashion way.
		if (drops.isEmpty()) 
			drops = ListUtil.<ItemStack>combineLists(drops, blockToDig.getDrops(world, workingPos, workingPosState, 0));
		return drops;
	}
	
	public void clearUpgrades() 
	{
		_upgrades.clear();
		_upgradeOnPos.clear();
	}
	
	public void modifyQuarry(IQuarry quarry)
	{
		for (Entry<BlockPos, IQuarryUpgrade> entry : _upgradeOnPos.entrySet())
		{
			IQuarryUpgrade upgrade = entry.getValue();
			BlockPos upgradePos = entry.getKey();
			MinecraftForge.EVENT_BUS.post(new EventQuarry.ModifyQuarry.Before(quarry, quarry.getTileEntity().getWorld(), quarry.getMachinePos(), upgrade, upgradePos));
			upgrade.modifyQuarry(quarry);
			MinecraftForge.EVENT_BUS.post(new EventQuarry.ModifyQuarry.After(quarry, quarry.getTileEntity().getWorld(), quarry.getMachinePos(), upgrade, upgradePos));
		}
	}
	
	public List<String> addAdditionalInfo(List<String> baseInfo)
	{
		baseInfo.add("Upgrades:");
		for (int i = 0; i < _upgrades.size(); ++i) baseInfo.add("   " + (i+1) + ") " + _upgrades.get(i).getUpgradeName());
		return baseInfo;
	}
}