package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.api.event.EventPlayerUseCauldron;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.*;
import com.google.gson.JsonObject;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestOreMultiplier extends Quest {
    protected int multiplier;
    protected IWand requiredMinimalWand;

    public void readData(JsonObject jo) {
        super.readData(jo);

        this.multiplier = jo.get("multiplier").getAsInt();
        this.requiredMinimalWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
    }

    @SubscribeEvent
    public void doubleOre(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        BlockPos cauldronPos = event.getPos();
        ItemStack fuelOffHand = player.getHeldItemOffhand();

        if (!WandRegistry.isWandInMainHandRight(player, requiredMinimalWand.getWandID()) || ItemStackUtil.isNullStack(fuelOffHand) || !SmeltingUtil.isItemFuel(fuelOffHand)) {
            return;
        }

        World world = player.world;

        if (!(world.getBlockState(cauldronPos).getBlock() instanceof CauldronBlock) || !OreMultiplierUtil.check(world, cauldronPos)) {
            return;
        }

        List<ItemEntity> oresInCauldron = SmeltingUtil.getOresInCauldron(world, cauldronPos);

        if (oresInCauldron.size() <= 0) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player) || EventUtil.postEvent(new EventPlayerUseCauldron.OreMultiplier(player, world, cauldronPos, oresInCauldron))) {
            return;
        }

        multiply(player, oresInCauldron, cauldronPos);
    }

    public void multiply(PlayerEntity player, List<ItemEntity> oresInCauldron, BlockPos cauldronPos) {
        List<ItemEntity> smeltingResult = SmeltingUtil.countAndSmelt_OneByOneItemFromOffHand(player, oresInCauldron, cauldronPos.offset(Direction.UP), this, false);

        World world = player.world;

        for (int i = 0; i < multiplier; ++i) {
            for (ItemEntity itemEntity : smeltingResult) {
                world.addEntity(EntityUtil.copyItem(itemEntity));
            }
        }
    }
}