package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCraftOnAltar;
import com.github.sejoslaw.vanillamagic2.common.utils.AltarUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCraftOnAltar extends EventCaller<QuestCraftOnAltar> {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        final List<ItemEntity>[] ingredientsInCauldron = new List[1];

        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) -> {
                    if (!(world.getBlockState(pos).getBlock() instanceof CauldronBlock)) {
                        return null;
                    }

                    List<QuestCraftOnAltar> questsWithValidAltar = this.quests
                            .stream()
                            .filter(quest -> AltarUtils.checkAltarTier(world, pos, quest.altarTier))
                            .collect(Collectors.toList());

                    if (questsWithValidAltar.size() <= 0) {
                        return null;
                    }

                    ingredientsInCauldron[0] = WorldUtils.getItems(world, pos);

                    for (QuestCraftOnAltar quest : questsWithValidAltar) {
                        List<ItemEntity> validItemEntities = new ArrayList<>();

                        for (ItemStack currentlyCheckedIngredient : quest.ingredients) {
                            for (ItemEntity currentlyCheckedItemEntity : ingredientsInCauldron[0]) {
                                if (ItemStack.areItemStacksEqual(currentlyCheckedIngredient, currentlyCheckedItemEntity.getItem())) {
                                    validItemEntities.add(currentlyCheckedItemEntity);
                                    break;
                                }
                            }
                        }

                        if (quest.ingredients.size() != validItemEntities.size()) {
                            continue;
                        }

                        return quest;
                    }

                    return null;
                },
                (player, world, pos, direction, quest) -> {
                    ingredientsInCauldron[0].forEach(Entity::remove);
                    BlockPos newItemPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
                    quest.results.forEach(stack -> Block.spawnAsEntity(world, newItemPos, stack.copy()));
                });
    }
}
