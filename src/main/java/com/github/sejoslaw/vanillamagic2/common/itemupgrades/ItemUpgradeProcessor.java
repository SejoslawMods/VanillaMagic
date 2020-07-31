package com.github.sejoslaw.vanillamagic2.common.itemupgrades;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeProcessor {
    private final Class<? extends ItemUpgradeEventCaller> itemUpgradeEventCallerClass;
    private final Class<? extends ItemUpgrade> itemUpgradeClass;

    private ItemUpgradeEventCaller itemUpgradeEventCaller;

    public ItemUpgradeProcessor(Class<? extends ItemUpgradeEventCaller> itemUpgradeEventCallerClass, Class<? extends ItemUpgrade> itemUpgradeClass) {
        this.itemUpgradeEventCallerClass = itemUpgradeEventCallerClass;
        this.itemUpgradeClass = itemUpgradeClass;
    }

    public ItemUpgradeProcessor register() {
        try {
            this.itemUpgradeEventCaller = this.itemUpgradeEventCallerClass.newInstance();
            this.itemUpgradeEventCaller.itemUpgrade = this.itemUpgradeClass.newInstance();
            this.itemUpgradeEventCaller.register();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    public ItemUpgradeEventCaller getItemUpgradeEventCaller() {
        return this.itemUpgradeEventCaller;
    }
}
