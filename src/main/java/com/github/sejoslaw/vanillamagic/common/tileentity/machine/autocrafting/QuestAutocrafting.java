package com.github.sejoslaw.vanillamagic.common.tileentity.machine.autocrafting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.tileentity.ICustomTileEntity;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.IAutocrafting;
import com.github.sejoslaw.vanillamagic.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.WorldUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestAutocrafting extends Quest {
	/**
	 * How many blocks lower than the Cauldron are the IInventories.
	 */
	private static int IINVDOWN = 3;

	int count = 0; // simple counter

	@SubscribeEvent
	public void changeSlot(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		World world = player.world;
		ItemStack rightHand = player.getHeldItemMainhand();

		if (world.isRemote || !ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		BlockPos tilePos = event.getPos();
		ICustomTileEntity tile = CustomTileEntityHandler.getCustomTileEntity(tilePos, world);

		if ((tile == null) || !(tile instanceof IAutocrafting)) {
			return;
		}

		if (count == 0) {
			count++;
		} else {
			count = 0;
			return;
		}

		IAutocrafting auto = (IAutocrafting) tile;

		if (player.isSneaking()) { // sneaking -> change slot
			if (auto.getCurrentCraftingSlot() == auto.getDefaultMaxCraftingSlot()) {
				auto.setCurrentCraftingSlot(auto.getDefaultCraftingSlot());
			} else {
				auto.setCurrentCraftingSlot(auto.getCurrentCraftingSlot() + 1);
			}

			EntityUtil.addChatComponentMessageNoSpam(player,
					"Current crafting slot set to: " + auto.getCurrentCraftingSlot());
		} else { // player is not sneaking -> show current crafting slot
			EntityUtil.addChatComponentMessageNoSpam(player, "Current crafting slot: " + auto.getCurrentCraftingSlot());
		}
	}

	@SubscribeEvent
	public void addAutocrafting(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		World world = player.world;

		if (!WandRegistry.areWandsEqual(WandRegistry.WAND_BLAZE_ROD.getWandStack(), player.getHeldItemMainhand())
				|| !player.isSneaking() || !ItemStackUtil.isNullStack(player.getHeldItemOffhand())) {
			return;
		}

		BlockPos workbenchPos = event.getPos();
		BlockPos cauldronPos = workbenchPos.up();

		if (!(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
				|| !isConstructionComplete(world, cauldronPos)) {
			return;
		}

		this.checkQuestProgress(player);

		if (!hasQuest(player)) {
			return;
		}

		TileAutocrafting tile = new TileAutocrafting();
		tile.init(player.world, cauldronPos);

		if (!CustomTileEntityHandler.addCustomTileEntity(tile, WorldUtil.getDimensionID(world))) {
			return;
		}

		EntityUtil.addChatComponentMessageNoSpam(player, tile.getClass().getSimpleName() + " added.");
	}

	@SubscribeEvent
	public void deleteAutocrafting(BreakEvent event) {
		PlayerEntity player = event.getPlayer();
		World world = player.world;
		BlockPos cauldronPos = event.getPos();
		Block cauldron = world.getBlockState(cauldronPos).getBlock();

		if (!(cauldron instanceof BlockCauldron)) {
			return;
		}

		BlockPos workbenchPos = cauldronPos.down();
		Block workbench = world.getBlockState(workbenchPos).getBlock();

		if (!(workbench instanceof BlockWorkbench)) {
			return;
		}

		CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, cauldronPos, player);
	}

	public static ItemStack[][] buildStackMatrix(IInventory[][] inventoryMatrix, int slot) {
		int size = inventoryMatrix.length;
		ItemStack[][] stackMat = new ItemStack[size][size];

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				stackMat[i][j] = inventoryMatrix[i][j].getStackInSlot(slot);
			}
		}

		return stackMat;
	}

	public static IInventory[][] buildIInventoryMatrix(World world, BlockPos[][] inventoryPosMatrix) {
		int size = inventoryPosMatrix.length;
		IInventory[][] invMat = new IInventory[size][size];

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				TileEntity tile = world.getTileEntity(inventoryPosMatrix[i][j]);

				if (tile instanceof IInventory) {
					invMat[i][j] = (IInventory) tile;
				}
			}
		}

		return invMat;
	}

	public static BlockPos[][] buildInventoryMatrix(BlockPos cauldronPos) {
		BlockPos leftTop = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - IINVDOWN, cauldronPos.getZ() + 2);
		BlockPos top = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - IINVDOWN, cauldronPos.getZ() + 2);
		BlockPos rightTop = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - IINVDOWN, cauldronPos.getZ() + 2);
		BlockPos right = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - IINVDOWN, cauldronPos.getZ());
		BlockPos rightBottom = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - IINVDOWN,
				cauldronPos.getZ() - 2);
		BlockPos bottom = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - IINVDOWN, cauldronPos.getZ() - 2);
		BlockPos leftBottom = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - IINVDOWN,
				cauldronPos.getZ() - 2);
		BlockPos left = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - IINVDOWN, cauldronPos.getZ());
		BlockPos middle = cauldronPos.offset(Direction.DOWN, IINVDOWN);

		return new BlockPos[][] { new BlockPos[] { leftTop, top, rightTop }, new BlockPos[] { left, middle, right },
				new BlockPos[] { leftBottom, bottom, rightBottom } };
	}

	public static boolean isConstructionComplete(World world, BlockPos cauldronPos) {
		boolean checkBasics = false;

		if ((world.getTileEntity(cauldronPos.offset(Direction.DOWN, IINVDOWN)) instanceof IInventory)
				&& (world.getTileEntity(cauldronPos.offset(Direction.DOWN, 2)) instanceof IHopper)
				&& (world.getBlockState(cauldronPos.offset(Direction.DOWN)).getBlock() instanceof BlockWorkbench)) {
			checkBasics = true;
		}

		if (checkBasics) {
			BlockPos leftTop = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - IINVDOWN,
					cauldronPos.getZ() + 2);
			BlockPos top = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - IINVDOWN, cauldronPos.getZ() + 2);
			BlockPos rightTop = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - IINVDOWN,
					cauldronPos.getZ() + 2);
			BlockPos right = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - IINVDOWN, cauldronPos.getZ());
			BlockPos rightBottom = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - IINVDOWN,
					cauldronPos.getZ() - 2);
			BlockPos bottom = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - IINVDOWN, cauldronPos.getZ() - 2);
			BlockPos leftBottom = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - IINVDOWN,
					cauldronPos.getZ() - 2);
			BlockPos left = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - IINVDOWN, cauldronPos.getZ());

			if ((world.getTileEntity(leftTop) instanceof IInventory) && (world.getTileEntity(top) instanceof IInventory)
					&& (world.getTileEntity(rightTop) instanceof IInventory)
					&& (world.getTileEntity(right) instanceof IInventory)
					&& (world.getTileEntity(rightBottom) instanceof IInventory)
					&& (world.getTileEntity(bottom) instanceof IInventory)
					&& (world.getTileEntity(leftBottom) instanceof IInventory)
					&& (world.getTileEntity(left) instanceof IInventory)) {
				return true;
			}
		}
		return false;
	}
}