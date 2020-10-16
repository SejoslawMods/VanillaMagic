package com.github.sejoslaw.vanillamagic2.common.networks;

import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SSyncQuestsPacket implements IPacket<IClientPlayNetHandler> {
    private String playerName;
    private Set<String> playerQuests;

    public SSyncQuestsPacket withPlayer(PlayerEntity player) {
        this.playerName = EntityUtils.getPlayerNameFormatted(player);
        this.playerQuests = PlayerQuestProgressRegistry.getPlayerQuests(player);

        return this;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.playerName = buf.readString();

        int questsCount = buf.readInt();
        this.playerQuests = new HashSet<>();

        for (int i = 0; i < questsCount; ++i) {
            this.playerQuests.add(buf.readString());
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(this.playerName);

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
        SSyncQuestsPacket packet = new SSyncQuestsPacket();

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
