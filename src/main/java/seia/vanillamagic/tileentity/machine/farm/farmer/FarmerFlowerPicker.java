package seia.vanillamagic.tileentity.machine.farm.farmer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.tileentity.machine.farm.HarvestResult;
import seia.vanillamagic.tileentity.machine.farm.IHarvestResult;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerFlowerPicker implements IFarmer {
	protected final List<ItemStack> flowers;

	public FarmerFlowerPicker(List<ItemStack> flowers) {
		this.flowers = flowers;
	}

	public FarmerFlowerPicker add(ItemStack flowers) {
		this.flowers.add(flowers);
		return this;
	}

	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) {
		return false;
	}

	public boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state) {
		return block instanceof BlockFlower;
	}

	public boolean canPlant(ItemStack stack) {
		return false;
	}

	@Nullable
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) {
		World worldObj = farm.getWorld();
		NonNullList<ItemStack> drops = null;

		if (block instanceof BlockFlower) {
			drops = NonNullList.create();
			drops.add(new ItemStack(block));
			farm.damageShears(block, pos);
		} else {
			if (!farm.hasHoe()) {
				return null;
			}

			block.getDrops(drops, worldObj, pos, state, farm.getMaxLootingValue());
			farm.damageHoe(1, pos);
		}

		List<EntityItem> result = new ArrayList<EntityItem>();

		if (drops != null) {
			for (ItemStack stack : drops) {
				result.add(
						new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
			}
		}

		worldObj.setBlockToAir(pos);
		return new HarvestResult(result, pos);
	}
}