package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCrystallizedLiquid;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCrystallizedLiquid extends EventCaller<QuestCrystallizedLiquid> {
    private final Map<List<ItemStack>, List<ItemStack>> recipes = new HashMap<>();

    @SubscribeEvent
    public void craft(PlayerInteractEvent.RightClickBlock event) {
        if (this.recipes.isEmpty()) {
            for (Map.Entry<ResourceLocation, Fluid> entry : ForgeRegistries.FLUIDS.getEntries()) {
                List<ItemStack> ingredients = new ArrayList<>();
                ingredients.add(new ItemStack(Items.NETHER_STAR));
                ingredients.add(new ItemStack(entry.getValue().getFilledBucket()));

                ItemStack stack = new ItemStack(Items.NETHER_STAR);
                stack.getOrCreateTag().putString(NbtUtils.NBT_CUSTOM_ITEM_UNIQUE_NAME, entry.getKey().toString());
                stack.setDisplayName(TextUtils.combine(TextUtils.translate("item.crystallizedLiquid.bucketNamePrefix"), entry.getKey().getPath()));

                List<ItemStack> results = new ArrayList<>();
                results.add(stack);

                this.recipes.put(ingredients, results);
            }
        }

        this.executor.craftOnAltar(event, this.recipes);
    }

    @SubscribeEvent
    public void spawnLiquid(PlayerInteractEvent.RightClickBlock event) {
        this.executor.onPlayerInteract(event, (player, world, pos, direction) -> {
            for (Map.Entry<List<ItemStack>, List<ItemStack>> entry : this.recipes.entrySet()) {
                String fluidKey = entry.getValue().get(0).getOrCreateTag().getString(NbtUtils.NBT_CUSTOM_ITEM_UNIQUE_NAME);

                this.executor.useCustomItem(player, fluidKey, handStack -> {
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
