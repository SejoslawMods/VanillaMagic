package com.github.sejoslaw.vanillamagic.core;

import com.github.sejoslaw.vanillamagic.common.config.VMConfigAchievements;
import net.minecraftforge.fml.common.Mod;

/**
 * Core mod file.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@Mod(VanillaMagic.MODID)
public class VanillaMagic {
    public static final String MODID = "vanillamagic";

    /**
     * Config used for loading achievements
     */
//    public static VMConfigAchievements CONFIG_ACHIEVEMENTS;

    /**
     * VM custom Creative Tab
     */
//    public static final VMItemGroup ITEM_GROUP = new VMItemGroup("vm");

    public VanillaMagic() {
    }

//    public VanillaMagic() {
//        this.preInit();
//        this.init();
//        this.postInit();
//    }
//
//    /**
//     * PreInitialization stage.
//     */
//    public void preInit() {
//        VanillaMagicAPI.logInfo("Starting VanillaMagicAPI from VanillaMagic...");
//
//        VMConfig.preInit(event);
//        ToolRegistry.preInit();
//        ItemUpgradeRegistry.preInit();
//
//        CONFIG_ACHIEVEMENTS = new VMConfigAchievements(
//                new File(event.getModConfigurationDirectory(), VMConfigAchievements.VM_DIRECTORY),
//                event.getSourceFile());
//
//        for (int i = 0; i < QuestList.size(); ++i) {
//            EventUtil.registerEvent(QuestList.get(i));
//        }
//
//        logInfo("Registered Quests: " + QuestList.size());
//
//        TileEntityRegistry.preInit();
//        ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, new ChunkLoadingHandler());
//
//        ItemUpgradeRegistry.registerEvents();
//        VanillaMagicIntegration.preInit(); // Integration should be read ALWAYS at the end.
//
//        this.registerEvents();
//    }
//
//    /**
//     * Initialization stage.
//     */
//    public void init() {
//        VanillaMagicIntegration.init(); // Integration should be read ALWAYS at the end.
//    }
//
//    /**
//     * PostInitialization stage.
//     */
//    public void postInit() {
//        BookRegistry.postInit();
//        EnchantedBucketUtil.registerFluids();
//        PotionedCrystalHelper.registerRecipes();
//        VMItems.postInit();
//        VanillaMagicIntegration.postInit();
//        logInfo("Registered Quarry Upgrades: " + QuarryUpgradeRegistry.countUpgrades());
//        MobSpawnerRegistry.postInit(); // Integration should be read ALWAYS at the end.
//    }
//
//    private void registerEvents() {
//        EventUtil.registerEvent(new EventQuestBook());
//        EventUtil.registerEvent(new PlayerEventHandler());
//        EventUtil.registerEvent(new InventorySelector());
//        EventUtil.registerEvent(new VanillaMagicDebug());
//        EventUtil.registerEvent(new WorldHandler());
//        EventUtil.registerEvent(new ActionEventAdditionalToolTips());
//        EventUtil.registerEvent(new ActionEventAutoplantItemEntity());
//        EventUtil.registerEvent(new ActionEventDeathPoint());
//    }
}