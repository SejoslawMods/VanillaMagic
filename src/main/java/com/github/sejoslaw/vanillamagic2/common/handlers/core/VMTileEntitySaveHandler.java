package com.github.sejoslaw.vanillamagic2.common.handlers.core;

import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.FileOutputStream;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileEntitySaveHandler extends VMTileEntityHandler {
    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) {
        this.execute(event, (world, file) -> {
            try {
                ListNBT tilesNbt = new ListNBT();

                WorldUtils.getVMTiles(world, vmTile -> true).forEach(tile -> {
                    CompoundNBT tileNbt = new CompoundNBT();
                    tile.write(tileNbt);
                    tileNbt.putString(NbtUtils.NBT_TILE_TYPE, tile.getTileEntity().getType().getRegistryName().toString());
                    tilesNbt.add(tileNbt);
                });

                CompoundNBT nbt = new CompoundNBT();
                nbt.put(this.key, tilesNbt);

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                CompressedStreamTools.writeCompressed(nbt, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
