package com.github.sejoslaw.vanillamagic.api.event;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.event.EventSpell.Cast.EvokerSpell;
import com.github.sejoslaw.vanillamagic.api.magic.IEvokerSpell;
import com.github.sejoslaw.vanillamagic.api.magic.ISpell;

/**
 * Base for all basic spell-related Events.<br>
 * <br>
 * NOT FOR EVOKER SPELL !!! For those spells see {@link EvokerSpell}
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventSpell extends EventPlayerOnWorld {
	private final ISpell spell;

	public EventSpell(ISpell spell, PlayerEntity caster, World world) {
		super(caster, world);
		this.spell = spell;
	}

	/**
	 * @return Returns casted Spell.
	 */
	public ISpell getSpell() {
		return spell;
	}

	/**
	 * This Event is fired BEFORE ANY Spell is casted.
	 */
	public static class Cast extends EventSpell {
		public Cast(ISpell spell, PlayerEntity caster, World world) {
			super(spell, caster, world);
		}

		/**
		 * This Event is fired before Player casts Evoker Spell.
		 */
		public static class EvokerSpell extends EventPlayerOnWorld {
			private final Entity target;
			private final IEvokerSpell spell;

			public EvokerSpell(PlayerEntity player, World world, @Nullable Entity target, IEvokerSpell spell) {
				super(player, world);
				this.target = target;
				this.spell = spell;
			}

			/**
			 * @return Returns Entity on which the spell was casted.
			 */
			public Entity getTarget() {
				return target;
			}

			/**
			 * @return Returns casted spell.
			 */
			public IEvokerSpell getSpell() {
				return spell;
			}
		}

		/**
		 * This Event is fired BEFORE the summoned Entity is spawn on World.
		 */
		public static class SummonSpell extends Cast {
			private final Entity summonedEntity;

			public SummonSpell(ISpell spell, PlayerEntity caster, World world, Entity summonedEntity) {
				super(spell, caster, world);
				this.summonedEntity = summonedEntity;
			}

			/**
			 * @return Returns the summoned Entity which will be spawned on World.
			 */
			public Entity getSummonedEntity() {
				return summonedEntity;
			}
		}
	}
}