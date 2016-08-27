package seia.vanillamagic.machine.farm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestMachineActivate;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.EntityHelper;

public class QuestMachineFarm extends QuestMachineActivate
{
	protected int radius;
	
	public QuestMachineFarm(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName,
			ItemStack mustHaveOffHand, ItemStack mustHaveMainHand, int radius) 
	{
		super(required, posX, posY, icon, questName, uniqueName, mustHaveOffHand, mustHaveMainHand);
		this.radius = radius;
	}
	
	@SubscribeEvent
	public void startFarm(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;
		BlockPos cauldronPos = event.getPos();
		if(startWorkWithCauldron(player, cauldronPos, achievement))
		{
			TileFarm tileFarm = new TileFarm();
			tileFarm.init(world, cauldronPos);
			tileFarm.radius = radius;
			if(CustomTileEntityHandler.INSTANCE.addCustomTileEntity(tileFarm, player.dimension))
			{
				player.getHeldItemOffhand().stackSize -= mustHaveOffHand.stackSize;
				EntityHelper.addChatComponentMessage(player, tileFarm.getClass().getSimpleName() + " added");
			}
		}
	}
	
	@SubscribeEvent
	public void stopFarm(BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		World world = event.getWorld();
		BlockPos cauldronPos = event.getPos();
		if(world.getTileEntity(cauldronPos.offset(EnumFacing.UP)) instanceof IInventory)
		{
			if(world.getTileEntity(cauldronPos.offset(EnumFacing.DOWN)) instanceof IInventory)
			{
				if(CustomTileEntityHandler.INSTANCE.removeCustomTileEntityAtPos(world, cauldronPos, event.getPlayer().dimension))
				{
					EntityHelper.addChatComponentMessage(player, "TileEntity removed");
				}
			}
		}
	}
}