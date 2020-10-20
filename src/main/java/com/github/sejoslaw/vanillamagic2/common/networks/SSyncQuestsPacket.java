package com.github.sejoslaw.vanillamagic2.common.networks;

import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SSyncQuestsPacket extends SVMPacket {
    private Set<String> playerQuests;

    public SSyncQuestsPacket(PlayerEntity player) {
        super(player);
        this.playerQuests = PlayerQuestProgressRegistry.getPlayerQuests(player);
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        super.readPacketData(buf);

        int questsCount = buf.readInt();
        this.playerQuests = new HashSet<>();

        for (int i = 0; i < questsCount; ++i) {
            this.playerQuests.add(buf.readString());
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        super.writePacketData(buf);

        buf.writeInt(this.playerQuests.size());
        this.playerQuests.forEach(buf::writeString);
    }

    public void processPacket(IClientPlayNetHandler handler) {
        sync(this);
    }

    public static void encode(SSyncQuestsPacket packet, PacketBuffer buf) {
        try {
            packet.writePacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SSyncQuestsPacket decode(PacketBuffer buf) {
        SSyncQuestsPacket packet = new SSyncQuestsPacket(null);

        try {
            packet.readPacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public static void consume(SSyncQuestsPacket packet, Supplier<NetworkEvent.Context> buf) {
        sync(packet);
    }

    private static void sync(SSyncQuestsPacket packet) {
        PlayerQuestProgressRegistry.clearData(packet.playerName);
        PlayerQuestProgressRegistry.setupPlayerData(packet.playerName, packet.playerQuests);
    }
}
