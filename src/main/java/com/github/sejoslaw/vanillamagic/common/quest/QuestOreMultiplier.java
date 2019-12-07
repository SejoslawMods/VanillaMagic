package com.github.sejoslaw.vanillamagic.quest;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.event.EventPlayerUseCauldron;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.OreMultiplierUtil;
import com.github.sejoslaw.vanillamagic.util.SmeltingUtil;

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

	public int getMultiplier() {
		return multiplier;
	}

	public IWand getRequiredWand() {
		return requiredMinimalWand;
	}

	@SubscribeEvent
	public void doubleOre(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		BlockPos cauldronPos = event.getPos();

		if (!WandRegistry.isWandInMainHandRight(player, requiredMinimalWand.getWandID())) {
			return;
		}

		ItemStack fuelOffHand = player.getHeldItemOffhand();

		if (ItemStackUtil.isNullStack(fuelOffHand) || !SmeltingUtil.isItemFuel(fuelOffHand)) {
			return;
		}

		World world = player.world;

		if (!(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
				|| !OreMultiplierUtil.check(world, cauldronPos)) {
			return;
		}

		List<ItemEntity> oresInCauldron = SmeltingUtil.getOresInCauldron(world, cauldronPos);

		if (oresInCauldron.size() <= 0) {
			return;
		}

		checkQuestProgress(player);

		if (!hasQuest(player) || EventUtil
				.postEvent(new EventPlayerUseCauldron.OreMultiplier(player, world, cauldronPos, oresInCauldron))) {
			return;
		}

		multiply(player, oresInCauldron, cauldronPos);
	}

	public void multiply(PlayerEntity player, List<ItemEntity> oresInCauldron, BlockPos cauldronPos) {
		List<ItemEntity> smeltingResult = SmeltingUtil.countAndSmelt_OneByOneItemFromOffHand(player, oresInCauldron,
				cauldronPos.offset(Direction.UP), this, false);

		if (smeltingResult == null) {
			return;
		}

		World world = player.world;

		for (int i = 0; i < multiplier; ++i) {
			for (int j = 0; j < smeltingResult.size(); ++j) {
				world.spawnEntity(EntityUtil.copyItem(smeltingResult.get(j)));
			}
		}

		world.updateEntities();
	}
}