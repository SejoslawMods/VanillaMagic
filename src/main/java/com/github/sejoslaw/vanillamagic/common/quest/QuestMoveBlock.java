package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.NBTUtil;
import com.google.gson.JsonObject;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMoveBlock extends Quest {
    /*
     * Stack offHand must be an item that has maxStackSize = 1 if it is a book, it must be a renamed book.
     */
    protected ItemStack requiredStackOffHand;
    protected IWand requiredWand;

    private int clickedTimes = 0;

    public void readData(JsonObject jo) {
        this.requiredStackOffHand = ItemStackUtil.getItemStackFromJSON(jo.get("requiredStackOffHand").getAsJsonObject());
        this.requiredWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
        this.icon = requiredStackOffHand.copy();

        super.readData(jo);
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) throws RuntimeException {
        PlayerEntity player = event.getPlayer();
        BlockPos wantedBlockPos = event.getPos();
        World world = player.world;
        ItemStack mainHand = player.getHeldItemMainhand();

        if (ItemStackUtil.isNullStack(mainHand)) {
            return;
        }

        IWand wandPlayerHand = WandRegistry.getWandByItemStack(mainHand);

        if ((wandPlayerHand == null) || !WandRegistry.areWandsEqual(requiredWand, wandPlayerHand) || !player.isSneaking()) {
            return;
        }

        ItemStack stackOffHand = player.getHeldItemOffhand();

        if (ItemStackUtil.isNullStack(stackOffHand)) {
            return;
        }

        if (ItemStack.areItemsEqual(requiredStackOffHand, stackOffHand)) {
            if (ItemStackUtil.getStackSize(stackOffHand) == ItemStackUtil.getStackSize(requiredStackOffHand)) {
                checkQuestProgress(player);

                if (hasQuest(player)) {
                    if (clickedTimes > 0) {
                        clickedTimes = 0;
                        return;
                    }

                    if (!EventUtil.postEvent(new BlockEvent.BreakEvent(world, wantedBlockPos, world.getBlockState(wantedBlockPos), player))) {
                        handleSave(world, player, wantedBlockPos);
                        clickedTimes++;
                    }
                }
            }
            return;
        }

        CompoundNBT stackTag = stackOffHand.getOrCreateTag();
        if (!stackTag.contains(NBTUtil.NBT_TAG_COMPOUND_NAME)) {
            return;
        }

        if (clickedTimes > 0) {
            clickedTimes = 0;
            return;
        }

        handleLoad(world, player, stackOffHand, wantedBlockPos, event.getFace());
        clickedTimes++;
    }

    private void handleSave(World world, PlayerEntity player, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        ItemStack bookWithDataStack = new ItemStack(Items.ENCHANTED_BOOK);
        bookWithDataStack.setDisplayName(TextUtil.wrap("Block: " + new TranslationTextComponent(block.getTranslationKey()).getFormattedText()));

        CompoundNBT bookWithDataNbt = bookWithDataStack.getOrCreateTag();
        bookWithDataNbt.put(NBTUtil.NBT_BLOCK, new CompoundNBT());
        bookWithDataNbt = bookWithDataNbt.getCompound(NBTUtil.NBT_BLOCK);

        int stateId = Block.getStateId(blockState);
        bookWithDataNbt.putInt(NBTUtil.NBT_BLOCK_STATE, stateId);

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null) {
            tileEntity.write(bookWithDataNbt);
            bookWithDataStack.setDisplayName(TextUtil.wrap("[TileEntity] " + bookWithDataStack.getDisplayName().getFormattedText()));
            world.removeTileEntity(pos);
            bookWithDataNbt.remove("x");
            bookWithDataNbt.remove("y");
            bookWithDataNbt.remove("z");
        }

        player.setItemStackToSlot(EquipmentSlotType.MAINHAND, bookWithDataStack);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    private void handleLoad(World world, PlayerEntity player, ItemStack bookStack, BlockPos pos, Direction offset) {
        CompoundNBT bookNbt = bookStack.getTag();

        if (!bookNbt.contains(NBTUtil.NBT_BLOCK)) {
            return;
        }

        bookNbt = bookNbt.getCompound(NBTUtil.NBT_BLOCK);
        pos = pos.offset(offset);

        if (!world.isAirBlock(pos)) {
            return;
        }

        int stateId = bookNbt.getInt(NBTUtil.NBT_BLOCK_STATE);
        BlockState blockState = Block.getStateById(stateId);

        world.setBlockState(pos, blockState);
        world.notifyNeighborsOfStateChange(pos, blockState.getBlock());

        TileEntity tile = world.getTileEntity(pos);

        if (tile != null) {
            bookNbt.putInt("x", pos.getX());
            bookNbt.putInt("y", pos.getY());
            bookNbt.putInt("z", pos.getZ());
            tile.read(bookNbt);
        }

        player.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOOK));
    }

    @SubscribeEvent
    public void addSavedBlockDataTooltip(ItemTooltipEvent event) {
        ItemStack savedPosBook = event.getItemStack();
        CompoundNBT savedPosTagCompound = savedPosBook.getOrCreateTag();

        if (!savedPosTagCompound.contains(NBTUtil.NBT_TAG_COMPOUND_NAME)) {
            return;
        }

        CompoundNBT questTag = savedPosTagCompound.getCompound(NBTUtil.NBT_TAG_COMPOUND_NAME);
        BlockState state = Block.getStateById(questTag.getInt(NBTUtil.NBT_BLOCK));

        List<ITextComponent> blockInfo = event.getToolTip();
        blockInfo.add(TextUtil.wrap("Saved Block: " + state.getBlock().getNameTextComponent().getFormattedText()));
    }
}