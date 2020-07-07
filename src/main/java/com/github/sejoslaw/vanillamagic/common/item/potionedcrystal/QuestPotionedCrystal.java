package com.github.sejoslaw.vanillamagic.common.item.potionedcrystal;

import com.github.sejoslaw.vanillamagic.api.event.EventPotionedCrystalTick;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.CauldronUtil;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * Class which describes Quest used by PotionedCrystals.
 */
public class QuestPotionedCrystal extends Quest
{
	int countTicks = 0;

	/**
	 * Craft single PotionedCrystal.
	 */
	@SubscribeEvent
	public void craftPotionedCrystal(PlayerInteractEvent.RightClickBlock event)
	{
		if (countTicks == 0) {
			countTicks++;
		} else {
			countTicks = 0;
			return;
		}
		
		World world = event.getWorld();
		PlayerEntity player = event.getPlayer();
		BlockPos clickedPos = event.getPos();
		ItemStack stackRightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(stackRightHand) ||
				!player.isSneaking() ||
				!WandRegistry.areWandsEqual(stackRightHand, WandRegistry.WAND_BLAZE_ROD.getWandStack()) ||
				!(world.getBlockState(clickedPos).getBlock() instanceof CauldronBlock)) {
			return;
		}

		List<ItemEntity> itemsInCauldron = CauldronUtil.getItemsInCauldron(world, clickedPos);

		if (itemsInCauldron.size() != 2) {
			return;
		}

		boolean hasNetherStar = false;

		for (ItemEntity item : itemsInCauldron) {
			if (item.getItem().getItem().equals(Items.NETHER_STAR)) {
				hasNetherStar = true;
				break;
			}
		}

		IPotionedCrystal potionedCrystal = PotionedCrystalHelper.getPotionedCrystalFromCauldron(world, clickedPos);

		if (!hasNetherStar || potionedCrystal == null) {
			return;
		}

		if (!hasQuest(player)) {
			addStat(player);
		}

		if (hasQuest(player)) {
			ItemEntity newEI = new ItemEntity(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(), potionedCrystal.getItem().copy());
			world.addEntity(newEI);
			itemsInCauldron.forEach(Entity::remove);
		}
	}
	
	/**
	 * Use Potion effect when Player holds a PotionedCrystal inside inventory.
	 */
	@SubscribeEvent
	public void onWearTick(TickEvent.PlayerTickEvent event)
	{
		PlayerEntity player = event.player;
		PlayerInventory inventory = player.inventory;
		NonNullList<ItemStack> mainInv = inventory.mainInventory;

		for (ItemStack stack : mainInv) {
			if (ItemStackUtil.isNullStack(stack)) {
				continue;
			}

			IPotionedCrystal ipc = PotionedCrystalHelper.getPotionedCrystal(stack);

			if (ipc == null) {
				continue;
			}

			if (!hasQuest(player)) {
				addStat(player);
			}

			if (!hasQuest(player)) {
				continue;
			}

			List<EffectInstance> effects = ipc.getPotion().getEffects();

			for (EffectInstance effect : effects) {
				EffectInstance newPE = new EffectInstance(effect.getPotion(), 1000, effect.getAmplifier(), effect.isAmbient(), effect.doesShowParticles());

				if (!MinecraftForge.EVENT_BUS.post(new EventPotionedCrystalTick(player, stack, newPE))) {
					player.addPotionEffect(newPE);
				}
			}
		}
	}
}