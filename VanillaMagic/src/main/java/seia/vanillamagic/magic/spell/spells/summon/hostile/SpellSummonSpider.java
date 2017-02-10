package seia.vanillamagic.magic.spell.spells.summon.hostile;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.magic.wand.IWand;

public class SpellSummonSpider extends SpellSummonHostile 
{
	public SpellSummonSpider(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) 
	{
		Entity entity = null;
		int percent = new Random().nextInt(100);
		if(percent < 50)
		{
			entity = new EntitySpider(world);
		}
		else
		{
			entity = new EntityCaveSpider(world);
		}
		return entity;
	}
}