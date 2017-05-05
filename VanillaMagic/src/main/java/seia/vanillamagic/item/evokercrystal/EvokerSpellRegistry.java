package seia.vanillamagic.item.evokercrystal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.api.magic.IEvokerSpell;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.item.evokercrystal.spell.EvokerSpellFangAttack;
import seia.vanillamagic.item.evokercrystal.spell.EvokerSpellSummonVex;
import seia.vanillamagic.item.evokercrystal.spell.EvokerSpellWololo;

/**
 * Class which contains data about all registered Evoker Spells.
 */
public class EvokerSpellRegistry 
{
	/**
	 * List with all registered Evoker Spells.
	 */
	private static List<IEvokerSpell> _SPELLS = new ArrayList<>();
	
	// Spells
	private static IEvokerSpell SPELL_FANGS = new EvokerSpellFangAttack();
	private static IEvokerSpell SPELL_SUMMON_VEX = new EvokerSpellSummonVex();
	private static IEvokerSpell SPELL_WOLOLO = new EvokerSpellWololo();
	
	private EvokerSpellRegistry()
	{
	}
	
	/**
	 * Register new Evoker Spell.
	 */
	public static void add(IEvokerSpell spell)
	{
		_SPELLS.add(spell);
	}
	
	/**
	 * @return Returns all Evoker Spells list.
	 */
	public static List<IEvokerSpell> getEvokerSpells()
	{
		return _SPELLS;
	}
	
	/**
	 * @return Returns the Evoker Spell from the given ID.
	 */
	@Nullable
	public static IEvokerSpell getSpell(int spellID)
	{
		for(IEvokerSpell spell : _SPELLS)
		{
			if(spell.getSpellID() == spellID)
			{
				return spell;
			}
		}
		return null;
	}
	
	/**
	 * @return Returns the current Evoker Spell from given ItemStack.
	 */
	@Nullable
	public static IEvokerSpell getCurrentSpell(ItemStack stack)
	{
		if(VanillaMagicItems.isCustomItem(stack, VanillaMagicItems.EVOKER_CRYSTAL))
		{
			NBTTagCompound stackTag = stack.getTagCompound();
			int spellID = stackTag.getInteger(ItemEvokerCrystal.NBT_SPELL_ID);
			return getSpell(spellID);
		}
		return null;
	}
	
	/**
	 * Change Evoker Spell to next one (on shift-right-click).
	 */
	public static void changeSpell(EntityPlayer player, ItemStack crystal)
	{
		if(player == null || crystal == null)
		{
			return;
		}
		IEvokerSpell currentSpell = getCurrentSpell(crystal);
		int spellID = currentSpell.getSpellID();
		spellID++;
		IEvokerSpell nextSpell = getSpell(spellID);
		if(nextSpell == null)
		{
			nextSpell = getSpell(1);
		}
		ItemStack newCrystal = crystal.copy();
		newCrystal.getTagCompound().setInteger(ItemEvokerCrystal.NBT_SPELL_ID, nextSpell.getSpellID());
		newCrystal.setStackDisplayName(VanillaMagicItems.EVOKER_CRYSTAL.getItemName() + ": " + nextSpell.getSpellName());
		player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, newCrystal);
	}
}