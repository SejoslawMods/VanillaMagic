package seia.vanillamagic.magic.spell.spells.summon.hostile;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;

public class SpellSummonGuardian extends SpellSummonHostile 
{
	public SpellSummonGuardian(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) 
	{
		Entity entity = null;
		int percent = new Random().nextInt(100); // 100%
		if (percent < 30) entity = new EntityElderGuardian(world); // percent with which this Guardian will be an Elder
		else entity = new EntityGuardian(world);
		return entity;
	}
}