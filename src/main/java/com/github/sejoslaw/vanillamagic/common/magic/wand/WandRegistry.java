package com.github.sejoslaw.vanillamagic.common.magic.wand;

import com.github.sejoslaw.vanillamagic.api.magic.ISpell;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.util.TranslationUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Registry containing all data about currently registered Wands.
 */
public final class WandRegistry {
    public static final List<IWand> WANDS = new ArrayList<>();

    public static final IWand WAND_STICK = new Wand(1, new ItemStack(Items.STICK), TranslationUtil.translateToLocal("wand.stick"));
    public static final IWand WAND_BLAZE_ROD = new Wand(2, new ItemStack(Items.BLAZE_ROD), TranslationUtil.translateToLocal("wand.blazeRod"));
    public static final IWand WAND_NETHER_STAR = new Wand(3, new ItemStack(Items.NETHER_STAR), TranslationUtil.translateToLocal("wand.netherStar"));
    public static final IWand WAND_END_ROD = new Wand(4, new ItemStack(Blocks.END_ROD), TranslationUtil.translateToLocal("wand.endRod"));

    private WandRegistry() {
    }

    /**
     * @param wand Wand that should be added to the Wand Registry.
     */
    public static void addWand(IWand wand) {
        WANDS.add(wand);
    }

    /**
     * @param player - player we are checking
     * @return Returns the Wand which player has got in main hand - null if the item
     * is not a Wand
     */
    @Nullable
    public static IWand isWandInMainHand(PlayerEntity player) {
        return getWandByItemStack(player.getHeldItemMainhand());
    }

    /**
     * @param player                  - player we are checking
     * @param requiredWandMinimalTier - minimal tier of wark thet we want to finish
     * @return Returns true if the player has got in hand the Wand that can do the
     * work
     */
    public static boolean isWandInMainHandRight(PlayerEntity player, int requiredWandMinimalTier) {
        IWand wandInMainHand = isWandInMainHand(player);

        if (wandInMainHand == null) {
            return false;
        }

        return wandInMainHand.canWandDoWork(requiredWandMinimalTier);
    }

    /**
     * @return Returns the Wand from given ItemStack.
     */
    @Nullable
    public static IWand getWandByItemStack(ItemStack inHand) {
        for (IWand currentlyCheckingEnumWand : WANDS) {
            ItemStack currentlyCheckingWand = currentlyCheckingEnumWand.getWandStack();

            if (ItemStack.areItemsEqual(currentlyCheckingWand, inHand)) {
                return currentlyCheckingEnumWand;
            }
        }

        return null;
    }

    /**
     * @return Returns the Wand which given Player held in MainHand.
     */
    @Nullable
    public static IWand getCasterWand(PlayerEntity caster) {
        return getWandByItemStack(caster.getHeldItemMainhand());
    }

    /**
     * @return Returns TRUE if the given Wands are the same.
     */
    public static boolean areWandsEqual(IWand wand1, IWand wand2) {
        return areWandsEqual(wand1.getWandStack(), wand2.getWandStack());
    }

    public static boolean areWandsEqual(ItemStack wand1, ItemStack wand2) {
        return ItemStack.areItemsEqual(wand1, wand2);
    }

    /**
     * @return Returns TRUE if the given Wand is right to cast given Spell.
     */
    public static boolean isWandRightForSpell(IWand wandPlayerHand, ISpell spell) {
        return wandPlayerHand.canWandDoWork(spell.getWand().getWandID());
    }

    /**
     * Try to use {@link #getWandByItemStack(ItemStack)} or
     * {@link #getCasterWand(PlayerEntity)}
     *
     * @return Returns Wand by it's ID.
     */
    @Nullable
    public static IWand getWandByTier(int wandID) {
        for (IWand currentlyCheckingEnumWand : WANDS) {
            if (currentlyCheckingEnumWand.getWandID() == wandID) {
                return currentlyCheckingEnumWand;
            }
        }

        return null;
    }
}
