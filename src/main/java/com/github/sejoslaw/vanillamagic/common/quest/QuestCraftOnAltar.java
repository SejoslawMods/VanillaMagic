package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.api.event.EventPlayerUseCauldron;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.AltarUtil;
import com.github.sejoslaw.vanillamagic.common.util.CauldronUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCraftOnAltar extends Quest {
    /*
     * Each ItemStack is a different Item Instead of doing 1x Coal + 1x Coal, do 2x Coal -> 2 will be stackSize
     */
    protected ItemStack[] ingredients;
    protected ItemStack[] result;
    protected int requiredAltarTier;
    protected IWand requiredMinimalWand;

    public void readData(JsonObject jo) {
        this.ingredients = ItemStackUtil.getItemStackArrayFromJSON(jo, "ingredients");
        this.result = ItemStackUtil.getItemStackArrayFromJSON(jo, "result");
        this.icon = result[0].copy();
        this.requiredAltarTier = jo.get("requiredAltarTier").getAsInt();
        this.requiredMinimalWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());

        super.readData(jo);
    }

    public ItemStack[] getResult() {
        return result;
    }

    public int getIngredientsStackSize() {
        return Arrays.stream(ingredients).map(ItemStack::getCount).reduce(0, Integer::sum);
    }

    public int getIngredientsInCauldronStackSize(List<ItemEntity> entitiesInCauldron) {
        return entitiesInCauldron.stream().map(entity -> entity.getItem().getCount()).reduce(0, Integer::sum);
    }

    @SubscribeEvent
    public void craftOnAltar(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        BlockPos cauldronPos = event.getPos();

        if (!WandRegistry.isWandInMainHandRight(player, requiredMinimalWand.getWandID())) {
            return;
        }

        World world = player.world;

        if (!(world.getBlockState(cauldronPos).getBlock() instanceof CauldronBlock) || !AltarUtil.checkAltarTier(world, cauldronPos, requiredAltarTier)) {
            return;
        }

        List<ItemEntity> entitiesInCauldron = CauldronUtil.getItemsInCauldron(world, cauldronPos);
        int ingredientsStackSize = getIngredientsStackSize();
        int ingredientsInCauldronStackSize = getIngredientsInCauldronStackSize(entitiesInCauldron);

        if (ingredientsStackSize != ingredientsInCauldronStackSize) {
            return;
        }

        List<ItemEntity> alreadyCheckedItemEntities = new ArrayList<>();

        for (ItemStack currentlyCheckedIngredient : ingredients) {
            for (ItemEntity currentlyCheckedItemEntity : entitiesInCauldron) {
                if (ItemStack.areItemStacksEqual(currentlyCheckedIngredient, currentlyCheckedItemEntity.getItem())) {
                    alreadyCheckedItemEntities.add(currentlyCheckedItemEntity);
                    break;
                }
            }
        }

        if (ingredients.length != alreadyCheckedItemEntities.size()) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player) || EventUtil.postEvent(new EventPlayerUseCauldron.CraftOnAltar(player, world, cauldronPos, alreadyCheckedItemEntities, result))) {
            return;
        }

        for (ItemEntity alreadyCheckedItemEntity : alreadyCheckedItemEntities) {
            alreadyCheckedItemEntity.remove();
        }

        BlockPos newItemPos = new BlockPos(cauldronPos.getX(), cauldronPos.getY() + 1, cauldronPos.getZ());

        for (ItemStack itemStack : result) {
            Block.spawnAsEntity(world, newItemPos, itemStack.copy());
        }
    }
}