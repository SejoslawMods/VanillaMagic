package seia.vanillamagic.quest.quarry;

import java.io.Serializable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	private BlockPos lastDiggedPos = new BlockPos(0, 0, 0);
	
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

	/*
	 * TODO:
	 * showing the rendered particles around the digging area
	 */
	public void showBoundingBox() 
	{
	}
	
	/*
	 * TODO:
	 * updating the quarry (dig block, place block in chest)
	 */
	public void doWork()
	{
	}
}