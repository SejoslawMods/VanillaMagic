package seia.vanillamagic.machine.farm.farmer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import seia.vanillamagic.machine.farm.HarvestResult;
import seia.vanillamagic.machine.farm.IHarvestResult;
import seia.vanillamagic.machine.farm.TileFarm;

public class FarmerFlowerPicker implements IFarmer
{
	protected List<ItemStack> flowers = new ArrayList<ItemStack>();
	
	public FarmerFlowerPicker()
	{
		this.flowers.add(new ItemStack(Blocks.LOG, 1, 0));
		this.flowers.add(new ItemStack(Blocks.WHEAT));
		this.flowers.add(new ItemStack(Blocks.LEAVES, 1, 0));
		this.flowers.add(new ItemStack(Items.APPLE));
	}
	
	public FarmerFlowerPicker add(ItemStack flowers) 
	{
		this.flowers.add(flowers);
		return this;
	}
	
	public boolean prepareBlock(TileFarm farm, BlockPos bc, Block block, IBlockState state) 
	{
		return false;
	}
	
	public boolean canHarvest(TileFarm farm, BlockPos bc, Block block, IBlockState state) 
	{
		return flowers.contains(block) || block instanceof IShearable;
	}
	
	public boolean canPlant(ItemStack stack) 
	{
		return false;
	}
	
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		World worldObj = farm.getWorld();
		List<ItemStack> drops = null;
		if(block instanceof IShearable) 
		{
			ItemStack shears = farm.getShearsFromInput();
			if(!((IShearable) block).isShearable(shears, worldObj, pos)) 
			{
				return null;
			}
			drops = ((IShearable) block).onSheared(shears, worldObj, pos, 0);
			shears.attemptDamageItem(1, new Random());
		} 
		else 
		{
			drops = block.getDrops(worldObj, pos, state, 0);
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