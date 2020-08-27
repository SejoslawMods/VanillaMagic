package com.github.sejoslaw.vanillamagic2.common.handlers;

import com.github.sejoslaw.vanillamagic2.common.functions.Action;
import com.github.sejoslaw.vanillamagic2.common.functions.Consumer2;
import com.github.sejoslaw.vanillamagic2.common.functions.Consumer3;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventHandler {
    protected final String squareSymbol = "\u25A0";

    public void onLivingDeath(LivingDeathEvent event,
                              Consumer2<LivingEntity, DamageSource> consumer) {
        LivingEntity entity = event.getEntityLiving();
        DamageSource source = event.getSource();

        consumer.accept(entity, source);
    }

    public void onShowTooltip(ItemTooltipEvent event,
                              boolean show,
                              Consumer3<PlayerEntity, ItemStack, List<ITextComponent>> consumer) {
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        List<ITextComponent> tooltips = event.getToolTip();

        if (!show) {
            return;
        }

        consumer.accept(player, stack, tooltips);
    }

    public void withFood(ItemStack stack, Consumer<Food> consumer) {
        if (stack.getItem().isFood()) {
            consumer.accept(stack.getItem().getFood());
        }
    }

    public void forDamageable(ItemStack stack, Action action) {
        if (stack.isDamageable()) {
            action.execute();
        }
    }
}
