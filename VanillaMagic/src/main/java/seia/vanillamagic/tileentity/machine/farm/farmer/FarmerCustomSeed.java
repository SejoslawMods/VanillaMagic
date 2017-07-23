package seia.vanillamagic.tileentity.machine.farm.farmer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import seia.vanillamagic.tileentity.machine.farm.HarvestResult;
import seia.vanillamagic.tileentity.machine.farm.IHarvestResult;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;
import seia.vanillamagic.util.ItemStackUtil;

public class FarmerCustomSeed implements IFarmer
{
	public Block plantedBlock;
	public int plantedBlockMeta;  
	public int grownBlockMeta;
	public ItemStack seeds;
	public boolean requiresFarmland = true;
	public List<Block> tilledBlocks = new ArrayList<Block>();
	public boolean ignoreSustainCheck = false;
	public boolean checkGroundForFarmland = false;
	public boolean disableTreeFarm;

	public FarmerCustomSeed(Block plantedBlock, ItemStack seeds)
	{
		this(plantedBlock, 0, 7, seeds);
	}

	public FarmerCustomSeed(Block plantedBlock, int grownBlockMeta, ItemStack seeds) 
	{
		this(plantedBlock, 0, grownBlockMeta, seeds);
	}

	public FarmerCustomSeed(Block plantedBlock, int plantedBlockMeta, int grownBlockMeta, ItemStack seeds) 
	{
		this.plantedBlock = plantedBlock;
		this.plantedBlockMeta = plantedBlockMeta;
		this.grownBlockMeta = grownBlockMeta;
		this.seeds = seeds;
		addTilledBlock(Blocks.FARMLAND);
	}
	  
	public void clearTilledBlocks() 
	{
		tilledBlocks.clear();
	}
		
	public void addTilledBlock(Block block) 
	{
		tilledBlocks.add(block);
	}
	
	public boolean isIgnoreGroundCanSustainCheck() 
	{
		return ignoreSustainCheck;
	}

	public void setIgnoreGroundCanSustainCheck(boolean ignoreSustainCheck) 
	{
		this.ignoreSustainCheck = ignoreSustainCheck;
	}

	public boolean isCheckGroundForFarmland() 
	{
		return checkGroundForFarmland;
	}

	public void setCheckGroundForFarmland(boolean checkGroundForFarmland) 
	{
		this.checkGroundForFarmland = checkGroundForFarmland;
	}

	public int getPlantedBlockMeta() 
	{
		return plantedBlockMeta;
	}

	public Block getPlantedBlock() 
	{
		return plantedBlock;
	}

	public ItemStack getSeeds() 
	{
		return seeds;
	}

	public int getFullyGrownBlockMeta() 
	{
		return grownBlockMeta;
	}
	
	public boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState bs) 
	{
		int meta = bs.getBlock().getMetaFromState(bs);
		return block == getPlantedBlock() && getFullyGrownBlockMeta() == meta;
	}
	
	public boolean canPlant(ItemStack stack)
	{
		if (ItemStackUtil.isNullStack(stack)) return false;
		return stack.isItemEqual(getSeeds());
	}
	
	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) 
	{
		if (!farm.isOpen(pos)) return false;
		
		if (requiresFarmland()) 
		{      
			if (isGroundTilled(farm, pos)) return plantFromInventory(farm, pos);
			
			if (farm.hasSeed(getSeeds()/*, pos*/)) 
			{
				boolean tilled = tillBlock(farm, pos);
				if (!tilled) return false;
			}
		}
		return plantFromInventory(farm, pos);
	}

	public boolean requiresFarmland() 
	{
		return requiresFarmland;
	}
	  
	public void setRequiresFarmland(boolean requiresFarmland) 
	{
		this.requiresFarmland = requiresFarmland;
	}

	protected boolean plantFromInventory(TileFarm farm, BlockPos pos) 
	{
		World worldObj = farm.getWorld();
		ItemStack seed = farm.takeSeedFromSupplies(getSeeds(), pos);
		if (canPlant(farm, worldObj, pos) && !ItemStackUtil.isNullStack(seed)) return plant(farm, worldObj, pos, seed);
		return false;
	}
	
	@Nullable
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		if (!canHarvest(farm, pos, block, state)) return null;
		if (!farm.hasHoe()) return null;
		
		World worldObj = farm.getWorld();
		//final EntityPlayerMP fakePlayer = farm.getFarmer(); // TODO:
		final int fortune = farm.getMaxLootingValue();
		List<EntityItem> result = new ArrayList<EntityItem>();
		NonNullList<ItemStack> drops = NonNullList.create();
		block.getDrops(drops, worldObj, pos, state, fortune);
		float chance = ForgeEventFactory.fireBlockHarvesting(drops, worldObj, pos, state, fortune, 1.0F, false, null/*fakePlayer*/);
		farm.damageHoe(1, pos);
		boolean removed = false;
		if (drops != null) 
		{
			for (ItemStack stack : drops) 
			{
				if (worldObj.rand.nextFloat() <= chance) 
				{
					if (!removed && stack.isItemEqual(getSeeds())) 
					{
						ItemStackUtil.decreaseStackSize(stack, 1);
						removed = true;
						if (ItemStackUtil.getStackSize(stack) > 0)
							result.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
					} 
					else 
						result.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
				}
			}
		}
		
		if (removed) 
		{
			if (!plant(farm, worldObj, pos, null)) 
			{
				result.add(new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, getSeeds().copy()));
				worldObj.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
			}
		} 
		else 
			worldObj.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
		return new HarvestResult(result, pos);
	}

	protected boolean tillBlock(TileFarm farm, BlockPos plantingLocation) 
	{
		World worldObj = farm.getWorld();
		BlockPos dirtLoc = plantingLocation.offset(EnumFacing.DOWN);
		Block dirtBlock = farm.getBlock(dirtLoc);
		if ((dirtBlock == Blocks.DIRT || dirtBlock == Blocks.GRASS) && farm.hasHoe()) 
		{
			farm.damageHoe(1, dirtLoc);
			worldObj.setBlockState(dirtLoc, Blocks.FARMLAND.getDefaultState());
			worldObj.playSound(dirtLoc.getX() + 0.5F, dirtLoc.getY() + 0.5F, dirtLoc.getZ() + 0.5F, Blocks.FARMLAND.getSoundType().getStepSound(), SoundCategory.BLOCKS,
					(Blocks.FARMLAND.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.FARMLAND.getSoundType().getPitch() * 0.8F, false);
			return true;
		}
		return false;
	}
	
	protected boolean isGroundTilled(TileFarm farm, BlockPos plantingLocation) 
	{
		return tilledBlocks.contains(farm.getWorld().getBlockState(plantingLocation.offset(EnumFacing.DOWN)));
	}

	protected boolean canPlant(TileFarm farm, World worldObj, BlockPos pos) 
	{
		Block target = getPlantedBlock();
		BlockPos groundPos = pos.down();
		IBlockState bs = worldObj.getBlockState(groundPos);
		Block ground = bs.getBlock();
		IPlantable plantable = (IPlantable) getPlantedBlock();
		if (target.canPlaceBlockAt(worldObj, pos) &&        
				(ground.canSustainPlant(bs, worldObj, groundPos, EnumFacing.UP, plantable) || ignoreSustainCheck)
				&& (!checkGroundForFarmland || isGroundTilled(farm, pos))) 
		{
			return true;
		}
		return false;
	}

	protected boolean plant(TileFarm farm, World worldObj, BlockPos pos, ItemStack seed) 
	{
		worldObj.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
		if (canPlant(farm, worldObj, pos)) 
		{
			worldObj.setBlockState(pos, getPlantedBlock().getStateFromMeta(getPlantedBlockMeta()), 1 | 2);
			if (seed != null) ItemStackUtil.decreaseStackSize(seed, 1);
			return true;
		}
		return false;
	}

	public boolean doesDisableTreeFarm()
	{
		return disableTreeFarm;
	}
}