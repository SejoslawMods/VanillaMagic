package seia.vanillamagic.item.liquidsuppressioncrystal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import seia.vanillamagic.api.event.EventLiquidSuppressionCrystal;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.BlockUtil;
import seia.vanillamagic.util.ItemStackUtil;

public class QuestLiquidSuppressionCrystal extends Quest
{
	/**
	 * if  Player has the Crystal in inventory and walk near liquid source, 
	 * the liquid should disappear for some time.
	 */
	@SubscribeEvent
	public void onItemHeld(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		World world = player.world;
		ItemStack leftHand = player.getHeldItemOffhand();
		if (ItemStackUtil.isNullStack(leftHand)) return;
		
		if (playerHasCrystalInLeftHand(leftHand))
		{
			if (!hasQuest(player)) addStat(player);
			if (hasQuest(player)) onCrystalUpdate(leftHand, world, player);
		}
	}
	
	/**
	 * Returns true if  the given stack is a Liquid Suppression Crystal. <br>
	 * In other words. Returns true if  Player has Liquid Suppression Crystal in left hand.
	 */
	public boolean playerHasCrystalInLeftHand(ItemStack leftHand)
	{
		return VanillaMagicItems.isCustomItem(leftHand, VanillaMagicItems.LIQUID_SUPPRESSION_CRYSTAL);
	}
	
	/**
	 * Once per tick.
	 * Make liquid sources disappear.
	 */
	public void onCrystalUpdate(ItemStack leftHand, World world, EntityPlayer player)
	{
		int x = (int) player.posX;
		int y = (int) player.posY;
		int z = (int) player.posZ;
		int radius = VMConfig.LIQUID_SUPPRESSION_CRYSTAL_RADIUS; // def 5
		int refresh = 100; // how often block should be refreshed
		
		for (int i = -radius; i <= radius; ++i)
		{
			for (int j = -radius; j <= radius; ++j)
			{
				for (int k = -radius; k <= radius; ++k)
				{
					BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
					IBlockState state = world.getBlockState(blockPos);
					if (BlockUtil.isBlockLiquid(state) && world.getTileEntity(blockPos) == null)
					{
						if (!MinecraftForge.EVENT_BUS.post(new EventLiquidSuppressionCrystal.CreateAirBlock(
								player, world, leftHand, blockPos, VanillaMagicItems.LIQUID_SUPPRESSION_CRYSTAL)))
						{
							TileLiquidSuppression.createAirBlock(world, blockPos, refresh);
						}
					}
					else
					{
						TileEntity tile = world.getTileEntity(blockPos);
						if (tile instanceof TileLiquidSuppression)
						{
							if (!MinecraftForge.EVENT_BUS.post(new EventLiquidSuppressionCrystal.UseOnTileEntity(
									player, world, leftHand, blockPos, VanillaMagicItems.LIQUID_SUPPRESSION_CRYSTAL, tile)))
							{
								((TileLiquidSuppression) tile).resetDuration(refresh);
							}
						}
					}
				}
			}
		}
	}
}