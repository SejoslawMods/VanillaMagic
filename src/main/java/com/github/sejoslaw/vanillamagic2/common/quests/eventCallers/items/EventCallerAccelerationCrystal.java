package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestAccelerationCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerAccelerationCrystal extends EventCallerCustomItem<QuestAccelerationCrystal> {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event, (player, world, pos, direction) ->
                this.executor.useCustomItem(player, this.getCustomItem().getUniqueKey(), (handStack) -> {
                    TileEntity tile = world.getTileEntity(pos);
                    boolean isTickable = tile instanceof ITickable;

                    Random rand = new Random();
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    for (int i = 0; i < VMForgeConfig.ACCELERATION_CRYSTAL_UPDATE_TICKS.get(); i++) {
                        if (isTickable) {
                            ((ITickable) tile).tick();
                        } else {
                            block.tick(state, (ServerWorld) world, pos, rand);
                        }
                    }
                }));
    }
}
