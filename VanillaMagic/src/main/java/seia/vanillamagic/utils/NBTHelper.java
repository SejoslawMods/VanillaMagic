package seia.vanillamagic.utils;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;

/**
 * Stores all the Vanilla Magic NBT tags.
 * Additional tags are stored in interfaces.
 */
public class NBTHelper
{
	public static final String NBT_MACHINE_POS_X = "NBT_MACHINE_POS_X";
	public static final String NBT_MACHINE_POS_Y = "NBT_MACHINE_POS_Y";
	public static final String NBT_MACHINE_POS_Z = "NBT_MACHINE_POS_Z";
	public static final String NBT_WORKING_POS_X = "NBT_WORKING_POS_X";
	public static final String NBT_WORKING_POS_Y = "NBT_WORKING_POS_Y";
	public static final String NBT_WORKING_POS_Z = "NBT_WORKING_POS_Z";
	public static final String NBT_RADIUS = "NBT_RADIUS";
	public static final String NBT_ONE_OPERATION_COST = "NBT_ONE_OPERATION_COST";
	public static final String NBT_TICKS = "NBT_TICKS";
	public static final String NBT_MAX_TICKS = "NBT_MAX_TICKS";
	public static final String NBT_IS_ACTIVE = "NBT_IS_ACTIVE";
	public static final String NBT_DIMENSION = "NBT_DIMENSION";
	public static final String NBT_NEEDS_FUEL = "NBT_NEEDS_FUEL";
	public static final String NBT_LOCALIZED_NAME_BLOCK = "NBT_LOCALIZED_NAME_BLOCK";
	public static final String NBT_UNLOCALIZED_NAME_BLOCK = "NBT_UNLOCALIZED_NAME_BLOCK";
	public static final String NBT_POSX = "NBT_POSX";
	public static final String NBT_POSY = "NBT_POSY";
	public static final String NBT_POSZ = "NBT_POSZ";
	public static final String NBT_HAS_TILEENTITY = "NBT_HAS_TILEENTITY";
	public static final String NBT_TAG_COMPOUND_NAME = "NBT_TAG_COMPOUND_NAME";
	public static final String NBT_IINVENTORY_ITEMS = "NBT_IINVENTORY_ITEMS";
	public static final String NBT_IINVENTORY_SLOT = "NBT_IINVENTORY_SLOT";
	public static final String NBT_SERIALIZABLE = "NBT_SERIALIZABLE";
	public static final String NBT_IITEMHANDLER_SLOT = "NBT_IITEMHANDLER_SLOT";
	public static final String NBT_IITEMHANDLER_ITEMS = "NBT_IITEMHANDLER_ITEMS";
	public static final String NBT_CLASS_NAME = "NBT_CLASS_NAME";
	public static final String NBT_TAG_COMPOUND_ENTITY = "NBTQuestCaptureEntity";
	public static final String NBT_ENTITY_TYPE = "EntityType";
	public static final String NBT_SPEEDY_TICKS = "NBT_SPEEDY_TICKS";
	public static final String NBT_TICKS_REMAINING = "NBT_TICKS_REMAINING";
	public static final String NBT_BLOCK_NAME = "NBT_BLOCK_NAME";
	public static final String NBT_BLOCK_META = "NBT_BLOCK_META";
	public static final String NBT_BLOCK_ID = "NBT_BLOCK_ID";
	public static final String NBT_PLAYER_NAME = "NBT_PLAYER_NAME";
	
	private NBTHelper()
	{
	}

	public static String getBlockNameFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey(NBT_UNLOCALIZED_NAME_BLOCK))
		{
			return nbt.getString(NBT_UNLOCALIZED_NAME_BLOCK);
		}
		return "";
	}
	
	@Nullable
	public static Block getBlockFromNBT(NBTTagCompound nbt)
	{
		String blockName = getBlockNameFromNBT(nbt);
		return Block.getBlockFromName(blockName);
	}
	
	public static NBTTagCompound setBlockPosDataToNBT(BlockPos pos, NBTTagCompound nbt)
	{
		if(nbt == null)
		{
			nbt = new NBTTagCompound();
		}
		nbt.setInteger(NBTHelper.NBT_POSX, pos.getX());
		nbt.setInteger(NBTHelper.NBT_POSY, pos.getY());
		nbt.setInteger(NBTHelper.NBT_POSZ, pos.getZ());
		return nbt;
	}

	@Nullable
	public static BlockPos getBlockPosDataFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey(NBT_POSX) && nbt.hasKey(NBT_POSY) && nbt.hasKey(NBT_POSZ))
		{
			int posX = nbt.getInteger(NBT_POSX);
			int posY = nbt.getInteger(NBT_POSY);
			int posZ = nbt.getInteger(NBT_POSZ);
			return new BlockPos(posX, posY, posZ);
		}
		return null;
	}

	@Nullable
	public static TileEntity getTileEntityFromNBT(World world, NBTTagCompound nbt)
	{
		return TileEntity.func_190200_a(world, nbt);
	}
	
	public static NBTTagCompound writeIInventoryToNBT(IInventory inv, NBTTagCompound nbt)
	{
		if(inv == null)
		{
			return null;
		}
		if(nbt == null)
		{
			nbt = new NBTTagCompound();
		}
		NBTTagList nbtItemsList = new NBTTagList();
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			if(inv.getStackInSlot(i) != null)
			{
				NBTTagCompound nbtItemTagCompound = new NBTTagCompound();
				nbtItemTagCompound.setByte(NBT_IINVENTORY_SLOT, (byte)i);
				inv.getStackInSlot(i).writeToNBT(nbtItemTagCompound);
				nbtItemsList.appendTag(nbtItemTagCompound);
			}
		}
		nbt.setTag(NBT_IINVENTORY_ITEMS, nbtItemsList);
		return nbt;
	}
	
	public static IInventory readIInventoryFromNBT(IInventory inv, NBTTagCompound nbt)
	{
		if((inv == null) || (nbt == null))
		{
			return null;
		}
		NBTTagList nbtItemsList = nbt.getTagList(NBT_IINVENTORY_ITEMS, 10);
		for(int i = 0; i < nbtItemsList.tagCount(); i++)
		{
			NBTTagCompound nbtItemTagCompound = nbtItemsList.getCompoundTagAt(i);
			int gettedByte = nbtItemTagCompound.getByte(NBT_IINVENTORY_SLOT);
			if(gettedByte >= 0 && gettedByte < inv.getSizeInventory())
			{
				inv.setInventorySlotContents(gettedByte, ItemStack.loadItemStackFromNBT(nbtItemTagCompound));
			}
		}
		return inv;
	}
	
	public static NBTTagCompound writeToINBTSerializable(INBTSerializable<NBTTagCompound> nbtSerial, NBTTagCompound nbt)
	{
		nbt.setTag(NBT_SERIALIZABLE, nbtSerial.serializeNBT());
		return nbt;
	}
	
	public static INBTSerializable<NBTTagCompound> readFromINBTSerializable(INBTSerializable<NBTTagCompound> nbtSerial, NBTTagCompound nbt)
	{
		nbtSerial.deserializeNBT(nbt.getCompoundTag(NBT_SERIALIZABLE));
		return nbtSerial;
	}
	
	public static NBTTagCompound writeToIItemHandler(IItemHandler handler, NBTTagCompound nbt)
	{
		if(handler == null)
		{
			return null;
		}
		if(nbt == null)
		{
			nbt = new NBTTagCompound();
		}
		NBTTagList nbtItemsList = new NBTTagList();
		for(int i = 0; i < handler.getSlots(); ++i)
		{
			if(handler.getStackInSlot(i) != null)
			{
				NBTTagCompound nbtItemTagCompound = new NBTTagCompound();
				nbtItemTagCompound.setByte(NBT_IITEMHANDLER_SLOT, (byte)i);
				handler.getStackInSlot(i).writeToNBT(nbtItemTagCompound);
				nbtItemsList.appendTag(nbtItemTagCompound);
			}
		}
		nbt.setTag(NBT_IITEMHANDLER_ITEMS, nbtItemsList);
		return nbt;
	}
	
	public static IItemHandler readFromIItemHandler(IItemHandler handler, NBTTagCompound nbt)
	{
		if((handler == null) || (nbt == null))
		{
			return null;
		}
		NBTTagList nbtItemsList = nbt.getTagList(NBT_IITEMHANDLER_ITEMS, 10);
		for(int i = 0; i < nbtItemsList.tagCount(); i++)
		{
			NBTTagCompound nbtItemTagCompound = nbtItemsList.getCompoundTagAt(i);
			int gettedByte = nbtItemTagCompound.getByte(NBT_IITEMHANDLER_SLOT);
			if(gettedByte >= 0 && gettedByte < handler.getSlots())
			{
				handler.insertItem(gettedByte, ItemStack.loadItemStackFromNBT(nbtItemTagCompound), false);
			}
		}
		return handler;
	}
}