package seia.vanillamagic.tileentity.blockabsorber;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.WorldHelper;

public class QuestBlockAbsorber extends Quest
{
	/**
	 * ItemStack required in left hand to create {@link TileBlockAbsorber}
	 */
	public final ItemStack requiredLeftHand = new ItemStack(Blocks.GLASS);
	
	@SubscribeEvent
	public void onRightClickHopper(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(rightHand == null)
		{
			return;
		}
		if(!EnumWand.areWandsEqual(EnumWand.BLAZE_ROD.wandItemStack, rightHand))
		{
			return;
		}
		ItemStack leftHand = player.getHeldItemOffhand();
		if(leftHand == null)
		{
			return;
		}
		if(!ItemStack.areItemsEqual(leftHand, requiredLeftHand))
		{
			return;
		}
		if(player.isSneaking())
		{
			World world = event.getWorld();
			BlockPos clickedPos = event.getPos();
			// RightClicked on IHopper
			TileEntity clickedHopper = world.getTileEntity(clickedPos);
			if(clickedHopper == null)
			{
				return;
			}
			if(!(clickedHopper instanceof IHopper))
			{
				return;
			}
			if(!(clickedHopper instanceof TileEntityHopper))
			{
				return;
			}
			/*
			 * Player has got Blaze Rod in right hand and "requiredLeftHand" in left hand.
			 * We can now spawn TileEntity and let it do work.
			 */
			if(canPlayerGetAchievement(player))
			{
				player.addStat(achievement, 1);
			}
			if(player.hasAchievement(achievement))
			{
				TileBlockAbsorber tile = new TileBlockAbsorber();
				tile.init(player, clickedPos.offset(EnumFacing.UP));
				tile.setConnectedHopper((TileEntityHopper) clickedHopper);
				if(CustomTileEntityHandler.INSTANCE.addCustomTileEntity(tile, player.dimension))
				{
					EntityHelper.addChatComponentMessage(player, tile.getClass().getSimpleName() + " added");
					leftHand.stackSize--;
					if(leftHand.stackSize == 0)
					{
						player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, null);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onHopperDestroyed(BreakEvent event)
	{
		BlockPos hopperPos = event.getPos();
		World world = event.getWorld();
		IBlockState hopperState = world.getBlockState(hopperPos);
		if(Block.isEqualTo(hopperState.getBlock(), Blocks.HOPPER)) // if You broke Hopper
		{
			BlockPos customTilePos = hopperPos.offset(EnumFacing.UP);
			ICustomTileEntity customTile = CustomTileEntityHandler.INSTANCE.getCustomTileEntity(customTilePos, WorldHelper.getDimensionID(world));
			if(customTile == null)
			{
				return;
			}
			if(CustomTileEntityHandler.INSTANCE.removeCustomTileEntityAtPos(world, customTilePos))
			{
				EntityHelper.addChatComponentMessage(event.getPlayer(), customTile.getClass().getSimpleName() + " removed");
			}
		}
	}
}