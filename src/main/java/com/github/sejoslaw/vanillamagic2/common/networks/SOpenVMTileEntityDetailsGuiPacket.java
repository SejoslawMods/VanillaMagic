package com.github.sejoslaw.vanillamagic2.common.networks;

import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.GuiUtils;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SOpenVMTileEntityDetailsGuiPacket extends SVMPacket {
    private String simpleName;
    private CompoundNBT nbt;
    private List<ITextComponent> lines;

    public SOpenVMTileEntityDetailsGuiPacket(PlayerEntity player, IVMTileEntity tileEntity) {
        super(player);

        if (tileEntity == null) {
            return;
        }

        this.simpleName = tileEntity.getClass().getSimpleName();
        this.nbt = tileEntity.serializeNBT();

        this.lines = new ArrayList<>();
        tileEntity.addInformation(lines);
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        super.readPacketData(buf);

        this.simpleName = buf.readString();
        this.nbt = buf.readCompoundTag();

        int linesCount = buf.readInt();
        this.lines = new ArrayList<>();

        for (int i = 0; i < linesCount; ++i) {
            this.lines.add(buf.readTextComponent());
        }
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        super.writePacketData(buf);

        buf.writeString(this.simpleName);
        buf.writeCompoundTag(this.nbt);

        buf.writeInt(this.lines.size());
        this.lines.forEach(buf::writeTextComponent);
    }

    public void processPacket(IClientPlayNetHandler handler) {
        process(this);
    }

    public static void encode(SOpenVMTileEntityDetailsGuiPacket packet, PacketBuffer buf) {
        try {
            packet.writePacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SOpenVMTileEntityDetailsGuiPacket decode(PacketBuffer buf) {
        SOpenVMTileEntityDetailsGuiPacket packet = new SOpenVMTileEntityDetailsGuiPacket(null, null);

        try {
            packet.readPacketData(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return packet;
    }

    public static void consume(SOpenVMTileEntityDetailsGuiPacket packet, Supplier<NetworkEvent.Context> buf) {
        process(packet);
    }

    private static void process(SOpenVMTileEntityDetailsGuiPacket packet) {
        GuiUtils.displayGui(GuiUtils.VM_TILE_ENTITY_DETAILS_GUI_CLASS, packet.simpleName, packet.nbt, packet.lines);
    }
}
