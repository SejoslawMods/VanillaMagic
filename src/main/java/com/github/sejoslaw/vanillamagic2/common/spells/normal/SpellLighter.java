package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellLighter extends Spell {
    public void cast(PlayerEntity player, World world, BlockPos pos, Direction face) {
        pos = pos.offset(face);

        if (world.isAirBlock(pos)) {
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
            world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
        }
    }
}
