package com.github.sejoslaw.vanillamagic2.common.itemupgrades.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgradeEventCaller;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeEventCallerThor extends ItemUpgradeEventCaller {
    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        this.eventCaller.executor.onAttackEntity(event,
                (player, world, entity) -> this.eventCaller.quests.get(0),
                (player, world, entity, quest) ->
                        this.execute(player, () ->
                                world.addEntity(new LightningBoltEntity(world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), false))));
    }
}
