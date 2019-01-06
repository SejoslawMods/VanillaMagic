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
import seia.vanillamagic.api.event.EventQuarry;
import seia.vanillamagic.api.tileentity.machine.IQuarry;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgrade;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgradeHelper;
import seia.vanillamagic.util.EventUtil;
import seia.vanillamagic.util.ListUtil;

/**
 * Each Quarry has it's own helper to handler the upgrades.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryUpgradeHelper implements IQuarryUpgradeHelper {
	private final IQuarry quarry;
	private final List<IQuarryUpgrade> upgrades = new ArrayList<>();
	private final Map<BlockPos, IQuarryUpgrade> upgradeOnPos = new HashMap<>();

	public QuarryUpgradeHelper(IQuarry quarry) {
		this.quarry = quarry;
	}

	public IQuarry getQuarry() {
		return this.quarry;
	}

	public List<IQuarryUpgrade> getUpgradesList() {
		return this.upgrades;
	}

	public Map<BlockPos, IQuarryUpgrade> getUpgradesMap() {
		return this.upgradeOnPos;
	}

	public void addUpgradeFromBlock(Block block, BlockPos upgradePos) {
		IQuarryUpgrade iqu = QuarryUpgradeRegistry.getUpgradeFromBlock(block);

		if (!hasRegisteredRequireUpgrade(iqu)) {
			return;
		}

		if (canAddUpgrade(iqu)) {
			this.upgrades.add(iqu);
			this.upgradeOnPos.put(upgradePos, iqu);
			EventUtil.postEvent(new EventQuarry.AddUpgrade(this.quarry, this.quarry.getTileEntity().getWorld(),
					this.quarry.getMachinePos(), iqu, upgradePos));
		}
	}

	/**
	 * Returns TRUE if the required upgrade for the given upgrade is registered.
	 */
	private boolean hasRegisteredRequireUpgrade(IQuarryUpgrade iqu) {
		if (iqu.requiredUpgrade() == null) {
			return true;
		}

		IQuarryUpgrade required = null;

		for (IQuarryUpgrade registeredUpgrade : this.upgrades) {
			required = QuarryUpgradeRegistry.getReguiredUpgrade(iqu);

			if (QuarryUpgradeRegistry.isTheSameUpgrade(registeredUpgrade, required)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * if the given upgrade is already on the list, return FALSE. <br>
	 * Otherwise return TRUE.
	 */
	private boolean canAddUpgrade(IQuarryUpgrade iqu) {
		for (IQuarryUpgrade up : this.upgrades) {
			if (Block.isEqualTo(up.getBlock(), iqu.getBlock())) {
				return false;
			}
		}

		return true;
	}

	@Nullable
	public BlockPos getUpgradePos(IQuarryUpgrade upgrade) {
		for (Entry<BlockPos, IQuarryUpgrade> entry : this.upgradeOnPos.entrySet()) {
			IQuarryUpgrade iqu = entry.getValue();

			if (QuarryUpgradeRegistry.isTheSameUpgrade(upgrade, iqu)) {
				return entry.getKey();
			}
		}

		return null;
	}

	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos,
			IBlockState workingPosState) {
		List<ItemStack> drops = new ArrayList<ItemStack>();

		for (IQuarryUpgrade upgrade : this.upgrades) {
			List<ItemStack> dropsFromUpgrade = upgrade.getDrops(blockToDig, world, workingPos, workingPosState);
			drops = ListUtil.<ItemStack>combineLists(drops, dropsFromUpgrade);
		}

		if (drops.isEmpty()) {
			drops = ListUtil.<ItemStack>combineLists(drops, blockToDig.getDrops(world, workingPos, workingPosState, 0));
		}

		return drops;
	}

	public void clearUpgrades() {
		this.upgrades.clear();
		this.upgradeOnPos.clear();
	}

	public void modifyQuarry(IQuarry quarry) {
		for (Entry<BlockPos, IQuarryUpgrade> entry : this.upgradeOnPos.entrySet()) {
			IQuarryUpgrade upgrade = entry.getValue();
			BlockPos upgradePos = entry.getKey();

			EventUtil.postEvent(new EventQuarry.ModifyQuarry.Before(quarry, quarry.getTileEntity().getWorld(),
					quarry.getMachinePos(), upgrade, upgradePos));

			upgrade.modifyQuarry(quarry);

			EventUtil.postEvent(new EventQuarry.ModifyQuarry.After(quarry, quarry.getTileEntity().getWorld(),
					quarry.getMachinePos(), upgrade, upgradePos));
		}
	}

	public List<String> addAdditionalInfo(List<String> baseInfo) {
		baseInfo.add("Upgrades:");

		for (int i = 0; i < this.upgrades.size(); ++i) {
			baseInfo.add("   " + (i + 1) + ") " + this.upgrades.get(i).getUpgradeName());
		}

		return baseInfo;
	}
}