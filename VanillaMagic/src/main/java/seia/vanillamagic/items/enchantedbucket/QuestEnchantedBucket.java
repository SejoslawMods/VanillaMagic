package seia.vanillamagic.items.enchantedbucket;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;

public class QuestEnchantedBucket extends Quest
{
	public QuestEnchantedBucket(Quest required, int posX, int posY, ItemStack icon, String questName,
			String uniqueName)
	{
		super(required, posX, posY, icon, questName, uniqueName);
	}
	
	@SubscribeEvent
	public void placeLiquid(RightClickBlock event) // onItemUse
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if(stackRightHand == null)
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
				for(IEnchantedBucket bucket : IEnchantedBucket.enchantedBuckets)
				{
					NBTTagCompound bucketTag = bucket.getItem().getTagCompound();
					String bucketUnique = bucketTag.getString(IEnchantedBucket.NBT_ENCHANTED_BUCKET);
					String stackUnique = stackTag.getString(IEnchantedBucket.NBT_ENCHANTED_BUCKET);
					if(bucketUnique.equals(stackUnique))
					{
						onItemUse(event, bucket);
					}
				}
			}
		}
	}
	
	public void onItemUse(RightClickBlock event, IEnchantedBucket bucket) 
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos blockPos = event.getPos();
		if(world.isRemote || player.isSneaking())
		{
			return;
		}
		if(!world.canMineBlockBody(player, blockPos))
		{
			return;
		}
		TileEntity tile = world.getTileEntity(blockPos);
		if(tile instanceof IFluidHandler)
		{
			FluidStack fluid = new FluidStack(bucket.getFluidInBucket(), 1000);
			int amount = ((IFluidHandler) tile).fill(fluid, false);
			if(amount > 0)
			{
				((IFluidHandler) tile).fill(fluid, true);
			}
			return;
		}
		if(bucket.getFluidInBucket() == FluidRegistry.WATER)
		{
			if(world.getBlockState(blockPos).getBlock() == Blocks.CAULDRON)
			{
				world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState().withProperty(BlockCauldron.LEVEL, 3));
			}
		}
	}

	@SubscribeEvent
	public void placeLiquid(RightClickItem event) // onItemRightClick
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if(stackRightHand == null)
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
				for(IEnchantedBucket bucket : IEnchantedBucket.enchantedBuckets)
				{
					NBTTagCompound bucketTag = bucket.getItem().getTagCompound();
					String bucketUnique = bucketTag.getString(IEnchantedBucket.NBT_ENCHANTED_BUCKET);
					String stackUnique = stackTag.getString(IEnchantedBucket.NBT_ENCHANTED_BUCKET);
					if(bucketUnique.equals(stackUnique))
					{
						onItemRightClick(event, bucket);
					}
				}
			}
		}
	}

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
				world.setBlockState(hittedBlock, fluidBlock.getDefaultState());
				world.updateBlockTick(hittedBlock, fluidBlock, 1000, 1);
			}
		}
	}
	
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
	
	public boolean canPlaceWater(World world, BlockPos blockPos)
	{
		if (!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial(world.getBlockState(blockPos)).isSolid())
		{
			return false;
		}
		else if ((world.getBlockState(blockPos).getBlock() == Blocks.WATER || world.getBlockState(blockPos).getBlock() == Blocks.FLOWING_WATER) && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0)
		{
			return false;
		}
		return true;
    }

	public boolean tryPlaceWater(World worldIn, BlockPos pos)
	{
		Material material = worldIn.getBlockState(pos).getBlock().getMaterial(worldIn.getBlockState(pos));
		boolean notSolid = !material.isSolid();
		if (!worldIn.isAirBlock(pos) && !notSolid)
		{
			return false;
		} 
		else
		{
			if (worldIn.provider.doesWaterVaporize())
			{
				int i = pos.getX();
				int j = pos.getY();
				int k = pos.getZ();
				worldIn.playSound(null, i, j, k, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
				for (int l = 0; l < 8; ++l)
				{
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D, 0);
				}
			} 
			else
			{
				if (!worldIn.isRemote && notSolid && !material.isLiquid())
				{
					worldIn.destroyBlock(pos, true);
				}
				worldIn.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 3);
			}
			return true;
		}
	}
}