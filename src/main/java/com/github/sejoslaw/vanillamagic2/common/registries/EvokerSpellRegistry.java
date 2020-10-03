package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.spells.evokers.EvokerSpell;
import com.github.sejoslaw.vanillamagic2.common.spells.evokers.EvokerSpellFangs;
import com.github.sejoslaw.vanillamagic2.common.spells.evokers.EvokerSpellSummonVex;
import com.github.sejoslaw.vanillamagic2.common.spells.evokers.EvokerSpellWololo;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EvokerSpellRegistry {
    private static final class EvokerSpellDefinition {
        public final String translationKey;
        public final EvokerSpell spell;

        private EvokerSpellDefinition(String translationKey, EvokerSpell spell) {
            this.translationKey = translationKey;
            this.spell = spell;
        }
    }

    private static final List<EvokerSpellDefinition> SPELLS = new ArrayList<>();

    public static void initialize() {
        SPELLS.add(new EvokerSpellDefinition("spell.evoker.wololo.name", new EvokerSpellWololo()));
        SPELLS.add(new EvokerSpellDefinition("spell.evoker.summonVex.name", new EvokerSpellSummonVex()));
        SPELLS.add(new EvokerSpellDefinition("spell.evoker.fangs.name", new EvokerSpellFangs()));
    }

    public static void changeSpell(ItemStack stack) {
        CompoundNBT nbt = stack.getOrCreateTag();

        int spellId = nbt.getInt(NbtUtils.NBT_SPELL_ID);
        spellId = spellId + 1 >= SPELLS.size() ? 0 : spellId + 1;
        nbt.putInt(NbtUtils.NBT_SPELL_ID, spellId);

        String displayNamePrefix = TextUtils.getFormattedText("vmitem." + nbt.getString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME) + ".displayName");
        String displayNamePostfix = TextUtils.getFormattedText(SPELLS.get(spellId).translationKey);
        stack.setDisplayName(TextUtils.toComponent(displayNamePrefix + ": " + displayNamePostfix));
    }

    public static void castSpell(IWorld world, PlayerEntity player, ItemStack stack) {
        Entity target = EntityUtils.getLookingAt(player);
        int spellId = stack.getOrCreateTag().getInt(NbtUtils.NBT_SPELL_ID);
        SPELLS.get(spellId).spell.cast(world, player, target);
    }
}
