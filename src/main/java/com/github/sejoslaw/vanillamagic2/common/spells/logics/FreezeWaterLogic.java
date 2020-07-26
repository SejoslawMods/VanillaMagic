package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FreezeWaterLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, World world, RayTraceResult result) {
        if (!entitySpell.isInWater()) {
            return;
        }

        Vec3d hitVec = result.getHitVec();
        BlockPos hitPos = new BlockPos(hitVec);
        List<BlockPos> blockPosToReplace = this.getBlockPos3x3x3(hitPos);

        blockPosToReplace.forEach(pos -> {
            Block block = world.getBlockState(pos).getBlock();

            if (block instanceof FlowingFluidBlock) {
                world.setBlockState(pos, Blocks.ICE.getDefaultState());
            }
        });
    }

    /**
     * @return List with 3x3x3 coordinates around the specified middle block.
     */
    public List<BlockPos> getBlockPos3x3x3(BlockPos pos) {
        List<BlockPos> positions = new ArrayList<>();

        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z) {
                    positions.add(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                }
            }
        }

        return positions;
    }
}
