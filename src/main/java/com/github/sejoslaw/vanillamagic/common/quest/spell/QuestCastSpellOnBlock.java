package com.github.sejoslaw.vanillamagic.common.quest.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCastSpellOnBlock extends QuestCastSpell {
    /**
     * Cast spell when right-clicked while pointing at block.
     */
    @SubscribeEvent
    public void caseSpell(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        Vec3d hitVec = player.getLookVec();
        castSpell(player, event.getPos(), event.getFace(), hitVec);
    }
}