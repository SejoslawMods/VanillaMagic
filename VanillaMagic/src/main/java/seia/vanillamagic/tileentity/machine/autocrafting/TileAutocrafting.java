package seia.vanillamagic.tileentity.machine.autocrafting;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventAutocrafting;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.InventoryWrapper;
import seia.vanillamagic.api.tileentity.machine.IAutocrafting;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.tileentity.machine.TileMachine;
import seia.vanillamagic.util.BlockPosHelper;
import seia.vanillamagic.util.NBTHelper;

public class TileAutocrafting extends TileMachine implements IAutocrafting
{
	public static final String REGISTRY_NAME = TileAutocrafting.class.getName();
	
	private ContainerAutocrafting _container;
	private int _currentlyCraftingSlot = 0;
	
	private final int _defaultCraftingSlot = 0;
	private final int _defaultMaxCraftingSlot = 4;
	
	public void init(World world, BlockPos machinePos)
	{
		super.init(world, machinePos);
		this.oneOperationCost = 100; // 1 Coal = 16 crafting operations ?
		this.startPos = BlockPosHelper.copyPos(getMachinePos());
		this.chestPosInput = getMachinePos().up();
		this.chestPosOutput = getMachinePos().down(2);
		try
		{
			this.inventoryInput = new InventoryWrapper(world, chestPosInput);
			this.inventoryOutput = new InventoryWrapper(world, chestPosOutput);
		}
		catch(NotInventoryException e)
		{
			VanillaMagic.LOGGER.log(Level.ERROR, this.getClass().getSimpleName() + " - error when converting to IInventory at position: " + e.position.toString());
		}
	}
	
	public void initContainer()
	{
		BlockPos[][] inventoryPosMatrix = QuestAutocrafting.buildInventoryMatrix(getPos());
		IInventory[][] inventoryMatrix = QuestAutocrafting.buildIInventoryMatrix(world, inventoryPosMatrix);
		ItemStack[][] stackMatrix = QuestAutocrafting.buildStackMatrix(inventoryMatrix, _currentlyCraftingSlot);
		_container = new ContainerAutocrafting(world, stackMatrix, this);
	}
	
	public boolean checkSurroundings() 
	{
		return QuestAutocrafting.isConstructionComplete(this.world, getMachinePos());
	}
	
	public void doWork() 
	{
		if(inventoryOutputHasSpace())
		{
			initContainer();
			for(int i = 0; i < 4; ++i)
			{
				boolean crafted = _container.craft();
				if(crafted)
				{
					//container.outputResult(getHopperForStructure());
					_container.outputResult(this.inventoryOutput.getInventory());
					_container.removeStacks(
							QuestAutocrafting.buildIInventoryMatrix(world, 
									QuestAutocrafting.buildInventoryMatrix(getMachinePos())));
					return;
				}
				_container.rotateMatrix();
			}
			decreaseTicks();
		}
		MinecraftForge.EVENT_BUS.post(new EventAutocrafting.Work((IAutocrafting) this, world, pos));
	}
	
	public EnumFacing getOutputFacing() 
	{
		return EnumFacing.DOWN;
	}
	
	/**
	 * @see IAutocrafting#setCurrentlyCraftingSlot(int)
	 */
	public void setCurrentCraftingSlot(int slot)
	{
		this._currentlyCraftingSlot = slot;
	}
	
	/**
	 * @see IAutocrafting#getCurrentlyCraftingSlot()
	 */
	public int getCurrentCraftingSlot()
	{
		return _currentlyCraftingSlot;
	}
	
	/**
	 * @see IAutocrafting#getDefaultCraftingSlot()
	 */
	public int getDefaultCraftingSlot()
	{
		return _defaultCraftingSlot;
	}
	
	/**
	 * @see IAutocrafting#getDefaultMaxCraftingSlot()
	 */
	public int getDefaultMaxCraftingSlot()
	{
		return _defaultMaxCraftingSlot;
	}
	
	public List<String> getAdditionalInfo()
	{
		List<String> info = super.getAdditionalInfo();
		info.add("Currently selected slot for crafting: " + _currentlyCraftingSlot);
		return info;
	}
	
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = super.serializeNBT();
		tag.setInteger(NBTHelper.NBT_CRAFTING_SLOT, _currentlyCraftingSlot);
		return tag;
	}
	
	public void deserializeNBT(NBTTagCompound compound)
	{
		super.deserializeNBT(compound);
		this._currentlyCraftingSlot = compound.getInteger(NBTHelper.NBT_CRAFTING_SLOT);
	}
}