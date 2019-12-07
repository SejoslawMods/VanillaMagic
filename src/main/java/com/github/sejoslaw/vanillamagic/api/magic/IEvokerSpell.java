package com.github.sejoslaw.vanillamagic.api.magic;

import net.minecraft.entity.Entity;

/**
 * Base definition for Evoker Spell.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IEvokerSpell {
    /**
     * @return Returns the unique ID for this Spell.
     */
    int getSpellID();

    /**
     * @return Returns the name of this Spell.
     */
    String getSpellName();

    /**
     * What should happen when the Spell is casted.
     */
    void castSpell(Entity caster, Entity target);
}