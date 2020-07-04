package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.magic.spell.SpellRegistry;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.ListUtil;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestSummonHorde extends Quest {
    protected int level; // it will tell how many monsters will be summoned
    protected int range = 10; // range in blocks
    protected int verticalRange = 1; // vertical range in blocks
    // the number is amount of blocks in which the enemy can spawn,
    // requiredDistanceToPlayer away from Player
    protected double requiredDistanceToPlayer = range - 2;
    protected ItemStack requiredLeftHand;

    public void readData(JsonObject jo) {
        this.level = jo.get("level").getAsInt();
        this.requiredLeftHand = ItemStackUtil.getItemStackFromJSON(jo.get("requiredLeftHand").getAsJsonObject());
        this.icon = requiredLeftHand.copy();

        super.readData(jo);
    }

    @SubscribeEvent
    public void spawnHorde(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        World world = player.world;
        ItemStack rightHand = player.getHeldItemMainhand();
        ItemStack leftHand = player.getHeldItemOffhand();

        if (ItemStackUtil.isNullStack(rightHand) ||
                ItemStackUtil.isNullStack(leftHand) ||
                !WandRegistry.areWandsEqual(rightHand, WandRegistry.WAND_NETHER_STAR.getWandStack()) ||
                !ItemStack.areItemsEqual(leftHand, requiredLeftHand) ||
                ItemStackUtil.getStackSize(leftHand) != ItemStackUtil.getStackSize(requiredLeftHand)) {
            return;
        }

        checkQuestProgress(player);

        if (!hasQuest(player)) {
            return;
        }

        EntityUtil.addChatComponentMessageNoSpam(player, TextUtil.wrap(player.getDisplayName() + " summoned horde lvl: " + this.level + ". Prepare to DIE !!!"));
        spawnHorde(player, world);
        ItemStackUtil.decreaseStackSize(leftHand, ItemStackUtil.getStackSize(requiredLeftHand));
    }

    public void spawnHorde(PlayerEntity player, World world) {
        int posX = (int) Math.round(player.getPosX() - 0.5f);
        int posY = (int) player.getPosY();
        int posZ = (int) Math.round(player.getPosZ() - 0.5f);

        for (int i = 0; i < level; ++i) {
            for (int ix = posX - range; ix <= posX + range; ++ix) {
                for (int iz = posZ - range; iz <= posZ + range; ++iz) {
                    for (int iy = posY - verticalRange; iy <= posY + verticalRange; ++iy) {
                        BlockPos spawnPos = new BlockPos(ix, iy, iz);
                        double distanceToPlayer = spawnPos.distanceSq(new Vec3i(posX, posY, posZ));

                        if ((distanceToPlayer >= requiredDistanceToPlayer) && (world.rand.nextInt(50) == 0)) {
                            spawnEnemy(player, world, spawnPos);
                        }
                    }
                }
            }
        }
    }

    public void spawnEnemy(PlayerEntity player, World world, BlockPos spawnPos) {
        int randID = ListUtil.getRandomObjectFromTab(SpellRegistry.getSummonMobSpellIDsWithoutSpecific(SpellRegistry.SPELL_SUMMON_WITHER.getSpellID()));
        SpellRegistry.castSummonMob(player, world, spawnPos, Direction.UP, randID, true);
    }
}