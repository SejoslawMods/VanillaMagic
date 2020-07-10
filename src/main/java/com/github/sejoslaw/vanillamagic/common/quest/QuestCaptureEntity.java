package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.api.event.EventCaptureEntity;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.NBTUtil;
import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCaptureEntity extends Quest {
    protected ItemStack requiredStackOffHand;
    protected IWand requiredWand;

    private int clickedTimes = 0;
    private int clickedTimesFree = 0;

    public void readData(JsonObject jo) {
        this.requiredStackOffHand = ItemStackUtil.getItemStackFromJSON(jo.get("requiredStackOffHand").getAsJsonObject());
        this.requiredWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
        this.icon = requiredStackOffHand.copy();

        super.readData(jo);
    }

    @SubscribeEvent
    public void captureEntity(PlayerInteractEvent.EntityInteract event) {
        PlayerEntity player = event.getPlayer();
        World world = player.world;
        Entity target = event.getTarget();
        ItemStack rightHand = player.getHeldItemMainhand();

        if (ItemStackUtil.isNullStack(rightHand)) {
            return;
        }

        IWand wandPlayerHand = WandRegistry.getWandByItemStack(rightHand);

        if ((wandPlayerHand == null) || !WandRegistry.areWandsEqual(requiredWand, wandPlayerHand) || !player.isSneaking()) {
            return;
        }

        ItemStack stackOffHand = player.getHeldItemOffhand();

        if (!ItemStack.areItemsEqual(requiredStackOffHand, stackOffHand) && (ItemStackUtil.getStackSize(stackOffHand) != ItemStackUtil.getStackSize(requiredStackOffHand))) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        if (clickedTimes > 0) {
            clickedTimes = 0;
            return;
        }

        if (target instanceof PlayerEntity) {
            event.setCanceled(true);
            return;
        }

        if (!EventUtil.postEvent(new EventCaptureEntity.Capture(world, player, target))) {
            handleCapture(world, player, target);
        }

        clickedTimes++;
    }

    @SubscribeEvent
    public void respawnEntity(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        World world = player.world;
        BlockPos respawnPos = event.getPos();
        ItemStack rightHand = player.getHeldItemMainhand();

        if (ItemStackUtil.isNullStack(rightHand)) {
            return;
        }

        IWand wandPlayerHand = WandRegistry.getWandByItemStack(rightHand);

        if ((wandPlayerHand == null) || !WandRegistry.areWandsEqual(requiredWand, wandPlayerHand) || !player.isSneaking()) {
            return;
        }

        ItemStack stackOffHand = player.getHeldItemOffhand();

        if (ItemStackUtil.isNullStack(stackOffHand)) {
            return;
        }

        CompoundNBT stackTag = stackOffHand.getOrCreateTag();

        if (!stackTag.contains(NBTUtil.NBT_TAG_COMPOUND_ENTITY)) {
            return;
        }

        if (clickedTimesFree > 0) {
            clickedTimesFree = 0;
            return;
        }

        handleRespawn(world, player, stackOffHand, respawnPos, event.getFace());
        clickedTimesFree++;
    }

    public void handleCapture(World world, PlayerEntity player, Entity target) {
        if (world.isRemote) {
            return;
        }

        ItemStack stackOffHand = new ItemStack(Items.ENCHANTED_BOOK);
        stackOffHand.setDisplayName(TextUtil.wrap("EntityBook"));
        CompoundNBT stackTagCompound = stackOffHand.getOrCreateTag();

        if (!stackTagCompound.contains(NBTUtil.NBT_TAG_COMPOUND_ENTITY)) {
            CompoundNBT entityTag = new CompoundNBT();
            target.writeUnlessRemoved(entityTag);

            stackTagCompound.put(NBTUtil.NBT_TAG_COMPOUND_ENTITY, entityTag);
            stackTagCompound.putString(NBTUtil.NBT_ENTITY_TYPE, target.getClass().getCanonicalName());
            stackTagCompound.putString(NBTUtil.NBT_ENTITY_NAME, target.getName().getFormattedText());
            stackOffHand.setDisplayName(TextUtil.wrap("Captured Entity: " + target.getName().getFormattedText()));

            target.remove();
        }

        player.setItemStackToSlot(EquipmentSlotType.OFFHAND, stackOffHand);
    }

    public void handleRespawn(World world, PlayerEntity player, ItemStack stackOffHand, BlockPos respawnPos, Direction face) {
        if (world.isRemote) {
            return;
        }

        respawnPos = respawnPos.offset(face);
        CompoundNBT stackTagCompound = stackOffHand.getOrCreateTag();

        if (!stackTagCompound.contains(NBTUtil.NBT_TAG_COMPOUND_ENTITY)) {
            return;
        }

        CompoundNBT entityTag = stackTagCompound.getCompound(NBTUtil.NBT_TAG_COMPOUND_ENTITY);
        String type = stackTagCompound.getString(NBTUtil.NBT_ENTITY_TYPE);
        Entity entity = createEntity(world, type);

        if ((entity == null) || EventUtil.postEvent(new EventCaptureEntity.Respawn(world, player, respawnPos, face, entity, type))) {
            return;
        }

        entity.read(entityTag);
        entity.setLocationAndAngles(respawnPos.getX() + 0.5D, respawnPos.getY() + 0.5d, respawnPos.getZ() + 0.5D, 0, 0);
        world.addEntity(entity);

        ItemStack newOffHand = requiredStackOffHand.copy();
        player.setItemStackToSlot(EquipmentSlotType.OFFHAND, newOffHand);
    }

    public Entity createEntity(World world, String type) {
        Entity entity = null;

        try {
            entity = (Entity) Class.forName(type).getConstructor(World.class).newInstance(world);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    @SubscribeEvent
    public void addEntityDataTooltip(ItemTooltipEvent event) {
        ItemStack entityContainer = event.getItemStack();
        CompoundNBT entityContainerTag = entityContainer.getOrCreateTag();

        if (!entityContainerTag.contains(NBTUtil.NBT_TAG_COMPOUND_ENTITY)) {
            return;
        }

        String type = entityContainerTag.getString(NBTUtil.NBT_ENTITY_NAME);
        event.getToolTip().add(TextUtil.wrap("Captured Entity: " + type));
    }
}