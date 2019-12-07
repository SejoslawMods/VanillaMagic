package com.github.sejoslaw.vanillamagic.quest;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.event.EventCaptureEntity;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.NBTUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCaptureEntity extends Quest {
	protected ItemStack requiredStackOffHand;
	protected IWand requiredWand;

	public void readData(JsonObject jo) {
		this.requiredStackOffHand = ItemStackUtil
				.getItemStackFromJSON(jo.get("requiredStackOffHand").getAsJsonObject());
		this.requiredWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
		this.icon = requiredStackOffHand.copy();
		super.readData(jo);
	}

	public ItemStack getRequiredStackOffHand() {
		return requiredStackOffHand;
	}

	public IWand getRequirdWand() {
		return requiredWand;
	}

	int clickedTimes = 0;

	@SubscribeEvent
	public void captureEntity(EntityInteract event) {
		PlayerEntity player = event.getPlayerEntity();
		World world = player.world;
		Entity target = event.getTarget();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		IWand wandPlayerHand = WandRegistry.getWandByItemStack(rightHand);

		if ((wandPlayerHand == null) || !WandRegistry.areWandsEqual(requiredWand, wandPlayerHand)
				|| !player.isSneaking()) {
			return;
		}

		ItemStack stackOffHand = player.getHeldItemOffhand();

		if (!ItemStack.areItemsEqual(requiredStackOffHand, stackOffHand)
				&& (ItemStackUtil.getStackSize(stackOffHand) != ItemStackUtil.getStackSize(requiredStackOffHand))) {
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

	int clickedTimesFree = 0;

	@SubscribeEvent
	public void respawnEntity(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		World world = player.world;
		BlockPos respawnPos = event.getPos();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		IWand wandPlayerHand = WandRegistry.getWandByItemStack(rightHand);

		if ((wandPlayerHand == null) || !WandRegistry.areWandsEqual(requiredWand, wandPlayerHand)
				|| !player.isSneaking()) {
			return;
		}

		ItemStack stackOffHand = player.getHeldItemOffhand();

		if (ItemStackUtil.isNullStack(stackOffHand)) {
			return;
		}

		CompoundNBT stackTag = stackOffHand.getTagCompound();

		if ((stackTag == null) || !stackTag.hasKey(NBTUtil.NBT_TAG_COMPOUND_ENTITY)) {
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
		stackOffHand.setDisplayName("EntityBook");
		CompoundNBT stackTagCompound = stackOffHand.getTagCompound();

		// Save to ItemStack
		if (!stackTagCompound.hasKey(NBTUtil.NBT_TAG_COMPOUND_ENTITY)) {
			CompoundNBT entityTag = new CompoundNBT();
			target.writeToNBT(entityTag);

			stackTagCompound.setTag(NBTUtil.NBT_TAG_COMPOUND_ENTITY, entityTag);
			stackTagCompound.putString(NBTUtil.NBT_ENTITY_TYPE, target.getClass().getCanonicalName());
			stackTagCompound.putString(NBTUtil.NBT_ENTITY_NAME, target.getName());
			stackOffHand.setDisplayName("Captured Entity: " + target.getName());

			world.removeEntity(target);
		}
		player.setItemStackToSlot(EquipmentSlotType.OFFHAND, stackOffHand);
	}

	public void handleRespawn(World world, PlayerEntity player, ItemStack stackOffHand, BlockPos respawnPos,
			Direction face) {
		if (world.isRemote) {
			return;
		}

		respawnPos = respawnPos.offset(face);
		CompoundNBT stackTagCompound = stackOffHand.getTagCompound();

		if (!stackTagCompound.hasKey(NBTUtil.NBT_TAG_COMPOUND_ENTITY)) {
			return;
		}

		CompoundNBT entityTag = stackTagCompound.getCompoundTag(NBTUtil.NBT_TAG_COMPOUND_ENTITY);
		String type = stackTagCompound.getString(NBTUtil.NBT_ENTITY_TYPE);
		Entity entity = createEntity(player, world, type);

		if ((entity == null)
				|| EventUtil.postEvent(new EventCaptureEntity.Respawn(world, player, respawnPos, face, entity, type))) {
			return;
		}

		entity.readFromNBT(entityTag);
		entity.setLocationAndAngles(respawnPos.getX() + 0.5D, respawnPos.getY() + 0.5d, respawnPos.getZ() + 0.5D, 0, 0);
		world.spawnEntity(entity);

		ItemStack newOffHand = requiredStackOffHand.copy();
		player.setItemStackToSlot(EquipmentSlotType.OFFHAND, newOffHand);
	}

	@Nullable
	public Entity createEntity(PlayerEntity player, World world, String type) {
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
		CompoundNBT entityContainerTag = entityContainer.getTagCompound();

		if ((entityContainerTag == null) || !entityContainerTag.hasKey(NBTUtil.NBT_TAG_COMPOUND_ENTITY)) {
			return;
		}

		String type = entityContainerTag.getString(NBTUtil.NBT_ENTITY_NAME);
		event.getToolTip().add("Captured Entity: " + type);
	}
}