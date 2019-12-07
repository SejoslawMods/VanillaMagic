package com.github.sejoslaw.vanillamagic.quest;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.config.VMConfig;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestItemMagnet extends Quest {
	public final ItemStack star = new ItemStack(Items.NETHER_STAR);
	public final int range = VMConfig.ITEM_MAGNET_RANGE;
	public final int maxPulledItems = VMConfig.ITEM_MAGNET_PULLED_ITEMS;

	@SubscribeEvent
	public void playerTick(LivingUpdateEvent event) {
		if (!(event.getEntity() instanceof PlayerEntity)) {
			return;
		}

		PlayerEntity player = (PlayerEntity) event.getEntity();

		if (!playerHasRightItemsInInventory(player)) {
			return;
		}

		checkQuestProgress(player);

		if (!hasQuest(player)) {
			return;
		}

		double x = player.posX;
		double y = player.posY + 0.75;
		double z = player.posZ;

		List<ItemEntity> items = player.world.getEntitiesWithinAABB(ItemEntity.class,
				new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
		int pulledItems = 0;

		for (ItemEntity item : items) {
			if (pulledItems > maxPulledItems) {
				break;
			}

			setEntityMotionFromVector(item, VectorWrapper.wrap(x, y, z), 0.45F);
			pulledItems++;
		}
	}

	public boolean playerHasRightItemsInInventory(PlayerEntity player) {
		boolean star1 = false;
		boolean star2 = false;
		NonNullList<ItemStack> inventory = player.inventory.mainInventory;

		for (int i = 0; i < inventory.size(); ++i) {
			if (!ItemStack.areItemStacksEqual(star, inventory.get(i))) {
				continue;
			}

			if (star1 == true) {
				star2 = true;
				break;
			} else {
				star1 = true;
			}
		}

		if ((star1 == true) && (star2 == true)) {
			return true;
		}

		return false;
	}

	public void setEntityMotionFromVector(Entity entity, Vector3D originalPosVector, float modifier) {
		Vector3D entityVector = VectorWrapper.wrap(entity.posX, entity.posY - entity.getYOffset() + entity.height / 2,
				entity.posZ);
		Vector3D finalVector = originalPosVector.subtract(entityVector);

		if (finalVector.mag() > 1) {
			finalVector = finalVector.normalize();
		}

		entity.motionX = finalVector.getX() * modifier;
		entity.motionY = finalVector.getY() * modifier;
		entity.motionZ = finalVector.getZ() * modifier;
	}
}