package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestMoveBlock;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Consumer;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerMoveBlock extends EventCaller<QuestMoveBlock> {
    private int counter = 0; // TODO: Find a way to detect if the event was called more than once

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (counter > 0) {
            counter = 0;
            return;
        }

        this.executor.onPlayerInteract(event, (player, world, pos, direction) ->
                this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                    if (this.canLoad(leftHandStack) && leftHandStack.getItem() == Items.ENCHANTED_BOOK) {
                        this.execute(player, new ItemStack(Items.BOOK), bookStack -> this.load(world, pos, direction, leftHandStack));
                    } else if (leftHandStack.getItem() == Items.BOOK) {
                        this.execute(player, new ItemStack(Items.ENCHANTED_BOOK), bookStack -> this.save(bookStack, world, pos));
                    }
                }));

        counter++;
    }

    private boolean canLoad(ItemStack leftHandStack) {
        CompoundNBT nbt = leftHandStack.getOrCreateTag();
        return nbt.contains(NbtUtils.NBT_BLOCK) && nbt.getCompound(NbtUtils.NBT_BLOCK).contains(NbtUtils.NBT_BLOCK_STATE);
    }

    private void execute(PlayerEntity player, ItemStack bookStack, Consumer<ItemStack> consumer) {
        consumer.accept(bookStack);
        player.setItemStackToSlot(EquipmentSlotType.OFFHAND, bookStack);
    }

    private void load(IWorld world, BlockPos pos, Direction direction, ItemStack leftHandStack) {
        CompoundNBT nbt = leftHandStack.getOrCreateTag().getCompound(NbtUtils.NBT_BLOCK);
        BlockPos spawnPos = pos.offset(direction);

        if (!world.isAirBlock(spawnPos)) {
            return;
        }

        BlockState blockState = Block.getStateById(nbt.getInt(NbtUtils.NBT_BLOCK_STATE));
        world.setBlockState(spawnPos, blockState, 1 | 2);

        TileEntity tile = world.getTileEntity(spawnPos);

        if (tile != null) {
            nbt.putInt("x", spawnPos.getX());
            nbt.putInt("y", spawnPos.getY());
            nbt.putInt("z", spawnPos.getZ());
            tile.read(tile.getBlockState(), nbt);
        }
    }

    private void save(ItemStack bookStack, IWorld world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);

        bookStack.setDisplayName(TextUtils.combine(TextUtils.translate("quest.moveBlock.block"), " " + TextUtils.getFormattedText(blockState.getBlock().getTranslationKey())));

        CompoundNBT nbt = new CompoundNBT();
        bookStack.getOrCreateTag().put(NbtUtils.NBT_BLOCK, nbt);
        nbt.putInt(NbtUtils.NBT_BLOCK_STATE, Block.getStateId(blockState));

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null) {
            tileEntity.write(nbt);
            bookStack.setDisplayName(TextUtils.combine(TextUtils.translate("quest.moveBlock.tileEntity"), " " + bookStack.getDisplayName().getString()));
            WorldUtils.asWorld(world).removeTileEntity(pos);
            nbt.remove("x");
            nbt.remove("y");
            nbt.remove("z");
        }

        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
    }
}
