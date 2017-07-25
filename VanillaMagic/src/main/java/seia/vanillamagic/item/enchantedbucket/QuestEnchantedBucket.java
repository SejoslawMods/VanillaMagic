package seia.vanillamagic.item.enchantedbucket;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
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
import seia.vanillamagic.api.event.EventEnchantedBucket;
import seia.vanillamagic.api.item.IEnchantedBucket;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.CauldronUtil;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;

public class QuestEnchantedBucket extends Quest
{
	// Craft new Enchanted Bucket
	int countTicks = 0;
	@SubscribeEvent
	public void craftEnchantedBucket(RightClickBlock event) // onItemUse
	{
		if (countTicks == 0) countTicks++;
		else
		{
			countTicks = 0;
			return;
		}
		
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();
		BlockPos clickedPos = event.getPos();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(stackRightHand)) return;
		if (!player.isSneaking()) return;
		
		if (WandRegistry.areWandsEqual(stackRightHand, WandRegistry.WAND_BLAZE_ROD.getWandStack()))
		{
			if (world.getBlockState(clickedPos).getBlock() instanceof BlockCauldron)
			{
				List<EntityItem> itemsInCauldron = CauldronUtil.getItemsInCauldron(world, clickedPos);
				if (itemsInCauldron.size() == 0) return;
				else if (itemsInCauldron.size() == 2)
				{
					boolean ns = false;
					for (EntityItem item : itemsInCauldron)
					{
						if (item.getItem().getItem().equals(Items.NETHER_STAR))
						{
							ns = true;
							break;
						}
					}
					IEnchantedBucket bucket = EnchantedBucketUtil.getEnchantedBucketFromCauldron(world, clickedPos);
					if (ns == true && bucket != null)
					{
						if (!hasQuest(player)) addStat(player);
						
						if (hasQuest(player))
						{
							EntityItem newEI = new EntityItem(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(), bucket.getItem().copy());
							world.spawnEntity(newEI);
							EntityUtil.removeEntities(world, itemsInCauldron);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void spawnLiquid(LeftClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(stackRightHand)) return;
		if (!player.isSneaking()) return;
		
		NBTTagCompound stackTag = stackRightHand.getTagCompound();
		if (stackTag != null)
		{
			if (stackTag.hasKey(IEnchantedBucket.NBT_ENCHANTED_BUCKET))
			{
				if (!hasQuest(player)) addStat(player);
				
				if (hasQuest(player))
				{
					for (IEnchantedBucket bucket : VanillaMagicItems.ENCHANTED_BUCKETS)
					{
						NBTTagCompound bucketTag = bucket.getItem().getTagCompound();
						String bucketFluid = bucketTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
						String stackFluid = stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
						if (bucketFluid.equals(stackFluid))
						{
							onItemUse(event, bucket);
							return;
						}
					}
				}
			}
		}
	}
	
	public void onItemUse(LeftClickBlock event, IEnchantedBucket bucket) 
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos blockPos = event.getPos();
		TileEntity tile = world.getTileEntity(blockPos);
		EnumFacing face = event.getFace();
		if (tile instanceof IFluidHandler)
		{
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
			int amount = ((IFluidHandler) tile).fill(fluid, false);
			if (amount > 0)
			{
				if (!MinecraftForge.EVENT_BUS.post(new EventEnchantedBucket.FillFluidHandler(
						bucket, player, world, blockPos, tile, (IFluidHandler) tile, fluid)))
				{
					((IFluidHandler) tile).fill(fluid, true);
				}
			}
			return;
		}
		if (tile instanceof IFluidTank)
		{
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
			int amount = ((IFluidTank) tile).fill(fluid, false);
			if (amount > 0)
			{
				if (!MinecraftForge.EVENT_BUS.post(new EventEnchantedBucket.FillFluidTank(
						bucket, player, world, blockPos, tile, (IFluidTank) tile, fluid)))
				{
					((IFluidTank) tile).fill(fluid, true);
				}
			}
			return;
		}
		if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face) && tile != null)
		{
			IFluidHandler fh = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
			if (fh != null)
			{
				FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
				int amount = fh.fill(fluid, false);
				if (amount > 0)
				{
					if (!MinecraftForge.EVENT_BUS.post(new EventEnchantedBucket.FillFluidHandler.UsingCapability(
							bucket, player, world, blockPos, tile, (IFluidHandler) tile, fluid)))
					{
						fh.fill(fluid, true);
					}
				}
				return;
			}
		}
		if (bucket.getFluidInBucket() == FluidRegistry.WATER)
		{
			if (world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON)
			{
				if (!MinecraftForge.EVENT_BUS.post(new EventEnchantedBucket.FillCauldron(bucket, player, world, blockPos)))
					world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, 3));
				return;
			}
		}
		blockPos = blockPos.offset(event.getFace());
		if (!MinecraftForge.EVENT_BUS.post(new EventEnchantedBucket.SpawnLiquid(
				bucket, player, world, blockPos)))
		{
			IBlockState fluidState = bucket.getFluidInBucket().getBlock().getDefaultState();
			if (!MinecraftForge.EVENT_BUS.post(new CreateFluidSourceEvent(world, blockPos, fluidState)))
				world.setBlockState(blockPos, fluidState);
		}
	}
	
	@SubscribeEvent
	public void shootLiquid(RightClickItem event)
	{
		shootLiquid(event.getEntityPlayer(), event.getWorld());
	}
	
	@SubscribeEvent
	public void spawnLiquid(RightClickBlock event)
	{
		shootLiquid(event.getEntityPlayer(), event.getWorld());
	}
	
	public void shootLiquid(EntityPlayer player, World world)
	{
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(stackRightHand)) return;
		
		NBTTagCompound stackTag = stackRightHand.getTagCompound();
		if (stackTag == null) return;
		
		if (stackTag.hasKey(IEnchantedBucket.NBT_ENCHANTED_BUCKET))
		{
			if (!hasQuest(player)) addStat(player);
			
			if (hasQuest(player))
			{
				for (IEnchantedBucket bucket : VanillaMagicItems.ENCHANTED_BUCKETS)
				{
					NBTTagCompound bucketTag = bucket.getItem().getTagCompound();
					String bucketFluid = bucketTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
					String stackFluid = stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
					if (bucketFluid.equals(stackFluid))
					{
						onItemRightClick(player, world, bucket);
						return;
					}
				}
			}
		}
	}

	// Shoot
	public void onItemRightClick(EntityPlayer player, World world, IEnchantedBucket bucket) 
	{
		if (!world.isRemote)
		{
			RayTraceResult rayTrace = EntityUtil.rayTrace(world, player, false);
			if (rayTrace != null)
			{
				BlockPos hittedBlock = rayTrace.getBlockPos();
				hittedBlock = hittedBlock.offset(rayTrace.sideHit);
				Block fluidBlock = bucket.getFluidInBucket().getBlock();
				if (fluidBlock == null) return;
				
				IBlockState fluidState = fluidBlock.getDefaultState();
				if (!MinecraftForge.EVENT_BUS.post(new EventEnchantedBucket.SpawnLiquid(
						bucket, player, world, hittedBlock)))
				{
					ItemStack bucketStack = player.getHeldItemMainhand().copy();
					if (!MinecraftForge.EVENT_BUS.post(new CreateFluidSourceEvent(world, hittedBlock, fluidState)))
					{
						world.setBlockState(hittedBlock, fluidState);
						world.updateBlockTick(hittedBlock, fluidBlock, 1000, 1);
					}
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, bucketStack);
				}
			}
		}
	}
}