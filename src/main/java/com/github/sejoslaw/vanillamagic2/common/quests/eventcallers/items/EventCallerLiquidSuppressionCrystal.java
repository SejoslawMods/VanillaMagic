package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestLiquidSuppressionCrystal;
import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileLiquidSuppressor;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerLiquidSuppressionCrystal extends EventCallerVMItem<QuestLiquidSuppressionCrystal> {
    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        this.executor.onPlayerTick(event, (player, world, quest) ->
                this.executor.useVMItem(player, this.getVMItem().getUniqueKey(), (handStack) -> {
                    int x = (int) player.getPosX();
                    int y = (int) player.getPosY();
                    int z = (int) player.getPosZ();

                    int radius = VMForgeConfig.LIQUID_SUPPRESSION_CRYSTAL_RADIUS.get();
                    int refresh = VMForgeConfig.LIQUID_SUPPRESSION_CRYSTAL_REFRESH_RATE.get();

                    for (int i = -radius; i <= radius; ++i) {
                        for (int j = -radius; j <= radius; ++j) {
                            for (int k = -radius; k <= radius; ++k) {
                                BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
                                BlockState state = world.getBlockState(blockPos);

                                if (state.getMaterial().isLiquid() && (world.getTileEntity(blockPos) == null)) {
                                    WorldUtils.spawnVMTile(world, blockPos, new VMTileLiquidSuppressor(), (tile) -> {
                                        tile.initialize(state, refresh);
                                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 1 | 2);
                                        return true;
                                    });
                                } else {
                                    TileEntity tile = world.getTileEntity(blockPos);

                                    if (tile instanceof VMTileLiquidSuppressor) {
                                        ((VMTileLiquidSuppressor) tile).resetDuration(refresh);
                                    }
                                }
                            }
                        }
                    }
                }));
    }
}
