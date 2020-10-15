package com.github.sejoslaw.vanillamagic2.common.itemupgrades.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgradeEventCaller;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.util.Direction.*;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeEventCallerMining3x3 extends ItemUpgradeEventCaller {
    private static final Map<Direction, Vector3i> DIRECTIONS = new HashMap<>();

    static {
        DIRECTIONS.put(SOUTH, EAST.getDirectionVec().down(1));
        DIRECTIONS.put(EAST, NORTH.getDirectionVec().down(1));
        DIRECTIONS.put(NORTH, WEST.getDirectionVec().down(1));
        DIRECTIONS.put(WEST, SOUTH.getDirectionVec().down(1));
        DIRECTIONS.put(UP, SOUTH.getDirectionVec().offset(EAST, 1));
        DIRECTIONS.put(DOWN, SOUTH.getDirectionVec().offset(EAST, 1));
    }

    @SubscribeEvent
    public void onMined(BlockEvent.BreakEvent event) {
        this.eventCaller.executor.onBlockBreakNoHandsCheck(event,
                (player, world, pos, state) -> this.getQuest(player),
                (player, world, pos, state, quest) ->
                        this.execute(player, () ->
                                this.eventCaller.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                                    Vector3d lookVec = player.getLookVec();
                                    Direction playerFacing = Direction.getFacingFromVector(lookVec.getX(), lookVec.getY(), lookVec.getZ());

                                    int size = 3;
                                    List<BlockPos> minedPoses = new ArrayList<>();

                                    Vector3i cornerVec = DIRECTIONS.get(playerFacing);
                                    BlockPos cornerPos = pos.add(cornerVec.getX() * (size / 2), cornerVec.getY() * (size / 2), cornerVec.getZ() * (size / 2));

                                    for (int row = 0; row < size; ++row) {
                                        for (int col = 0; col < size; ++col) {
                                            minedPoses.add(cornerPos.add(row * -cornerVec.getX(), col * -cornerVec.getY(), row * -cornerVec.getZ()));
                                        }
                                    }

                                    minedPoses.forEach(minedPos -> BlockUtils.breakBlock(rightHandStack, world, player, minedPos));
                                })));
    }
}
