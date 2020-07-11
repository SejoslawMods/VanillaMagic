package com.github.sejoslaw.vanillamagic.common.item.thecrystalofmothernature;

import com.github.sejoslaw.vanillamagic.api.event.EventMotherNatureCrystal;
import com.github.sejoslaw.vanillamagic.core.VMConfig;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMotherNatureCrystal extends Quest {
	/**
	 * Tick all around Plants when Player holds Crystal in LeftHand (OffHand).
	 */
	@SubscribeEvent
	public void onHoldInLeftHandTick(TickEvent.PlayerTickEvent event) // left hand
	{
		PlayerEntity player = event.player;
		World world = player.world;
		ItemStack leftHand = player.getHeldItemOffhand();

		if (ItemStackUtil.isNullStack(leftHand)) {
			return;
		}

		CompoundNBT stackTag = leftHand.getTag();
		if (stackTag == null) {
			return;
		}

		if (VMItems.isCustomItem(leftHand, VMItems.MOTHER_NATURE_CRYSTAL)) {
			onTickUpdate(world, player);
		}
	}

	/**
	 * One tick - tick all plants around Player.
	 */
	public void onTickUpdate(World world, PlayerEntity player) {
		int range = VMConfig.MOTHER_NATURE_CRYSTAL_RANGE.get(); // def 10
		int verticalRange = 3;

		int posX = (int) Math.round(player.getPosX() - 0.5f);
		int posY = (int) player.getPosY();
		int posZ = (int) Math.round(player.getPosZ() - 0.5f);

		for (int ix = posX - range; ix <= posX + range; ++ix) {
			for (int iz = posZ - range; iz <= posZ + range; ++iz) {
				for (int iy = posY - verticalRange; iy <= posY + verticalRange; ++iy) {
					this.updateTick(world, player, ix, iz, iy);
				}
			}
		}
	}

	public void updateTick(World world, PlayerEntity player, int ix, int iz, int iy) {
		BlockPos blockPos = new BlockPos(ix, iy, iz);
		Block block = world.getBlockState(blockPos).getBlock();

		if (!(block instanceof IPlantable) && !(block instanceof IGrowable)) {
			return;
		}

		if ((world.rand.nextInt(50) != 0) || (EventUtil.postEvent(new EventMotherNatureCrystal.TickBlock(VMItems.MOTHER_NATURE_CRYSTAL, player, world, blockPos)))) {
			return;
		}

		BlockState preBlockState = world.getBlockState(blockPos);
		block.tick(world.getBlockState(blockPos), (ServerWorld) world, blockPos, world.rand);
		BlockState newState = world.getBlockState(blockPos);

		if (!newState.equals(preBlockState)) {
			world.playEvent(2005, blockPos, 0);
		}
	}

	int countTicks = 0;

	/**
	 * When Crystal is used normally (from RightHand - MainHand) it should work like
	 * bonemeal.
	 */
	@SubscribeEvent
	public void onCrystalUse(PlayerInteractEvent.RightClickBlock event) // right hand
	{
		if (countTicks == 0) {
			countTicks++;
		} else {
			countTicks = 0;
			return;
		}

		PlayerEntity player = event.getPlayer();
		World world = event.getWorld();
		BlockPos clickedPos = event.getPos();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		CompoundNBT stackTag = rightHand.getTag();
		if (stackTag == null) {
			return;
		}

		if (!VMItems.isCustomItem(rightHand, VMItems.MOTHER_NATURE_CRYSTAL)) {
			return;
		}

		if (!hasQuest(player)) {
			addStat(player);
		}

		if (hasQuest(player) && BoneMealItem.applyBonemeal(rightHand, world, clickedPos, player)) {
			world.playEvent(2005, clickedPos, 0);
		}
	}
}
