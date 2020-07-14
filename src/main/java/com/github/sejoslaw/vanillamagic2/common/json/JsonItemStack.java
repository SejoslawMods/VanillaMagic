package com.github.sejoslaw.vanillamagic2.common.json;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class JsonItemStack {
    private final IJsonService service;

    public JsonItemStack(IJsonService service) {
        this.service = service;
    }

    public int getId() {
        return this.service.getInt("id");
    }

    public int getStackSize() {
        return this.service.getInt("stackSize");
    }
}
