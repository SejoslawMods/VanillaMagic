package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestMotherNatureCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerMotherNatureCrystal extends EventCallerCustomItem<QuestMotherNatureCrystal> {
    private final ItemStack boneMealStack = new ItemStack(Items.BONE_MEAL);

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        this.executor.onPlayerTick(event, (player, world, quest) ->
                this.useCustomItem(player, (handStack, customItem) -> {
                    int range = VMForgeConfig.MOTHER_NATURE_CRYSTAL_RANGE.get();
                    int verticalRange = 3;

                    int posX = (int) Math.round(player.getPosX() - 0.5f);
                    int posY = (int) player.getPosY();
                    int posZ = (int) Math.round(player.getPosZ() - 0.5f);

                    for (int ix = posX - range; ix <= posX + range; ++ix) {
                        for (int iz = posZ - range; iz <= posZ + range; ++iz) {
                            for (int iy = posY - verticalRange; iy <= posY + verticalRange; ++iy) {
                                BlockPos pos = new BlockPos(ix, iy, iz);
                                BlockState state = world.getBlockState(pos);
                                Block block = state.getBlock();

                                if (!(block instanceof IGrowable)) {
                                    return;
                                }

                                this.boneMealStack.setCount(64);
                                BoneMealItem.applyBonemeal(this.boneMealStack, world, pos, player);
                            }
                        }
                    }
                }));
    }
}
