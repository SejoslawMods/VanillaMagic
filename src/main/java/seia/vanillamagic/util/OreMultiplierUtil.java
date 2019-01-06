package seia.vanillamagic.util;

import net.minecraft.block.BlockFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class which store various methods connected with Ore Multiplier. Checking
 * structure, etc.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class OreMultiplierUtil {
	private OreMultiplierUtil() {
	}

	public static boolean check(World world, BlockPos cauldronPos) {
		BlockPos furnaceNorth = cauldronPos.offset(EnumFacing.NORTH);
		BlockPos furnaceSouth = cauldronPos.offset(EnumFacing.SOUTH);
		BlockPos furnaceEast = cauldronPos.offset(EnumFacing.EAST);
		BlockPos furnaceWest = cauldronPos.offset(EnumFacing.WEST);

		return world.getBlockState(furnaceNorth).getBlock() instanceof BlockFurnace
				&& world.getBlockState(furnaceSouth).getBlock() instanceof BlockFurnace
				&& world.getBlockState(furnaceEast).getBlock() instanceof BlockFurnace
				&& world.getBlockState(furnaceWest).getBlock() instanceof BlockFurnace;
	}
}