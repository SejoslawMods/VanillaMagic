package com.github.sejoslaw.vanillamagic2.common.itemUpgrades.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.itemUpgrades.ItemUpgradeEventCaller;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeEventCallerAutosmelt extends ItemUpgradeEventCaller {
    @SubscribeEvent
    public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        this.eventCaller.executor.onHarvestDrops(event,
                (player, world, pos, state, drops) -> this.eventCaller.quests.get(0),
                (player, world, pos, state, drops, quest) ->
                        this.execute(player, () ->
                                drops.forEach(drop -> {
                                    ItemStack smeltingResult = ItemStackUtils.getSmeltingResultAsNewStack(drop, world);

                                    if (smeltingResult != ItemStack.EMPTY) {
                                        ItemEntity afterSmeltEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), smeltingResult);
                                        world.addEntity(afterSmeltEntity);
                                        drops.remove(drop);
                                    }
                                })));
    }
}
