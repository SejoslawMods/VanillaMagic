package com.github.sejoslaw.vanillamagic2.common.itemUpgrades;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BaseItemType {
    public static final Set<BaseItemType> TYPES = new HashSet<>();

    public static final BaseItemType PICKAXE = new BaseItemType("pickaxe");
    public static final BaseItemType SWORD = new BaseItemType("sword");

    public final String itemType;

    public BaseItemType(String itemType) {
        this.itemType = itemType;

        TYPES.add(this);
    }
}
