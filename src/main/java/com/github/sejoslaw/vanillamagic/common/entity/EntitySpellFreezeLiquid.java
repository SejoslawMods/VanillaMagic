package com.github.sejoslaw.vanillamagic.common.entity;

import com.github.sejoslaw.vanillamagic.common.util.BlockPosUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class which describes the Spell which Freeze Water.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntitySpellFreezeLiquid extends EntitySpell {
    public EntitySpellFreezeLiquid(EntityType<? extends EntitySpell> entityType, World world) {
        super(entityType, world);
    }

    protected void onHit(RayTraceResult result) {
        if (!this.isInWater()) {
            return;
        }

        Vec3d hitVec = result.getHitVec();
        BlockPos hitPos = new BlockPos(hitVec);
        List<BlockPos> blockPosToReplace = BlockPosUtil.getBlockPos3x3x3(hitPos);

        blockPosToReplace.forEach(pos -> {
            Block block = world.getBlockState(pos).getBlock();

            if (block instanceof FlowingFluidBlock) {
                world.setBlockState(pos, Blocks.ICE.getDefaultState());
            }
        });
    }
}