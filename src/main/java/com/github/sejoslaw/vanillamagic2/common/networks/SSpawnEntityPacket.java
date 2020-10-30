package com.github.sejoslaw.vanillamagic2.common.networks;

import com.github.sejoslaw.vanillamagic2.common.entities.EntityMeteor;
import com.github.sejoslaw.vanillamagic2.common.registries.EntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.ClientUtils;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.world.IWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SSpawnEntityPacket extends SSpawnObjectPacket {
    public SSpawnEntityPacket() {
        super();
    }

    public SSpawnEntityPacket(Entity entity) {
        super(entity);
    }

    public void processPacket(IClientPlayNetHandler handler) {
        process(this);
    }

    public static void encode(SSpawnEntityPacket packet, PacketBuffer buf) {
        try {
            packet.writePacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SSpawnEntityPacket decode(PacketBuffer buf) {
        SSpawnEntityPacket packet = new SSpawnEntityPacket();

        try {
            packet.readPacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public static void consume(SSpawnEntityPacket packet, Supplier<NetworkEvent.Context> buf) {
        process(packet);
    }

    private static void process(SSpawnEntityPacket packet) {
        IWorld world = ClientUtils.getClientWorld();
        PlayerEntity player = ClientUtils.getClientPlayer();
        double posX = packet.getX();
        double posY = packet.getY();
        double posZ = packet.getZ();
        EntityType<?> type = packet.getType();
        Entity entity = null;

        if (type == EntityRegistry.METEOR.get()) {
            entity = EntityMeteor.create(world, posX, posZ, null);
        }

        if (entity != null) {
            int entityId = packet.getEntityID();
            entity.rotationPitch = (float)(packet.getPitch() * 360) / 256.0F;
            entity.rotationYaw = (float)(packet.getYaw() * 360) / 256.0F;
            entity.setEntityId(entityId);
            entity.setUniqueId(packet.getUniqueId());
            ClientUtils.addEntity(entityId, entity);
        }
    }
}
