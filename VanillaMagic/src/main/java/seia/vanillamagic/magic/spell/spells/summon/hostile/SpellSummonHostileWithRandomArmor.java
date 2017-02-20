package seia.vanillamagic.magic.spell.spells.summon.hostile;

import java.util.Random;

import net.minecraft.item.ItemStack;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.config.VMConfig;

public abstract class SpellSummonHostileWithRandomArmor extends SpellSummonHostile
{
	public SpellSummonHostileWithRandomArmor(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}
	
	public boolean getSpawnWithArmor()
	{
		int rand = new Random().nextInt(100);
		if(rand < VMConfig.percentForSpawnWithArmor)
		{
			return true;
		}
		return false;
	}
}