package com.github.sejoslaw.vanillamagic.common.util;

import javax.annotation.Nullable;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Class which wraps various PlayerEntity related things.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EntityUtil {
    private static final int DELETION_ID = 2525277;
    private static int LAST_ADDED;

    private EntityUtil() {
    }

    public static void addChatComponentMessage(String message) {
        addChatComponentMessageNoSpam(new StringTextComponent(message));
    }

    /**
     * Adds a message to Player's chat with no spam.
     */
    public static void addChatComponentMessageNoSpam(ITextComponent msg) {
        addChatComponentMessageNoSpam(new ITextComponent[]{ msg });
    }

    /**
     * Adds a message to Player's chat with no spam.
     */
    public static void addChatComponentMessageNoSpam(ITextComponent[] msg) {
        NewChatGui chat = GuiUtil.getChatGui();

        for (int i = DELETION_ID + msg.length - 1; i <= LAST_ADDED; ++i) {
            chat.deleteChatLine(i);
        }

        for (int i = 0; i < msg.length; ++i) {
            chat.printChatMessageWithOptionalDeletion(TextUtil.getVanillaMagicInfo(msg[i]), DELETION_ID + i);
        }

        LAST_ADDED = DELETION_ID + msg.length - 1;
    }

    public static boolean hasPlayerCraftingTableInMainHand(PlayerEntity player) {
        ItemStack mainHand = player.getHeldItemMainhand();

        if (ItemStackUtil.isNullStack(mainHand)) {
            return false;
        }

        Block ct = Block.getBlockFromItem(mainHand.getItem());

        return BlockUtil.areEqual(ct, Blocks.CRAFTING_TABLE);
    }

    public static ItemEntity copyItem(ItemEntity original) {
        return new ItemEntity(original.world, original.getPosX(), original.getPosY(), original.getPosZ(), original.getItem().copy());
    }

    /**
     * knocks back "toKnockBack" entity
     */
    public static void knockBack(Entity toKnockBack, float strength, double xRatio, double zRatio) {
        toKnockBack.isAirBorne = true;
        float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);

        double motionX = toKnockBack.getMotion().getX();
        double motionY = toKnockBack.getMotion().getY();
        double motionZ = toKnockBack.getMotion().getZ();

        motionX /= 2.0D;
        motionZ /= 2.0D;

        motionX -= xRatio / (double) f * (double) strength;
        motionZ -= zRatio / (double) f * (double) strength;

        if (toKnockBack.onGround) {
            motionY /= 2.0D;
            motionY += strength;

            if (motionY > 0.4000000059604645D) {
                motionY = 0.4000000059604645D;
            }
        }

        toKnockBack.setMotion(motionX, motionY, motionZ);
    }

    /**
     * knocks back "toKnockBack" entity
     */
    public static void knockBack(Entity user, Entity toKnockBack, float strength) {
        double xRatio = user.getPosX() - toKnockBack.getPosX();
        double zRatio = user.getPosZ() - toKnockBack.getPosZ();

        knockBack(toKnockBack, strength, xRatio, zRatio);
    }

    public static void addRandomArmorToEntity(Entity entity) {
        addRandomItemToSlot(entity, EquipmentSlotType.CHEST);
        addRandomItemToSlot(entity, EquipmentSlotType.FEET);
        addRandomItemToSlot(entity, EquipmentSlotType.HEAD);
        addRandomItemToSlot(entity, EquipmentSlotType.LEGS);
        addRandomItemToSlot(entity, EquipmentSlotType.MAINHAND);
    }

    public static Entity addRandomItemToSlot(Entity entity, EquipmentSlotType slot) {
        entity.setItemStackToSlot(slot, getRandomItemForSlot(slot));

        return entity;
    }

    public static ItemStack getRandomItemForSlot(EquipmentSlotType slot) {
        Item item = null;

        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
            item = ListUtil.getRandomObjectFromList(EquipmentUtil.WEAPONS);
        } else if (slot == EquipmentSlotType.CHEST) {
            item = ListUtil.getRandomObjectFromList(EquipmentUtil.CHEST_PLATES);
        } else if (slot == EquipmentSlotType.FEET) {
            item = ListUtil.getRandomObjectFromList(EquipmentUtil.BOOTS);
        } else if (slot == EquipmentSlotType.HEAD) {
            item = ListUtil.getRandomObjectFromList(EquipmentUtil.HELMETS);
        } else if (slot == EquipmentSlotType.LEGS) {
            item = ListUtil.getRandomObjectFromList(EquipmentUtil.LEGGINGS);
        }

        return new ItemStack(item);
    }

    // TODO: Fix long range Fangs attaks.
    @Nullable
    public static Entity getClosestLookingAt(Entity looking, double distance) {
        double checkingDistance = 0;

        while (checkingDistance < distance) {
            RayTraceResult result = looking.pick(checkingDistance, 0.1F, true);

            if (result instanceof BlockRayTraceResult) {
                return null;
            } else if (result instanceof EntityRayTraceResult) {
                return ((EntityRayTraceResult) result).getEntity();
            }

            checkingDistance += 1.0D;
        }

        return null;
    }

    public static BlockRayTraceResult rayTrace(World worldIn, PlayerEntity player) {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;

        double x = player.getPosX();
        double y = player.getPosY() + (double) player.getEyeHeight();
        double z = player.getPosZ();

        Vec3d vec3d = new Vec3d(x, y, z);

        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);

        float f6 = f3 * f4;
        float f7 = f2 * f4;

        double d3 = 1000.0D;
        Vec3d vec3d1 = vec3d.add((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);

        RayTraceContext context = new RayTraceContext(vec3d, vec3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, player);
        return worldIn.rayTraceBlocks(context);
    }

    @Nullable
    public static ServerPlayerEntity getServerPlayerFromClientPlayer(ServerWorld world, ClientPlayerEntity clientPlayer) {
        if (world == null || clientPlayer == null) {
            return null;
        }

        return world.getPlayers()
                .stream()
                .filter(serverPlayer -> serverPlayer.getName().equals(clientPlayer.getName()))
                .findAny()
                .get();
    }
}
