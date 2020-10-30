package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.functions.Action;
import com.github.sejoslaw.vanillamagic2.common.networks.SOpenVMTileEntityDetailsGuiPacket;
import com.github.sejoslaw.vanillamagic2.common.networks.SSpawnEntityPacket;
import com.github.sejoslaw.vanillamagic2.common.networks.SSyncQuestsPacket;
import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import com.github.sejoslaw.vanillamagic2.core.VanillaMagic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMNetworkRegistry {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(VanillaMagic.MODID, "channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void initialize() {
        int id = 0;

        CHANNEL.registerMessage(id++, SSyncQuestsPacket.class, SSyncQuestsPacket::encode, SSyncQuestsPacket::decode, SSyncQuestsPacket::consume);
        CHANNEL.registerMessage(id++, SOpenVMTileEntityDetailsGuiPacket.class, SOpenVMTileEntityDetailsGuiPacket::encode, SOpenVMTileEntityDetailsGuiPacket::decode, SOpenVMTileEntityDetailsGuiPacket::consume);
        CHANNEL.registerMessage(id++, SSpawnEntityPacket.class, SSpawnEntityPacket::encode, SSpawnEntityPacket::decode, SSpawnEntityPacket::consume);
    }

    /**
     * Tries to sync Player's Quests with Client.
     */
    public static void syncQuests(PlayerEntity player) {
        execute(player, () -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity) player)), new SSyncQuestsPacket(player)));
    }

    /**
     * Opens VM TileEntity Details GUI for specified Player.
     */
    public static void openVMTileEntityDetailsGui(PlayerEntity player, IVMTileEntity tileEntity) {
        execute(player, () -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity) player)), new SOpenVMTileEntityDetailsGuiPacket(player, tileEntity)));
    }

    /**
     * Spawns specified Entity on the client-side.
     */
    public static void spawnEntity(PlayerEntity player, Entity entity) {
        execute(player, () -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity) player)), new SSpawnEntityPacket(entity)));
    }

    private static void execute(PlayerEntity player, Action action) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        action.execute();
    }
}
