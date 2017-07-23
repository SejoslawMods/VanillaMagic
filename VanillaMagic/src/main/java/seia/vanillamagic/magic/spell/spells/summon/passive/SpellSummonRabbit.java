package seia.vanillamagic.magic.spell.spells.summon.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import seia.vanillamagic.api.magic.IWand;

public class SpellSummonRabbit extends SpellSummonPassive 
{
	public SpellSummonRabbit(int spellID, String spellName, String spellUniqueName, IWand wand, 
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) 
	{
		return new EntityRabbit(world);
	}
}