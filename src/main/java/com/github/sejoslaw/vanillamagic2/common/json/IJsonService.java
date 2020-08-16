package com.github.sejoslaw.vanillamagic2.common.json;

import java.util.List;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IJsonService {
    /**
     * @return Converts current service into JsonItemStack.
     */
    JsonItemStack toItemStack();

    /**
     * @return String from given key.
     */
    String getString(String key);

    /**
     * @return Byte from given key.
     */
    byte getByte(String key);

    /**
     * @return Int from given key.
     */
    int getInt(String key);

    /**
     * @return Wrapped ItemStack from given key.
     */
    JsonItemStack getItemStack(String key);

    /**
     * @return List of wrapped objects from given key.
     */
    List<IJsonService> getList(String key);

    /**
     * @return Entries (fields from Json object) from current object.
     */
    Set<String> getEntries();
}
