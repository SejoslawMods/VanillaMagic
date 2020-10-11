package com.github.sejoslaw.vanillamagic2.common.handlers.global;

import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherWartBlock;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BoneMealNetherWartHandler extends EventHandler {
    @SubscribeEvent
    public void onRightClick(BonemealEvent event) {
        this.onBoneMeal(event, (player, world, pos, state, stack) -> {
            if (state.getBlock() != Blocks.NETHER_WART) {
                return;
            }

            int age = state.get(NetherWartBlock.AGE);
            int maxAge = BlockUtils.getMaxValue(NetherWartBlock.AGE);

            if (age >= maxAge) {
                return;
            }

            world.setBlockState(pos, state.with(NetherWartBlock.AGE, age + 1), 1 | 2);
        });
    }
}
