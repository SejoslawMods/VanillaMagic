package seia.vanillamagic.tileentity.machine.farm;

import java.util.List;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Level;

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
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventFarm;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.InventoryWrapper;
import seia.vanillamagic.api.tileentity.machine.IFarm;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.tileentity.machine.TileMachine;
import seia.vanillamagic.util.ItemStackUtil;

public class TileFarm extends TileMachine implements IFarm
{
	public static final String REGISTRY_NAME = TileFarm.class.getName();
	
	public void init(World world, BlockPos machinePos)
	{
		super.init(world, machinePos);
		if (this.startPos == null) this.startPos = new BlockPos(pos.getX() + radius, pos.getY(), pos.getZ() + radius);
		if (chestPosInput == null) chestPosInput = pos.offset(EnumFacing.UP);
		if (chestPosOutput == null) this.chestPosOutput = pos.offset(EnumFacing.DOWN);
		
		try
		{
			if (inventoryInput == null) this.inventoryInput = new InventoryWrapper(world, this.chestPosInput);
			if (inventoryOutput == null) inventoryOutput = new InventoryWrapper(world, this.chestPosOutput);
		}
		catch (NotInventoryException e)
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
			if (chestPosInput == null) chestPosInput = pos.offset(EnumFacing.UP);
			if (chestPosOutput == null) chestPosOutput = pos.offset(EnumFacing.DOWN);
			
			return (this.world.getTileEntity(chestPosInput) instanceof IInventory) &&
					(this.world.getTileEntity(chestPosOutput) instanceof IInventory);
		}
		catch (Exception e)
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
		if (shouldDecreaseTicks) decreaseTicks();
		MinecraftForge.EVENT_BUS.post(new EventFarm.Work((IFarm) this, world, pos));
	}
	
	public boolean tillBlock(BlockPos plantingLocation) 
	{
		BlockPos dirtLoc = plantingLocation.offset(EnumFacing.DOWN);
		Block dirtBlock = getBlock(dirtLoc);
		if ((dirtBlock == Blocks.DIRT || dirtBlock == Blocks.GRASS)) 
		{
			if (!hasHoe()) return false;
			
			damageHoe(1, dirtLoc);
			world.setBlockState(dirtLoc, Blocks.FARMLAND.getDefaultState());
			world.playSound(dirtLoc.getX() + 0.5F, dirtLoc.getY() + 0.5F, dirtLoc.getZ() + 0.5F, 
					SoundEvents.BLOCK_GRASS_STEP, SoundCategory.BLOCKS,
					(Blocks.FARMLAND.getSoundType().getVolume() + 1.0F) / 2.0F, Blocks.FARMLAND.getSoundType().getPitch() * 0.8F, false);
			return true;
		} 
		else if (dirtBlock == Blocks.FARMLAND) return true;
		return false;
	}

	public int getMaxLootingValue() 
	{
		int result = 0;
		IInventory inv = getInputInventory().getInventory();
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null)
			{
				int level = getLooting(stack);
				if (level > result) result = level;
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
		if (ItemStackUtil.isNullStack(tool)) return 0;
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
		IInventory inv = getInputInventory().getInventory();
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (!ItemStackUtil.isNullStack(stack))
				if (type.itemMatches(stack) && ItemStackUtil.getStackSize(stack)  > 0)
					return stack;
		}
		return null;
	}

	public void damageTool(ToolType type, Block blk, BlockPos pos, int damage) 
	{
		ItemStack tool = getTool(type);
		if (ItemStackUtil.isNullStack(tool)) return;
		
		boolean canDamage = canDamage(tool);
		if (type == ToolType.AXE) tool.setItemDamage(tool.getItemDamage() + 1);
		else if (type == ToolType.HOE) 
		{
			IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();
            if (world.isAirBlock(pos.up()))
            {
                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH)
                {
                    if (!world.isRemote)
                    {
                    	world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                    	tool.setItemDamage(tool.getItemDamage() + 1);
                    }
                }
                if (block == Blocks.DIRT)
                {
                    switch((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
                    {
                        case DIRT:
                            if (!world.isRemote)
                            {
                            	world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                            	tool.setItemDamage(tool.getItemDamage() + 1);
                            }
                        case COARSE_DIRT:
                            if (!world.isRemote)
                            {
                            	world.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), 11);
                            	tool.setItemDamage(tool.getItemDamage() + 1);
                            }
					case PODZOL:
						break;
					default:
						break;
                    }
                }
            }
		} 
		else if (canDamage) tool.setItemDamage(tool.getItemDamage() + 1);
		
		if (ItemStackUtil.getStackSize(tool) == 0 || (canDamage && tool.getItemDamage() >= tool.getMaxDamage())) 
			destroyTool(type);
	}

	private boolean canDamage(ItemStack stack) 
	{
		return !ItemStackUtil.isNullStack(stack) && stack.isItemStackDamageable() && stack.getItem().isDamageable();
	}

	private void destroyTool(ToolType type) 
	{
		IInventory inv = getInputInventory().getInventory();
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (type.itemMatches(stack) && ItemStackUtil.getStackSize(stack) == 0)
			{
				inv.setInventorySlotContents(i, ItemStackUtil.NULL_STACK);
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
		if (redstoneChecksPassed) doTick();
		return false;
	}

	protected void doTick()
	{
		workingPos = getNextCoord();
		IBlockState bs = getBlockState(workingPos);
		Block block = bs.getBlock();
		
		if (isOpen(workingPos)) 
		{
			boolean prepared = FarmersRegistry.prepareBlock(this, workingPos, block, bs);
			if (prepared) shouldDecreaseTicks = true;
			bs = getBlockState(workingPos);
			block = bs.getBlock();
		}
		
		if (isOutputFull()) return;
		
		if (!isOpen(workingPos))
		{
			IHarvestResult harvest = FarmersRegistry.harvestBlock(this, workingPos, block, bs);
			if (harvest != null && harvest.getDrops() != null) 
			{
				for(EntityItem ei : harvest.getDrops()) 
					if (ei != null) 
						insertHarvestDrop(ei, workingPos);
				shouldDecreaseTicks = true;
				return;
			}
		}
	}
	
	private boolean isOutputFull() 
	{
		return !this.inventoryOutputHasSpace();
	}

	public boolean hasSeed(ItemStack seeds) 
	{
		IInventory inv = getInputInventory().getInventory();
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if ((!ItemStackUtil.isNullStack(stack)) && (ItemStackUtil.getStackSize(stack) > 1) && (stack.isItemEqual(seeds))) return true;
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
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (!ItemStackUtil.isNullStack(stack))
			{
				Block blockSapling = Block.getBlockFromItem(stack.getItem());
				if (blockSapling != null)
					if (blockSapling instanceof BlockSapling)
						return 90 * (farmSaplingReserveAmount - (ItemStackUtil.isNullStack(stack) ? 0 : ItemStackUtil.getStackSize(stack))) / farmSaplingReserveAmount;
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
		if (ItemStackUtil.isNullStack(stack) || forBlock == null) return ItemStackUtil.NULL_STACK;
		
		IInventory inv = getInputInventory().getInventory();
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack invStack = inv.getStackInSlot(i);
			if (!ItemStackUtil.isNullStack(invStack))
			{
				if (matchMetadata ? invStack.isItemEqual(stack) : invStack.getItem() == stack.getItem())
				{
					if (ItemStackUtil.getStackSize(stack) > 1)
					{
						ItemStack result = stack.copy();
						ItemStackUtil.setStackSize(result, 1);
						stack = stack.copy();
						ItemStackUtil.decreaseStackSize(stack, 1);
						if (ItemStackUtil.getStackSize(stack) == 0) stack = ItemStackUtil.NULL_STACK;
						getInputInventory().getInventory().setInventorySlotContents(i, stack);
						return result;
					}
				}
			}
		}
		return ItemStackUtil.NULL_STACK;
	}
	
	private void insertHarvestDrop(Entity entity, BlockPos pos) 
	{
		if (!world.isRemote) 
		{
			if (entity instanceof EntityItem && !entity.isDead) 
			{
				EntityItem item = (EntityItem) entity;
				ItemStack stack = item.getItem().copy();
				int numInserted = insertResult(stack, pos);
				ItemStackUtil.decreaseStackSize(stack, numInserted);
				item.setItem(stack);
				if (ItemStackUtil.getStackSize(stack) == 0) item.setDead();
			}
		}
	}

	private int insertResult(ItemStack stack, BlockPos pos) 
	{
		ItemStack left = InventoryHelper.putStackInInventoryAllSlots(getOutputInventory().getInventory(), stack, EnumFacing.DOWN);
		if (ItemStackUtil.isNullStack(left)) return 0;
		return ItemStackUtil.getStackSize(left);
	}

	@Nonnull
	private BlockPos getNextCoord() 
	{
		int size = radius;
		BlockPos loc = getPos();
		if (workingPos == null) 
		{
			workingPos = new BlockPos(loc.getX() - size, loc.getY(), loc.getZ() - size);
			return workingPos;
		}
		int nextX = workingPos.getX() + 1;
		int nextZ = workingPos.getZ();
		if (nextX > loc.getX() + size) 
		{
			nextX = loc.getX() - size;
			nextZ += 1;
			if (nextZ > loc.getZ() + size) 
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
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if (!ItemStackUtil.isNullStack(stack))
				if (ItemStackUtil.getStackSize(stack) <= 0)
					inv.setInventorySlotContents(i, ItemStackUtil.NULL_STACK);
		}
	}
	
	public List<String> getAdditionalInfo()
	{
		List<String> list = super.getAdditionalInfo();
		list.add("Radius: " + radius);
		return list;
	}
}