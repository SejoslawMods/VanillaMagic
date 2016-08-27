package seia.vanillamagic.machine.farm.farmer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import seia.vanillamagic.machine.farm.HarvestResult;
import seia.vanillamagic.machine.farm.IHarvestResult;
import seia.vanillamagic.machine.farm.TileFarm;

public class FarmerPlantable implements IFarmer
{
	private Set<Block> harvestExcludes = new HashSet<Block>();
	
	public void addHarvestExlude(Block block) 
	{
		harvestExcludes.add(block);
	}
	
	public boolean canPlant(ItemStack stack)
	{
		if(stack == null) 
		{
			return false;
		}
		return stack.getItem() instanceof IPlantable;
	}
	
	public boolean prepareBlock(TileFarm farm, BlockPos bc, Block block, IBlockState meta) 
	{
		if(block == null) 
		{
			return false;
		}
		int slot = farm.getSupplySlotForCoord(bc);
		ItemStack seedStack = farm.getSeedTypeInSuppliesFor(slot);
		if(seedStack == null) 
		{
			return false;
		}
		if(!(seedStack.getItem() instanceof IPlantable)) 
		{
			return false;
		}
		IPlantable plantable = (IPlantable) seedStack.getItem();
		EnumPlantType type = plantable.getPlantType(farm.getWorld(), bc);
		if(type == null) 
		{
			return false;
		}
		Block ground = farm.getBlock(bc.offset(EnumFacing.DOWN));
		if(type == EnumPlantType.Nether) 
		{
			if(ground != Blocks.SOUL_SAND) 
			{
				return false;
			}
			return plantFromInventory(farm, bc, plantable);
		}
		if(type == EnumPlantType.Crop) 
		{
			farm.tillBlock(bc);        
			return plantFromInventory(farm, bc, plantable);
		}
		if (type == EnumPlantType.Water) 
		{
			return plantFromInventory(farm, bc, plantable);
		}
		return false;
	}

	protected boolean plantFromInventory(TileFarm farm, BlockPos bc, IPlantable plantable) 
	{
		World worldObj = farm.getWorld();
		if(canPlant(worldObj, bc, plantable) && farm.takeSeedFromSupplies(bc) != null) 
		{
			return plant(farm, worldObj, bc, plantable);
		}
		return false;
	}

	protected boolean plant(TileFarm farm, World worldObj, BlockPos bc, IPlantable plantable) 
	{
		worldObj.setBlockState(bc, Blocks.AIR.getDefaultState(), 1 | 2);
		IBlockState target = plantable.getPlant(null, new BlockPos(0, 0, 0));    
		worldObj.setBlockState(bc, target, 1 | 2);
		return true;
	}

	protected boolean canPlant(World worldObj, BlockPos bc, IPlantable plantable) 
	{
		IBlockState target = plantable.getPlant(null, new BlockPos(0, 0, 0));
		BlockPos groundPos = bc.down();
		IBlockState groundBS = worldObj.getBlockState(groundPos);
		Block ground = groundBS.getBlock();
		if(target != null && target.getBlock().canPlaceBlockAt(worldObj, bc) &&        
				ground.canSustainPlant(groundBS, worldObj, groundPos, EnumFacing.UP, plantable)) 
		{
			return true;
		}
		return false;
	}
	
	public boolean canHarvest(TileFarm farm, BlockPos bc, Block block, IBlockState meta)
	{
		if(!harvestExcludes.contains(block) && block instanceof IGrowable && !(block instanceof BlockStem)) 
		{
			return !((IGrowable) block).canGrow(farm.getWorld(), bc, meta, true);
		}
		return false;
	}
	
	@Nullable
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos bc, Block block, IBlockState meta) 
	{
		if(!canHarvest(farm, bc, block, meta)) 
		{
			return null;
		}
		if(!farm.hasHoe()) 
		{
			return null;
		}
		World worldObj = farm.getWorld();
		List<EntityItem> result = new ArrayList<EntityItem>();
		final EntityPlayerMP fakePlayer = farm.getFarmer();
		final int fortune = farm.getMaxLootingValue();
		ItemStack removedPlantable = null;
		List<ItemStack> drops = block.getDrops(worldObj, bc, meta, fortune);
		float chance = ForgeEventFactory.fireBlockHarvesting(drops, worldObj, bc, meta, fortune, 1.0F, false, fakePlayer);
		farm.damageHoe(1, bc);
		boolean removed = false;
		if(drops != null) 
		{
			for (ItemStack stack : drops) 
			{
				if (stack != null && stack.stackSize > 0 && worldObj.rand.nextFloat() <= chance) 
				{
					if (!removed && isPlantableForBlock(stack, block)) 
					{
						removed = true;
						removedPlantable = stack.copy();
						removedPlantable.stackSize = 1;
						stack.stackSize--;
						if (stack.stackSize > 0) 
						{
							result.add(new EntityItem(worldObj, bc.getX() + 0.5, bc.getY() + 0.5, bc.getZ() + 0.5, stack.copy()));
						}
					} 
					else 
					{
						result.add(new EntityItem(worldObj, bc.getX() + 0.5, bc.getY() + 0.5, bc.getZ() + 0.5, stack.copy()));
					}
				}
			}
		}
		ItemStack[] inv = fakePlayer.inventory.mainInventory;
		for (int slot = 0; slot < inv.length; slot++) 
		{
			ItemStack stack = inv[slot];
			if (stack != null) 
			{
				inv[slot] = null;
				EntityItem entityitem = new EntityItem(worldObj, bc.getX() + 0.5, bc.getY() + 0.5, bc.getZ() + 0.5, stack);
				result.add(entityitem);
			}
		}
		if(removed) 
		{
			if(!plant(farm, worldObj, bc, (IPlantable) removedPlantable.getItem())) 
			{
				result.add(new EntityItem(worldObj,bc.getX() + 0.5, bc.getY() + 0.5, bc.getZ() + 0.5, removedPlantable.copy()));
				worldObj.setBlockState(bc, Blocks.AIR.getDefaultState(), 1 | 2);
			}
		} 
		else 
		{
			worldObj.setBlockState(bc, Blocks.AIR.getDefaultState(), 1 | 2);
		}
		return new HarvestResult(result, bc);
	}

	private boolean isPlantableForBlock(ItemStack stack, Block block) 
	{
		if(!(stack.getItem() instanceof IPlantable)) 
		{
			return false;
		}
		IPlantable plantable = (IPlantable) stack.getItem();
		IBlockState b = plantable.getPlant(null, new BlockPos(0, 0, 0));
		return b != null && b.getBlock() == block;
	}
}