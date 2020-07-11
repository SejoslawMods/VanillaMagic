package com.github.sejoslaw.vanillamagic.core;

import com.github.sejoslaw.vanillamagic.api.quest.QuestRegistry;
import com.github.sejoslaw.vanillamagic.api.upgrade.toolupgrade.ToolRegistry;
import com.github.sejoslaw.vanillamagic.common.file.VMConfigQuests;
import com.github.sejoslaw.vanillamagic.common.item.book.BookRegistry;
import com.github.sejoslaw.vanillamagic.common.item.enchantedbucket.EnchantedBucketUtil;
import com.github.sejoslaw.vanillamagic.common.item.potionedcrystal.PotionedCrystalHelper;
import com.github.sejoslaw.vanillamagic.common.quest.mobspawnerdrop.MobSpawnerRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.ItemUpgradeRegistry;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.QuarryUpgradeRegistry;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Core mod file.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@Mod(VanillaMagic.MODID)
public class VanillaMagic {
    public static final String MODID = "vanillamagic";

    public static final VMItemGroup ITEM_GROUP = new VMItemGroup("vm");

    public VanillaMagic() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, VMConfig.COMMON_CONFIG, "VanillaMagicConfig.toml");
        VMConfigQuests.initialize();
        ToolRegistry.initialize();
        ItemUpgradeRegistry.initialize();
        QuestRegistry.getQuests().forEach(EventUtil::registerEvent);
        VMLogger.logInfo("Registered Quests: " + QuestRegistry.size());
        EventUtil.initialize();
        BookRegistry.initialize();
        EnchantedBucketUtil.initialize();
        PotionedCrystalHelper.initialize();
        VMItems.initialize();
        VMLogger.logInfo("Registered Quarry Upgrades: " + QuarryUpgradeRegistry.countUpgrades());
        MobSpawnerRegistry.initialize();
    }
}
