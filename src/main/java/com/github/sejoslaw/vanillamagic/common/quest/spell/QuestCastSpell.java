package com.github.sejoslaw.vanillamagic.common.quest.spell;

import com.github.sejoslaw.vanillamagic.api.event.EventSpell;
import com.github.sejoslaw.vanillamagic.api.magic.ISpell;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.SpellRegistry;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * Base Quest for casting Spells.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class QuestCastSpell extends Quest {
    /**
     * Spell which should be casted.
     */
    protected ISpell spell;

    public int timesCaster = 1;

    public void readData(JsonObject jo) {
        this.spell = SpellRegistry.getSpellById(jo.get("spellID").getAsInt());
        this.icon = spell.getRequiredStackOffHand().copy();
        this.questName = spell.getSpellName();
        this.uniqueName = spell.getSpellUniqueName();

        super.readData(jo);
    }

    /**
     * @return Returns the Spell for this Quest.
     */
    public ISpell getSpell() {
        return spell;
    }

    /**
     * Method for checking if is it possible to cast current Spell.
     */
    public boolean castSpell(PlayerEntity caster, Hand hand, ItemStack inHand, BlockPos pos, Direction face, Vec3d hitVec) {
        if (!finishedAdditionalQuests(caster)) {
            return false;
        }

        IWand wandPlayerHand = WandRegistry.getWandByItemStack(caster.getHeldItemMainhand());
        ItemStack casterOffHand = caster.getHeldItemOffhand();

        if (wandPlayerHand == null ||
                !WandRegistry.isWandRightForSpell(wandPlayerHand, spell) ||
                ItemStackUtil.isNullStack(casterOffHand) ||
                !spell.isItemOffHandRightForSpell(casterOffHand)) {
            return false;
        }

        checkQuestProgress(caster);

        if (!hasQuest(caster) || (ItemStackUtil.getStackSize(casterOffHand) >= ItemStackUtil.getStackSize(spell.getRequiredStackOffHand()))) {
            return false;
        }

        if ((timesCaster == 1) && castRightSpell(caster, pos, face, hitVec)) {
            ItemStackUtil.decreaseStackSize(casterOffHand, ItemStackUtil.getStackSize(spell.getRequiredStackOffHand()));
            timesCaster++;
            return true;
        } else {
            timesCaster = 1;
        }

        return false;
    }

    /**
     * Method for casting the right spell
     */
    public boolean castRightSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        List<ISpell> spells = SpellRegistry.getSpells();
        for (ISpell iSpell : spells) {
            if ((spell.getSpellID() == iSpell.getSpellID()) &&
                    (spell.getWand().getWandID() == iSpell.getWand().getWandID()) &&
                    !EventUtil.postEvent(new EventSpell.Cast(iSpell, caster, caster.world))) {
                return SpellRegistry.castSpellById(spell.getSpellID(), caster, pos, face, hitVec);
            }
        }
        return false;
    }
}