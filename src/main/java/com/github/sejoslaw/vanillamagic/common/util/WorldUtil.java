package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.common.quest.QuestJumper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;

/**
 * Class which wraps various World related stuff.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class WorldUtil {
    private WorldUtil() {
    }

    public static NonNullList<ItemStack> fillList(NonNullList<ItemStack> list) {
        BlockPos pos = new BlockPos(100, 100, 100); // Default position

        DimensionManager.getRegistry().stream().forEach(dimType -> {
            list.add(QuestJumper.writeDataToBook(dimType, pos));
        });

        return list;
    }

    public static World getWorld(MinecraftServer server, int dimId) {
        DimensionType dt = DimensionType.getById(dimId);
        return server.getWorld(dt);
    }
}
