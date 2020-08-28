package com.github.sejoslaw.vanillamagic2.common.handlers.global;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class AutoplantHandler extends EventHandler {
    @SubscribeEvent
    public void autoplant(ItemExpireEvent event) {
        this.onItemExpire(event, (entity, stack, world, pos) -> {
            Block block = Block.getBlockFromItem(stack.getItem());

            if (!VMForgeConfig.CAN_AUTOPLANT.get() || block == Blocks.AIR) {
                return;
            }

            BlockPos soilPos = pos.down();
            BlockState soilState = world.getBlockState(soilPos);
            BlockState plantState = null;

            if (block instanceof IGrowable) {
                plantState = block.getDefaultState();
            } else if (block instanceof IPlantable && soilState.canSustainPlant(world, soilPos, Direction.UP, (IPlantable) block)) {
                plantState = ((IPlantable) block).getPlant(world, pos);
            } else if (block instanceof ChorusPlantBlock) {
                plantState = ((ChorusPlantBlock) Blocks.CHORUS_PLANT).makeConnections(world, pos);
            }

            if (!block.isValidPosition(block.getDefaultState(), world, pos) || plantState == null) {
                return;
            }

            Block.replaceBlock(null, plantState, world, pos, 3);
        });
    }
}
