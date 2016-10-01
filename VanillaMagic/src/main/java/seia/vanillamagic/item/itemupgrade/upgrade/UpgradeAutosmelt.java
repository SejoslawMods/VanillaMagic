package seia.vanillamagic.item.itemupgrade.upgrade;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.util.SmeltingHelper;

public class UpgradeAutosmelt implements IItemUpgrade
{
	public ItemStack getIngredient() 
	{
		return new ItemStack(Items.MAGMA_CREAM);
	}
	
	public String getUniqueNBTTag() 
	{
		return "NBT_UPGRADE_AUTOSMELT";
	}
	
	public String getUpgradeName() 
	{
		return "Autosmelt Upgrade";
	}
	
	@SubscribeEvent
	public void onDig(HarvestDropsEvent event)
	{
		EntityPlayer player = event.getHarvester();
		if(player == null)
		{
			return;
		}
		if(!containsTag(player.getHeldItemMainhand()))
		{
			return;
		}
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		List<ItemStack> drops = event.getDrops();
		for(int i = 0; i < drops.size(); i++)
		{
			ItemStack dropStack = drops.get(i);
			ItemStack afterSmelt = SmeltingHelper.getSmeltingResultAsNewStack(dropStack);
			if(afterSmelt != null)
			{
				EntityItem afterSmeltEntity = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), afterSmelt);
				world.spawnEntityInWorld(afterSmeltEntity);
				drops.remove(i);
			}
		}
	}

	public boolean containsTag(ItemStack stack) 
	{
		if(stack == null)
		{
			return false;
		}
		NBTTagCompound tag = stack.getTagCompound();
		if(tag == null)
		{
			return false;
		}
		return tag.getString(NBT_ITEM_UPGRADE_TAG).equals(getUniqueNBTTag());
	}
}