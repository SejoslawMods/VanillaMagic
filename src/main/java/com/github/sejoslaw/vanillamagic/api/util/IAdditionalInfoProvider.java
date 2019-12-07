package com.github.sejoslaw.vanillamagic.api.util;

import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Interface used for storing additional information.
 */
public interface IAdditionalInfoProvider {
    /**
     * @return Returns the list with additional informations.
     */
    List<ITextComponent> getAdditionalInfo();
}