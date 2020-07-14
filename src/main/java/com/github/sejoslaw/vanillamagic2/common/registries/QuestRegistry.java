package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.quests.QuestEventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class QuestRegistry {
    public static final List<QuestEventCaller> QUEST_EVENT_CALLERS = new ArrayList<>();

    public static void initialize() {
        // Standard Quests
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("fullTreeCut", EventCallerFullTreeCut.class, QuestFullTreeCut.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("mobSpawnerDrop", EventCallerMobSpawnerDrop.class, QuestMobSpawnerDrop.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("portableCraftingTable", EventCallerPortableCraftingTable.class, QuestPortableCraftingTable.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("castSpellInAir", EventCallerCastSpellInAir.class, QuestCastSpellInAir.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("castSpellOnBlock", EventCallerCastSpellOnBlock.class, QuestCastSpellOnBlock.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("itemUpgrade", EventCallerItemUpgrade.class, QuestItemUpgrade.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("toolUpgrade", EventCallerToolUpgrade.class, QuestToolUpgrade.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("arrowMachineGun ", EventCallerArrowMachineGun .class, QuestArrowMachineGun.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("buildAltar", EventCallerBuildAltar.class, QuestBuildAltar.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("captureEntity", EventCallerCaptureEntity.class, QuestCaptureEntity.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("craft", EventCallerCraft.class, QuestCraft.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("craftOnAltar", EventCallerCraftOnAltar.class, QuestCraftOnAltar.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("itemMagnet", EventCallerItemMagnet.class, QuestItemMagnet.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("jumper", EventCallerJumper.class, QuestJumper.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("mineBlock", EventCallerMineBlock.class, QuestMineBlock.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("moveBlock", EventCallerMoveBlock.class, QuestMoveBlock.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("oreMultiplier", EventCallerOreMultiplier.class, QuestOreMultiplier.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("pickup", EventCallerPickup.class, QuestPickup.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("shootWitherSkull", EventCallerShootWitherSkull.class, QuestShootWitherSkull.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("smeltOnAltar", EventCallerSmeltOnAltar.class, QuestSmeltOnAltar.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("summonHorde", EventCallerSummonHorde.class, QuestSummonHorde.class).register());

        // Connected with Items
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("accelerationCrystal ", EventCallerAccelerationCrystal .class, QuestAccelerationCrystal.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("enchantedBucket", EventCallerEnchantedBucket.class, QuestEnchantedBucket.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("evokerCrystal ", EventCallerEvokerCrystal .class, QuestEvokerCrystal.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("liquidSuppressionCrystal", EventCallerLiquidSuppressionCrystal.class, QuestLiquidSuppressionCrystal.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("potionCrystal", EventCallerPotionCrystal.class, QuestPotionCrystal.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("motherNatureCrystal", EventCallerMotherNatureCrystal.class, QuestMotherNatureCrystal.class).register());

        // Connected with Custom Tile Entities
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("blockAbsorber", EventCallerBlockAbsorber.class, QuestBlockAbsorber.class).register());
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("chunkLoader", EventCallerChunkLoader.class, QuestChunkLoader.class).register()); // ???
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("inventoryBridge", EventCallerInventoryBridge.class, QuestInventoryBridge.class).register()); // ???
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("accelerant", EventCallerAccelerant.class, QuestAccelerant.class).register()); // old Speedy

        // Machines
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("quarry", EventCallerQuarry.class, QuestQuarry.class).register());

        // TODO: Add hierarchy for quests and callers
        // TODO: Add QuestExecutor for handling common code with lambdas for additional code
    }

    public static void readQuest(IJsonService jsonService) {
        try {
            String questEventCallerKey = jsonService.getString("eventCaller");
            QuestEventCaller caller = QUEST_EVENT_CALLERS.stream().filter(c -> c.key.equals(questEventCallerKey)).findFirst().get();
            caller.addNewQuest(jsonService);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Quest getQuest(String questUniqueName) {
        for (QuestEventCaller caller : QUEST_EVENT_CALLERS) {
            for (Quest quest : caller.getEventCaller().quests) {
                if (quest.uniqueName.equals(questUniqueName)) {
                    return quest;
                }
            }
        }

        return null;
    }
}
