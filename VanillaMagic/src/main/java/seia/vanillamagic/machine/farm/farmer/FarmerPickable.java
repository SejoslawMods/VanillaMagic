package seia.vanillamagic.machine.farm.farmer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.machine.farm.HarvestResult;
import seia.vanillamagic.machine.farm.IHarvestResult;
import seia.vanillamagic.machine.farm.TileFarm;

public class FarmerPickable extends FarmerCustomSeed
{
	public FarmerPickable(Block plantedBlock, int plantedBlockMeta, int grownBlockMeta, ItemStack seeds) 
	{
		super(plantedBlock, plantedBlockMeta, grownBlockMeta, seeds);
	}

	public FarmerPickable(Block plantedBlock, int grownBlockMeta, ItemStack seeds) 
	{
		super(plantedBlock, grownBlockMeta, seeds);
	}

	public FarmerPickable(Block plantedBlock, ItemStack seeds) 
	{
		super(plantedBlock, seeds);
	}
	  
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		if(!canHarvest(farm, pos, block, state)) 
		{
			return null;
		}
		if(!farm.hasHoe()) 
		{
			return null;
		}
		EntityPlayerMP player = farm.getFarmer();
		World world = farm.getWorld();        
		player.interactionManager.processRightClickBlock(player, player.worldObj, null, EnumHand.MAIN_HAND, pos, EnumFacing.DOWN, 0, 0, 0);
		List<EntityItem> drops = new ArrayList<EntityItem>();
		ItemStack[] inv = player.inventory.mainInventory;
		for(int slot = 0; slot < inv.length; slot++) 
		{
			ItemStack stack = inv[slot];
			if(stack != null) 
			{
				inv[slot] = null;        
				EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
				drops.add(entityitem);
			}
		}
		farm.damageHoe(1, pos);
		return new HarvestResult(drops, pos);
	}
}