package seia.vanillamagic.quest.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumSpell;

public class QuestCastSpellOnBlock extends QuestCastSpell
{
	public QuestCastSpellOnBlock(Quest required, int posX, int posY, EnumSpell spell) 
	{
		super(required, posX, posY, spell);
	}
	
	@SubscribeEvent
	public void caseSpell(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		EnumHand hand = event.getHand();
		ItemStack inHand = event.getItemStack();
		BlockPos pos = event.getPos();
		EnumFacing face = event.getFace();
		Vec3d hitVec = event.getHitVec();
		
		if(castSpell(player, hand, inHand, pos, face, hitVec))
		{
			event.setCanceled(true);
		}
	}
}