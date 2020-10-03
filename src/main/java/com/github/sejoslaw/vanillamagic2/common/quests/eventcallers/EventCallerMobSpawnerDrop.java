package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestMobSpawnerDrop;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerMobSpawnerDrop extends EventCaller<QuestMobSpawnerDrop> {
    @SubscribeEvent
    public void onMobSpawnerBreak(BlockEvent.BreakEvent event) {
        this.executor.onBlockBreak(event,
                (playerEntity, world, pos, state) -> state.getBlock() instanceof SpawnerBlock ? this.quests.get(0) : null,
                (playerEntity, world, pos, state) -> {
                    Block.spawnAsEntity(WorldUtils.asWorld(world), pos, new ItemStack(state.getBlock()));

                    AbstractSpawner spawner = ((MobSpawnerTileEntity) world.getTileEntity(pos)).getSpawnerBaseLogic();
                    CompoundNBT spawnerNbt = spawner.write(new CompoundNBT());
                    String entityId = spawnerNbt.getList("SpawnPotentials", StringNBT.valueOf("").getId()).getCompound(0).getString("id");
                    ResourceLocation entityName = new ResourceLocation(entityId);
                    EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(entityName);

                    ItemStack bookStack = new ItemStack(Items.ENCHANTED_BOOK);
                    bookStack.setDisplayName(TextUtils.combine(TextUtils.translate("quest.mobSpawner.bookTitlePrefix"), entityType.getName().getString()));
                    bookStack.getOrCreateTag().putString(NbtUtils.NBT_SPAWNER_ENTITY, entityName.toString());

                    Block.spawnAsEntity(WorldUtils.asWorld(world), pos, bookStack);
                    WorldUtils.asWorld(world).removeTileEntity(pos);
                });
    }

    @SubscribeEvent
    public void onRightClickWithBook(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event, (player, world, pos, direction) ->
                this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                    CompoundNBT stackNbt = rightHandStack.getOrCreateTag();
                    TileEntity tile = world.getTileEntity(pos);

                    if (!stackNbt.contains(NbtUtils.NBT_SPAWNER_ENTITY) || !(tile instanceof MobSpawnerTileEntity)) {
                        return;
                    }

                    String entityId = stackNbt.getString(NbtUtils.NBT_SPAWNER_ENTITY);
                    ResourceLocation entityName = new ResourceLocation(entityId);
                    EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(entityName);
                    ((MobSpawnerTileEntity) tile).getSpawnerBaseLogic().setEntityType(entityType);

                    Block.spawnAsEntity(WorldUtils.asWorld(world), player.getPosition(), new ItemStack(Items.BOOK));
                }));
    }
}
