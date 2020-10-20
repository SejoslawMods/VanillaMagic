package com.github.sejoslaw.vanillamagic2.common.networks;

import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class SVMPacket implements IPacket<IClientPlayNetHandler> {
    protected String playerName;

    public SVMPacket(PlayerEntity player) {
        this.playerName = EntityUtils.getPlayerNameFormatted(player);
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.playerName = buf.readString();
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeString(this.playerName);
    }
}
