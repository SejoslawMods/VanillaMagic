package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCrystallizedLiquid;
import com.github.sejoslaw.vanillamagic2.common.recipes.AltarRecipe;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCrystallizedLiquid extends EventCallerCraftable<QuestCrystallizedLiquid> {
    public void fillRecipes() {
        this.fillCrystalRecipesFromRegistry(
                ForgeRegistries.FLUIDS.getEntries(),
                (fluid) -> new ItemStack(fluid.getFilledBucket()),
                (fluid) -> new ItemStack(Items.NETHER_STAR),
                "item.crystallizedLiquid.namePrefix");
    }

    @SubscribeEvent
    public void spawnLiquid(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event, (player, world, pos, direction) -> {
            for (AltarRecipe altarRecipe : this.recipes) {
                String fluidKey = altarRecipe.results.get(0).getOrCreateTag().getString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME);

                this.executor.useVMItem(player, fluidKey, handStack -> {
                    TileEntity tile = world.getTileEntity(pos);
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidKey));

                    if (tile == null || fluid == null) {
                        return;
                    }

                    if (tile instanceof IFluidHandler) {
                        ((IFluidHandler) tile).fill(this.getFluidStackToFill(fluid), IFluidHandler.FluidAction.EXECUTE);
                    } else if (tile instanceof IFluidTank) {
                        ((IFluidTank) tile).fill(this.getFluidStackToFill(fluid), IFluidHandler.FluidAction.EXECUTE);
                    } else if (fluid == Fluids.WATER && world.getBlockState(pos).getBlock() == Blocks.CAULDRON) {
                        world.setBlockState(pos, Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, 3));
                    } else {
                        IFluidHandler fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(null);

                        if (fluidHandler != null) {
                            fluidHandler.fill(this.getFluidStackToFill(fluid), IFluidHandler.FluidAction.EXECUTE);
                        } else {
                            world.setBlockState(pos.offset(direction), fluid.getStateContainer().getBaseState().getBlockState());
                        }
                    }
                });
            }
        });
    }

    private FluidStack getFluidStackToFill(Fluid fluid) {
        return new FluidStack(fluid, 1000);
    }
}
