package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCaptureEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCaptureEntity extends EventCaller<QuestCaptureEntity> {
    @SubscribeEvent
    public void onEntityCapture(PlayerInteractEvent.EntityInteract event) {
        this.executor.onEntityInteract(event,
                (player, entity, world, pos) -> WorldUtils.getIsRemote(world) || player.getHeldItemOffhand().getItem() != Items.BOOK ? null : this.quests.get(0),
                (player, entity, world, pos) ->
                        this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                            if (entity instanceof PlayerEntity) {
                                return;
                            }

                            leftHandStack = new ItemStack(Items.ENCHANTED_BOOK);
                            CompoundNBT stackNbt = leftHandStack.getOrCreateTag();

                            CompoundNBT entityTag = new CompoundNBT();
                            entity.writeUnlessRemoved(entityTag);

                            stackNbt.put(NbtUtils.NBT_CAPTURED, entityTag);
                            stackNbt.putString(NbtUtils.NBT_ENTITY_TYPE, entity.getType().getRegistryName().toString());
                            stackNbt.putString(NbtUtils.NBT_ENTITY_NAME, entity.getName().getString());
                            leftHandStack.setDisplayName(TextUtils.combine(TextUtils.translate("quest.capturedEntity.bookTitlePrefix"), " " + entity.getName().getString()));

                            entity.remove();

                            player.setItemStackToSlot(EquipmentSlotType.OFFHAND, leftHandStack);
                        }));
    }

    @SubscribeEvent
    public void onEntityRelease(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) -> {
                    CompoundNBT nbt = player.getHeldItemOffhand().getTag();
                    return nbt != null && nbt.contains(NbtUtils.NBT_CAPTURED) ? this.quests.get(0) : null;
                },
                (player, world, pos, direction, quest) ->
                    this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                        CompoundNBT stackNbt = leftHandStack.getTag();

                        if (stackNbt == null || !stackNbt.contains(NbtUtils.NBT_CAPTURED) || WorldUtils.getIsRemote(world)) {
                            return;
                        }

                        String type = stackNbt.getString(NbtUtils.NBT_ENTITY_TYPE);
                        Entity entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(type)).create(WorldUtils.asWorld(world));
                        entity.read(stackNbt.getCompound(NbtUtils.NBT_CAPTURED));

                        BlockPos spawnPos = pos.offset(direction);
                        entity.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY() + 0.5d, spawnPos.getZ() + 0.5D, 0, 0);
                        world.addEntity(entity);

                        player.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.BOOK));
                    }));
    }
}
