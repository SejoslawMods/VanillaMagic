package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.api.event.EventPlayerUseCauldron;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.AltarUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.SmeltingUtil;
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
public class QuestSmeltOnAltar extends Quest {
    // It takes 200 ticks to smelt 1 item in Furnace.
    public static final int ONE_ITEM_SMELT_TICKS = 200;

    protected int requiredAltarTier;
    protected IWand requiredMinimalWand;

    public void readData(JsonObject jo) {
        super.readData(jo);

        this.requiredAltarTier = jo.get("requiredAltarTier").getAsInt();
        this.requiredMinimalWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
    }

    @SubscribeEvent
    public void smeltOnAltar(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        BlockPos cauldronPos = event.getPos();
        World world = player.world;
        ItemStack fuelOffHand = player.getHeldItemOffhand();

        if (!WandRegistry.isWandInMainHandRight(player, requiredMinimalWand.getWandID()) ||
				ItemStackUtil.isNullStack(fuelOffHand) ||
				!SmeltingUtil.isItemFuel(fuelOffHand) ||
				!(world.getBlockState(cauldronPos).getBlock() instanceof CauldronBlock) ||
				!AltarUtil.checkAltarTier(world, cauldronPos, requiredAltarTier)) {
            return;
        }

        List<ItemEntity> itemsToSmelt = SmeltingUtil.getSmeltable(world, cauldronPos);

        if (itemsToSmelt.size() <= 0) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player) || EventUtil.postEvent(new EventPlayerUseCauldron.SmeltOnAltar(player, world, cauldronPos, itemsToSmelt))) {
            return;
        }

        SmeltingUtil.countAndSmelt_OneByOneItemFromOffHand(player, itemsToSmelt, cauldronPos.offset(Direction.UP), this, true);
    }
}