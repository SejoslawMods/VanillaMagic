package com.github.sejoslaw.vanillamagic.quest.mobspawnerdrop;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMobSpawnerDrop extends Quest {
	@SubscribeEvent
	public void onMobSpawnerBreak(BreakEvent event) {
		IBlockState spawnerState = event.getState();
		Block spawnerBlock = spawnerState.getBlock();

		if (!(spawnerBlock instanceof BlockMobSpawner)) {
			return;
		}

		PlayerEntity player = event.getPlayer();
		checkQuestProgress(player);

		if (!hasQuest(player)) {
			return;
		}

		BlockPos spawnerPos = event.getPos();
		World world = event.getWorld();
		TileEntity tile = world.getTileEntity(spawnerPos);

		if ((tile == null) || !(tile instanceof TileEntityMobSpawner)) {
			return;
		}

		TileEntityMobSpawner tileMobSpawner = (TileEntityMobSpawner) tile;
		ItemStack spawnerStack = new ItemStack(spawnerBlock);
		ItemEntity spawnerEI = new ItemEntity(world, spawnerPos.getX() + 0.5D, spawnerPos.getY(),
				spawnerPos.getZ() + 0.5D, spawnerStack);
		world.spawnEntity(spawnerEI);

		ItemStack bookWithData = MobSpawnerRegistry.getStackFromTile(tileMobSpawner);
		ItemEntity spawnerBook = new ItemEntity(world, spawnerPos.getX() + 0.5D, spawnerPos.getY(),
				spawnerPos.getZ() + 0.5D, bookWithData);
		world.spawnEntity(spawnerBook);
		world.removeTileEntity(spawnerPos);
	}

	@SubscribeEvent
	public void onRightClickWithBook(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (!hasQuest(player) || ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		CompoundNBT stackTag = rightHand.getTagCompound();

		if ((stackTag == null) || !stackTag.hasKey(MobSpawnerRegistry.NBT_MOB_SPAWNER_DATA)) {
			return;
		}

		String id = stackTag.getString(MobSpawnerRegistry.NBT_ID);
		World world = event.getWorld();
		BlockPos spawnerPos = event.getPos();
		TileEntity tile = world.getTileEntity(spawnerPos);

		if ((tile == null) || !(tile instanceof TileEntityMobSpawner)) {
			return;
		}

		TileEntityMobSpawner tileMS = (TileEntityMobSpawner) tile;
		MobSpawnerRegistry.setID(tileMS, id, world, event.getPos());
		player.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStackUtil.NULL_STACK);
	}
}