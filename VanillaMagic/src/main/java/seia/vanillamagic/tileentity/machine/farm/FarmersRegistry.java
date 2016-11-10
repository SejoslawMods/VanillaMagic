package seia.vanillamagic.tileentity.machine.farm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerCocoa;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerCustomSeed;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerFlowerPicker;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerMelon;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerNetherWart;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerOreDictionaryTree;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerPlantable;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerStem;
import seia.vanillamagic.tileentity.machine.farm.farmer.FarmerTree;
import seia.vanillamagic.tileentity.machine.farm.farmer.IFarmer;

public class FarmersRegistry implements IFarmer
{
	public static final FarmersRegistry INSTANCE = new FarmersRegistry();
	
	//====================================================================
	
	private List<IFarmer> farmers;
	private List<ItemStack> saplings = OreDictionary.getOres("treeSapling");
	private List<ItemStack> woods = OreDictionary.getOres("logWood");
	private List<ItemStack> flowers = new ArrayList<ItemStack>();
	
	public final FarmerPlantable DEFAULT_FARMER = new FarmerPlantable();
	
	private FarmersRegistry()
	{
		flowers.add(new ItemStack(Blocks.YELLOW_FLOWER));
		flowers.add(new ItemStack(Blocks.RED_FLOWER));
		
		farmers = new ArrayList<IFarmer>();
		farmers.add(new FarmerFlowerPicker(flowers));
		farmers.add(new FarmerStem(Blocks.REEDS, new ItemStack(Items.REEDS)));
	    farmers.add(new FarmerStem(Blocks.CACTUS, new ItemStack(Blocks.CACTUS)));
	    farmers.add(new FarmerOreDictionaryTree(saplings, woods));
	    farmers.add(new FarmerTree(true, Blocks.RED_MUSHROOM, Blocks.RED_MUSHROOM_BLOCK));
	    farmers.add(new FarmerTree(true, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK));
	    //special case of plantables to get spacing correct
	    farmers.add(new FarmerMelon(Blocks.MELON_STEM, Blocks.MELON_BLOCK, new ItemStack(Items.MELON_SEEDS)));
	    farmers.add(new FarmerMelon(Blocks.PUMPKIN_STEM, Blocks.PUMPKIN, new ItemStack(Items.PUMPKIN_SEEDS)));
	    //'BlockNetherWart' is not an IGrowable
	    farmers.add(new FarmerNetherWart());
	    //Cocoa is odd
	    farmers.add(new FarmerCocoa());
	    //Handles all 'Vanilla' style crops
	    farmers.add(DEFAULT_FARMER);
	}
	
	public void addFarmer(IFarmer farmer)
	{
		farmers.add(farmer);
	}
	
	public boolean canHarvest(TileFarm farm,  BlockPos pos, Block block, IBlockState meta) 
	{
		for(IFarmer farmer : farmers) 
		{
			if(farmer.canHarvest(farm, pos, block, meta)) 
			{
				return true;
			}
		}
		return false;
	}

	@Nullable
	public IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) 
	{
		for(IFarmer farmer : farmers) 
		{
			if(farmer.canHarvest(farm, pos, block, meta))
			{
				return farmer.harvestBlock(farm, pos, block, meta);
			}
		}
		return null;
	}

	public boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState meta) 
	{
		for(IFarmer farmer : farmers) 
		{
			if(farmer.prepareBlock(farm, pos, block, meta))
			{
				return true;
			}
		}
		return false;
	}

	public boolean canPlant(ItemStack stack) 
	{
		for(IFarmer farmer : farmers) 
		{
			if(farmer.canPlant(stack)) 
			{
				return true;
			}
		}
		return false;
	}

	@Nullable
	public FarmerCustomSeed addSeed(String mod, String blockName, String itemName, Block... extraFarmland) 
	{
		if(Block.REGISTRY.containsKey(new ResourceLocation(mod, blockName))) 
		{
			Block cropBlock = Block.REGISTRY.getObject(new ResourceLocation(mod, blockName));
			Item seedItem = Item.REGISTRY.getObject(new ResourceLocation(mod, itemName));
			if(seedItem != null) 
			{
				FarmerCustomSeed farmer = new FarmerCustomSeed(cropBlock, new ItemStack(seedItem));
				if(extraFarmland != null) 
				{
					for(Block farmland : extraFarmland) 
					{
						if(farmland != null) 
						{
							farmer.addTilledBlock(farmland);
						}
					}
				}
				addFarmer(farmer);
				return farmer;
			}
		}
		return null;
	}
}