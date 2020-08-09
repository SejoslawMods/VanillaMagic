package com.github.sejoslaw.vanillamagic2.common.json;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class JsonItemStack {
    private final IJsonService service;

    public JsonItemStack(IJsonService service) {
        this.service = service;
    }

    public String getId() {
        return this.service.getString("id");
    }

    public byte getCount() {
        return this.service.getByte("count");
    }

    public int getMeta() {
        return this.service.getInt("meta");
    }
}
