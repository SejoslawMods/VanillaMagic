package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestMoveBlock;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerMoveBlock extends EventCaller<QuestMoveBlock> {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event, (player, world, pos, direction) ->
                this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                    ItemStack bookStack = new ItemStack(Items.BOOK);

                    if (leftHandStack.getOrCreateTag().contains(NbtUtils.NBT_CAPTURED)) {
                        BlockState blockState = world.getBlockState(pos);

                        bookStack = new ItemStack(Items.ENCHANTED_BOOK);
                        bookStack.setDisplayName(TextUtils.combine(TextUtils.translate("quest.moveBlock.block"), TextUtils.translate(blockState.getBlock().getTranslationKey()).getFormattedText()));

                        CompoundNBT bookNbt = bookStack.getOrCreateTag();
                        bookNbt.put(NbtUtils.NBT_BLOCK, new CompoundNBT());
                        bookNbt = bookNbt.getCompound(NbtUtils.NBT_BLOCK);
                        bookNbt.putInt(NbtUtils.NBT_BLOCK_STATE, Block.getStateId(blockState));

                        TileEntity tileEntity = world.getTileEntity(pos);

                        if (tileEntity != null) {
                            tileEntity.write(bookNbt);
                            bookStack.setDisplayName(TextUtils.combine(TextUtils.translate("quest.moveBlock.tileEntity"), " " + bookStack.getDisplayName().getFormattedText()));
                            world.removeTileEntity(pos);
                            bookNbt.remove("x");
                            bookNbt.remove("y");
                            bookNbt.remove("z");
                        }

                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    } else {
                        CompoundNBT bookNbt = leftHandStack.getTag();

                        if (!bookNbt.contains(NbtUtils.NBT_BLOCK)) {
                            return;
                        }

                        bookNbt = bookNbt.getCompound(NbtUtils.NBT_BLOCK);
                        BlockPos spawnPos = pos.offset(direction);

                        if (!world.isAirBlock(spawnPos)) {
                            return;
                        }

                        BlockState blockState = Block.getStateById(bookNbt.getInt(NbtUtils.NBT_BLOCK_STATE));

                        world.setBlockState(spawnPos, blockState);
                        world.notifyNeighborsOfStateChange(spawnPos, blockState.getBlock());

                        TileEntity tile = world.getTileEntity(spawnPos);

                        if (tile != null) {
                            bookNbt.putInt("x", spawnPos.getX());
                            bookNbt.putInt("y", spawnPos.getY());
                            bookNbt.putInt("z", spawnPos.getZ());
                            tile.read(bookNbt);
                        }
                    }

                    player.setItemStackToSlot(EquipmentSlotType.MAINHAND, bookStack);
                }));
    }
}
