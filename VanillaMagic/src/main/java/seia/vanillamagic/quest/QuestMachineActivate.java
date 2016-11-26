package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.util.ItemStackHelper;

public abstract class QuestMachineActivate extends Quest
{
	protected ItemStack mustHaveOffHand;
	protected ItemStack mustHaveMainHand;
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		if(jo.has("mustHaveOffHand"))
		{
			this.mustHaveOffHand = ItemStackHelper.getItemStackFromJSON(jo.get("mustHaveOffHand").getAsJsonObject());
		}
		if(jo.has("mustHaveMainHand"))
		{
			this.mustHaveMainHand = ItemStackHelper.getItemStackFromJSON(jo.get("mustHaveMainHand").getAsJsonObject());
		}
	}
	
	public ItemStack getRequiredStackOffHand()
	{
		return mustHaveOffHand;
	}
	
	public ItemStack getRequiredStackMainHand()
	{
		return mustHaveMainHand;
	}
	
	public boolean canActivate(EntityPlayer player)
	{
		ItemStack offHand = player.getHeldItemOffhand();
		ItemStack mainHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(offHand) || ItemStackHelper.isNullStack(mainHand))
		{
			return false;
		}
		if(ItemStack.areItemsEqual(offHand, mustHaveOffHand))
		{
			if(ItemStackHelper.getStackSize(offHand) >= ItemStackHelper.getStackSize(mustHaveOffHand))
			{
				if(ItemStack.areItemsEqual(mainHand, mustHaveMainHand))
				{
					if(ItemStackHelper.getStackSize(mainHand) >= ItemStackHelper.getStackSize(mustHaveMainHand))
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
		if(player.world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
		{
			if(canActivate(player))
			{
				if(canPlayerGetAchievement(player))
				{
					player.addStat(achievement, 1);
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