package seia.vanillamagic.quest.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.util.VectorWrapper;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCastSpellOnBlock extends QuestCastSpell {
	/**
	 * Cast spell when right-clicked while pointing at block.
	 */
	@SubscribeEvent
	public void caseSpell(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack inHand = event.getItemStack();
		BlockPos pos = event.getPos();
		EnumFacing face = event.getFace();
		Vector3D hitVec = VectorWrapper.wrap(event.getHitVec());

		castSpell(player, hand, inHand, pos, face, hitVec);
	}
}