package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestPortableCraftingTable;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.stats.Stats;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerPortableCraftingTable extends EventCaller<QuestPortableCraftingTable> {
    public static class PortableWorkbenchContainer extends WorkbenchContainer {
        private final CraftingInventory craftMatrix = new CraftingInventory(this, 3, 3);
        private final CraftResultInventory craftResult = new CraftResultInventory();
        private final PlayerEntity player;

        public PortableWorkbenchContainer(int id, PlayerInventory playerInventory) {
            super(id, playerInventory);
            this.player = playerInventory.player;

            this.inventorySlots.clear();

            this.addSlot(new CraftingResultSlot(this.player, this.craftMatrix, this.craftResult, 0, 124, 35));

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    this.addSlot(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
                }
            }

            for (int k = 0; k < 3; ++k) {
                for (int i1 = 0; i1 < 9; ++i1) {
                    this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
                }
            }

            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
            }
        }

        public void onCraftMatrixChanged(IInventory inv) {
            func_217066_a(this.windowId, this.player.getEntityWorld(), this.player, this.craftMatrix, this.craftResult);
        }

        public void onContainerClosed(PlayerEntity player) {
            super.onContainerClosed(player);
            this.clearContainer(player, player.getEntityWorld(), this.craftMatrix);
        }

        public boolean canInteractWith(PlayerEntity player) {
            return player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.CRAFTING_TABLE);
        }

        public boolean matches(IRecipe<? super CraftingInventory> recipe) {
            return recipe.matches(this.craftMatrix, this.player.world);
        }

        public void fillStackedContents(RecipeItemHelper helper) {
            this.craftMatrix.fillStackedContents(helper);
        }

        public void clear() {
            this.craftMatrix.clear();
            this.craftResult.clear();
        }

        public boolean canMergeSlot(ItemStack stack, Slot slot) {
            return slot.inventory != this.craftResult && super.canMergeSlot(stack, slot);
        }
    }

    @SubscribeEvent
    public void openCraftingTableGui(PlayerInteractEvent.RightClickItem event) {
        this.executor.onPlayerInteract(event, (player, world, pos, face) -> {
            player.openContainer(new SimpleNamedContainerProvider((windowId, playerInventory, playerEntity) ->
                    new PortableWorkbenchContainer(windowId, playerInventory),
                    new TranslationTextComponent("container.crafting")));
            player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        });
    }
}
