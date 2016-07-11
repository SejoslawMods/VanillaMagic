package seia.vanillamagic.quest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.spell.EnumSpell;
import seia.vanillamagic.utils.spell.EnumWand;
import seia.vanillamagic.utils.spell.SpellHelper;

//TODO: Fix the Items being taken twice 1 for each method when clicked on block
public class QuestCastSpell extends Quest
{
	protected EnumSpell spell;
	
	public QuestCastSpell(Achievement required, int posX, int posY, String questName, String uniqueName, 
			EnumSpell spell)
	{
		this(required, posX, posY, spell.itemOffHand.getItem(), questName, uniqueName, 
				spell);
	}
	
	public QuestCastSpell(Achievement required, int posX, int posY, Item itemIcon, String questName, String uniqueName, 
			EnumSpell spell)
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
		this.spell = spell;
	}
	
	public EnumSpell getSpell()
	{
		return spell;
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
	
	/*
	 * Method for checking the possibilities to cast spell
	 */
	public boolean castSpell(EntityPlayer caster, EnumHand hand, ItemStack inHand, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		try
		{
			EnumWand wandPlayerHand = EnumWand.getWandByItemStack(caster.getHeldItemMainhand());
			if(EnumWand.isWandRightForSpell(wandPlayerHand, spell))
			{
				ItemStack casterOffHand = caster.getHeldItemOffhand();
				if(spell.isItemOffHandRightForSpell(casterOffHand))
				{
					if(!caster.hasAchievement(achievement))
					{
						caster.addStat(achievement, 1);
					}
					else
					{
						if(castRightSpell(caster, pos, face, hitVec))
						{
							casterOffHand.stackSize--;
							return true;
						}
					}
				}
			}
		}
		catch(Exception e)
		{
		}
		return false;
	}
	
	/*
	 * Method for casting the right spell (if-else)
	 * This method will take care for all the spell-related aspects
	 */
	public boolean castRightSpell(EntityPlayer caster, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		for(int i = 0; i < EnumSpell.values().length; i++)
		{
			EnumSpell es = EnumSpell.values()[i];
			{
				if(spell.spellID == es.spellID)
				{
					if(spell.minimalWandTier.wandTier == es.minimalWandTier.wandTier)
					{
						return SpellHelper.castSpellById(spell.spellID, caster, pos, face, hitVec);
					}
				}
			}
		}
		return false;
	}
}