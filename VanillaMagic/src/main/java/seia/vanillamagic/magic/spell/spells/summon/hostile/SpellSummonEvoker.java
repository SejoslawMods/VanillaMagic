package seia.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.magic.wand.IWand;

public class SpellSummonEvoker extends SpellSummonHostile 
{
	public SpellSummonEvoker(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) 
	{
		return new EntityEvoker(world);
	}
}