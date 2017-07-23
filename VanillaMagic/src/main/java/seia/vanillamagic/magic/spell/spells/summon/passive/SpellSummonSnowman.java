package seia.vanillamagic.magic.spell.spells.summon.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;

public class SpellSummonSnowman extends SpellSummonPassive 
{
	public SpellSummonSnowman(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) 
	{
		return new EntitySnowman(world);
	}
}