package com.github.sejoslaw.vanillamagic2.common.itemupgrades.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgradeEventCaller;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import net.minecraft.block.Block;
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
                                        Block.spawnAsEntity(world, pos, smeltingResult);
                                        drops.remove(drop);
                                    }
                                })));
    }
}
