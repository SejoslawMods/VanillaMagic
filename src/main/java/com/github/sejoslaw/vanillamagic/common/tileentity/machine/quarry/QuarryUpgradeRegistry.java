package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry;

import com.github.sejoslaw.vanillamagic.api.exception.MappingExistsException;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.upgrade.QuarryUpgradeAutoInventoryOutputPlacer;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.upgrade.QuarryUpgradeAutosmelt;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.upgrade.QuarryUpgradeFortune;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.upgrade.QuarryUpgradeSilkTouch;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.block.Block;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for holding the single instances of the upgrades.<br>
 * This is the main registry for ALL the Quarry upgrades.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class QuarryUpgradeRegistry {
    private static List<Block> LIST_BLOCK = new ArrayList<>();
    private static Map<Block, IQuarryUpgrade> MAP_BLOCK_UPGRADE = new HashMap<>();
    private static Map<Class<? extends IQuarryUpgrade>, IQuarryUpgrade> MAP_CLASS_UPGRADE = new HashMap<>();

    private QuarryUpgradeRegistry() {
    }

    static {
        try {
            addUpgrade(QuarryUpgradeSilkTouch.class);
            addUpgrade(QuarryUpgradeFortune.One.class);
            addUpgrade(QuarryUpgradeFortune.Two.class);
            addUpgrade(QuarryUpgradeFortune.Three.class);
            addUpgrade(QuarryUpgradeAutoInventoryOutputPlacer.class);
            addUpgrade(QuarryUpgradeAutosmelt.class);
        } catch (MappingExistsException e) {
            e.printStackTrace();
            VMLogger.log(Level.ERROR, "Error while registering QuarryUpgrade for block: " + e.checkingKey);
        }
    }

    public static boolean addUpgrade(Class<? extends IQuarryUpgrade> quarryUpgradeClass) throws MappingExistsException {
        try {
            IQuarryUpgrade instance = quarryUpgradeClass.newInstance();

            if (MAP_BLOCK_UPGRADE.get(instance.getBlock()) != null) {
                throw new MappingExistsException("Mapping already exists: (Block, Upgrade)", instance.getBlock(), MAP_BLOCK_UPGRADE.get(instance.getBlock()));
            }

            LIST_BLOCK.add(instance.getBlock());
            MAP_BLOCK_UPGRADE.put(instance.getBlock(), instance);
            MAP_CLASS_UPGRADE.put(quarryUpgradeClass, instance);
            VMLogger.logInfo("Registered QuarryUpgrade: " + instance);

            return true;
        } catch (Exception e) {
            VMLogger.log(Level.WARN, "Couldn't register Quarry upgrade: " + quarryUpgradeClass.getSimpleName());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isUpgradeBlock(Block block) {
        for (Block b : LIST_BLOCK) {
            if (BlockUtil.areEqual(b, block)) {
                return true;
            }
        }

        return false;
    }

    public static IQuarryUpgrade getUpgradeFromBlock(Block block) {
        for (Map.Entry<Block, IQuarryUpgrade> entry : MAP_BLOCK_UPGRADE.entrySet()) {
            if (BlockUtil.areEqual(block, entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * Returns the number of currently registered upgrades.
     */
    public static int countUpgrades() {
        return MAP_CLASS_UPGRADE.size();
    }

    public static IQuarryUpgrade getReguiredUpgrade(IQuarryUpgrade iqu) {
        return MAP_CLASS_UPGRADE.get(iqu.requiredUpgrade());
    }

    public static boolean isTheSameUpgrade(IQuarryUpgrade iqu1, IQuarryUpgrade iqu2) {
        return BlockUtil.areEqual(iqu1.getBlock(), iqu2.getBlock());
    }

    public static List<IQuarryUpgrade> getUpgrades() {
        List<IQuarryUpgrade> upgrades = new ArrayList<>();

        for (IQuarryUpgrade iqu : MAP_CLASS_UPGRADE.values()) {
            upgrades.add(iqu);
        }

        return upgrades;
    }
}