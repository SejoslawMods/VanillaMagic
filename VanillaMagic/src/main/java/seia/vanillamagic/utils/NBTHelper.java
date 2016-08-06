package seia.vanillamagic.utils;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class NBTHelper
{
	public static final String NBT_LOCALIZED_NAME_BLOCK = "localizedNameBlock";
	public static final String NBT_UNLOCALIZED_NAME_BLOCK = "unlocalizedNameBlock";
	public static final String NBT_POSX = "posX";
	public static final String NBT_POSY = "posY";
	public static final String NBT_POSZ = "posZ";
	public static final String NBT_HAS_TILEENTITY = "hasTileEntity";
	public static final String NBT_TAG_COMPOUND_NAME = "NBTQuestSaveBlock";
	public static final String NBT_ITEMS = "Items";
	public static final String NBT_SLOT = "Slot";
	public static final String NBT_SERIALIZABLE = "INBTSerializable";
	
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
	
	public static Block getBlockFromNBT(NBTTagCompound nbt)
	{
		String blockName = getBlockNameFromNBT(nbt);
		return Block.getBlockFromName(blockName);
	}

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
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			if(inv.getStackInSlot(i) != null)
			{
				NBTTagCompound nbtItemTagCompound = new NBTTagCompound();
				nbtItemTagCompound.setByte(NBT_SLOT, (byte)i);
				inv.getStackInSlot(i).writeToNBT(nbtItemTagCompound);
				nbtItemsList.appendTag(nbtItemTagCompound);
			}
		}
		nbt.setTag(NBT_ITEMS, nbtItemsList);
		return nbt;
	}
	
	public static IInventory readIInventoryFromNBT(IInventory inv, NBTTagCompound nbt)
	{
		if((inv == null) || (nbt == null))
		{
			return null;
		}
		NBTTagList nbtItemsList = nbt.getTagList(NBT_ITEMS, 10);
		for(int i = 0; i < nbtItemsList.tagCount(); i++)
		{
			NBTTagCompound nbtItemTagCompound = nbtItemsList.getCompoundTagAt(i);
			int gettedByte = nbtItemTagCompound.getByte(NBT_SLOT);
			if (gettedByte >= 0 && gettedByte < inv.getSizeInventory())
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
}