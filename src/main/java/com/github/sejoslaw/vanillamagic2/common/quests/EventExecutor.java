package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.common.functions.*;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.VMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public TQuest click(Block block, World world, BlockPos pos, Supplier<TQuest> action) {
        return world.getBlockState(pos).getBlock() == block ? action.get() : null;
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

    public void onAttackEntity(AttackEntityEvent event,
                               Function3<PlayerEntity, World, Entity, TQuest> check,
                               Consumer4<PlayerEntity, World, Entity, TQuest> consumer) {
        PlayerEntity player = event.getPlayer();
        World world = player.getEntityWorld();
        Entity target = event.getTarget();

        performCheck(player,
                () -> check.apply(player, world, target),
                (quest) -> consumer.accept(player, world, target, quest));
    }

    public void onBlockEvent(BlockEvent event, PlayerEntity player,
                             Function3<World, BlockPos, BlockState, TQuest> check,
                             Consumer4<World, BlockPos, BlockState, TQuest> consumer) {
        World world = event.getWorld().getWorld();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        performCheck(player,
                () -> check.apply(world, pos, state),
                (quest) -> consumer.accept(world, pos, state, quest));
    }

    public void onHarvestDrops(BlockEvent.HarvestDropsEvent event,
                               Function5<PlayerEntity, World, BlockPos, BlockState, List<ItemStack>, TQuest> check,
                               Consumer6<PlayerEntity, World, BlockPos, BlockState, List<ItemStack>, TQuest> consumer) {
        PlayerEntity player = event.getHarvester();
        List<ItemStack> drops = event.getDrops();

        onBlockEvent(event, player,
                (world, pos, state) -> check.apply(player, world, pos, state, drops),
                (world, pos, state, quest) -> consumer.accept(player, world, pos, state, drops, quest));
    }

    public void onBlockBreak(BlockEvent.BreakEvent event,
                             Function4<PlayerEntity, World, BlockPos, BlockState, TQuest> check,
                             Consumer4<PlayerEntity, World, BlockPos, BlockState> consumer) {
        PlayerEntity player = event.getPlayer();

        onBlockEvent(event, player,
                (world, pos, state) -> check.apply(player, world, pos, state),
                (world, pos, state, quest) -> consumer.accept(player, world, pos, state));
    }

    public void onPlayerInteract(PlayerInteractEvent event,
                                 Consumer4<PlayerEntity, World, BlockPos, Direction> consumer) {
        PlayerEntity player = event.getPlayer();

        performCheck(player, (quest) -> consumer.accept(player, player.world, event.getPos(), event.getFace()));
    }

    public void onPlayerInteract(PlayerInteractEvent event,
                                 Function4<PlayerEntity, World, BlockPos, Direction, TQuest> check,
                                 Consumer5<PlayerEntity, World, BlockPos, Direction, TQuest> consumer) {
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Direction dir = event.getFace();

        performCheck(player,
                () -> check.apply(player, world, pos, dir),
                (quest) -> consumer.accept(player, world, pos, dir, quest));
    }

    public void onEntityPlace(BlockEvent.EntityPlaceEvent event,
                              Function4<PlayerEntity, World, BlockState, BlockPos, TQuest> check,
                              Consumer4<PlayerEntity, World, BlockState, BlockPos> consumer) {
        Entity entity = event.getEntity();
        PlayerEntity player = entity instanceof PlayerEntity ? (PlayerEntity) entity : null;

        onBlockEvent(event, player,
                (world, pos, state) -> check.apply(player, world, state, pos),
                (world, pos, state, quest) -> consumer.accept(player, world, state, pos));
    }

    public void onEntityInteract(PlayerInteractEvent.EntityInteract event,
                                 Function4<PlayerEntity, Entity, World, BlockPos, TQuest> check,
                                 Consumer4<PlayerEntity, Entity, World, BlockPos> consumer) {
        Entity target = event.getTarget();

        onPlayerInteract(event,
                (player, world, pos, direction) -> check.apply(player, target, world, pos),
                (player, world, pos, direction, quest) -> consumer.accept(player, target, world, pos));
    }

    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event,
                              Function3<PlayerEntity, ItemStack, IInventory, TQuest> check,
                              Consumer3<PlayerEntity, ItemStack, IInventory> consumer) {
        PlayerEntity player = event.getPlayer();
        ItemStack result = event.getCrafting();
        IInventory inv = event.getInventory();

        performCheck(player,
                () -> check.apply(player, result, inv),
                (quest) -> consumer.accept(player, result, inv));
    }

    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, Consumer<PlayerEntity> consumer) {
        LivingEntity entity = event.getEntityLiving();

        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) entity;

        performCheck(player, (quest) -> consumer.accept(player));
    }

    public void onItemPickup(PlayerEvent.ItemPickupEvent event,
                             Function4<PlayerEntity, World, ItemEntity, ItemStack, TQuest> check,
                             Consumer5<PlayerEntity, World, ItemEntity, ItemStack, TQuest> consumer) {
        PlayerEntity player = event.getPlayer();
        ItemEntity originalEntity = event.getOriginalEntity();
        ItemStack pickedStack = event.getStack();
        World world = player.world;

        performCheck(player,
                () -> check.apply(player, world, originalEntity, pickedStack),
                (quest) -> consumer.accept(player, world, originalEntity, pickedStack, quest));
    }

    public void onPlayerTick(TickEvent.PlayerTickEvent event, Consumer3<PlayerEntity, World, TQuest> consumer) {
        PlayerEntity player = event.player;
        World world = player.world;

        performCheck(player, quest -> consumer.accept(player, world, quest));
    }

    public void craftOnAltar(PlayerInteractEvent event, Map<List<ItemStack>, List<ItemStack>> recipes) {
        final List<ItemEntity>[] ingredientsInCauldron = new List[1];
        final int[] index = {0};
        final List<Map.Entry<List<ItemStack>, List<ItemStack>>>[] entries = new List[1];

        this.onPlayerInteract(event,
                (player, world, pos, direction) ->
                        this.click(Blocks.CAULDRON, world, pos, () -> {
                            ingredientsInCauldron[0] = WorldUtils.getItems(world, pos);

                            if (ingredientsInCauldron[0].size() == 0) {
                                return null;
                            }

                            entries[0] = new ArrayList<>(recipes.entrySet());

                            for (int i = 0; i < entries[0].size(); ++i) {
                                Map.Entry<List<ItemStack>, List<ItemStack>> entry = entries[0].get(i);

                                if (!AltarUtils.canCraftOnAltar(entry.getKey(), ingredientsInCauldron[0])) {
                                    continue;
                                }

                                index[0] = i;
                                return this.caller.quests.get(i);
                            }

                            return null;
                        }),
                (player, world, pos, direction, quest) -> {
                    ingredientsInCauldron[0].forEach(Entity::remove);
                    BlockPos newItemPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                    entries[0].get(index[0]).getValue().forEach(stack -> Block.spawnAsEntity(world, newItemPos, stack.copy()));
                });
    }

    public void useVMItem(PlayerEntity player, String vmItemUniqueKey, Consumer<ItemStack> consumer) {
        this.withHands(player, (leftHandStack, rightHandStack) -> {
            CompoundNBT nbt = rightHandStack.getOrCreateTag();
            String key = NbtUtils.NBT_VM_ITEM_UNIQUE_NAME;

            if (nbt.contains(key) && nbt.getString(key).equals(vmItemUniqueKey)) {
                consumer.accept(rightHandStack);
                return;
            }

            nbt = leftHandStack.getOrCreateTag();

            if (nbt.contains(key) && nbt.getString(key).equals(vmItemUniqueKey)) {
                consumer.accept(leftHandStack);
            }
        });
    }

    public void addVMTileMachine(PlayerInteractEvent.RightClickBlock event, String moduleKey) {
        this.onPlayerInteract(event,
                (player, world, pos, direction) -> this.click(Blocks.CAULDRON, world, pos, () -> this.caller.quests.get(0)),
                (player, world, pos, direction, quest) -> WorldUtils.spawnVMTile(world, pos, new VMTileMachine(),
                        (tile) -> tile.setModuleKey(moduleKey)));
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
            if (quest.leftHandStack != null && !ItemStackUtils.areEqual(player.getHeldItemOffhand(), quest.leftHandStack)) {
                continue;
            }

            if (quest.rightHandStack != null && !ItemStackUtils.areEqual(player.getHeldItemMainhand(), quest.rightHandStack)) {
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
                this.onQuestCompleted(quest);
            } else {
                return;
            }
        }

        consumer.accept(quest);
    }

    private void onQuestCompleted(TQuest quest) {
        String questAcquiredMessage =
                TextFormatting.GREEN + TextUtils.getFormattedText("vm.message.questCompleted") +
                TextFormatting.WHITE + " " + quest.getDisplayName();

        TextUtils.addChatMessage(questAcquiredMessage);
    }
}
