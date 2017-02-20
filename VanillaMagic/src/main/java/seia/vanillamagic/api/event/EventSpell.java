package seia.vanillamagic.api.event;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import seia.vanillamagic.api.event.EventSpell.Cast.EvokerSpell;
import seia.vanillamagic.api.magic.IEvokerSpell;
import seia.vanillamagic.api.magic.ISpell;

/**
 * Base for all basic spell-related Events.<br>
 * <br>
 * NOT FOR EVOKER SPELL !!! For those spells see {@link EvokerSpell}
 */
public class EventSpell extends EventPlayerOnWorld 
{
	private final ISpell _spell;
	
	public EventSpell(ISpell spell, EntityPlayer caster, World world)
	{
		super(caster, world);
		this._spell = spell;
	}
	
	/**
	 * @return Returns casted Spell.
	 */
	public ISpell getSpell()
	{
		return _spell;
	}
	
	/**
	 * This Event is fired BEFORE ANY Spell is casted.
	 */
	public static class Cast extends EventSpell
	{
		public Cast(ISpell spell, EntityPlayer caster, World world) 
		{
			super(spell, caster, world);
		}

		/**
		 * This Event is fired before Player casts Evoker Spell.
		 */
		public static class EvokerSpell extends EventPlayerOnWorld
		{
			private final Entity _target;
			private final IEvokerSpell _spell;
			
			public EvokerSpell(EntityPlayer player, World world, @Nullable Entity target, IEvokerSpell spell) 
			{
				super(player, world);
				this._target = target;
				this._spell = spell;
			}
			
			/**
			 * @return Returns Entity on which the spell was casted.
			 */
			public Entity getTarget()
			{
				return _target;
			}
			
			/**
			 * @return Returns casted spell.
			 */
			public IEvokerSpell getSpell()
			{
				return _spell;
			}
		}
		
		/**
		 * This Event is fired BEFORE the summoned Entity is spawn on World.
		 */
		public static class SummonSpell extends Cast
		{
			private final Entity _summonedEntity;
			
			public SummonSpell(ISpell spell, EntityPlayer caster, World world,
					Entity summonedEntity) 
			{
				super(spell, caster, world);
				this._summonedEntity = summonedEntity;
			}
			
			/**
			 * @return Returns the summoned Entity which will be spawned on World.
			 */
			public Entity getSummonedEntity()
			{
				return _summonedEntity;
			}
		}
	}
}