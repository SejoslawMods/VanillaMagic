package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.utils.ItemStackHelper;

public abstract class QuestMachineActivate extends Quest
{
	public ItemStack mustHaveOffHand;
	public ItemStack mustHaveMainHand;
//	
//	public QuestMachineActivate(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName,
//			ItemStack mustHaveOffHand, ItemStack mustHaveMainHand) 
//	{
//		super(required, posX, posY, icon, questName, uniqueName);
//		this.mustHaveOffHand = mustHaveOffHand;
//		this.mustHaveMainHand = mustHaveMainHand;
//	}
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		this.mustHaveOffHand = ItemStackHelper.getItemStackFromJSON(jo.get("mustHaveOffHand").getAsJsonObject());
		this.mustHaveMainHand = ItemStackHelper.getItemStackFromJSON(jo.get("mustHaveMainHand").getAsJsonObject());
	}
	
	public boolean canActivate(EntityPlayer player)
	{
		ItemStack offHand = player.getHeldItemOffhand();
		ItemStack mainHand = player.getHeldItemMainhand();
		if(offHand == null || mainHand == null)
		{
			return false;
		}
		if(ItemStack.areItemsEqual(offHand, mustHaveOffHand))
		{
			if(offHand.stackSize >= mustHaveOffHand.stackSize)
			{
				if(ItemStack.areItemsEqual(mainHand, mustHaveMainHand))
				{
					if(mainHand.stackSize >= mustHaveMainHand.stackSize)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean startWorkWithCauldron(EntityPlayer player, BlockPos cauldronPos, Achievement requiredToWork)
	{
		if(player.worldObj.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
		{
			if(canActivate(player))
			{
				if(!player.hasAchievement(requiredToWork))
				{
					player.addStat(requiredToWork, 1);
				}
				if(player.hasAchievement(requiredToWork))
				{
					if(player.isSneaking())
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}