package com.github.sejoslaw.vanillamagic2.common.handlers;

import com.github.sejoslaw.vanillamagic2.common.functions.*;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
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
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
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

    public void withHands(PlayerEntity player,
                          Consumer2<ItemStack, ItemStack> consumer) {
        consumer.accept(player.getHeldItemOffhand(), player.getHeldItemMainhand());
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

    public void onBlockBreak(BlockEvent.BreakEvent event,
                             Consumer4<PlayerEntity, IWorld, BlockPos, BlockState> consumer) {
        PlayerEntity player = event.getPlayer();
        IWorld world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        consumer.accept(player, world, pos, state);
    }

    public void onBoneMeal(BonemealEvent event,
                           Consumer5<PlayerEntity, IWorld, BlockPos, BlockState, ItemStack> consumer) {
        PlayerEntity player = event.getPlayer();
        IWorld world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = event.getBlock();
        ItemStack stack = event.getStack();

        consumer.accept(player, world, pos, state, stack);
    }

    public void onKeyPressed(InputEvent.KeyInputEvent event,
                             Consumer4<Integer, Integer, Integer, Integer> consumer) {
        consumer.accept(event.getKey(), event.getScanCode(), event.getAction(), event.getModifiers());
    }

    public void onScroll(GuiScreenEvent.MouseScrollEvent.Pre event,
                         Consumer4<Screen, Double, Double, Double> consumer) {
        consumer.accept(event.getGui(), event.getMouseX(), event.getMouseY(), event.getScrollDelta());
    }
}
