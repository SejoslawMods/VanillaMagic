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
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;

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
		if (ItemStackUtil.isNullStack(rightHand)) return;
		if (!WandRegistry.areWandsEqual(WandRegistry.WAND_BLAZE_ROD.getWandStack(), rightHand)) return;
		
		ItemStack leftHand = player.getHeldItemOffhand();
		if (ItemStackUtil.isNullStack(leftHand)) return;
		if (!ItemStack.areItemsEqual(leftHand, requiredLeftHand)) return;
		
		if (player.isSneaking())
		{
			World world = event.getWorld();
			BlockPos clickedPos = event.getPos();
			// RightClicked on IHopper
			TileEntity clickedHopper = world.getTileEntity(clickedPos);
			if (clickedHopper == null) return;
			if (!(clickedHopper instanceof IHopper)) return;
			if (!(clickedHopper instanceof TileEntityHopper)) return;
			
			/*
			 * Player has got Blaze Rod in right hand and "requiredLeftHand" in left hand.
			 * We can now spawn TileEntity and let it do work.
			 */
			if (canPlayerGetQuest(player)) addStat(player);
			
			if (hasQuest(player))
			{
				TileBlockAbsorber tile = new TileBlockAbsorber();
				tile.init(player.world, clickedPos.offset(EnumFacing.UP));
				if (CustomTileEntityHandler.addCustomTileEntity(tile, player.dimension))
				{
					EntityUtil.addChatComponentMessageNoSpam(player, tile.getClass().getSimpleName() + " added");
					ItemStackUtil.decreaseStackSize(leftHand, 1);
					if (ItemStackUtil.getStackSize(leftHand) == 0) player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, null);
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
		if (Block.isEqualTo(hopperState.getBlock(), Blocks.HOPPER)) // if  You broke Hopper
		{
			BlockPos customTilePos = hopperPos.offset(EnumFacing.UP);
			CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, customTilePos, event.getPlayer());
		}
	}
}