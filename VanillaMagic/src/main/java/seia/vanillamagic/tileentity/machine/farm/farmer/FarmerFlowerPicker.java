package seia.vanillamagic.tileentity.machine.farm.farmer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import seia.vanillamagic.tileentity.machine.farm.HarvestResult;
import seia.vanillamagic.tileentity.machine.farm.IHarvestResult;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;
import seia.vanillamagic.tileentity.machine.farm.ToolType;

public class FarmerFlowerPicker implements IFarmer
{
	protected final List<ItemStack> flowers;
	
	public FarmerFlowerPicker(List<ItemStack> flowers) 
	{
		this.flowers = flowers;
	}

	public FarmerFlowerPicker add(ItemStack flowers) 
	{
		this.flowers.add(flowers);
		return this;
	}
	
	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		return false;
	}
	
	public boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		return flowers.contains(block) || block instanceof IShearable || block instanceof IPlantable;
	}
	
	public boolean canPlant(ItemStack stack) 
	{
		return false;
	}
	
	@Nullable
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		World worldObj = farm.getWorld();
		List<ItemStack> drops = null;
		if(block instanceof IShearable) 
		{
			if(!farm.hasShears()) 
			{
				return null;
			}
			ItemStack shears = farm.getTool(ToolType.SHEARS);
			if(!((IShearable) block).isShearable(shears, worldObj, pos)) 
			{
				return null;
			}
			drops = ((IShearable) block).onSheared(shears, worldObj, pos, farm.getMaxLootingValue());
			farm.damageShears(block, pos);
		}
		else if(block instanceof IPlantable) // TODO:
		{
			if(!farm.hasShears()) 
			{
				return null;
			}
			ItemStack shears = farm.getTool(ToolType.SHEARS);
			drops = new ArrayList();
			IBlockState flowerState = ((IPlantable) block).getPlant(worldObj, pos);
			Block flowerBlock = flowerState.getBlock();
			drops.add(new ItemStack(flowerBlock));
			farm.damageShears(block, pos);
		}
		else 
		{
			if(!farm.hasHoe()) 
			{
				return null;
			}
			drops = block.getDrops(worldObj, pos, state, farm.getMaxLootingValue());
			farm.damageHoe(1, pos);
		}
		List<EntityItem> result = new ArrayList<EntityItem>();
		if(drops != null) 
		{
			for(ItemStack stack : drops) 
			{
				result.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
			}
		}
		worldObj.setBlockToAir(pos);
		return new HarvestResult(result, pos);
	}
}