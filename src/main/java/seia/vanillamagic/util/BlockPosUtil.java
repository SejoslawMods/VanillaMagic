package seia.vanillamagic.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.core.VanillaMagic;

/**
 * Class which store various methods connected with Block Position.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class BlockPosUtil {
	public static final int SEARCH_RADIUS = 100;

	private BlockPosUtil() {
	}

	public static BlockPos getEntityBlockPos(Entity entity) {
		return new BlockPos(entity.posX, entity.posY, entity.posZ);
	}

	public static int distanceInLine(BlockPos pos1, BlockPos pos2) {
		if ((pos1.getX() == pos2.getX()) && (pos1.getY() == pos2.getY())) {
			return Math.max(pos1.getZ(), pos2.getZ()) - Math.min(pos1.getZ(), pos2.getZ());
		} else if ((pos1.getZ() == pos2.getZ()) && (pos1.getY() == pos2.getY())) {
			return Math.max(pos1.getX(), pos2.getX()) - Math.min(pos1.getX(), pos2.getX());
		} else {
			return Math.max(pos1.getY(), pos2.getY()) - Math.min(pos1.getY(), pos2.getY());
		}
	}

	public static BlockPos copyPos(BlockPos toCopy) {
		return new BlockPos(toCopy.getX(), toCopy.getY(), toCopy.getZ());
	}

	public static boolean isSameBlockPos(BlockPos pos1, BlockPos pos2) {
		return (pos1.getX() == pos2.getX()) && (pos1.getY() == pos2.getY()) && (pos1.getZ() == pos2.getZ());
	}

	public static void printCoords(BlockPos pos) {
		VanillaMagic.logInfo(" X = " + pos.getX());
		VanillaMagic.logInfo(" Y = " + pos.getY());
		VanillaMagic.logInfo(" Z = " + pos.getZ());
	}

	public static void printCoords(Level level, String text, BlockPos pos) {
		VanillaMagic.log(level, text);
		printCoords(pos);
	}

	public static void printCoords(Level level, Block block, BlockPos pos) {
		VanillaMagic.log(level, block.toString());
		printCoords(pos);
	}

	public static void printCoords(Level level, World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		printCoords(level, block, pos);

		VanillaMagic.logInfo(" Dimension = " + WorldUtil.getDimensionID(world));
	}

	public static void printCoords(Level level, EntityPlayer player, BlockPos pos) {
		printCoords(level, player.world, pos);

		VanillaMagic.logInfo(" Dimension = " + WorldUtil.getDimensionID(player));
	}

	public static void freezeNearby(Entity entity, World world, BlockPos pos, int size) {
		BlockPos.MutableBlockPos blockPosCenter = new BlockPos.MutableBlockPos(0, 0, 0);

		for (BlockPos.MutableBlockPos blockPosAround : BlockPos.getAllInBoxMutable(
				pos.add((double) (-size), -1.0D, (double) (-size)), pos.add((double) size, -1.0D, (double) size))) {
			if (blockPosAround.distanceSqToCenter(entity.posX, entity.posY, entity.posZ) <= (double) (size * size)) {
				blockPosCenter.setPos(blockPosAround.getX(), blockPosAround.getY() + 1, blockPosAround.getZ());
				IBlockState blockCenterState = world.getBlockState(blockPosCenter);

				if (blockCenterState.getMaterial() == Material.AIR) {
					IBlockState blockAroundState = world.getBlockState(blockPosAround);

					if (blockAroundState.getMaterial() == Material.WATER
							&& ((Integer) blockAroundState.getValue(BlockLiquid.LEVEL)).intValue() == 0
							&& Blocks.ICE.canPlaceBlockAt(world, blockPosAround.toImmutable())) {
						world.setBlockState(blockPosAround, Blocks.ICE.getDefaultState());
					}
				}
			}
		}
	}

	public static List<BlockPos> getBlockPos3x3XZAroundCenter(int centerX, int centerY, int centerZ) {
		List<BlockPos> blocks = new ArrayList<BlockPos>();

		blocks.add(new BlockPos(centerX - 1, centerY, centerZ + 1)); // top-left
		blocks.add(new BlockPos(centerX, centerY, centerZ + 1)); // top
		blocks.add(new BlockPos(centerX + 1, centerY, centerZ + 1)); // top-right
		blocks.add(new BlockPos(centerX + 1, centerY, centerZ)); // right
		blocks.add(new BlockPos(centerX + 1, centerY, centerZ - 1)); // bottom-right
		blocks.add(new BlockPos(centerX, centerY, centerZ - 1)); // bottom
		blocks.add(new BlockPos(centerX - 1, centerY, centerZ - 1)); // bottom-left
		blocks.add(new BlockPos(centerX - 1, centerY, centerZ)); // left

		return blocks;
	}

	public static List<BlockPos> getBlockPos3x3XZAroundCenterWithCenter(int centerX, int centerY, int centerZ) {
		List<BlockPos> blocks = getBlockPos3x3XZAroundCenter(centerX, centerY, centerZ);

		blocks.add(new BlockPos(centerX, centerY, centerZ)); // center

		return blocks;
	}

	public static List<BlockPos> drawCircle2DXZ(int centerX, int posY, int centerZ, int radius, Block fillWith) {
		List<BlockPos> toFill = new ArrayList<BlockPos>();

		int d = (5 - radius * 4) / 4;
		int x = 0;
		int z = radius;

		do {
			toFill.add(new BlockPos(centerX + x, posY, centerZ + z));
			toFill.add(new BlockPos(centerX + x, posY, centerZ + z));
			toFill.add(new BlockPos(centerX + x, posY, centerZ - z));
			toFill.add(new BlockPos(centerX - x, posY, centerZ + z));
			toFill.add(new BlockPos(centerX - x, posY, centerZ - z));
			toFill.add(new BlockPos(centerX + z, posY, centerZ + x));
			toFill.add(new BlockPos(centerX + z, posY, centerZ - x));
			toFill.add(new BlockPos(centerX - z, posY, centerZ + x));
			toFill.add(new BlockPos(centerX - z, posY, centerZ - x));

			if (d < 0) {
				d += 2 * x + 1;
			} else {
				d += 2 * (x - z) + 1;
				z--;
			}

			x++;
		} while (x <= z);

		return toFill;
	}
}