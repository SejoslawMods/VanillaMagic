package com.github.sejoslaw.vanillamagic2.common.itemUpgrades;

import com.github.sejoslaw.vanillamagic2.common.functions.Action;
import com.github.sejoslaw.vanillamagic2.common.quests.eventCallers.EventCallerItemUpgrade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeEventCaller {
    public EventCallerItemUpgrade eventCaller;
    public ItemUpgrade itemUpgrade;

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void execute(PlayerEntity player, Action action) {
        if (!this.itemUpgrade.containsTag(player.getHeldItemMainhand())) {
            return;
        }

        action.execute();
    }
}
