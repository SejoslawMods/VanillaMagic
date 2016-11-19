package seia.vanillamagic.quest.spell;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import seia.vanillamagic.event.EventCastSpell;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumSpell;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.spell.SpellHelper;
import seia.vanillamagic.util.ItemStackHelper;

public abstract class QuestCastSpell extends Quest
{
	protected EnumSpell spell;
	
	public void readData(JsonObject jo)
	{
		this.spell = EnumSpell.getSpellById(jo.get("spellID").getAsInt());
		this.icon = spell.itemOffHand.copy();
		this.questName = spell.spellName;
		this.uniqueName = spell.spellUniqueName;
		super.readData(jo);
	}
	
	public EnumSpell getSpell()
	{
		return spell;
	}

	/**
	 * Method for checking the possibilities to cast spell
	 */
	int howManyTimesCasted = 1;
	public boolean castSpell(EntityPlayer caster, EnumHand hand, ItemStack inHand, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(!finishedAdditionalQuests(caster))
		{
			return false;
		}
		EnumWand wandPlayerHand = EnumWand.getWandByItemStack(caster.getHeldItemMainhand());
		if(wandPlayerHand == null)
		{
			return false;
		}
		if(EnumWand.isWandRightForSpell(wandPlayerHand, spell))
		{
			ItemStack casterOffHand = caster.getHeldItemOffhand();
			if(ItemStackHelper.isNullStack(casterOffHand))
			{
				return false;
			}
			if(spell.isItemOffHandRightForSpell(casterOffHand))
			{
				if(!caster.hasAchievement(achievement))
				{
					if(canPlayerGetAchievement(caster))
					{
						caster.addStat(achievement, 1);
					}
				}
				if(caster.hasAchievement(achievement))
				{
					if(ItemStackHelper.getStackSize(casterOffHand) >= 
							ItemStackHelper.getStackSize(spell.itemOffHand))
					{
						if(howManyTimesCasted == 1)
						{
							if(castRightSpell(caster, pos, face, hitVec))
							{
								ItemStackHelper.decreaseStackSize(casterOffHand, ItemStackHelper.getStackSize(spell.itemOffHand));
								howManyTimesCasted++;
								return true;
							}
						}
						else
						{
							howManyTimesCasted = 1;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Method for casting the right spell
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
						EventCastSpell event = new EventCastSpell(caster, pos, face, hitVec, es);
						return SpellHelper.castSpellById(spell.spellID, caster, pos, face, hitVec);
					}
				}
			}
		}
		return false;
	}
}