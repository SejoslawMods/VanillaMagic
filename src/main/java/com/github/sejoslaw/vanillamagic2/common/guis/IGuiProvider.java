package com.github.sejoslaw.vanillamagic2.common.guis;

import net.minecraft.client.gui.screen.Screen;

/**
 * Allows VM TileEntity to provide custom GUI to be displayed.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IGuiProvider {
    Screen getGui();
}
