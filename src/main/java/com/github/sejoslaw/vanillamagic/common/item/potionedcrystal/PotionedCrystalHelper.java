package com.github.sejoslaw.vanillamagic.common.item.potionedcrystal;

import com.github.sejoslaw.vanillamagic.common.util.CauldronUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Class which add various methods to work with PotionedCrystals.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PotionedCrystalHelper {
	private PotionedCrystalHelper() {
	}

	/**
	 * @return Returns the name of the Potion from given PotionType.
	 */
	public static String getPotionTypeName(Potion potion) {
		return ForgeRegistries.POTION_TYPES.getKey(potion).getPath();
	}

	/**
	 * @return Returns the IPotionedCrystal from ItemPotion. Used mainly during
	 *         Cauldron Crafting.
	 */
	@Nullable
	public static IPotionedCrystal getPotionedCrystalFromCauldron(World world, BlockPos cauldronPos) {
		List<ItemEntity> itemsInCauldron = CauldronUtil.getItemsInCauldron(world, cauldronPos);

		for (ItemEntity ei : itemsInCauldron) {
			ItemStack stack = ei.getItem();

			if (ItemStackUtil.isNullStack(stack)) {
				return null;
			}

			if (!(stack.getItem() instanceof PotionItem)) {
				continue;
			}

			Potion potion = PotionUtils.getPotionFromItem(stack);
			String ptName = getPotionTypeName(potion);

			for (IPotionedCrystal pc : VMItems.POTIONED_CRYSTALS) {
				if (pc.getPotionUnlocalizedName().equals(ptName)) {
					return pc;
				}
			}
		}

		return null;
	}

	/**
	 * Given ItemStack is a PotionedCrystal - Nether Star.
	 * 
	 * @return Returns the PotionedCrystal from given ItemStack.
	 */
	@Nullable
	public static IPotionedCrystal getPotionedCrystal(ItemStack netherStarStack) {
		if (ItemStackUtil.isNullStack(netherStarStack)) {
			return null;
		}

		CompoundNBT stackTag = netherStarStack.getTag();

		if ((stackTag == null) || !stackTag.hasUniqueId(IPotionedCrystal.NBT_POTION_TYPE_NAME)) {
			return null;
		}

		String name = stackTag.getString(IPotionedCrystal.NBT_POTION_TYPE_NAME);

		for (IPotionedCrystal pc : VMItems.POTIONED_CRYSTALS) {
			if (name.equals(pc.getPotionUnlocalizedName())) {
				return pc;
			}
		}

		return null;
	}

	public static void initialize() {
		for (Potion potion : ForgeRegistries.POTION_TYPES.getValues()) {
			VMItems.POTIONED_CRYSTALS.add(() -> potion);
		}

		VMLogger.logInfo("Registered Potioned Crystals: " + VMItems.POTIONED_CRYSTALS.size());
	}
}
