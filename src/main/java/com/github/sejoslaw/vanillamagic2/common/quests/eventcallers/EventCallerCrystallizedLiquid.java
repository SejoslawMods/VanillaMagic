package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCrystallizedLiquid;
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

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCrystallizedLiquid extends EventCallerCraftable<QuestCrystallizedLiquid> {
    public void fillRecipes() {
        Set<Map.Entry<ResourceLocation, Fluid>> entries = ForgeRegistries.FLUIDS
                .getEntries()
                .stream()
                .filter(entry -> !entry.getKey().toString().contains("flowing_"))
                .collect(Collectors.toSet());

        this.fillCrystalRecipesFromRegistry(
                entries,
                (fluid) -> new ItemStack(fluid.getFilledBucket()),
                (fluid) -> new ItemStack(Items.NETHER_STAR),
                "vmitem.crystallizedLiquid.namePrefix",
                (entry) -> entry.getValue().getDefaultState().getBlockState().getBlock().getNameTextComponent().getFormattedText());
    }

    @SubscribeEvent
    public void spawnLiquid(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event, (player, world, pos, direction) -> {
            this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                String fluidKey = rightHandStack.getOrCreateTag().getString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME);

                this.executor.useVMItem(player, fluidKey, handStack -> {
                    TileEntity tile = world.getTileEntity(pos);
                    Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidKey));

                    if (tile instanceof IFluidHandler) {
                        ((IFluidHandler) tile).fill(this.getFluidStackToFill(fluid), IFluidHandler.FluidAction.EXECUTE);
                    } else if (tile instanceof IFluidTank) {
                        ((IFluidTank) tile).fill(this.getFluidStackToFill(fluid), IFluidHandler.FluidAction.EXECUTE);
                    } else if (fluid == Fluids.WATER && world.getBlockState(pos).getBlock() == Blocks.CAULDRON) {
                        world.setBlockState(pos, Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, 3));
                    } else if (tile != null) {
                        IFluidHandler fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(null);

                        if (fluidHandler != null) {
                            fluidHandler.fill(this.getFluidStackToFill(fluid), IFluidHandler.FluidAction.EXECUTE);
                        }
                    } else {
                        world.setBlockState(pos.offset(direction), fluid.getDefaultState().getBlockState());
                    }
                });
            });
        });
    }

    private FluidStack getFluidStackToFill(Fluid fluid) {
        return new FluidStack(fluid, 1000);
    }
}
