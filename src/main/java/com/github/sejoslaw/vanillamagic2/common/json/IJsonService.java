package com.github.sejoslaw.vanillamagic2.common.json;

import java.util.List;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IJsonService {
    /**
     * @param key
     * @return String from given key.
     */
    String getString(String key);

    /**
     * @param key
     * @return Int from given key.
     */
    int getInt(String key);

    /**
     * @param key
     * @return Wrapped ItemStack from given key.
     */
    JsonItemStack getItemStack(String key);

    /**
     * @param key
     * @return List of wrapped objects from given key.
     */
    List<IJsonService> getList(String key);

    /**
     * @return Entries (fields from Json object) from current object.
     */
    Set<String> getEntries();
}
