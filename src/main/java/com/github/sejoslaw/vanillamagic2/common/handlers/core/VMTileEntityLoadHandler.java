package com.github.sejoslaw.vanillamagic2.common.handlers.core;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.FileInputStream;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileEntityLoadHandler extends VMTileEntityHandler {
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.execute(event, (world, file) -> {
            try {
                if (!file.exists()) {
                    return;
                }

                FileInputStream fis = new FileInputStream(file);
                CompoundNBT nbt = CompressedStreamTools.readCompressed(fis);
                fis.close();

                ListNBT tilesNbt = (ListNBT) nbt.get(this.key);
                tilesNbt.stream()
                        .map(tileNbt -> (CompoundNBT) tileNbt)
                        .forEach(tileNbt -> {
                            try {
                                WorldUtils.spawnVMTile(world, tileNbt);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
