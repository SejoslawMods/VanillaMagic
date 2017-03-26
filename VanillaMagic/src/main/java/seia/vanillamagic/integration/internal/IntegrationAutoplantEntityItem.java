package seia.vanillamagic.integration.internal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChorusPlant;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.event.EventAutoplant;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.integration.IIntegration;
import seia.vanillamagic.util.ItemStackHelper;

public class IntegrationAutoplantEntityItem implements IIntegration
{
	public String getModName() 
	{
		return "VM Autoplant";
	}
	
	public void preInit() throws Exception
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void tryToPlant(ItemExpireEvent event) // Plant anything that in theory is a seed or should be able to be plant.
	{
		if(!VMConfig.itemCanAutoplant)
		{
			return;
		}
		EntityItem entityItem = event.getEntityItem();
		ItemStack itemStack = entityItem.getEntityItem();
		if(itemStack != null && !ItemStackHelper.isNullStack(itemStack))
		{
			Item itemToExpire = itemStack.getItem();
			Block block = Block.getBlockFromItem(itemToExpire);
			World world = entityItem.world;
			BlockPos plantPosition = entityItem.getPosition();
			if(block.canPlaceBlockAt(world, plantPosition) && block != Blocks.AIR) // Anything Plantable or Growable
			{
				if(block instanceof IGrowable)
				{
					if(!MinecraftForge.EVENT_BUS.post(new EventAutoplant(entityItem, world, plantPosition)))
					{
						IBlockState plantState = block.getStateFromMeta(itemToExpire.getMetadata(itemStack));
						world.setBlockState(plantPosition, plantState);
					}
				}
				else if(block instanceof IPlantable)
				{
					if(!MinecraftForge.EVENT_BUS.post(new EventAutoplant(entityItem, world, plantPosition)))
					{
						IBlockState plantState = ((IPlantable) block).getPlant(world, plantPosition);
						world.setBlockState(plantPosition, plantState);
					}
				}
				else if(itemToExpire instanceof IPlantable)
				{
					if(!MinecraftForge.EVENT_BUS.post(new EventAutoplant(entityItem, world, plantPosition)))
					{
						IBlockState plantState = ((IPlantable) itemToExpire).getPlant(world, plantPosition);
						world.setBlockState(plantPosition, plantState);
					}
				}
				else if(block instanceof BlockChorusPlant) // Chorus Plant
				{
					IBlockState endStoneState = world.getBlockState(plantPosition.offset(EnumFacing.DOWN));
					Block endStoneBlock = endStoneState.getBlock();
					if(Block.isEqualTo(endStoneBlock, Blocks.END_STONE))
					{
						if(!MinecraftForge.EVENT_BUS.post(new EventAutoplant(entityItem, world, plantPosition)))
						{
							world.setBlockState(plantPosition, Blocks.CHORUS_PLANT.getDefaultState());
						}
					}
				}
			}
			else if(itemToExpire instanceof ItemDye && EnumDyeColor.byDyeDamage(itemStack.getMetadata()) == EnumDyeColor.BROWN) // Cocoa
			{
				for(EnumFacing facing : EnumFacing.HORIZONTALS)
				{
					BlockPos woodPos = plantPosition.offset(facing);
					IBlockState woodState = world.getBlockState(woodPos);
					Block woodBlock = woodState.getBlock();
					if(woodBlock == Blocks.LOG 
							&& woodState.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE)
	                {
						if(world.isAirBlock(plantPosition))
						{
							if(!MinecraftForge.EVENT_BUS.post(new EventAutoplant(entityItem, world, plantPosition)))
							{
								world.setBlockState(plantPosition, Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.FACING, facing));
								return;
							}
						}
	                }
				}
			}
		}
	}
}