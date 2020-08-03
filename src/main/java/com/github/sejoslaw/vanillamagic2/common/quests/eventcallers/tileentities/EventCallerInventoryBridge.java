package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.tileentities;

import com.github.sejoslaw.vanillamagic2.common.items.VMItemInventorySelector;
import com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.items.EventCallerVMItem;
import com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities.QuestInventoryBridge;
import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileInventoryBridge;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerInventoryBridge extends EventCallerVMItem<QuestInventoryBridge> {
    @SubscribeEvent
    public void select(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event, (player, world, pos, direction) ->
                this.executor.useVMItem(player, this.getVMItem().getUniqueKey(), (handStack) -> {
                    CompoundNBT nbt = handStack.getOrCreateTag();

                    if (nbt.getLong(NbtUtils.NBT_POSITION) == 0) {
                        VMItemInventorySelector.setPosition(handStack, pos.toLong(), world.getDimension().getType().getId());
                        TextUtils.addChatMessage("tile.inventorySelector.added");
                    } else {
                        WorldUtils.spawnVMTile(world, pos.offset(Direction.UP), new VMTileInventoryBridge(), (tile) -> tile.setSource(nbt));
                        TextUtils.addChatMessage("tile.inventorySelector.placed");
                    }
                }));
    }

    @SubscribeEvent
    public void clear(PlayerInteractEvent.RightClickItem event) {
        this.executor.onPlayerInteract(event, (player, world, blockPos, direction) ->
                this.executor.useVMItem(player, this.getVMItem().getUniqueKey(), (handStack) -> {
                    VMItemInventorySelector.clearPosition(handStack);
                    TextUtils.addChatMessage("tile.inventorySelector.clear");
                }));
    }
}
