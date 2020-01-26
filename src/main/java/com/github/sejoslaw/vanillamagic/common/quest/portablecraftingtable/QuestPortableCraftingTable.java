package com.github.sejoslaw.vanillamagic.common.quest.portablecraftingtable;

import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import net.minecraft.entity.player.PlayerEntity;
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
public class QuestPortableCraftingTable extends Quest {
    /**
     * On right-click open Portable Crafting Table interface.
     */
    @SubscribeEvent
    public void openCrafting(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();

        if (!EntityUtil.hasPlayerCraftingTableInMainHand(player)) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        player.openContainer(new SimpleNamedContainerProvider((p_220270_2_, playerInventory, playerEntity) ->
                new WorkbenchContainer(p_220270_2_, playerInventory, IWorldPosCallable.of(player.world, player.getPosition())),
                new TranslationTextComponent("container.crafting")));
        player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
    }
}