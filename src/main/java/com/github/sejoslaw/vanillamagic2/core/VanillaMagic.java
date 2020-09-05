package com.github.sejoslaw.vanillamagic2.core;

import net.minecraftforge.fml.common.Mod;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@Mod(VanillaMagic.MODID)
@Mod.EventBusSubscriber(modid = VanillaMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class VanillaMagic {
    public static final String MODID = "vanillamagic2";

    public VanillaMagic() {
        VMFiles.initialize();
        VMRegistries.initialize();
        VMQuests.initialize();
        VMEvents.initialize();
    }
}
