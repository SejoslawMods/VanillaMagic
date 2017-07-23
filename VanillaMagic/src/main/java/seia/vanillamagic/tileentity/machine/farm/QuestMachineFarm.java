package seia.vanillamagic.tileentity.machine.farm;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.quest.QuestMachineActivate;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;

public class QuestMachineFarm extends QuestMachineActivate
{
	protected int radius;
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		radius = jo.get("radius").getAsInt();
		if (radius < 0) radius = -radius;
	}
	
	@SubscribeEvent
	public void startFarm(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		BlockPos cauldronPos = event.getPos();
		if (startWorkWithCauldron(player, cauldronPos, this))
		{
			TileFarm tileFarm = new TileFarm();
			tileFarm.init(player.world, cauldronPos);
			tileFarm.setWorkRadius(radius);
			if (CustomTileEntityHandler.addCustomTileEntity(tileFarm, player.dimension))
			{
				ItemStackUtil.decreaseStackSize(player.getHeldItemOffhand(), ItemStackUtil.getStackSize(mustHaveOffHand));
				EntityUtil.addChatComponentMessageNoSpam(player, tileFarm.getClass().getSimpleName() + " added");
			}
		}
	}
	
	@SubscribeEvent
	public void stopFarm(BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		World world = event.getWorld();
		BlockPos cauldronPos = event.getPos();
		if (world.getTileEntity(cauldronPos.offset(EnumFacing.UP)) instanceof IInventory)
			if (world.getTileEntity(cauldronPos.offset(EnumFacing.DOWN)) instanceof IInventory)
				CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, cauldronPos, player);
	}
}