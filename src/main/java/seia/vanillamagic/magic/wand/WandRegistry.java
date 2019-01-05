package seia.vanillamagic.magic.wand;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.api.magic.ISpell;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.util.TextUtil;

/**
 * Registry containing all data about currently registered Wands.
 */
public final class WandRegistry {
	// All registered Wands.
	private static final List<IWand> WANDS = new ArrayList<>();

	/** basic wand - Stick Wand */
	public static final IWand WAND_STICK = new Wand(1, new ItemStack(Items.STICK),
			TextUtil.translateToLocal("wand.stick"));
	/** the most common wand - Blaze Wand */
	public static final IWand WAND_BLAZE_ROD = new Wand(2, new ItemStack(Items.BLAZE_ROD),
			TextUtil.translateToLocal("wand.blazeRod"));
	/** used mainly for summoning and resurrecting - Nether Star */
	public static final IWand WAND_NETHER_STAR = new Wand(3, new ItemStack(Items.NETHER_STAR),
			TextUtil.translateToLocal("wand.netherStar"));
	/** ??? - End Wand */
	public static final IWand WAND_END_ROD = new Wand(4, new ItemStack(Blocks.END_ROD),
			TextUtil.translateToLocal("wand.endRod"));

	private WandRegistry() {
	}

	/**
	 * @param wand Wand that should be added to the Wand Registry.
	 */
	public static void addWand(IWand wand) {
		WANDS.add(wand);
	}

	/**
	 * @return Returns the list which contains all Wands.
	 */
	public static List<IWand> getWands() {
		return WANDS;
	}

	/**
	 * @param player - player we are checking
	 * 
	 * @return Returns the Wand which player has got in main hand - null if the item
	 *         is not a Wand
	 */
	@Nullable
	public static IWand isWandInMainHand(EntityPlayer player) {
		return getWandByItemStack(player.getHeldItemMainhand());
	}

	/**
	 * @param player - player we are checking
	 * 
	 * @return Returns the Wand which player has got in off hand - null if the item
	 *         is not a Wand
	 */
	@Nullable
	public static IWand isWandInOffHand(EntityPlayer player) {
		return getWandByItemStack(player.getHeldItemOffhand());
	}

	/**
	 * @param player                  - player we are checking
	 * @param requiredWandMinimalTier - minimal tier of wark thet we want to finish
	 * 
	 * @return Returns true if the player has got in hand the Wand that can do the
	 *         work
	 */
	public static boolean isWandInMainHandRight(EntityPlayer player, int requiredWandMinimalTier) {
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
		for (int i = 0; i < WANDS.size(); ++i) {
			IWand currentlyCheckingEnumWand = WANDS.get(i);
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
	public static IWand getCasterWand(EntityPlayer caster) {
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
	 * {@link #getCasterWand(EntityPlayer)}
	 * 
	 * @return Returns Wand by it's ID.
	 */
	@Nullable
	public static IWand getWandByTier(int wandID) {
		for (int i = 0; i < WANDS.size(); ++i) {
			IWand currentlyCheckingEnumWand = WANDS.get(i);

			if (currentlyCheckingEnumWand.getWandID() == wandID) {
				return currentlyCheckingEnumWand;
			}
		}
		return null;
	}
}