package seia.vanillamagic.quest.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCastSpellInAir extends QuestCastSpell {
	/**
	 * Cast spell when right-clicked while pointing in air.
	 */
	@SubscribeEvent
	public void castSpell(RightClickItem event) {
		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack inHand = event.getItemStack();

		castSpell(player, hand, inHand, null, null, null);
	}
}