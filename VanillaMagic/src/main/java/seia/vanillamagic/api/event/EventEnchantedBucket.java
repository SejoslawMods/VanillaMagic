package seia.vanillamagic.api.event;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import seia.vanillamagic.api.item.IEnchantedBucket;

/**
 * Event connected with Enchanted Bucket.
 */
public class EventEnchantedBucket extends EventCustomItem.OnUseByPlayer.OnTileEntity
{
	private final IEnchantedBucket _bucket;
	private final FluidStack _fluidStack;
	
	public EventEnchantedBucket(IEnchantedBucket customItem, EntityPlayer player, World world, BlockPos blockPos, 
			TileEntity tile, FluidStack fluidStack) 
	{
		super(customItem, player, world, blockPos, tile);
		this._bucket = customItem;
		this._fluidStack = fluidStack;
	}
	
	/**
	 * @return Returns the bucket which will be used.
	 */
	public IEnchantedBucket getEnchantedBucket()
	{
		return _bucket;
	}
	
	/**
	 * @return Returns the FluidStack which will be added to IFluidHandler. <br>
	 * 		   NULL if used on Cauldron on spawn on World.
	 */
	@Nullable
	public FluidStack getFluidStack()
	{
		return _fluidStack;
	}
	
	/**
	 * This Event is fired when Player used Enchanted Bucket to fill Fluid Handler.
	 */
	public static class FillFluidHandler extends EventEnchantedBucket 
	{
		private final IFluidHandler _handler;
		
		public FillFluidHandler(IEnchantedBucket bucket, EntityPlayer player, World world, BlockPos blockPos, 
				TileEntity tile, IFluidHandler fluidHandler, FluidStack fluidStack)
		{
			super(bucket, player, world, blockPos, tile, fluidStack);
			this._handler = fluidHandler;
		}
		
		/**
		 * @return Returns the TileEntity but as IFluidHandler.
		 */
		public IFluidHandler getFluidHandler()
		{
			return _handler;
		}
		
		/**
		 * This Event is fired when IFluidHandler is filled using {@link CapabilityFluidHandler#FLUID_HANDLER_CAPABILITY}
		 */
		public static class UsingCapability extends FillFluidHandler
		{
			public UsingCapability(IEnchantedBucket bucket, EntityPlayer player, World world, BlockPos blockPos,
					TileEntity tile, IFluidHandler fluidHandler, FluidStack fluidStack) 
			{
				super(bucket, player, world, blockPos, tile, fluidHandler, fluidStack);
			}
		}
	}
	
	/**
	 * This Event is fired when Player used Enchanted Bucket to fill Fluid Tank.
	 */
	public static class FillFluidTank extends EventEnchantedBucket 
	{
		private final IFluidTank _fluidTank;
		
		public FillFluidTank(IEnchantedBucket bucket, EntityPlayer player, World world, BlockPos blockPos,
				TileEntity tile, IFluidTank fluidTank, FluidStack fluidStack) 
		{
			super(bucket, player, world, blockPos, tile, fluidStack);
			this._fluidTank = fluidTank;
		}
		
		/**
		 * @return Returns the TileEntity but as IFluidTank.
		 */
		public IFluidTank getFluidTank()
		{
			return _fluidTank;
		}
	}
	
	/**
	 * This Event is fired when Player used Enchanted Bucket to fill Cauldron.
	 */
	public static class FillCauldron extends EventEnchantedBucket
	{
		public FillCauldron(IEnchantedBucket bucket, EntityPlayer player, World world, BlockPos blockPos) 
		{
			super(bucket, player, world, blockPos, null, null);
		}
	}
	
	/**
	 * This Event is fired when Player used Enchanted Bucket to spawn liquid.
	 */
	public static class SpawnLiquid extends EventEnchantedBucket
	{
		public SpawnLiquid(IEnchantedBucket bucket, EntityPlayer player, World world, BlockPos blockPos) 
		{
			super(bucket, player, world, blockPos, null, null);
		}
	}
}