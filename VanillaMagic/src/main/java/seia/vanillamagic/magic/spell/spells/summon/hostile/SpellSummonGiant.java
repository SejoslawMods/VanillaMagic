package seia.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.magic.wand.IWand;

public class SpellSummonGiant extends SpellSummonHostile 
{
	public SpellSummonGiant(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) 
	{
		return new EntityGiantZombie(world);
	}
}