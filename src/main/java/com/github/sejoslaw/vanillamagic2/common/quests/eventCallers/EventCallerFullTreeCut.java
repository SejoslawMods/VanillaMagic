package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestFullTreeCut;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerFullTreeCut extends EventCaller<QuestFullTreeCut> {
    @SubscribeEvent
    public void onTreeCut(BlockEvent.BreakEvent event) {
        this.executor.onBlockBreak(event,
                (player, world, pos, state) -> isLog(world, pos) ? this.quests.get(0) : null,
                (player, world, pos, state) -> {
                    if (!isBreakingTree(world, pos)) {
                        return;
                    }

                    this.executor.withHands(player, (leftHandStack, rightHandStack) ->
                            new TreeChopTask(player, world, pos, rightHandStack).execute());
                });
    }

    private static boolean isLog(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().getRegistryName().toString().toLowerCase().contains("log");
    }

    private static boolean isBreakingTree(World world, BlockPos pos) {
        BlockPos currentPos = null;
        Stack<BlockPos> candidates = new Stack<>();
        candidates.add(pos);

        while (!candidates.isEmpty()) {
            BlockPos candidate = candidates.pop();

            if (((currentPos == null) || (candidate.getY() > currentPos.getY())) && isLog(world, candidate)) {
                currentPos = candidate.up();

                while (isLog(world, currentPos)) {
                    currentPos = currentPos.up();
                }

                candidates.add(currentPos.north());
                candidates.add(currentPos.east());
                candidates.add(currentPos.south());
                candidates.add(currentPos.west());
            }
        }

        if (currentPos == null) {
            return false;
        }

        int distance = 3;
        int radius = -1;
        int leaves = 0;

        for (int x = 0; x < distance; ++x)
            for (int y = 0; y < distance; ++y)
                for (int z = 0; z < distance; ++z) {
                    BlockPos leaf = currentPos.add(radius + x, radius + y, radius + z);
                    BlockState state = world.getBlockState(leaf);

                    if (state.getMaterial() == Material.LEAVES && (++leaves >= 5)) {
                        return true;
                    }
                }

        return false;
    }

    private final class TreeChopTask {
        private final PlayerEntity player;
        private final World world;
        private final BlockPos pos;
        private final ItemStack rightHandStack;

        public Queue<BlockPos> blocks = new LinkedList<>();
        public Set<BlockPos> visited = new HashSet<>();

        public TreeChopTask(PlayerEntity player, World world, BlockPos pos, ItemStack rightHandStack) {
            this.player = player;
            this.world = world;
            this.pos = pos;
            this.rightHandStack = rightHandStack;

            this.blocks.add(this.pos);
        }

        public void execute() {
            while (this.blocks.size() > 0) {
                BlockPos current = this.blocks.remove();

                if (!visited.add(current) || !isLog(world, current)) {
                    continue;
                }

                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    BlockPos pos2 = current.offset(facing);

                    if (!visited.contains(pos2)) {
                        blocks.add(pos2);
                    }
                }

                for (int x = 0; x < 3; ++x) {
                    for (int z = 0; z < 3; ++z) {
                        BlockPos pos2 = current.add(-1 + x, 1, -1 + z);

                        if (!visited.contains(pos2)) {
                            blocks.add(pos2);
                        }
                    }
                }

                BlockUtils.breakBlock(rightHandStack, world, player, pos);
            }
        }
    }
}
