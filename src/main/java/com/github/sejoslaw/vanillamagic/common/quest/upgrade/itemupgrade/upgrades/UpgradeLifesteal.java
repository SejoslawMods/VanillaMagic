package com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.upgrades;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Collection;

/**
 * Class which represents Lifesteal upgrade for Swords.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class UpgradeLifesteal extends UpgradeSword {
    public ItemStack getIngredient() {
        return new ItemStack(Items.GOLDEN_APPLE);
    }

    public String getUniqueNBTTag() {
        return "NBT_UPGRADE_LIFESTEAL";
    }

    public String getUpgradeName() {
        return "Lifesteal";
    }

    public String getTextColor() {
        return TextUtil.COLOR_GREEN;
    }

    public void onAttack(PlayerEntity player, Entity target) {
        ItemStack playerMainHandStack = player.getHeldItemMainhand();
        Multimap<String, AttributeModifier> attributes = playerMainHandStack.getItem().getAttributeModifiers(EquipmentSlotType.MAINHAND, playerMainHandStack);

        String attributeName = SharedMonsterAttributes.ATTACK_DAMAGE.getName();
        Collection<AttributeModifier> modifiers = attributes.get(attributeName);

        double amount = modifiers.stream().mapToDouble(am -> am.getAmount() / 2).sum();
        player.heal((float) amount);
    }
}