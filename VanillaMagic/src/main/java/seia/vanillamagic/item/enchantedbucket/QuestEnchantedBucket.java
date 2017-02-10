package seia.vanillamagic.item.enchantedbucket;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.item.IEnchantedBucket;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.CauldronHelper;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.ItemStackHelper;

public class QuestEnchantedBucket extends Quest
{
	// Craft new Enchanted Bucket
	int countTicks = 0;
	@SubscribeEvent
	public void craftEnchantedBucket(RightClickBlock event) // onItemUse
	{
		if(countTicks == 0)
		{
			countTicks++;
		}
		else
		{
			countTicks = 0;
			return;
		}
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();
		BlockPos clickedPos = event.getPos();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(stackRightHand))
		{
			return;
		}
		if(!player.isSneaking())
		{
			return;
		}
		if(WandRegistry.areWandsEqual(stackRightHand, WandRegistry.WAND_BLAZE_ROD.getWandStack()))
		{
			if(world.getBlockState(clickedPos).getBlock() instanceof BlockCauldron)
			{
				List<EntityItem> itemsInCauldron = CauldronHelper.getItemsInCauldron(world, clickedPos);
				if(itemsInCauldron.size() == 0)
				{
					return;
				}
				else if(itemsInCauldron.size() == 2)
				{
					boolean ns = false;
					for(EntityItem item : itemsInCauldron)
					{
						if(item.getEntityItem().getItem().equals(Items.NETHER_STAR))
						{
							ns = true;
							break;
						}
					}
					IEnchantedBucket bucket = EnchantedBucketHelper.getEnchantedBucketFromCauldron(world, clickedPos);
					if(ns == true && bucket != null)
					{
						if(!player.hasAchievement(achievement))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							EntityItem newEI = new EntityItem(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(), bucket.getItem().copy());
							world.spawnEntity(newEI);
							EntityHelper.removeEntities(world, itemsInCauldron);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void spawnLiquid(LeftClickBlock event)
	{
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();
		BlockPos clickedPos = event.getPos();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(stackRightHand))
		{
			return;
		}
		if(!player.isSneaking())
		{
			return;
		}
		NBTTagCompound stackTag = stackRightHand.getTagCompound();
		if(stackTag != null)
		{
			if(stackTag.hasKey(IEnchantedBucket.NBT_ENCHANTED_BUCKET))
			{
				if(!player.hasAchievement(achievement))
				{
					player.addStat(achievement, 1);
				}
				if(player.hasAchievement(achievement))
				{
					for(IEnchantedBucket bucket : VanillaMagicItems.ENCHANTED_BUCKETS)
					{
						NBTTagCompound bucketTag = bucket.getItem().getTagCompound();
						String bucketFluid = bucketTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
						String stackFluid = stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
						if(bucketFluid.equals(stackFluid))
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
		if(tile instanceof IFluidHandler)
		{
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
			int amount = ((IFluidHandler) tile).fill(fluid, false);
			if(amount > 0)
			{
				((IFluidHandler) tile).fill(fluid, true);
			}
			return;
		}
		if(tile instanceof IFluidTank)
		{
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
			int amount = ((IFluidTank) tile).fill(fluid, false);
			if(amount > 0)
			{
				((IFluidTank) tile).fill(fluid, true);
			}
			return;
		}
		if(tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face))
		{
			IFluidHandler fh = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, face);
			if(fh != null)
			{
				FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), Fluid.BUCKET_VOLUME);
				int amount = fh.fill(fluid, false);
				if(amount > 0)
				{
					fh.fill(fluid, true);
				}
				return;
			}
		}
		if(bucket.getFluidInBucket() == FluidRegistry.WATER)
		{
			if(world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON)
			{
				world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, 3));
				return;
			}
		}
		blockPos = blockPos.offset(event.getFace());
		world.setBlockState(blockPos, bucket.getFluidInBucket().getBlock().getDefaultState());
	}

	// Shoot
	@SubscribeEvent
	public void shootLiquid(RightClickItem event) // onItemRightClick
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(stackRightHand))
		{
			return;
		}
		NBTTagCompound stackTag = stackRightHand.getTagCompound();
		if(stackTag == null)
		{
			return;
		}
		if(stackTag.hasKey(IEnchantedBucket.NBT_ENCHANTED_BUCKET))
		{
			if(!player.hasAchievement(achievement))
			{
				player.addStat(achievement, 1);
			}
			if(player.hasAchievement(achievement))
			{
				for(IEnchantedBucket bucket : VanillaMagicItems.ENCHANTED_BUCKETS)
				{
					NBTTagCompound bucketTag = bucket.getItem().getTagCompound();
					String bucketFluid = bucketTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
					String stackFluid = stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME);
					if(bucketFluid.equals(stackFluid))
					{
						onItemRightClick(event, bucket);
						return;
					}
				}
			}
		}
	}

	// Shoot
	public void onItemRightClick(RightClickItem event, IEnchantedBucket bucket) 
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos blockPos = event.getPos();
		ItemStack stack = player.getHeldItemMainhand();
		EnumHand hand = EnumHand.MAIN_HAND;
		if(!world.isRemote)
		{
			RayTraceResult rayTrace = rayTrace(world, player, false);
			if(rayTrace != null)
			{
				BlockPos hittedBlock = rayTrace.getBlockPos();
				hittedBlock = hittedBlock.offset(rayTrace.sideHit);
				Block fluidBlock = bucket.getFluidInBucket().getBlock();
				if(fluidBlock == null)
				{
					return;
				}
				world.setBlockState(hittedBlock, fluidBlock.getDefaultState());
				world.updateBlockTick(hittedBlock, fluidBlock, 1000, 1);
			}
		}
	}
	
	@Nullable
	public RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids)
	{
		float pitch = playerIn.rotationPitch;
		float yaw = playerIn.rotationYaw;
		double x = playerIn.posX;
		double y = playerIn.posY + (double)playerIn.getEyeHeight();
		double z = playerIn.posZ;
		Vec3d vec3d = new Vec3d(x, y, z);
		float f2 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		float f4 = -MathHelper.cos(-pitch * 0.017453292F);
		float f5 = MathHelper.sin(-pitch * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = 1000.0D;
		Vec3d vec3d1 = vec3d.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
		return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}
}