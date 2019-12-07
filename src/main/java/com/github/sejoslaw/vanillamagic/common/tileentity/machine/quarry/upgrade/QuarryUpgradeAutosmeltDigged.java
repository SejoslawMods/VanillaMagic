package com.github.sejoslaw.vanillamagic.tileentity.machine.quarry.upgrade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.IQuarryUpgrade;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.SmeltingUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryUpgradeAutosmeltDigged implements IQuarryUpgrade {
	public String getUpgradeName() {
		return "Autosmelt";
	}

	public Block getBlock() {
		return Blocks.FURNACE;
	}

	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos,
			IBlockState workingPosState) {
		List<ItemStack> list = new ArrayList<ItemStack>();
		ItemStack burnedStack = SmeltingUtil.getSmeltingResultAsNewStack(new ItemStack(blockToDig));

		if (!ItemStackUtil.isNullStack(burnedStack)) {
			list.add(burnedStack);
		}

		return list;
	}
}