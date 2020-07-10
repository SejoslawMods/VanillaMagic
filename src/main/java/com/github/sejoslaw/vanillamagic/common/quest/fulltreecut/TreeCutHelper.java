package com.github.sejoslaw.vanillamagic.common.quest.fulltreecut;

import com.github.sejoslaw.vanillamagic.common.event.EventExtraBlockBreak;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Stack;

/**
 * Class which contains various methods to help cut tree.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TreeCutHelper {
    private TreeCutHelper() {
    }

    public static boolean isLog(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WOOD;
    }

    /**
     * @return Returns TRUE if the tree was detected.
     */
    public static boolean detectTree(World world, BlockPos origin) {
        BlockPos pos = null;
        Stack<BlockPos> candidates = new Stack<>();
        candidates.add(origin);

        while (!candidates.isEmpty()) {
            BlockPos candidate = candidates.pop();

            if (((pos == null) || (candidate.getY() > pos.getY())) && isLog(world, candidate)) {
                pos = candidate.up();

                while (isLog(world, pos)) {
                    pos = pos.up();
                }

                candidates.add(pos.north());
                candidates.add(pos.east());
                candidates.add(pos.south());
                candidates.add(pos.west());
            }
        }

        if (pos == null) {
            return false;
        }

        int d = 3;
        int o = -1;
        int leaves = 0;

        for (int x = 0; x < d; ++x) {
            for (int y = 0; y < d; ++y) {
                for (int z = 0; z < d; ++z) {
                    BlockPos leaf = pos.add(o + x, o + y, o + z);
                    BlockState state = world.getBlockState(leaf);

                    if (state.getMaterial() == Material.LEAVES && (++leaves >= 5)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void fellTree(ItemStack itemstack, BlockPos start, PlayerEntity player) {
        if (player.world.isRemote) {
            return;
        }

        EventExtraBlockBreak event = EventExtraBlockBreak.fireEvent(itemstack, player, player.getEntityWorld().getBlockState(start), 3, 3, 3, 1);
        int speed = Math.round((event.width * event.height * event.depth) / 27f);

        if (event.distance > 0) {
            speed = event.distance + 1;
        }

        EventUtil.registerEvent(new TreeChopTask(itemstack, start, player, speed));
    }
}