package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry;

import com.github.sejoslaw.vanillamagic.api.event.EventQuarry;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarry;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgradeManager;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ListUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryUpgradeManager implements IQuarryUpgradeManager {
    private final IQuarry quarry;
    private final List<IQuarryUpgrade> upgrades = new ArrayList<>();
    private final Map<BlockPos, IQuarryUpgrade> upgradeOnPos = new HashMap<>();

    public QuarryUpgradeManager(IQuarry quarry) {
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
            EventUtil.postEvent(new EventQuarry.AddUpgrade(this.quarry, this.quarry.asTileEntity().getWorld(), this.quarry.asTileEntity().getPos(), iqu, upgradePos));
        }
    }

    /**
     * Returns TRUE if the required upgrade for the given upgrade is registered.
     */
    private boolean hasRegisteredRequireUpgrade(IQuarryUpgrade iqu) {
        if (iqu.requiredUpgrade() == null) {
            return true;
        }

        IQuarryUpgrade required;

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
            if (BlockUtil.areEqual(up.getBlock(), iqu.getBlock())) {
                return false;
            }
        }

        return true;
    }

    public BlockPos getUpgradePos(IQuarryUpgrade upgrade) {
        for (Map.Entry<BlockPos, IQuarryUpgrade> entry : this.upgradeOnPos.entrySet()) {
            IQuarryUpgrade iqu = entry.getValue();

            if (QuarryUpgradeRegistry.isTheSameUpgrade(upgrade, iqu)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public List<ItemStack> getDrops(Block blockToDig, World world, BlockPos workingPos, BlockState workingPosState) {
        List<ItemStack> drops = new ArrayList<ItemStack>();

        for (IQuarryUpgrade upgrade : this.upgrades) {
            List<ItemStack> dropsFromUpgrade = upgrade.getDrops(blockToDig, world, workingPos, workingPosState);
            drops = ListUtil.combineLists(drops, dropsFromUpgrade);
        }

        if (drops.isEmpty()) {
            drops = ListUtil.combineLists(drops, Block.getDrops(workingPosState, (ServerWorld) world, workingPos, null));
        }

        return drops;
    }

    public void clearUpgrades() {
        this.upgrades.clear();
        this.upgradeOnPos.clear();
    }

    public void modifyQuarry(IQuarry quarry) {
        for (Map.Entry<BlockPos, IQuarryUpgrade> entry : this.upgradeOnPos.entrySet()) {
            IQuarryUpgrade upgrade = entry.getValue();
            BlockPos upgradePos = entry.getKey();

            EventUtil.postEvent(new EventQuarry.ModifyQuarry.Before(quarry, quarry.asTileEntity().getWorld(), quarry.asTileEntity().getPos(), upgrade, upgradePos));

            upgrade.modifyQuarry(quarry);

            EventUtil.postEvent(new EventQuarry.ModifyQuarry.After(quarry, quarry.asTileEntity().getWorld(), quarry.asTileEntity().getPos(), upgrade, upgradePos));
        }
    }

    public void addAdditionalInfo(List<ITextComponent> list) {
        list.add(TextUtil.wrap("Upgrades:"));

        for (int i = 0; i < this.upgrades.size(); ++i) {
            list.add(TextUtil.wrap("   " + (i + 1) + ") " + this.upgrades.get(i).getUpgradeName()));
        }
    }
}