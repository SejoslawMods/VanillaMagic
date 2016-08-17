package seia.vanillamagic.machine.autocrafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.machine.TileMachine;

public class TileAutocrafting extends TileMachine
{
	public static final String REGISTRY_NAME = TileAutocrafting.class.getSimpleName();
	
	public ContainerAutocrafting container;
	
	public void addContainerAutocrafting(ContainerAutocrafting container)
	{
		this.container = container;
	}
	
	public IInventory getInputInventory() 
	{
		return ((IInventory) worldObj.getTileEntity(getMachinePos().up()));
	}
	
	public IInventory getOutputInventory() 
	{
		return ((IInventory) worldObj.getTileEntity(getMachinePos().down(4)));
	}
	
	public boolean checkSurroundings() 
	{
		return QuestAutocrafting.isConstructionComplete(this.worldObj, getMachinePos());
	}
	
	public void doWork() 
	{
		if(inventoryOutputHasSpace())
		{
			for(int i = 0; i < 4; i++)
			{
				boolean crafted = container.craft();
				if(crafted)
				{
					container.outputResult(getHopperForStructure());
					container.removeStacks(
							QuestAutocrafting.buildIInventoryMatrix(worldObj, 
									QuestAutocrafting.buildInventoryMatrix(getMachinePos())));
					return;
				}
				container.rotateMatrix();
			}
		}
	}
	
	public EnumFacing getOutputFacing() 
	{
		return EnumFacing.DOWN;
	}
	
	public IHopper getHopperForStructure()
	{
		BlockPos hopperPos = getMachinePos().down(4);
		TileEntity hopperTile = worldObj.getTileEntity(hopperPos);
		if(hopperTile instanceof IHopper)
		{
			return (IHopper) hopperTile;
		}
		return null;
	}
}