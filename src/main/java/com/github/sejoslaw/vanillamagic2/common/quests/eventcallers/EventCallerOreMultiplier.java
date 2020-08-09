package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestOreMultiplier;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerOreMultiplier extends EventCaller<QuestOreMultiplier> {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        final List<ItemEntity>[] oresInCauldron = new List[1];

        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) ->
                        this.executor.click(Blocks.CAULDRON, world, pos, () -> {
                           if (!isStructureValid(world, pos)) {
                               return null;
                           }

                            oresInCauldron[0] = WorldUtils.getOres(world, pos);

                            if (oresInCauldron[0].size() <= 0) {
                                return null;
                            }

                            return this.quests.get(0);
                        }),
                (player, world, pos, direction, quest) -> WorldUtils.spawnOnCauldron(
                        world,
                        pos,
                        ItemStackUtils.smeltItems(player, oresInCauldron[0], quest.oneItemSmeltCost),
                        stack -> stack.getCount() * quest.multiplier));
    }

    private boolean isStructureValid(World world, BlockPos pos) {
        return StreamSupport
                .stream(Direction.Plane.HORIZONTAL.spliterator(), false)
                .allMatch(face -> world.getBlockState(pos.offset(face)).getBlock() instanceof FurnaceBlock);
    }
}
