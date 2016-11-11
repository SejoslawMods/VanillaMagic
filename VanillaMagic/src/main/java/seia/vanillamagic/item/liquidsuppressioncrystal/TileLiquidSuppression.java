package seia.vanillamagic.item.liquidsuppressioncrystal;

import com.google.common.base.Strings;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.NBTHelper;
import seia.vanillamagic.util.WorldHelper;

public class TileLiquidSuppression extends CustomTileEntity
{
	public static final String REGISTRY_NAME = TileLiquidSuppression.class.getName();
	
	public int ticksRemaining;
	public String containedBlockName;
	public int containedBlockMeta;
	
	public void update() 
	{
		if(worldObj.isRemote)
		{
			return;
		}
		ticksRemaining--;
		if(ticksRemaining <= 0)
		{
			returnContainedBlock();
		}
	}
	
	public void returnContainedBlock()
	{
		Block block = null;
		if(!Strings.isNullOrEmpty(containedBlockName))
		{
			block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(containedBlockName));
		}
		if(block != null && worldObj.setBlockState(pos, block.getStateFromMeta(containedBlockMeta)))
		{
			getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
			CustomTileEntityHandler.INSTANCE.removeCustomTileEntityAtPos(worldObj, getPos(), WorldHelper.getDimensionID(worldObj));
		}
	}
	
	public void setContainedBlockInfo(IBlockState state)
	{
		containedBlockName = state.getBlock().getRegistryName().toString();
		containedBlockMeta = state.getBlock().getMetaFromState(state);
	}
	
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		ticksRemaining = tagCompound.getInteger(NBTHelper.NBT_TICKS_REMAINING);
		containedBlockName = tagCompound.getString(NBTHelper.NBT_BLOCK_NAME);
		containedBlockMeta = tagCompound.getInteger(NBTHelper.NBT_BLOCK_META);
	}
    
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger(NBTHelper.NBT_TICKS_REMAINING, ticksRemaining);
		tagCompound.setString(NBTHelper.NBT_BLOCK_NAME, Strings.isNullOrEmpty(containedBlockName) ? "" : containedBlockName);
		tagCompound.setInteger(NBTHelper.NBT_BLOCK_META, containedBlockMeta);
		return tagCompound;
	}
	
	public void resetDuration(int refresh) 
	{
		if(ticksRemaining < refresh)
		{
			ticksRemaining = refresh;
		}
	}
    
	public void setDuration(int duration)
	{
		ticksRemaining = duration;
	}

	public static TileLiquidSuppression createAirBlock(World world, BlockPos blockPos, int duration) 
	{
		if(world.isAirBlock(blockPos))
		{
			return null;
		}
		IBlockState cachedState = world.getBlockState(blockPos);
		world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
		//TileLiquidSuppression tile = (TileLiquidSuppression) world.getTileEntity(blockPos);
		TileLiquidSuppression tile = new TileLiquidSuppression();
		tile.init(world, blockPos);
		tile.setContainedBlockInfo(cachedState);
		tile.setDuration(duration);
		world.setTileEntity(blockPos, tile);
		return tile;
	}
}