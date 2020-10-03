package com.github.sejoslaw.vanillamagic2.common.handlers;

import com.github.sejoslaw.vanillamagic2.common.functions.Action;
import com.github.sejoslaw.vanillamagic2.common.functions.Consumer2;
import com.github.sejoslaw.vanillamagic2.common.functions.Consumer3;
import com.github.sejoslaw.vanillamagic2.common.functions.Consumer4;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventHandler {
    protected final String squareSymbol = "\u25A0";

    public void onItemExpire(ItemExpireEvent event,
                             Consumer4<ItemEntity, ItemStack, IWorld, BlockPos> consumer) {
        ItemEntity entity = event.getEntityItem();

        consumer.accept(entity, entity.getItem(), entity.getEntityWorld(), entity.getPosition());
    }

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

    public void onRegisterCommand(RegisterCommandsEvent event, Consumer<CommandDispatcher<CommandSource>> consumer) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        consumer.accept(dispatcher);
    }

    public void registerRenderers(FMLClientSetupEvent event, Consumer<Minecraft> consumer) {
        Minecraft mc = event.getMinecraftSupplier().get();
        consumer.accept(mc);
    }

    public void onPlayerInteract(PlayerInteractEvent event,
                                 Consumer4<PlayerEntity, IWorld, BlockPos, Direction> consumer) {
        PlayerEntity player = event.getPlayer();
        IWorld world = player.getEntityWorld();
        BlockPos pos = event.getPos();
        Direction face = event.getFace();

        consumer.accept(player, world, pos, face);
    }
}
