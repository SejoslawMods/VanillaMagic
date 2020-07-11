package com.github.sejoslaw.vanillamagic2.common.quests;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class Quest {
    public final Quest parent;
    public final Vec3d position;
    public final ItemStack iconStack;
    public final String uniqueName;

    public Quest(Quest parent, Vec3d position, ItemStack iconStack, String uniqueName) {
        this.parent = parent;
        this.position = position;
        this.iconStack = iconStack;
        this.uniqueName = uniqueName;
    }
}
