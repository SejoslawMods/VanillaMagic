package seia.vanillamagic.items.liquidsuppressioncrystal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import seia.vanillamagic.items.VanillaMagicItems;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.BlockHelper;

public class QuestLiquidSuppressionCrystal extends Quest
{
//	public QuestLiquidSuppressionCrystal(Quest required, int posX, int posY, ItemStack icon, String questName,
//			String uniqueName) 
//	{
//		super(required, posX, posY, icon, questName, uniqueName);
//	}
	
	@SubscribeEvent
	public void onItemHeld(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		World world = player.worldObj;
		ItemStack leftHand = player.getHeldItemOffhand();
		if(leftHand == null)
		{
			return;
		}
		if(playerHasCrystalInLeftHand(leftHand))
		{
			if(!player.hasAchievement(achievement))
			{
				player.addStat(achievement, 1);
			}
			if(player.hasAchievement(achievement))
			{
				onCrystalUpdate(leftHand, world, player);
			}
		}
	}
	
	/**
	 * Returns true if the given stack is a Liquid Suppression Crystal. <br>
	 * In other words. Returns true if Player has Liquid Suppression Crystal in left hand.
	 */
	public boolean playerHasCrystalInLeftHand(ItemStack leftHand)
	{
		return VanillaMagicItems.INSTANCE.isCustomItem(leftHand, VanillaMagicItems.INSTANCE.itemLiquidSuppressionCrystal);
	}
	
	public void onCrystalUpdate(ItemStack leftHand, World world, EntityPlayer player)
	{
		int x = (int) player.posX;
		int y = (int) player.posY;
		int z = (int) player.posZ;
		int radius = 5;
		int refresh = 100; // how often block should be refreshed
		
		for(int i = -radius; i <= radius; i++)
		{
			for(int j = -radius; j <= radius; j++)
			{
				for(int k = -radius; k <= radius; k++)
				{
					BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
					IBlockState state = world.getBlockState(blockPos);
					if(BlockHelper.isBlockLiquid(state) && world.getTileEntity(blockPos) == null)
					{
						TileLiquidSuppression.createAirBlock(world, blockPos, refresh);
					}
					else
					{
						TileEntity tile = world.getTileEntity(blockPos);
						if(tile instanceof TileLiquidSuppression)
						{
							((TileLiquidSuppression) tile).resetDuration(refresh);
						}
					}
				}
			}
		}
	}
}