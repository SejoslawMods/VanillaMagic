package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestSmeltOnAltar;
import com.github.sejoslaw.vanillamagic2.common.utils.AltarUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerSmeltOnAltar extends EventCaller<QuestSmeltOnAltar> {
    @SubscribeEvent
    public void smeltOnAltar(PlayerInteractEvent.RightClickBlock event) {
        final List<ItemEntity>[] smeltables = new List[1];

        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) ->
                        this.executor.click(Blocks.CAULDRON, world, pos, () -> {
                            ItemStack leftHandStack = player.getHeldItemOffhand();
                            QuestSmeltOnAltar quest = this.quests.get(0);

                            if (!ItemStackUtils.isFuel(leftHandStack) || !AltarUtils.checkAltarTier(world, pos, quest.altarTier) || WorldUtils.getIsRemote(world)) {
                                return null;
                            }

                            smeltables[0] = ItemStackUtils.getSmeltables(world, pos);

                            if (smeltables[0].size() <= 0) {
                                return null;
                            }

                            return quest;
                        }),
                (player, world, pos, direction, quest) -> WorldUtils.spawnOnCauldron(
                        world,
                        pos,
                        ItemStackUtils.smeltItems(player, smeltables[0], quest.oneItemSmeltCost),
                        ItemStack::getCount));
    }
}
