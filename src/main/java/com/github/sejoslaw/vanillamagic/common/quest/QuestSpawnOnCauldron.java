package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.CauldronUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Quest which describes an action in which result something should be crafting
 * on Cauldron.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class QuestSpawnOnCauldron extends Quest {
    int times = 0;

    @SubscribeEvent
    public void craftUpgrade(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld();

        if (times == 0) {
            times++;
        } else {
            times = 0;
            return;
        }

        BlockPos clickedPos = event.getPos();
        ItemStack rightHand = player.getHeldItemMainhand();

        if (ItemStackUtil.isNullStack(rightHand) || !WandRegistry.areWandsEqual(WandRegistry.WAND_BLAZE_ROD.getWandStack(), rightHand)) {
            return;
        }

        BlockState clickedBlock = world.getBlockState(clickedPos);

        if (!(clickedBlock.getBlock() instanceof CauldronBlock)) {
            return;
        }

        List<ItemEntity> inCauldron = CauldronUtil.getItemsInCauldron(world, clickedPos);
        ItemEntity base = getBaseStack(inCauldron);

        if ((base == null) || ItemStackUtil.isNullStack(base.getItem()) || !canGetUpgrade(base.getItem())) {
            return;
        }

        List<ItemEntity> ingredients = getIngredients(inCauldron);

        if ((ingredients == null) || (ingredients.size() <= 0)) {
            return;
        }

        ItemStack craftingResult = getResult(base, ingredients);

        if (ItemStackUtil.isNullStack(craftingResult)) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        base.remove();

        for (ItemEntity ei : ingredients) {
            ei.remove();
        }

        ItemEntity craftingResultEntity = new ItemEntity(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(), craftingResult);
        world.addEntity(craftingResultEntity);

        world.playSound((double) clickedPos.getX() + 0.5D, clickedPos.getY(), (double) clickedPos.getZ() + 0.5D,
                SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);

        Random rand = new Random();
        double particleX = (double) clickedPos.getX() + 0.5D;
        double particleY = (double) clickedPos.getY() + rand.nextDouble() * 6.0D / 16.0D;
        double particleZ = (double) clickedPos.getZ() + 0.5D;
        double randomPos = rand.nextDouble() * 0.6D - 0.3D;

        world.addParticle(ParticleTypes.SMOKE, particleX - 0.52D, particleY, particleZ + randomPos, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.FLAME, particleX - 0.52D, particleY, particleZ + randomPos, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.SMOKE, particleX + 0.52D, particleY, particleZ + randomPos, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.FLAME, particleX + 0.52D, particleY, particleZ + randomPos, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.SMOKE, particleX + randomPos, particleY, particleZ - 0.52D, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.FLAME, particleX + randomPos, particleY, particleZ - 0.52D, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.SMOKE, particleX + randomPos, particleY, particleZ + 0.52D, 0.0D, 0.0D, 0.0D);
        world.addParticle(ParticleTypes.FLAME, particleX + randomPos, particleY, particleZ + 0.52D, 0.0D, 0.0D, 0.0D);
    }

    /**
     * @return Returns the Item from list which is registered as a Base Item.
     */
    public ItemEntity getBaseStack(List<ItemEntity> inCauldron) {
        for (ItemEntity ei : inCauldron) {
            if (isBaseItem(ei)) {
                return ei;
            }
        }

        return null;
    }

    /**
     * @return Returns list of all Items registered as ingredients for given Base
     * Item from given list.
     */
    public List<ItemEntity> getIngredients(List<ItemEntity> inCauldron) {
        List<ItemEntity> list = new ArrayList<>();

        for (ItemEntity ei : inCauldron) {
            if (!isBaseItem(ei)) {
                list.add(ei);
            }
        }

        return list;
    }

    /**
     * @return Returns the result ItemStack from crafting on Cauldron.
     */
    public ItemStack getResult(ItemEntity base, List<ItemEntity> ingredients) {
        for (ItemEntity ingredient : ingredients) {
            ItemStack result = getResultSingle(base, ingredient);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     * @return Returns TRUE if the given Item is registered as base for upgrades.
     */
    public abstract boolean isBaseItem(ItemEntity entityItem);

    /**
     * @return Returns TRUE if the given ItemStack can get any more upgrades (don't
     * contains specif ied tag).
     */
    public abstract boolean canGetUpgrade(ItemStack base);

    /**
     * @return Returns ItemStack as a crafting result with all the NBT data already
     * written.
     */
    public abstract ItemStack getResultSingle(ItemEntity base, ItemEntity ingredient);
}