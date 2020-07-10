package com.github.sejoslaw.vanillamagic.common.item.liquidsuppressioncrystal;

import com.github.sejoslaw.vanillamagic.common.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.common.tileentity.CustomTileEntity;
import com.github.sejoslaw.vanillamagic.common.util.NBTUtil;
import com.github.sejoslaw.vanillamagic.core.VMTileEntities;
import com.google.common.base.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Class which represents Tile which is placed to hide liquid source block.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileLiquidSuppression extends CustomTileEntity {
	public static final String REGISTRY_NAME = TileLiquidSuppression.class.getName();

	public int ticksRemaining;
	public String containedBlockName;
	public int containedBlockMeta;

	public TileLiquidSuppression() {
		super(VMTileEntities.LIQUID_SUPPRESSION);
	}

	public void tick() {
		if (world.isRemote) {
			return;
		}

		ticksRemaining--;

		if (ticksRemaining <= 0) {
			returnContainedBlock();
		}
	}

	public void returnContainedBlock() {
		Block block = null;

		if (!Strings.isNullOrEmpty(containedBlockName)) {
			block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(containedBlockName));
		}

		if ((block != null) && world.setBlockState(pos, Block.getStateById(containedBlockMeta))) {
			BlockState blockState = this.getWorld().getBlockState(getPos());
			getWorld().notifyBlockUpdate(getPos(), blockState, blockState, 3);
			CustomTileEntityHandler.removeCustomTileEntityAtPos(world, getPos());
		}
	}

	public void setContainedBlockInfo(BlockState state) {
		containedBlockName = state.getBlock().getRegistryName().toString();
		containedBlockMeta = Block.getStateId(state);
	}

	public void read(CompoundNBT tagCompound) {
		super.read(tagCompound);

		ticksRemaining = tagCompound.getInt(NBTUtil.NBT_TICKS_REMAINING);
		containedBlockName = tagCompound.getString(NBTUtil.NBT_BLOCK_NAME);
		containedBlockMeta = tagCompound.getInt(NBTUtil.NBT_BLOCK_STATE);
	}

	public CompoundNBT write(CompoundNBT tagCompound) {
		super.write(tagCompound);

		tagCompound.putInt(NBTUtil.NBT_TICKS_REMAINING, ticksRemaining);
		tagCompound.putString(NBTUtil.NBT_BLOCK_NAME, Strings.isNullOrEmpty(containedBlockName) ? "" : containedBlockName);
		tagCompound.putInt(NBTUtil.NBT_BLOCK_STATE, containedBlockMeta);

		return tagCompound;
	}

	public void resetDuration(int refresh) {
		if (ticksRemaining < refresh) {
			ticksRemaining = refresh;
		}
	}

	public void setDuration(int duration) {
		ticksRemaining = duration;
	}

	public static TileLiquidSuppression createAirBlock(World world, BlockPos blockPos, int duration) {
		if (world.isAirBlock(blockPos)) {
			return null;
		}

		BlockState cachedState = world.getBlockState(blockPos);
		world.setBlockState(blockPos, Blocks.AIR.getDefaultState());

		TileLiquidSuppression tile = new TileLiquidSuppression();
		tile.setup(world, blockPos);
		tile.setContainedBlockInfo(cachedState);
		tile.setDuration(duration);

		world.setTileEntity(blockPos, tile);

		return tile;
	}
}