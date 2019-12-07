package com.github.sejoslaw.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.event.EventPlayerUseCauldron;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.util.AltarUtil;
import com.github.sejoslaw.vanillamagic.util.CauldronUtil;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCraftOnAltar extends Quest {
	/*
	 * Each ItemStack is a different Item Instead of doing 1x Coal + 1x Coal, do 2x
	 * Coal -> 2 will be stackSize
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

	public ItemStack[] getIngredients() {
		return ingredients;
	}

	public ItemStack[] getResult() {
		return result;
	}

	public int getRequiredAltarTier() {
		return requiredAltarTier;
	}

	public IWand getRequiredWand() {
		return requiredMinimalWand;
	}

	public int getIngredientsStackSize() {
		int stackSize = 0;

		for (int i = 0; i < ingredients.length; ++i) {
			stackSize += ItemStackUtil.getStackSize(ingredients[i]);
		}

		return stackSize;
	}

	public int getIngredientsInCauldronStackSize(List<ItemEntity> entitiesInCauldron) {
		int stackSize = 0;

		for (int i = 0; i < entitiesInCauldron.size(); ++i) {
			stackSize += ItemStackUtil.getStackSize(entitiesInCauldron.get(i).getItem());
		}

		return stackSize;
	}

	@SubscribeEvent
	public void craftOnAltar(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		BlockPos cauldronPos = event.getPos();

		if (!WandRegistry.isWandInMainHandRight(player, requiredMinimalWand.getWandID())) {
			return;
		}

		World world = player.world;

		if (!(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
				|| !AltarUtil.checkAltarTier(world, cauldronPos, requiredAltarTier)) {
			return;
		}

		List<ItemEntity> entitiesInCauldron = CauldronUtil.getItemsInCauldron(world, cauldronPos);
		int ingredientsStackSize = getIngredientsStackSize();
		int ingredientsInCauldronStackSize = getIngredientsInCauldronStackSize(entitiesInCauldron);

		if (ingredientsStackSize != ingredientsInCauldronStackSize) {
			return;
		}

		List<ItemEntity> alreadyCheckedItemEntitys = new ArrayList<ItemEntity>();

		for (int i = 0; i < ingredients.length; ++i) {
			ItemStack currentlyCheckedIngredient = ingredients[i];

			for (int j = 0; j < entitiesInCauldron.size(); ++j) {
				ItemEntity currentlyCheckedItemEntity = entitiesInCauldron.get(j);

				if (ItemStack.areItemStacksEqual(currentlyCheckedIngredient, currentlyCheckedItemEntity.getItem())) {
					alreadyCheckedItemEntitys.add(currentlyCheckedItemEntity);
					break;
				}
			}
		}

		if (ingredients.length != alreadyCheckedItemEntitys.size()) {
			return;
		}

		checkQuestProgress(player);

		if (!hasQuest(player) || EventUtil.postEvent(new EventPlayerUseCauldron.CraftOnAltar(player, world, cauldronPos,
				alreadyCheckedItemEntitys, result))) {
			return;
		}

		for (int i = 0; i < alreadyCheckedItemEntitys.size(); ++i) {
			world.removeEntity(alreadyCheckedItemEntitys.get(i));
		}

		BlockPos newItemPos = new BlockPos(cauldronPos.getX(), cauldronPos.getY() + 1, cauldronPos.getZ());

		for (int i = 0; i < result.length; ++i) {
			Block.spawnAsEntity(world, newItemPos, result[i].copy());
		}

		world.updateEntities();
	}
}