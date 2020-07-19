package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.common.functions.*;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EventExecutor<TQuest extends Quest> {
    private final EventCaller<TQuest> caller;

    public EventExecutor(EventCaller<TQuest> caller) {
        this.caller = caller;
    }

    public void withHands(PlayerEntity player, Consumer2<ItemStack, ItemStack> consumer) {
        consumer.accept(player.getHeldItemOffhand(), player.getHeldItemMainhand());
    }

    public void forQuestWithCheck(Function<TQuest, Boolean> check, Consumer<TQuest> action) {
        this.caller.quests
                .stream()
                .filter(check::apply)
                .forEach(action);
    }

    public void onBlockBreak(BlockEvent.BreakEvent event, Function4<PlayerEntity, World, BlockPos, BlockState, TQuest> check, Consumer4<PlayerEntity, World, BlockPos, BlockState> consumer) {
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld().getWorld();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        performCheck(player, () -> check.apply(player, world, pos, state), (quest) -> consumer.accept(player, world, pos, state));
    }

    public void onPlayerInteract(PlayerInteractEvent event, Consumer4<PlayerEntity, World, BlockPos, Direction> consumer) {
        PlayerEntity player = event.getPlayer();

        performCheck(player, (quest) -> consumer.accept(player, player.world, event.getPos(), event.getFace()));
    }

    public void onPlayerInteract(PlayerInteractEvent event, Function4<PlayerEntity, World, BlockPos, Direction, TQuest> check, Consumer5<PlayerEntity, World, BlockPos, Direction, TQuest> consumer) {
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Direction dir = event.getFace();

        performCheck(player, () -> check.apply(player, world, pos, dir), (quest) -> consumer.accept(player, world, pos, dir, quest));
    }

    public void onItemTooltip(ItemTooltipEvent event) {
    }

    public void onEntityPlace(BlockEvent.EntityPlaceEvent event, Function4<PlayerEntity, World, BlockState, BlockPos, TQuest> check, Consumer4<PlayerEntity, World, BlockState, BlockPos> consumer) {
        Entity entity = event.getEntity();
        PlayerEntity player = entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
        World world = event.getWorld().getWorld();
        BlockState state = event.getPlacedBlock();
        BlockPos pos = event.getPos();

        performCheck(player, () -> check.apply(player, world, state, pos), (quest) -> consumer.accept(player, world, state, pos));
    }

    public void onEntityInteract(PlayerInteractEvent.EntityInteract event, Function4<PlayerEntity, Entity, World, BlockPos, TQuest> check, Consumer4<PlayerEntity, Entity, World, BlockPos> consumer) {
        PlayerEntity player = event.getPlayer();
        Entity target = event.getTarget();
        World world = event.getWorld();
        BlockPos pos = event.getPos();

        performCheck(player, () -> check.apply(player, target, world, pos), (quest) -> consumer.accept(player, target, world, pos));
    }

    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event, Function3<PlayerEntity, ItemStack, IInventory, TQuest> check, Consumer3<PlayerEntity, ItemStack, IInventory> consumer) {
        PlayerEntity player = event.getPlayer();
        ItemStack result = event.getCrafting();
        IInventory inv = event.getInventory();

        performCheck(player, () -> check.apply(player, result, inv), (quest) -> consumer.accept(player, result, inv));
    }

    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, Consumer<PlayerEntity> consumer) {
        LivingEntity entity = event.getEntityLiving();

        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) entity;

        performCheck(player, (quest) -> consumer.accept(player));
    }

    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
    }

    public void onLivingDrops(LivingDropsEvent event) {
    }

    public void onEntityItemPickup(EntityItemPickupEvent event) {
    }

    public void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    }

    /*
     * -----====== Private Methods =====-----
     */

    private void performCheck(PlayerEntity player, Consumer<TQuest> consumer) {
         checkItemsInHands(player, (quest) -> checkQuestProgress(player, quest, consumer));
    }

    private void performCheck(PlayerEntity player, Supplier<TQuest> check, Consumer<TQuest> consumer) {
        checkItemsInHands(player, (skippedQuest) -> {
            TQuest quest = check.get();

            if (quest == null) {
                return;
            }

            checkQuestProgress(player, quest, consumer);
        });
    }

    private void checkItemsInHands(PlayerEntity player, Consumer<TQuest> consumer) {
        for (TQuest quest : this.caller.quests) {
            if (quest.leftHandStack != null && !ItemStack.areItemStacksEqual(player.getHeldItemOffhand(), quest.leftHandStack)) {
                continue;
            }

            if (quest.rightHandStack != null && !ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), quest.rightHandStack)) {
                continue;
            }

            consumer.accept(quest);
            return;
        }
    }

    private void checkQuestProgress(PlayerEntity player, TQuest quest, Consumer<TQuest> consumer) {
        String questUniqueName = quest.uniqueName;

        if (!PlayerQuestProgressRegistry.hasPlayerGotQuest(player, questUniqueName)) {
            if (PlayerQuestProgressRegistry.canPlayerGetQuest(player, questUniqueName)) {
                PlayerQuestProgressRegistry.givePlayerQuest(player, questUniqueName);
            } else {
                return;
            }
        }

        consumer.accept(quest);
    }
}
