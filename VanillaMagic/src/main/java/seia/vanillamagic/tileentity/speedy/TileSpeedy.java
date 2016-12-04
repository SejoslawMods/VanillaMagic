package seia.vanillamagic.tileentity.speedy;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.Box;
import seia.vanillamagic.util.NBTHelper;

public class TileSpeedy extends CustomTileEntity
{
	public static final String REGISTRY_NAME = TileSpeedy.class.getName();
	
	public int ticks;
	public Box box;
	public int size;

	public void init(World world, BlockPos pos)
	{
		super.init(world, pos);
		this.ticks = 1000;
		this.size = 4;
		setBox();
	}
	
	public IInventory getInventoryWithCrystal()
	{
		return (IInventory) worldObj.getTileEntity(getPos().offset(EnumFacing.UP));
	}
	
	public boolean containsCrystal()
	{
		IInventory inv = getInventoryWithCrystal();
		if(inv != null)
		{
			for(int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stack = inv.getStackInSlot(i);
				if(VanillaMagicItems.INSTANCE.isCustomItem(stack, VanillaMagicItems.INSTANCE.itemAccelerationCrystal))
				{
					return true;
				}
			}
		}
		return false;
	}
    
	public void update() 
	{
		if(this.worldObj.isRemote)
		{
			return;
		}
		if(this.ticks == 0)
		{
			return;
		}
		if(containsCrystal())
		{
			this.tickNeighbors();
		}
	}

	public void setBox() 
	{
		this.box = new Box();
		box.xMin = this.pos.getX() - this.size;
		box.yMin = this.pos.getY() - 1;
		box.zMin = this.pos.getZ() - this.size;
		box.xMax = this.pos.getX() + this.size;
		box.yMax = this.pos.getY() + 1;
		box.zMax = this.pos.getZ() + this.size;
	}

	public void tickNeighbors() 
	{
		for(int x = box.xMin; x <= box.xMax; x++) 
		{
			for(int y = box.yMin; y <= box.yMax; y++) 
			{
				for(int z = box.zMin; z <= box.zMax; z++) 
				{
					this.tickBlock(new BlockPos(x, y, z));
				}
			}
		}
	}

	public void tickBlock(BlockPos pos) 
	{
		if(!worldObj.isAirBlock(pos))
		{
			IBlockState blockState = this.worldObj.getBlockState(pos);
			Block block = blockState.getBlock();
			TileEntity tile = worldObj.getTileEntity(pos);
			Random rand = new Random();
			for(int i = 0; i < ticks; i++)
			{
				if(tile == null)
				{
					block.updateTick(worldObj, pos, blockState, rand);
					worldObj.spawnParticle(EnumParticleTypes.END_ROD, pos.getX(), pos.getY(), pos.getZ(), 100, 100, 100, new int[]{});
				}
				else if(tile instanceof ITickable)
				{
					((ITickable) tile).update();
					worldObj.spawnParticle(EnumParticleTypes.END_ROD, pos.getX(), pos.getY(), pos.getZ(), 100, 100, 100, new int[]{});
				}
			}
		}
	}
    
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger(NBTHelper.NBT_SPEEDY_TICKS, ticks);
		return tag;
	}
    
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		this.ticks = tag.getInteger(NBTHelper.NBT_SPEEDY_TICKS);
	}
	
	public List<String> getAdditionalInfo()
	{
		List<String> list = super.getAdditionalInfo();
		list.add("Size: " + size);
		list.add("Ticks in one MC tick: " + ticks);
		return list;
	}
}