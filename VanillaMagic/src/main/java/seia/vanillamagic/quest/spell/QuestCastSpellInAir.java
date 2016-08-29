package seia.vanillamagic.quest.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumSpell;

public class QuestCastSpellInAir extends QuestCastSpell
{
	public QuestCastSpellInAir(Quest required, int posX, int posY, EnumSpell spell, Quest[] additionalRequiredQuests) 
	{
		super(required, posX, posY, spell, additionalRequiredQuests);
	}
	
	public QuestCastSpellInAir(Quest required, int posX, int posY, EnumSpell spell) 
	{
		super(required, posX, posY, spell);
	}
	
	public QuestCastSpellInAir(Quest required, int posX, int posY, ItemStack itemIcon, EnumSpell spell) 
	{
		super(required, posX, posY, itemIcon, spell.spellName, spell.spellUniqueName, spell);
	}
	
	@SubscribeEvent
	public void castSpell(RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack inHand = event.getItemStack();
		
		castSpell(player, hand, inHand, null, null, null);
	}
}