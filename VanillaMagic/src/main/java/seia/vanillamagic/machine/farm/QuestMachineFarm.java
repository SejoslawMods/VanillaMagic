package seia.vanillamagic.machine.farm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
			player.getHeldItemOffhand().stackSize -= mustHaveOffHand.stackSize;
			TileFarm tileFarm = new TileFarm(player, cauldronPos, radius);
			world.setTileEntity(cauldronPos, tileFarm);
			System.out.println("Farm placed at:");
			BlockPosHelper.printCoords(cauldronPos);
		}
	}
}