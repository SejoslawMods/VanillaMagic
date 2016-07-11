package seia.vanillamagic.quest.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.spell.EnumSpell;

public class QuestCastSpellInAir extends QuestCastSpell
{
	public QuestCastSpellInAir(Achievement required, int posX, int posY, EnumSpell spell) 
	{
		super(required, posX, posY, spell);
	}
	
	@SubscribeEvent
	public void castSpell(RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack inHand = event.getItemStack();
		
		if(castSpell(player, hand, inHand, null, null, null))
		{
			event.setCanceled(true);
		}
	}
}