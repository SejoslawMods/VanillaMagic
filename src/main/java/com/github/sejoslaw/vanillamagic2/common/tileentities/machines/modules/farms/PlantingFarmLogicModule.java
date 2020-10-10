package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.UUID;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PlantingFarmLogicModule extends AbstractFarmLogicModule {
    protected void work(IVMTileMachine machine) {
        this.executeLogic(machine,
            (world, stack, pos) -> world.isAirBlock(pos),
            (world, stack, pos) -> {
                Block block = BlockUtils.getBlockFromItem(stack.getItem());

                if (!this.isSupportedBlock(stack)) {
                    return;
                }

                GameProfile fakeProfile = new GameProfile(UUID.randomUUID(), "VM Farmer");
                FakePlayer fakePlayer = FakePlayerFactory.get((ServerWorld) world, fakeProfile);
                BlockRayTraceResult result = BlockRayTraceResult.createMiss(Vector3d.fromPitchYaw(0, 90), Direction.DOWN, pos);
                BlockItemUseContext fakeContext = new BlockItemUseContext(fakePlayer, Hand.MAIN_HAND, stack, result);
                BlockItemUseContext context = BlockItemUseContext.func_221536_a(fakeContext, pos, Direction.DOWN);
                BlockState plantState = block.getStateForPlacement(context);

                if (plantState == null || !block.isValidPosition(plantState, world, pos)) {
                    return;
                }

                world.setBlockState(pos, plantState, 1 | 2);
                stack.shrink(1);
            });
    }
}
