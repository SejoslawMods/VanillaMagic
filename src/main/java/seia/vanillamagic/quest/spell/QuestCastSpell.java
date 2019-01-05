package seia.vanillamagic.quest.spell;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventSpell;
import seia.vanillamagic.api.magic.ISpell;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.magic.spell.SpellRegistry;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Base Quest for casting Spells.
 */
public abstract class QuestCastSpell extends Quest
{
	/**
	 * Spell which should be casted.
	 */
	protected ISpell spell;
	
	public void readData(JsonObject jo)
	{
		this.spell = SpellRegistry.getSpellById(jo.get("spellID").getAsInt());
		this.icon = spell.getRequiredStackOffHand().copy();
		this.questName = spell.getSpellName();
		this.uniqueName = spell.getSpellUniqueName();
		super.readData(jo);
	}
	
	/**
	 * @return Returns the Spell for this Quest.
	 */
	public ISpell getSpell()
	{
		return spell;
	}
	
	int howManyTimesCasted = 1;
	/**
	 * Method for checking if  is it possible to cast current Spell.
	 */
	public boolean castSpell(EntityPlayer caster, EnumHand hand, ItemStack inHand, 
			BlockPos pos, EnumFacing face, Vector3D hitVec)
	{
		if (!finishedAdditionalQuests(caster)) return false;
		
		IWand wandPlayerHand = WandRegistry.getWandByItemStack(caster.getHeldItemMainhand());
		if (wandPlayerHand == null) return false;
		
		if (WandRegistry.isWandRightForSpell(wandPlayerHand, spell))
		{
			ItemStack casterOffHand = caster.getHeldItemOffhand();
			if (ItemStackUtil.isNullStack(casterOffHand)) return false;
			
			if (spell.isItemOffHandRightForSpell(casterOffHand))
			{
				checkQuestProgress(caster);
				
				if (hasQuest(caster))
				{
					if (ItemStackUtil.getStackSize(casterOffHand) >= 
							ItemStackUtil.getStackSize(spell.getRequiredStackOffHand()))
					{
						if (howManyTimesCasted == 1)
						{
							if (castRightSpell(caster, pos, face, hitVec))
							{
								ItemStackUtil.decreaseStackSize(casterOffHand, ItemStackUtil.getStackSize(spell.getRequiredStackOffHand()));
								howManyTimesCasted++;
								return true;
							}
						}
						else
							howManyTimesCasted = 1;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Method for casting the right spell
	 */
	public boolean castRightSpell(EntityPlayer caster, BlockPos pos, EnumFacing face, Vector3D hitVec)
	{
		List<ISpell> spells = SpellRegistry.getSpells();
		for (int i = 0; i < spells.size(); ++i)
		{
			ISpell iSpell = spells.get(i);
			if (spell.getSpellID() == iSpell.getSpellID())
				if (spell.getWand().getWandID() == iSpell.getWand().getWandID())
					if (!MinecraftForge.EVENT_BUS.post(new EventSpell.Cast(iSpell, caster, caster.world)))
						return SpellRegistry.castSpellById(spell.getSpellID(), caster, pos, face, hitVec);
		}
		return false;
	}
}