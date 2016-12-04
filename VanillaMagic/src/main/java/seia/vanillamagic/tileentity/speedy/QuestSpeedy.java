package seia.vanillamagic.tileentity.speedy;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.WorldHelper;

public class QuestSpeedy extends Quest
{
	@SubscribeEvent
	public void placeSpeedy(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		ItemStack leftHand = player.getHeldItemOffhand();
		BlockPos clickedPos = event.getPos();
		if(world.getBlockState(clickedPos).getBlock() instanceof BlockCauldron)
		{
			if(VanillaMagicItems.isCustomItem(leftHand, VanillaMagicItems.ACCELERATION_CRYSTAL))
			{
				ItemStack rightHand = player.getHeldItemMainhand();
				if(EnumWand.areWandsEqual(rightHand, EnumWand.BLAZE_ROD.wandItemStack))
				{
					if(player.isSneaking())
					{
						if(canPlayerGetAchievement(player))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							TileSpeedy speedy = new TileSpeedy();
							speedy.init(player.world, clickedPos);
							if(speedy.containsCrystal())
							{
								if(CustomTileEntityHandler.addCustomTileEntity(speedy, WorldHelper.getDimensionID(world)))
								{
									EntityHelper.addChatComponentMessage(player, speedy.getClass().getSimpleName() + " added");
								}
							}
						}
					}
				}
			}
		}
	}
}