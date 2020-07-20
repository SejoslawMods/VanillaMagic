package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestOreMultiplier;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
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
                (player, world, pos, direction) -> {
                    if (!(world.getBlockState(pos).getBlock() instanceof CauldronBlock) || !isStructureValid(world, pos)) {
                        return null;
                    }

                    oresInCauldron[0] = BlockUtils.getOres(world, pos);

                    if (oresInCauldron[0].size() <= 0) {
                        return null;
                    }

                    return this.quests.get(0);
                },
                (player, world, pos, direction, quest) ->
                    this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                        List<ItemStack> smeltingResult = ItemStackUtils.smeltItems(player, oresInCauldron[0], quest.singleItemSmeltingCost);
                        BlockPos spawnPos = pos.offset(Direction.UP);

                        smeltingResult.forEach(stack -> {
                            stack.setCount(stack.getCount() * quest.multiplier);
                            world.addEntity(new ItemEntity(world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), stack));
                        });
                    }));
    }

    private boolean isStructureValid(World world, BlockPos pos) {
        return StreamSupport
                .stream(Direction.Plane.HORIZONTAL.spliterator(), false)
                .allMatch(face -> world.getBlockState(pos.offset(face)).getBlock() instanceof FurnaceBlock);
    }
}
