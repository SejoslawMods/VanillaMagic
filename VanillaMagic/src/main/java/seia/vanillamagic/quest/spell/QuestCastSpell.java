package seia.vanillamagic.quest.spell;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.event.EventCastSpell;
import seia.vanillamagic.magic.spell.ISpell;
import seia.vanillamagic.magic.spell.SpellRegistry;
import seia.vanillamagic.magic.wand.IWand;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.ItemStackHelper;

public abstract class QuestCastSpell extends Quest
{
	protected ISpell spell;
	
	public void readData(JsonObject jo)
	{
		this.spell = SpellRegistry.getSpellById(jo.get("spellID").getAsInt());
		this.icon = spell.getRequiredStackOffHand().copy();
		this.questName = spell.getSpellName();
		this.uniqueName = spell.getSpellUniqueName();
		super.readData(jo);
	}
	
	public ISpell getSpell()
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
		IWand wandPlayerHand = WandRegistry.getWandByItemStack(caster.getHeldItemMainhand());
		if(wandPlayerHand == null)
		{
			return false;
		}
		if(WandRegistry.isWandRightForSpell(wandPlayerHand, spell))
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
							ItemStackHelper.getStackSize(spell.getRequiredStackOffHand()))
					{
						if(howManyTimesCasted == 1)
						{
							if(castRightSpell(caster, pos, face, hitVec))
							{
								ItemStackHelper.decreaseStackSize(casterOffHand, ItemStackHelper.getStackSize(spell.getRequiredStackOffHand()));
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
		List<ISpell> spells = SpellRegistry.getSpells();
		for(int i = 0; i < spells.size(); ++i)
		{
			ISpell iSpell = spells.get(i);
			{
				if(spell.getSpellID() == iSpell.getSpellID())
				{
					if(spell.getWand().getWandID() == iSpell.getWand().getWandID())
					{
						// EventCastSpell event = new EventCastSpell(caster, pos, face, hitVec, iSpell);
						if(!MinecraftForge.EVENT_BUS.post(new EventCastSpell(caster, pos, face, hitVec, iSpell)))
						{
							return SpellRegistry.castSpellById(spell.getSpellID(), caster, pos, face, hitVec);
						}
						// return SpellRegistry.castSpellById(spell.getSpellID(), caster, pos, face, hitVec);
					}
				}
			}
		}
		return false;
	}
}