package seia.vanillamagic.quest.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.spell.EnumSpell;
import seia.vanillamagic.utils.spell.EnumWand;
import seia.vanillamagic.utils.spell.SpellHelper;

public abstract class QuestCastSpell extends Quest
{
	protected EnumSpell spell;
	
	public QuestCastSpell(Achievement required, int posX, int posY, 
			EnumSpell spell)
	{
		this(required, posX, posY, spell.spellName, spell.spellUniqueName,
				spell);
	}
	
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

	/*
	 * Method for checking the possibilities to cast spell
	 */
	int howManyTimesCasted = 1;
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
						if(casterOffHand.stackSize >= spell.itemOffHand.stackSize)
						{
							if(howManyTimesCasted == 1)
							{
								if(castRightSpell(caster, pos, face, hitVec))
								{
									casterOffHand.stackSize -= spell.itemOffHand.stackSize;
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
		}
		catch(Exception e)
		{
		}
		return false;
	}
	
	/*
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
						return SpellHelper.castSpellById(spell.spellID, caster, pos, face, hitVec);
					}
				}
			}
		}
		return false;
	}
}