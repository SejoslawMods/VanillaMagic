package com.github.sejoslaw.vanillamagic.common.quest.mobspawnerdrop;

import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMobSpawnerDrop extends Quest {
    @SubscribeEvent
    public void onMobSpawnerBreak(BlockEvent.BreakEvent event) {
        BlockState spawnerState = event.getState();
        Block spawnerBlock = spawnerState.getBlock();

        if (!(spawnerBlock instanceof SpawnerBlock)) {
            return;
        }

        PlayerEntity player = event.getPlayer();
        checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        BlockPos spawnerPos = event.getPos();
        World world = event.getWorld().getWorld();
        TileEntity tile = world.getTileEntity(spawnerPos);

        if (!(tile instanceof MobSpawnerTileEntity)) {
            return;
        }

        MobSpawnerTileEntity tileMobSpawner = (MobSpawnerTileEntity) tile;
        ItemStack spawnerStack = new ItemStack(spawnerBlock);
        ItemEntity spawnerEI = new ItemEntity(world, spawnerPos.getX() + 0.5D, spawnerPos.getY(), spawnerPos.getZ() + 0.5D, spawnerStack);
        world.addEntity(spawnerEI);

        ItemStack bookWithData = MobSpawnerRegistry.getStackFromTile(tileMobSpawner);
        ItemEntity spawnerBook = new ItemEntity(world, spawnerPos.getX() + 0.5D, spawnerPos.getY(), spawnerPos.getZ() + 0.5D, bookWithData);
        world.addEntity(spawnerBook);

        world.removeTileEntity(spawnerPos);
    }

    @SubscribeEvent
    public void onRightClickWithBook(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        ItemStack rightHand = player.getHeldItemMainhand();

        if (!hasQuest(player) || ItemStackUtil.isNullStack(rightHand)) {
            return;
        }

        CompoundNBT stackTag = rightHand.getOrCreateTag();

        if (!stackTag.contains(MobSpawnerRegistry.NBT_SPAWNER_ENTITY)) {
            return;
        }

        String entityId = stackTag.getString(MobSpawnerRegistry.NBT_SPAWNER_ENTITY);
        World world = event.getWorld();
        BlockPos spawnerPos = event.getPos();
        TileEntity tile = world.getTileEntity(spawnerPos);

        if (!(tile instanceof MobSpawnerTileEntity)) {
            return;
        }

        MobSpawnerTileEntity tileMS = (MobSpawnerTileEntity) tile;
        MobSpawnerRegistry.setID(tileMS, entityId, world, event.getPos());
        
        player.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStackUtil.NULL_STACK);
    }
}