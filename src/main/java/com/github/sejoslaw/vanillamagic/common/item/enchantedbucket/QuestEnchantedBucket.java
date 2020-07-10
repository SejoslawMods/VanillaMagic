package com.github.sejoslaw.vanillamagic.common.item.enchantedbucket;

import com.github.sejoslaw.vanillamagic.api.event.EventEnchantedBucket;
import com.github.sejoslaw.vanillamagic.api.item.IEnchantedBucket;
import com.github.sejoslaw.vanillamagic.common.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.CauldronUtil;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestEnchantedBucket extends Quest {
	int countTicks = 0;

	@SubscribeEvent
	public void craftEnchantedBucket(PlayerInteractEvent.RightClickBlock event) {
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

		boolean containsNetherStar = false;

		for (ItemEntity item : itemsInCauldron) {
			if (item.getItem().getItem().equals(Items.NETHER_STAR)) {
				containsNetherStar = true;
				break;
			}
		}

		IEnchantedBucket bucket = EnchantedBucketUtil.getEnchantedBucketFromCauldron(world, clickedPos);

		if ((containsNetherStar) && (bucket != null)) {
			if (!hasQuest(player)) {
				addStat(player);
			} else {
				ItemEntity newEI = new ItemEntity(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(), bucket.getItem().copy());
				world.addEntity(newEI);
				itemsInCauldron.forEach(Entity::remove);
			}
		}
	}

	@SubscribeEvent
	public void spawnLiquid(PlayerInteractEvent.LeftClickBlock event) {
		PlayerEntity player = event.getPlayer();
		ItemStack stackRightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(stackRightHand) || !player.isSneaking()) {
			return;
		}

		CompoundNBT stackTag = stackRightHand.getTag();

		if ((stackTag == null) || !stackTag.hasUniqueId(IEnchantedBucket.NBT_ENCHANTED_BUCKET)) {
			return;
		}

		if (!hasQuest(player)) {
			addStat(player);
		}

		if (hasQuest(player)) {
			for (IEnchantedBucket bucket : VMItems.ENCHANTED_BUCKETS) {
				CompoundNBT bucketTag = bucket.getItem().getTag();
				String bucketFluid = bucketTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
				String stackFluid = stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME);

				if (bucketFluid.equals(stackFluid)) {
					onItemUse(event, bucket);
					return;
				}
			}
		}
	}

	public void onItemUse(PlayerInteractEvent.LeftClickBlock event, IEnchantedBucket bucket) {
		PlayerEntity player = event.getPlayer();
		World world = event.getWorld();
		BlockPos blockPos = event.getPos();
		TileEntity tile = world.getTileEntity(blockPos);
		Direction face = event.getFace();

		if (tile instanceof IFluidHandler) {
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), 1000);
			int amount = ((IFluidHandler) tile).fill(fluid, IFluidHandler.FluidAction.EXECUTE);

			if ((amount > 0) && !EventUtil.postEvent(new EventEnchantedBucket.FillFluidHandler(bucket, player, world, blockPos, tile, (IFluidHandler) tile, fluid))) {
				((IFluidHandler) tile).fill(fluid, IFluidHandler.FluidAction.EXECUTE);
			}

			return;
		} else if (tile instanceof IFluidTank) {
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), 1000);
			int amount = ((IFluidTank) tile).fill(fluid, IFluidHandler.FluidAction.EXECUTE);

			if ((amount > 0) && !EventUtil.postEvent(new EventEnchantedBucket.FillFluidTank(bucket, player, world, blockPos, tile, (IFluidTank) tile, fluid))) {
				((IFluidTank) tile).fill(fluid, IFluidHandler.FluidAction.EXECUTE);
			}

			return;
		} else {
			if (tile != null) {
				IFluidHandler fh = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face).orElse(null);

				FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), 1000);
				int amount = fh.fill(fluid, IFluidHandler.FluidAction.EXECUTE);

				if ((amount > 0) && !EventUtil.postEvent(new EventEnchantedBucket.FillFluidHandler.UsingCapability(bucket, player, world, blockPos, tile, (IFluidHandler) tile, fluid))) {
					fh.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
				}
				return;
			} else if (bucket.getFluidInBucket() == Fluids.WATER) {
				if (world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON) {
					if (!EventUtil.postEvent(new EventEnchantedBucket.FillCauldron(bucket, player, world, blockPos))) {
						world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState().with(CauldronBlock.LEVEL, 3));
					}
					return;
				}
			}
		}

		blockPos = blockPos.offset(event.getFace());
		BlockState fluidState = bucket.getFluidInBucket().getDefaultState().getBlockState();

		if (!EventUtil.postEvent(new EventEnchantedBucket.SpawnLiquid(bucket, player, world, blockPos)) && !EventUtil.postEvent(new BlockEvent.CreateFluidSourceEvent(world, blockPos, fluidState))) {
			world.setBlockState(blockPos, fluidState);
		}
	}

	@SubscribeEvent
	public void shootLiquid(PlayerInteractEvent.RightClickItem event) {
		shootLiquid(event.getPlayer(), event.getWorld());
	}

	@SubscribeEvent
	public void spawnLiquid(PlayerInteractEvent.RightClickBlock event) {
		shootLiquid(event.getPlayer(), event.getWorld());
	}

	public void shootLiquid(PlayerEntity player, World world) {
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(stackRightHand)) {
			return;
		}

		CompoundNBT stackTag = stackRightHand.getTag();
		if ((stackTag == null) || !stackTag.hasUniqueId(IEnchantedBucket.NBT_ENCHANTED_BUCKET)) {
			return;
		}

		if (!hasQuest(player)) {
			addStat(player);
		}

		if (hasQuest(player)) {
			for (IEnchantedBucket bucket : VMItems.ENCHANTED_BUCKETS) {
				CompoundNBT bucketTag = bucket.getItem().getTag();
				String bucketFluid = bucketTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
				String stackFluid = stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME);

				if (bucketFluid.equals(stackFluid)) {
					onItemRightClick(player, world, bucket);
					return;
				}
			}
		}
	}

	public void onItemRightClick(PlayerEntity player, World world, IEnchantedBucket bucket) {
		if (world.isRemote) {
			return;
		}

		BlockRayTraceResult rayTrace = EntityUtil.rayTrace(world, player);

		BlockPos hittedBlock = rayTrace.getPos();
		hittedBlock = hittedBlock.offset(rayTrace.getFace());
		BlockState fluidState = bucket.getFluidInBucket().getDefaultState().getBlockState();

		if (!EventUtil.postEvent(new EventEnchantedBucket.SpawnLiquid(bucket, player, world, hittedBlock))) {
			ItemStack bucketStack = player.getHeldItemMainhand().copy();

			if (!EventUtil.postEvent(new BlockEvent.CreateFluidSourceEvent(world, hittedBlock, fluidState))) {
				world.setBlockState(hittedBlock, fluidState);
				world.tickBlockEntities();
			}

			player.setItemStackToSlot(EquipmentSlotType.MAINHAND, bucketStack);
		}
	}
}