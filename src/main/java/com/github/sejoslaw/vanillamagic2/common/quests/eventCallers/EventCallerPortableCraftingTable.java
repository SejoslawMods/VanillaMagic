package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestPortableCraftingTable;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerPortableCraftingTable extends EventCaller<QuestPortableCraftingTable> {
    @SubscribeEvent
    public void openCraftingTableGui(PlayerInteractEvent.RightClickItem event) {
        this.executor.onPlayerInteract(event, (player, pos, face) -> {
            player.openContainer(new SimpleNamedContainerProvider((windowId, playerInventory, playerEntity) ->
                    new WorkbenchContainer(windowId, playerInventory, IWorldPosCallable.of(player.world, player.getPosition())),
                    new TranslationTextComponent("container.crafting")));
            player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        });
    }
}
