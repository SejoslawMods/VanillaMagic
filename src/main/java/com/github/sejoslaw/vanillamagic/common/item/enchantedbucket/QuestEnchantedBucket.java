package com.github.sejoslaw.vanillamagic.item.enchantedbucket;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.BlockEvent.CreateFluidSourceEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.event.EventEnchantedBucket;
import com.github.sejoslaw.vanillamagic.api.item.IEnchantedBucket;
import com.github.sejoslaw.vanillamagic.item.VanillaMagicItems;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.CauldronUtil;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestEnchantedBucket extends Quest {
	int countTicks = 0;

	@SubscribeEvent
	public void craftEnchantedBucket(RightClickBlock event) {
		if (countTicks == 0) {
			countTicks++;
		} else {
			countTicks = 0;
			return;
		}

		World world = event.getWorld();
		PlayerEntity player = event.getPlayerEntity();
		BlockPos clickedPos = event.getPos();
		ItemStack stackRightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(stackRightHand) || !player.isSneaking()) {
			return;
		}

		if (!WandRegistry.areWandsEqual(stackRightHand, WandRegistry.WAND_BLAZE_ROD.getWandStack())
				|| !(world.getBlockState(clickedPos).getBlock() instanceof BlockCauldron)) {
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
		if ((containsNetherStar == true) && (bucket != null)) {
			if (!hasQuest(player)) {
				addStat(player);
			} else {
				ItemEntity newEI = new ItemEntity(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(),
						bucket.getItem().copy());
				world.spawnEntity(newEI);
				EntityUtil.removeEntities(world, itemsInCauldron);
			}
		}
	}

	@SubscribeEvent
	public void spawnLiquid(LeftClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		ItemStack stackRightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(stackRightHand) || !player.isSneaking()) {
			return;
		}

		CompoundNBT stackTag = stackRightHand.getTagCompound();
		if ((stackTag == null) || !stackTag.hasKey(IEnchantedBucket.NBT_ENCHANTED_BUCKET)) {
			return;
		}

		if (!hasQuest(player)) {
			addStat(player);
		}

		if (hasQuest(player)) {
			for (IEnchantedBucket bucket : VanillaMagicItems.ENCHANTED_BUCKETS) {
				CompoundNBT bucketTag = bucket.getItem().getTagCompound();
				String bucketFluid = bucketTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
				String stackFluid = stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME);

				if (bucketFluid.equals(stackFluid)) {
					onItemUse(event, bucket);
					return;
				}
			}
		}
	}

	public void onItemUse(LeftClickBlock event, IEnchantedBucket bucket) {
		PlayerEntity player = event.getPlayerEntity();
		World world = event.getWorld();
		BlockPos blockPos = event.getPos();
		TileEntity tile = world.getTileEntity(blockPos);
		Direction face = event.getFace();

		if (tile instanceof IFluidHandler) {
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
			int amount = ((IFluidHandler) tile).fill(fluid, false);

			if ((amount > 0) && !EventUtil.postEvent(new EventEnchantedBucket.FillFluidHandler(bucket, player, world,
					blockPos, tile, (IFluidHandler) tile, fluid))) {
				((IFluidHandler) tile).fill(fluid, true);
			}

			return;
		} else if (tile instanceof IFluidTank) {
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
			int amount = ((IFluidTank) tile).fill(fluid, false);

			if ((amount > 0) && !EventUtil.postEvent(new EventEnchantedBucket.FillFluidTank(bucket, player, world,
					blockPos, tile, (IFluidTank) tile, fluid))) {
				((IFluidTank) tile).fill(fluid, true);
			}

			return;
		} else if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face) && tile != null) {
			IFluidHandler fh = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);

			if (fh != null) {
				FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
				int amount = fh.fill(fluid, false);

				if ((amount > 0)
						&& !EventUtil.postEvent(new EventEnchantedBucket.FillFluidHandler.UsingCapability(bucket,
								player, world, blockPos, tile, (IFluidHandler) tile, fluid))) {
					fh.fill(fluid, true);
				}
				return;
			}
		} else if (bucket.getFluidInBucket() == FluidRegistry.WATER) {
			if (world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON) {
				if (!EventUtil.postEvent(new EventEnchantedBucket.FillCauldron(bucket, player, world, blockPos))) {
					world.setBlockState(blockPos,
							Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, 3));
				}
				return;
			}
		}

		blockPos = blockPos.offset(event.getFace());
		IBlockState fluidState = bucket.getFluidInBucket().getBlock().getDefaultState();

		if (!EventUtil.postEvent(new EventEnchantedBucket.SpawnLiquid(bucket, player, world, blockPos))
				&& !EventUtil.postEvent(new CreateFluidSourceEvent(world, blockPos, fluidState))) {
			world.setBlockState(blockPos, fluidState);
		}
	}

	@SubscribeEvent
	public void shootLiquid(RightClickItem event) {
		shootLiquid(event.getPlayerEntity(), event.getWorld());
	}

	@SubscribeEvent
	public void spawnLiquid(RightClickBlock event) {
		shootLiquid(event.getPlayerEntity(), event.getWorld());
	}

	public void shootLiquid(PlayerEntity player, World world) {
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(stackRightHand)) {
			return;
		}

		CompoundNBT stackTag = stackRightHand.getTagCompound();
		if ((stackTag == null) || !stackTag.hasKey(IEnchantedBucket.NBT_ENCHANTED_BUCKET)) {
			return;
		}

		if (!hasQuest(player)) {
			addStat(player);
		}

		if (hasQuest(player)) {
			for (IEnchantedBucket bucket : VanillaMagicItems.ENCHANTED_BUCKETS) {
				CompoundNBT bucketTag = bucket.getItem().getTagCompound();
				String bucketFluid = bucketTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
				String stackFluid = stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME);

				if (bucketFluid.equals(stackFluid)) {
					onItemRightClick(player, world, bucket);
					return;
				}
			}
		}
	}

	// Shoot
	public void onItemRightClick(PlayerEntity player, World world, IEnchantedBucket bucket) {
		if (world.isRemote) {
			return;
		}

		RayTraceResult rayTrace = EntityUtil.rayTrace(world, player, false);
		if (rayTrace == null) {
			return;
		}

		BlockPos hittedBlock = rayTrace.getBlockPos();
		hittedBlock = hittedBlock.offset(rayTrace.sideHit);
		Block fluidBlock = bucket.getFluidInBucket().getBlock();

		if (fluidBlock == null) {
			return;
		}

		IBlockState fluidState = fluidBlock.getDefaultState();
		if (!EventUtil.postEvent(new EventEnchantedBucket.SpawnLiquid(bucket, player, world, hittedBlock))) {
			ItemStack bucketStack = player.getHeldItemMainhand().copy();

			if (!EventUtil.postEvent(new CreateFluidSourceEvent(world, hittedBlock, fluidState))) {
				world.setBlockState(hittedBlock, fluidState);
				world.updateBlockTick(hittedBlock, fluidBlock, 1000, 1);
			}

			player.setItemStackToSlot(EquipmentSlotType.MAINHAND, bucketStack);
		}
	}
}