package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestMotherNatureCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerMotherNatureCrystal extends EventCallerVMItem<QuestMotherNatureCrystal> {
    private final ItemStack boneMealStack = new ItemStack(Items.BONE_MEAL);

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        this.executor.onPlayerTickNoHandsCheck(event,
                (player, world) -> player.inventory.mainInventory.stream().anyMatch(ItemRegistry.MOTHER_NATURE_CRYSTAL::isVMItem) ? this.quests.get(0) : null,
                (player, world, quest) ->
                    this.executor.useVMItem(player, this.getVMItem().getUniqueKey(), (handStack) -> {
                        int range = VMForgeConfig.MOTHER_NATURE_CRYSTAL_RANGE.get();
                        int verticalRange = 3;

                        int posX = (int) Math.round(player.getPosX() - 0.5f);
                        int posY = (int) player.getPosY();
                        int posZ = (int) Math.round(player.getPosZ() - 0.5f);

                        for (int ix = posX - range; ix <= posX + range; ++ix) {
                            for (int iz = posZ - range; iz <= posZ + range; ++iz) {
                                for (int iy = posY - verticalRange; iy <= posY + verticalRange; ++iy) {
                                    this.boneMealStack.setCount(64);
                                    BoneMealItem.applyBonemeal(this.boneMealStack, world, new BlockPos(ix, iy, iz), player);
                                }
                            }
                        }
                    }));
    }
}
