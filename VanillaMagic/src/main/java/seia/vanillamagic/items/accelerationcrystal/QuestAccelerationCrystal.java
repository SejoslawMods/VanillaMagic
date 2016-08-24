package seia.vanillamagic.items.accelerationcrystal;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.items.VanillaMagicItems;
import seia.vanillamagic.quest.Quest;

public class QuestAccelerationCrystal extends Quest
{
	public QuestAccelerationCrystal(Quest required, int posX, int posY, ItemStack icon, String questName,
			String uniqueName) 
	{
		super(required, posX, posY, icon, questName, uniqueName);
	}
	
	int updateTicks = 100;
	@SubscribeEvent
	public void rightClickBlock(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;
		BlockPos clickedPos = event.getPos();
		IBlockState clickedState = world.getBlockState(clickedPos);
		Block clickedBlock = clickedState.getBlock();
		if(world.isRemote)
		{
			return;
		}
		if(!world.isAirBlock(clickedPos))
		{
			ItemStack rightHand = player.getHeldItemMainhand();
			if(VanillaMagicItems.INSTANCE.isCustomItem(rightHand, VanillaMagicItems.INSTANCE.itemAccelerationCrystal))
			{
				if(!player.hasAchievement(achievement))
				{
					player.addStat(achievement, 1);
				}
				if(player.hasAchievement(achievement))
				{
					TileEntity tile = world.getTileEntity(clickedPos);
					Random rand = new Random();
					for(int i = 0; i < updateTicks; i++)
					{
						if(tile == null)
						{
							clickedBlock.updateTick(world, clickedPos, clickedState, rand);
						}
						else if(tile instanceof ITickable)
						{
							((ITickable) tile).update();
						}
					}
				}
			}
		}
	}
}