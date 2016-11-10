package seia.vanillamagic.tileentity.machine.farm;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;

public enum ToolType 
{
	HOE 
	{
		boolean match(ItemStack item) 
		{
			return (item.getItem() instanceof ItemHoe);
		}
	},
	AXE     
	{
		boolean match(ItemStack item) 
		{
			if(item.getItem() instanceof ItemAxe)
			{
				return true;
			}
			else if(item.getItem().getHarvestLevel(item, "axe", null, null) >= 0)
			{
				return true;
			}
			return false;
			
			//return (item.getItem() instanceof ItemAxe);
			// return item.getItem().getHarvestLevel(item, "axe") >= 0;
			// return item.getItem().getHarvestLevel(item, "axe", null, null) >= 0;
		}
	},
	SHEARS  
	{
		boolean match(ItemStack item) 
		{
	        return item.getItem() instanceof ItemShears;
		}
	},
	NONE  
	{
		boolean match(ItemStack item) 
		{
			return false;
		}
	};

	public final boolean itemMatches(ItemStack item) 
	{
		if(item == null) 
		{
			return false;
		}
		return match(item) /*&& !isBrokenTinkerTool(item)*/;
	}

//	public static boolean isBrokenTinkerTool(ItemStack item) 
//	{
//		return item != null && item.hasTagCompound() && item.getTagCompound().hasKey("Stats") && item.getTagCompound().getCompoundTag("Stats").getBoolean("Broken");
//	}

	abstract boolean match(ItemStack item);

	public static boolean isTool(ItemStack stack) 
	{
		for(ToolType type : values()) 
		{
			if(type.itemMatches(stack)) 
			{
				return true;
			}
		}
		return false;
	}

	public static ToolType getToolType(ItemStack stack) 
	{
		for(ToolType type : values()) 
		{
			if(type.itemMatches(stack)) 
			{
				return type;
			}
		}
		return NONE;
	}
}