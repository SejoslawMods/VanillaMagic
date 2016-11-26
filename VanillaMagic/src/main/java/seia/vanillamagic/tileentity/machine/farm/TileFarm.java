package seia.vanillamagic.tileentity.machine.farm;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.InventoryWrapper;
import seia.vanillamagic.api.tileentity.machine.IFarm;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.tileentity.machine.TileMachine;
import seia.vanillamagic.util.ItemStackHelper;

public class TileFarm extends TileMachine implements IFarm
{
	public static final String REGISTRY_NAME = TileFarm.class.getName();
	public static final GameProfile FARMER_PROFILE = new GameProfile(UUID.fromString("c1ddfd7f-120a-4000-8b64-38660d3ec62d"), "[VanillaMagicFarmer]");
	
	public void init(World world, BlockPos machinePos)
	{
		super.init(world, machinePos);
		if(this.startPos == null)
		{
			this.startPos = new BlockPos(pos.getX() + radius, pos.getY(), pos.getZ() + radius);
		}
		if(chestPosInput == null)
		{
			chestPosInput = pos.offset(EnumFacing.UP);
		}
		if(chestPosOutput == null)
		{
			this.chestPosOutput = pos.offset(EnumFacing.DOWN);
		}
		
		try
		{
			if(inventoryInput == null)
			{
				this.inventoryInput = new InventoryWrapper(world, this.chestPosInput);
			}
			if(inventoryOutput == null)
			{
				inventoryOutput = new InventoryWrapper(world, this.chestPosOutput);
			}
		}
		catch(NotInventoryException e)
		{
			VanillaMagic.LOGGER.log(Level.ERROR, this.getClass().getSimpleName() + 
					" - error when converting to IInventory at position: " + 
					e.position.toString());
		}
	}
	
	public boolean checkSurroundings() 
	{
		try
		{
			if(chestPosInput == null)
			{
				chestPosInput = pos.offset(EnumFacing.UP);
			}
			if(chestPosOutput == null)
			{
				chestPosOutput = pos.offset(EnumFacing.DOWN);
			}
			return (this.world.getTileEntity(chestPosInput) instanceof IInventory) &&
					(this.world.getTileEntity(chestPosOutput) instanceof IInventory);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	boolean shouldDecreaseTicks = false;
	public void doWork() 
	{
		shouldDecreaseTicks = false;
		doTick();
		if(shouldDecreaseTicks)
		{
			decreaseTicks();
		}
	}
	
	public boolean tillBlock(BlockPos plantingLocation) 
	{
		BlockPos dirtLoc = plantingLocation.offset(EnumFacing.DOWN);
		Block dirtBlock = getBlock(dirtLoc);
		if((dirtBlock == Blocks.DIRT || dirtBlock == Blocks.GRASS)) 
		{
			if(!hasHoe()) 
			{
				return false;
			}
			damageHoe(1, dirtLoc);
			world.setBlockState(dirtLoc, Blocks.FARMLAND.getDefaultState());
			world.playSound(dirtLoc.getX() + 0.5F, dirtLoc.getY() + 0.5F, dirtLoc.getZ() + 0.5F, 
					SoundEvents.BLOCK_GRASS_STEP, SoundCategory.BLOCKS,
					(Blocks.FARMLAND.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.FARMLAND.getSoundType().getPitch() * 0.8F, false);
			return true;
		} 
		else if(dirtBlock == Blocks.FARMLAND) 
		{
			return true;
		}
		return false;
	}

	public int getMaxLootingValue() 
	{
		int result = 0;
		IInventory inv = getInputInventory().getInventory();
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null)
			{
				int level = getLooting(stack);
				if(level > result)
				{
					result = level;
				}
			}
		}
		return result;
	}

	public boolean hasHoe() 
	{
		return hasTool(ToolType.HOE);
	}

	public boolean hasAxe() 
	{
		return hasTool(ToolType.AXE);
	}

	public boolean hasShears() 
	{
		return hasTool(ToolType.SHEARS);
	}

	public int getAxeLootingValue() 
	{
		ItemStack tool = getTool(ToolType.AXE);
		if(ItemStackHelper.isNullStack(tool)) 
		{
			return 0;
		}
		return getLooting(tool);
	}
	
	public void damageHoe(int damage, BlockPos pos) 
	{
		damageTool(ToolType.HOE, null, pos, damage);
	}

	public void damageAxe(Block blk, BlockPos pos) 
	{
		damageTool(ToolType.AXE, blk, pos, 1);
	}

	public void damageShears(Block blk, BlockPos pos) 
	{
		damageTool(ToolType.SHEARS, blk, pos, 1);
	}

	public boolean hasTool(ToolType type)
	{
		return getTool(type) != null;
	}

	public ItemStack getTool(ToolType type) 
	{
		int result = 0;
		IInventory inv = getInputInventory().getInventory();
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(!ItemStackHelper.isNullStack(stack))
			{
				if(type.itemMatches(stack) && ItemStackHelper.getStackSize(stack)  > 0 /*&& stack.getItemDamage() > 1*/)
				{
					return stack;
				}
			}
		}
		return null;
	}

	public void damageTool(ToolType type, Block blk, BlockPos pos, int damage) 
	{
		ItemStack tool = getTool(type);
		if(ItemStackHelper.isNullStack(tool)) 
		{
			return;
		}
		IBlockState bs = getBlockState(pos);
		boolean canDamage = canDamage(tool);
		if(type == ToolType.AXE) 
		{
			tool.setItemDamage(tool.getItemDamage() + 1);
		} 
		else if(type == ToolType.HOE) 
		{
			IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();
            if(world.isAirBlock(pos.up()))
            {
                if(block == Blocks.GRASS || block == Blocks.GRASS_PATH)
                {
                    if(!world.isRemote)
                    {
                    	world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                    	tool.setItemDamage(tool.getItemDamage() + 1);
                    }
                }
                if(block == Blocks.DIRT)
                {
                    switch((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
                    {
                        case DIRT:
                            if(!world.isRemote)
                            {
                            	world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                            	tool.setItemDamage(tool.getItemDamage() + 1);
                            }
                        case COARSE_DIRT:
                            if(!world.isRemote)
                            {
                            	world.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), 11);
                            	tool.setItemDamage(tool.getItemDamage() + 1);
                            }
                    }
                }
            }
		} 
		else if(canDamage) 
		{
			tool.setItemDamage(tool.getItemDamage() + 1);
		}
		
		if(ItemStackHelper.getStackSize(tool) == 0 || (canDamage && tool.getItemDamage() >= tool.getMaxDamage())) 
		{
			destroyTool(type);
		}
	}

	private boolean canDamage(ItemStack stack) 
	{
		return !ItemStackHelper.isNullStack(stack) && stack.isItemStackDamageable() && stack.getItem().isDamageable();
	}

	private void destroyTool(ToolType type) 
	{
		IInventory inv = getInputInventory().getInventory();
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(type.itemMatches(stack) && ItemStackHelper.getStackSize(stack) == 0)
			{
				inv.setInventorySlotContents(i, ItemStackHelper.NULL_STACK);
				markDirty();
				return;
			}
		}
	}

	private int getLooting(ItemStack stack) 
	{
		return Math.max(
				EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack),
				EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack));
	}
	
	public Block getBlock(int x, int y, int z) 
	{
		return getBlock(new BlockPos(x, y, z));
	}
	  
	public Block getBlock(BlockPos posIn) 
	{
		return getBlockState(posIn).getBlock();
	}
	  
	public IBlockState getBlockState(BlockPos posIn) 
	{
		return world.getBlockState(posIn);
	}

	public boolean isOpen(BlockPos pos)
	{
		Block block = getBlock(pos);
		IBlockState bs = getBlockState(pos);
		return block.isAir(bs, world, pos) || block.isReplaceable(world, pos);
	}
	
	protected boolean checkProgress(boolean redstoneChecksPassed) 
	{
		if(redstoneChecksPassed) 
		{
			doTick();
		}
		return false;
	}

	protected void doTick()
	{
		workingPos = getNextCoord();
		IBlockState bs = getBlockState(workingPos);
		Block block = bs.getBlock();
		if(isOpen(workingPos)) 
		{
			boolean prepared = FarmersRegistry.INSTANCE.prepareBlock(this, workingPos, block, bs);
			if(prepared)
			{
				shouldDecreaseTicks = true;
			}
			bs = getBlockState(workingPos);
			block = bs.getBlock();
		}
		if(isOutputFull())
		{
			return;
		}
		if(!isOpen(workingPos))
		{
			IHarvestResult harvest = FarmersRegistry.INSTANCE.harvestBlock(this, workingPos, block, bs);
			if(harvest != null && harvest.getDrops() != null) 
			{
				for(EntityItem ei : harvest.getDrops()) 
				{
					if(ei != null) 
					{
						insertHarvestDrop(ei, workingPos);
						//TODO: TileFarm -> Fix if output chest is full. Stop working.
//						if(!ei.isDead) 
//						{
//							worldObj.spawnEntityInWorld(ei);
//						}
					}
				}
				shouldDecreaseTicks = true;
				return;
			}
		}
//		if(hasBonemeal() && bonemealCooldown-- <= 0) 
//		{
//			IInventory inv = getInputInventory().getInventory();
//			for(int i = 0; i < inv.getSizeInventory(); i++)
//			{
//				ItemStack stack = inv.getStackInSlot(i);
//				Fertilizer fertilizer = Fertilizer.getInstance(stack);
//				if((fertilizer.applyOnPlant() != isOpen(workingPos)) || (fertilizer.applyOnAir() == worldObj.isAirBlock(workingPos))) 
//				{
//					farmer.inventory.mainInventory[0] = stack;
//					farmer.inventory.currentItem = 0;
//					if(fertilizer.apply(stack, farmer, worldObj, new BlockPos(workingPos))) 
//					{
//						//stack = farmer.inventory.mainInventory[0];
//						inv.setInventorySlotContents(i, farmer.inventory.mainInventory[0]);
//						if(inv.getStackInSlot(i) != null && inv.getStackInSlot(i).stackSize == 0)//if(stack != null && stack.stackSize == 0) 
//						{
//							inv.setInventorySlotContents(i, null);
//						}
//						bonemealCooldown = 20;
//					} 
//					else 
//					{
//						bonemealCooldown = 5;
//					}
//					farmer.inventory.mainInventory[0] = null;
//				}
//			}
//		}
	}

//	private int bonemealCooldown = 5; // no need to persist this
	  
//	private boolean hasBonemeal() 
//	{
//		IInventory inv = getInputInventory().getInventory();
//		for(int i = 0; i < inv.getSizeInventory(); i++)
//		{
//			ItemStack stack = inv.getStackInSlot(i);
//			if(Fertilizer.getInstance(stack) == Fertilizer.BONEMEAL)
//			{
//				return true;
//			}
//			if(Fertilizer.getInstance(inv.getStackInSlot(i)) != Fertilizer.NONE)
//			{
//				return true;
//			}
//		}
//		return false;
//	}

	private boolean isOutputFull() 
	{
		return !this.inventoryOutputHasSpace();
	}

	public boolean hasSeed(ItemStack seeds) 
	{
		IInventory inv = getInputInventory().getInventory();
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if((!ItemStackHelper.isNullStack(stack)) && (ItemStackHelper.getStackSize(stack) > 1) && (stack.isItemEqual(seeds)))
			{
				return true;
			}
		}
		return false;
	}

	int farmSaplingReserveAmount = 32;
	/*
	 * Returns a fuzzy boolean:
	 * 
	 * <=0 - break no leaves for saplings
	 *  50 - break half the leaves for saplings
	 *  90 - break 90% of the leaves for saplings
	 */
	public int isLowOnSaplings(BlockPos pos) 
	{
		IInventory inv = getInputInventory().getInventory();
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(!ItemStackHelper.isNullStack(stack))
			{
				Block blockSapling = Block.getBlockFromItem(stack.getItem());
				if(blockSapling != null)
				{
					if(blockSapling instanceof BlockSapling)
					{
						return 90 * (farmSaplingReserveAmount - (ItemStackHelper.isNullStack(stack) ? 0 : ItemStackHelper.getStackSize(stack))) / farmSaplingReserveAmount;
					}
				}
			}
		}
		return 0;
	}

	public ItemStack takeSeedFromSupplies(ItemStack stack, BlockPos forBlock) 
	{
		return takeSeedFromSupplies(stack, forBlock, true);
	}

	public ItemStack takeSeedFromSupplies(ItemStack stack, BlockPos forBlock, boolean matchMetadata) 
	{
		if(ItemStackHelper.isNullStack(stack) || forBlock == null) 
		{
			return ItemStackHelper.NULL_STACK;
		}
		IInventory inv = getInputInventory().getInventory();
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack invStack = inv.getStackInSlot(i);
			if(!ItemStackHelper.isNullStack(invStack))
			{
				if(matchMetadata ? invStack.isItemEqual(stack) : invStack.getItem() == stack.getItem())
				{
					if(ItemStackHelper.getStackSize(stack) > 1)
					{
						ItemStack result = stack.copy();
						ItemStackHelper.setStackSize(result, 1);
						stack = stack.copy();
						ItemStackHelper.decreaseStackSize(stack, 1);
						if(ItemStackHelper.getStackSize(stack) == 0)
						{
							stack = ItemStackHelper.NULL_STACK;
						}
						getInputInventory().getInventory().setInventorySlotContents(i, stack);
						return result;
					}
				}
			}
		}
		return ItemStackHelper.NULL_STACK;
	}
	
	private void insertHarvestDrop(Entity entity, BlockPos pos) 
	{
		if(!world.isRemote) 
		{
			if(entity instanceof EntityItem && !entity.isDead) 
			{
				EntityItem item = (EntityItem) entity;
				ItemStack stack = item.getEntityItem().copy();
				int numInserted = insertResult(stack, pos);
				ItemStackHelper.decreaseStackSize(stack, numInserted);
				item.setEntityItemStack(stack);
				if(ItemStackHelper.getStackSize(stack) == 0)
				{
					item.setDead();
				}
			}
		}
	}

	private int insertResult(ItemStack stack, BlockPos pos) 
	{
		ItemStack left = InventoryHelper.putStackInInventoryAllSlots(getOutputInventory().getInventory(), stack, EnumFacing.DOWN);
		if(ItemStackHelper.isNullStack(left))
		{
			return 0;
		}
		return ItemStackHelper.getStackSize(left);
	}

	@Nonnull
	private BlockPos getNextCoord() 
	{
		int size = radius;
		BlockPos loc = getPos();
		if(workingPos == null) 
		{
			workingPos = new BlockPos(loc.getX() - size, loc.getY(), loc.getZ() - size);
			return workingPos;
		}
		int nextX = workingPos.getX() + 1;
		int nextZ = workingPos.getZ();
		if(nextX > loc.getX() + size) 
		{
			nextX = loc.getX() - size;
			nextZ += 1;
			if(nextZ > loc.getZ() + size) 
			{
				nextX = loc.getX() - size;
				nextZ = loc.getZ() - size;
			}
		}
		return workingPos = new BlockPos(nextX, workingPos.getY(), nextZ);
	}
	
	public EnumFacing getOutputFacing() 
	{
		return EnumFacing.DOWN;
	}

	public void clearZeroStackSizeInventorySlots() 
	{
		IInventory inv = getInputInventory().getInventory();
		for(int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(!ItemStackHelper.isNullStack(stack))
			{
				if(ItemStackHelper.getStackSize(stack) <= 0)
				{
					inv.setInventorySlotContents(i, ItemStackHelper.NULL_STACK);
				}
			}
		}
	}
	
	public List<String> getAdditionalInfo()
	{
		List<String> list = super.getAdditionalInfo();
		list.add("Radius: " + radius);
		return list;
	}
}