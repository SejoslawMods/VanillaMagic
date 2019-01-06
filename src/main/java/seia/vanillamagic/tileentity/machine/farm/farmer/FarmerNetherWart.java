package seia.vanillamagic.tileentity.machine.farm.farmer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerNetherWart extends FarmerCustomSeed {
	public FarmerNetherWart() {
		super(Blocks.NETHER_WART, 3, new ItemStack(Items.NETHER_WART));
	}

	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) {
		if (!farm.isOpen(pos)) {
			return false;
		}

		return plantFromInventory(farm, pos);
	}
}