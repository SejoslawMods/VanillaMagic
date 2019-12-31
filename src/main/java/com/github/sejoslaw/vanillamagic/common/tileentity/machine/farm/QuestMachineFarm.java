package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm;


import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.common.quest.QuestMachineActivate;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMachineFarm extends QuestMachineActivate {
    protected int radius;

    public void readData(JsonObject jo) {
        super.readData(jo);

        this.radius = jo.get("radius").getAsInt();

        if (this.radius < 0) {
            this.radius = -this.radius;
        }
    }

    @SubscribeEvent
    public void startFarm(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        BlockPos cauldronPos = event.getPos();

        if (!startWorkWithCauldron(player, cauldronPos, this)) {
            return;
        }

        TileFarm tileFarm = new TileFarm();
        tileFarm.setup(player.world, cauldronPos);
        tileFarm.setWorkRadius(radius);

        if (!CustomTileEntityHandler.addCustomTileEntity(tileFarm, tileFarm.getWorld())) {
            return;
        }

        ItemStackUtil.decreaseStackSize(player.getHeldItemOffhand(), ItemStackUtil.getStackSize(mustHaveOffHand));
        EntityUtil.addChatComponentMessageNoSpam(player, TextUtil.wrap(tileFarm.getClass().getSimpleName() + " added"));
    }

    @SubscribeEvent
    public void stopFarm(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld().getWorld();
        BlockPos cauldronPos = event.getPos();

        if (!(world.getTileEntity(cauldronPos.offset(Direction.UP)) instanceof IInventory)
                || !(world.getTileEntity(cauldronPos.offset(Direction.DOWN)) instanceof IInventory)) {
            return;
        }

        CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, cauldronPos, player);
    }
}