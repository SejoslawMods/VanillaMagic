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
			try
			{
				TileFarm tileFarm = new TileFarm();
				tileFarm.init(player, cauldronPos, radius);
				if(CustomTileEntityHandler.INSTANCE.addCustomTileEntity(tileFarm, player.dimension))
				{
					player.getHeldItemOffhand().stackSize -= mustHaveOffHand.stackSize;
				}
			}
			catch(Exception e)
			{
			}
		}
	}
	
	@SubscribeEvent
	public void stopFarm(BreakEvent event)
	{
		World world = event.getWorld();
		BlockPos cauldronPos = event.getPos();
		if(world.getTileEntity(cauldronPos.offset(EnumFacing.UP)) instanceof IInventory)
		{
			if(world.getTileEntity(cauldronPos.offset(EnumFacing.DOWN)) instanceof IInventory)
			{
				for(int i = 0; i < world.tickableTileEntities.size(); i++)
				{
					TileEntity tile = world.tickableTileEntities.get(i);
					if(tile instanceof TileFarm)
					{
						if(BlockPosHelper.isSameBlockPos(tile.getPos(), cauldronPos))
						{
							CustomTileEntityHandler.INSTANCE.removeCustomTileEntityAtPos(world, cauldronPos, event.getPlayer().dimension);
						}
					}
				}
			}
		}
	}
}