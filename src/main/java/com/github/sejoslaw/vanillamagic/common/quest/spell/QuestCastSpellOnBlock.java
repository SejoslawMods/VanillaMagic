package com.github.sejoslaw.vanillamagic.quest.spell;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCastSpellOnBlock extends QuestCastSpell {
	/**
	 * Cast spell when right-clicked while pointing at block.
	 */
	@SubscribeEvent
	public void caseSpell(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		Hand hand = event.getHand();
		ItemStack inHand = event.getItemStack();
		BlockPos pos = event.getPos();
		Direction face = event.getFace();
		Vector3D hitVec = VectorWrapper.wrap(event.getHitVec());

		castSpell(player, hand, inHand, pos, face, hitVec);
	}
}