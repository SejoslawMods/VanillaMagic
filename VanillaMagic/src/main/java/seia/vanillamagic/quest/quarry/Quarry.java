package seia.vanillamagic.quest.quarry;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.utils.SmeltingHelper;

public class Quarry implements Serializable
{
	// Cost for mining one block
	public static final int ONE_OPERATION_COST = 100;
	// It's 11x11 but 9x9 is for digging
	public static final int BASIC_QUARRY_SIZE = 11;
	
	public final BlockPos quarryPos;
	public final EntityPlayer placedBy;
	public final ItemStack itemInHand; // should be cauldron
	public final World world;
	public final BlockCauldron cauldron;
	public final BlockPos diamondBlockPos;
	public final Block diamondBlock;
	public final BlockPos chestBlockPos;
	public final BlockChest chest;
	public final long id;
	
	private int ticks = 0;
	private BlockPos diggingPos;
	private Random rand = new Random();
	
	public Quarry(BlockPos quarryPos, EntityPlayer whoPlacedQuarry, ItemStack itemInHand)
	{
		this.quarryPos = quarryPos;
		this.placedBy = whoPlacedQuarry;
		this.itemInHand = itemInHand;
		this.world = placedBy.worldObj;
		this.cauldron = (BlockCauldron) world.getBlockState(quarryPos).getBlock();
		this.diamondBlockPos = new BlockPos(quarryPos.getX() + 1, quarryPos.getY(), quarryPos.getZ());
		this.diamondBlock = world.getBlockState(diamondBlockPos).getBlock();
		this.chestBlockPos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() - 1);
		this.chest = (BlockChest) world.getBlockState(chestBlockPos).getBlock();
		this.id = System.currentTimeMillis();
		
		this.diggingPos = new BlockPos(quarryPos.getX() - 1, quarryPos.getY(), quarryPos.getZ() + 1);
	}

	/**
	 * @return - true if quarry is complete and right, otherwise false 
	 * (this should be achievable if the quarry isn't correctly build 
	 * - exception should be thrown when the object is creating)
	 */
	public boolean isComplete() 
	{
		return true;
	}
	
	public BlockPos getTopPos()
	{
		return new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() + BASIC_QUARRY_SIZE - 1);
	}
	
	public BlockPos getLeftPos()
	{
		return new BlockPos(quarryPos.getX() - BASIC_QUARRY_SIZE + 1, quarryPos.getY(), quarryPos.getZ());
	}
	
	public BlockPos getTopLeftPos()
	{
		return new BlockPos(quarryPos.getX() - BASIC_QUARRY_SIZE + 1, quarryPos.getY(), quarryPos.getZ() + BASIC_QUARRY_SIZE - 1);
	}

	/*
	 * check if quarry has fuel to dig
	 */
	public void checkFuel()
	{
		List<EntityItem> fuelsInCauldron = SmeltingHelper.getFuelFromCauldron(world, quarryPos);
		if(fuelsInCauldron.size() == 0)
		{
			return;
		}
		for(EntityItem entityItem : fuelsInCauldron)
		{
			ItemStack stack = entityItem.getEntityItem();
			if(SmeltingHelper.isItemFuel(stack))
			{
				ticks += SmeltingHelper.countTicks(stack);
			}
		}
	}
	
	/*
	 * will return if quarry can dig next block
	 */
	public boolean canDig()
	{
		if(ticks >= ONE_OPERATION_COST)
		{
			ticks -= ONE_OPERATION_COST;
			if(ticks < 0)
			{
				ticks = 0;
			}
			return true;
		}
		return false;
	}

	/*
	 * showing the rendered particles around the digging area
	 */
	public void showBoundingBox()
	{
		int posX = quarryPos.getX();
		int posZ = quarryPos.getZ();
		for(int x = 0; x < BASIC_QUARRY_SIZE; x++)
		{
			for(int z = 0; z < BASIC_QUARRY_SIZE; z++)
			{
				if((x == posX) || // bottom line
						(z == posZ) || // right line
						(x == (posX - BASIC_QUARRY_SIZE - 1)) || // top line
						(z == (posZ + BASIC_QUARRY_SIZE - 1))) // left line
				{
					BlockPos particlePos = new BlockPos(x, quarryPos.getY(), z);
					world.spawnParticle(EnumParticleTypes.END_ROD,
							particlePos.getX(), 
							particlePos.getY(), 
							particlePos.getZ(), 
							rand.nextGaussian() * 0.005D, 
							rand.nextGaussian() * 0.005D, 
							rand.nextGaussian() * 0.005D, 
							new int[0]);
				}
			}
		}
	}
	
	/*
	 * TODO:
	 * updating the quarry (dig block, place block in chest)
	 */
	public void doWork() // once a world tick
	{
		/*
		if(world.isAirBlock(diggingPos)) 
		{
			return;
		}
		if(Block.isEqualTo(world.getBlockState(diggingPos).getBlock(), Blocks.BEDROCK))
		{
			int x = 0;
			int y = quarryPos.getY();
			int z = 0;
			if(diggingPos.getZ() + 1)
		}
		IBlockState diggingBlockState = world.getBlockState(diggingPos);
		Block diggingBlock = diggingBlockState.getBlock();
		{
			// Block Digging
		}
		{
			// Going to the next block
			int y = diggingPos.
		}
		*/
	}
}