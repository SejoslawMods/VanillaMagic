package seia.vanillamagic.machine.quarry;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestMachineActivate;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestQuarry extends QuestMachineActivate
{
	public QuestQuarry(Quest required, int posX, int posY, String questName, String uniqueName) 
	{
		super(required, posX, posY, new ItemStack(Items.CAULDRON), questName, uniqueName,
				null, EnumWand.BLAZE_ROD.wandItemStack);
	}
	
	@SubscribeEvent
	public void startQuarry(RightClickBlock event)
	{
		BlockPos quarryPos = event.getPos();
		EntityPlayer player = event.getEntityPlayer();
		ItemStack itemInHand = player.getHeldItemMainhand();
		World world = player.worldObj;
		if(!player.isSneaking())
		{
			return;
		}
		if(itemInHand == null)
		{
			return;
		}
		if(itemInHand.getItem().equals(EnumWand.BLAZE_ROD.wandItemStack.getItem()))
		{
			if(!player.hasAchievement(achievement))
			{
				player.addStat(achievement, 1);
			}
			if(player.hasAchievement(achievement))
			{
				TileQuarry tileQuarry = new TileQuarry();
				tileQuarry.init(world, quarryPos);
				if(tileQuarry.checkSurroundings())
				{
					CustomTileEntityHandler.INSTANCE.addCustomTileEntity(tileQuarry, player.dimension);
				}
			}
		}
	}

	@SubscribeEvent
	public void stopQuarry(BreakEvent event)
	{
		BlockPos quarryPos = event.getPos();
		EntityPlayer player = event.getPlayer();
		World world = player.worldObj;
		if(world.getBlockState(quarryPos).getBlock() instanceof BlockCauldron)
		{
			CustomTileEntityHandler.INSTANCE.removeCustomTileEntityAtPos(world, quarryPos, player.dimension);
		}
	}
}