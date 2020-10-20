package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.breeders;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.entity.passive.AnimalEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class UsingItemBreederLogicModule extends AbstractBreederLogicModule {
    protected void work(IVMTileMachine machine) {
        this.useSlot(machine, (inv, slotId, stack) ->
            this.getEntities(machine, AnimalEntity.class, entity -> true)
                    .forEach(entity -> {
                        if (entity.isBreedingItem(stack) && entity.canBreed() && !entity.isChild()) {
                            entity.setInLove(null);
                            stack.shrink(1);
                        }
                    }));
    }
}
