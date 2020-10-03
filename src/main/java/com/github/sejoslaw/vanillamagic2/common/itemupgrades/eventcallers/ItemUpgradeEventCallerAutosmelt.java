package com.github.sejoslaw.vanillamagic2.common.itemupgrades.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgradeEventCaller;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeEventCallerAutosmelt extends ItemUpgradeEventCaller {
    @SubscribeEvent
    public void onHarvestDrops(BlockEvent.BreakEvent event) {
        this.eventCaller.executor.onBlockBreakNoHandsCheck(event,
                (player, world, pos, state) -> this.getQuest(player),
                (player, world, pos, state, quest) ->
                        this.execute(player, () -> {
                            ItemStack stack = new ItemStack(state.getBlock());
                            ItemStack smeltingResult = ItemStackUtils.getSmeltingResultAsNewStack(stack, world);
                            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);

                            if (smeltingResult != ItemStack.EMPTY) {
                                Block.spawnAsEntity(WorldUtils.asWorld(world), pos, smeltingResult);
                            }
                        }));
    }
}
