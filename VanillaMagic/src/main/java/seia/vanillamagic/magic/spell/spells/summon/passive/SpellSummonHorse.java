package seia.vanillamagic.magic.spell.spells.summon.passive;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.magic.wand.IWand;

public class SpellSummonHorse extends SpellSummonPassive 
{
	public SpellSummonHorse(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) 
	{
		Entity entity = null;
		int rand = new Random().nextInt(100);
		if(rand < 30)
		{
			entity = new EntityDonkey(world);
		}
		else if(rand > 30 && rand < 60)
		{
			entity = new EntityMule(world);
		}
		else
		{
			entity = new EntityHorse(world);
		}
		return entity;
	}
}