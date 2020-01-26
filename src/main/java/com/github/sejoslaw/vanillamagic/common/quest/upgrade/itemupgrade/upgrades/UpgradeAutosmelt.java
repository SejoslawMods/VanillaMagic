package com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade.upgrades;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.itemupgrade.ItemUpgradeBase;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.SmeltingUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * Class which is a definition of Autosmelt ItemUpgrade.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class UpgradeAutosmelt extends ItemUpgradeBase {
    public ItemStack getIngredient() {
        return new ItemStack(Items.MAGMA_CREAM);
    }

    public String getUniqueNBTTag() {
        return "NBT_UPGRADE_AUTOSMELT";
    }

    public String getUpgradeName() {
        return "Autosmelt Upgrade";
    }

    public String getTextColor() {
        return TextUtil.COLOR_RED;
    }

    /**
     * Smelt digged blocks.
     */
    @SubscribeEvent
    public void onDig(BlockEvent.HarvestDropsEvent event) {
        PlayerEntity player = event.getHarvester();

        if ((player == null) || !containsTag(player.getHeldItemMainhand())) {
            return;
        }

        World world = event.getWorld().getWorld();
        BlockPos pos = event.getPos();
        List<ItemStack> drops = event.getDrops();

        for (int i = 0; i < drops.size(); ++i) {
            ItemStack dropStack = drops.get(i);
            ItemStack afterSmelt = SmeltingUtil.getSmeltingResultAsNewStack(dropStack, world);

            if (!ItemStackUtil.isNullStack(afterSmelt)) {
                ItemEntity afterSmeltEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), afterSmelt);
                world.addEntity(afterSmeltEntity);
                drops.remove(i);
            }
        }
    }
}