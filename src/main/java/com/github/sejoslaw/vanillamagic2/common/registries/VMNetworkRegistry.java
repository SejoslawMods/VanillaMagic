package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.networks.SSyncQuestsPacket;
import com.github.sejoslaw.vanillamagic2.core.VanillaMagic;
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
    }

    /**
     * Tries to sync Player's Quests with Client.
     */
    public static void syncQuests(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }

        CHANNEL.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayerEntity) player)), new SSyncQuestsPacket().withPlayer(player));
    }
}
