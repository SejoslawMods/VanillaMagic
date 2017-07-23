package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.util.ItemStackUtil;

public abstract class QuestMachineActivate extends Quest
{
	protected ItemStack mustHaveOffHand;
	protected ItemStack mustHaveMainHand;
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		if (jo.has("mustHaveOffHand")) 
			this.mustHaveOffHand = ItemStackUtil.getItemStackFromJSON(jo.get("mustHaveOffHand").getAsJsonObject());
		if (jo.has("mustHaveMainHand"))
			this.mustHaveMainHand = ItemStackUtil.getItemStackFromJSON(jo.get("mustHaveMainHand").getAsJsonObject());
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
		if (ItemStackUtil.isNullStack(offHand) || ItemStackUtil.isNullStack(mainHand)) return false;
		
		if (ItemStack.areItemsEqual(offHand, mustHaveOffHand))
			if (ItemStackUtil.getStackSize(offHand) >= ItemStackUtil.getStackSize(mustHaveOffHand))
				if (ItemStack.areItemsEqual(mainHand, mustHaveMainHand))
					if (ItemStackUtil.getStackSize(mainHand) >= ItemStackUtil.getStackSize(mustHaveMainHand))
						return true;
		return false;
	}
	
	public boolean startWorkWithCauldron(EntityPlayer player, BlockPos cauldronPos, IQuest quest)
	{
		if (!player.isSneaking()) return false;
		
		if (player.world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
		{
			if (canActivate(player))
			{
				if (canPlayerGetQuest(player)) addStat(player);
				if (hasQuest(player)) return true;
			}
		}
		return false;
	}
}