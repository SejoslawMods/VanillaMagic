package seia.vanillamagic.machine.speedy;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.items.VanillaMagicItems;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.EntityHelper;
import seia.vanillamagic.utils.WorldHelper;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestSpeedy extends Quest
{
	public QuestSpeedy(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName)
	{
		super(required, posX, posY, icon, questName, uniqueName);
	}
	
	@SubscribeEvent
	public void placeSpeedy(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		ItemStack leftHand = player.getHeldItemOffhand();
		if(world.getBlockState(event.getPos()).getBlock() instanceof BlockCauldron)
		{
			if(VanillaMagicItems.INSTANCE.isCustomItem(leftHand, VanillaMagicItems.INSTANCE.itemAccelerationCrystal))
			{
				ItemStack rightHand = player.getHeldItemMainhand();
				if(EnumWand.areWandsEqual(rightHand, EnumWand.BLAZE_ROD.wandItemStack))
				{
					if(player.isSneaking())
					{
						if(!player.hasAchievement(achievement))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							TileSpeedy speedy = new TileSpeedy();
							speedy.init(world, event.getPos());
							if(speedy.containsCrystal())
							{
								if(CustomTileEntityHandler.INSTANCE.addCustomTileEntity(speedy, WorldHelper.getDimensionID(world)))
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