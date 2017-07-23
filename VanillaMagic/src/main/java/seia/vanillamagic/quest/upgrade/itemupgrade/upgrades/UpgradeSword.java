package seia.vanillamagic.quest.upgrade.itemupgrade.upgrades;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.upgrade.itemupgrade.ItemUpgradeBase;

/**
 * Class which describes basic Sowrd upgrade.
 */
public abstract class UpgradeSword extends ItemUpgradeBase 
{
	/**
	 * Method which is run after checking all conditions in main event method.
	 */
	public abstract void onAttack(EntityPlayer player, Entity target);
	
	/**
	 * Check conditions for event to start.
	 * Also check if sword has got required Tag.
	 */
	@SubscribeEvent
	public void doEvent(AttackEntityEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		// if for some reason Player is null, quit.
		if (player == null) return; 
		// If item does not contains required tag, quit.
		ItemStack playerMainHandStack = player.getHeldItemMainhand();
		if (!containsTag(playerMainHandStack)) return; 
		else onAttack(player, event.getTarget());
	}
}