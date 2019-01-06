package seia.vanillamagic.tileentity.machine.autocrafting;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.event.EventAutocrafting;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.InventoryWrapper;
import seia.vanillamagic.api.tileentity.machine.IAutocrafting;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.tileentity.machine.TileMachine;
import seia.vanillamagic.util.BlockPosUtil;
import seia.vanillamagic.util.EventUtil;
import seia.vanillamagic.util.NBTUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileAutocrafting extends TileMachine implements IAutocrafting {
	public static final String REGISTRY_NAME = TileAutocrafting.class.getName();

	private ContainerAutocrafting container;
	private int currentlyCraftingSlot = 0;

	private final int defaultCraftingSlot = 0;
	private final int defaultMaxCraftingSlot = 4;

	public void init(World world, BlockPos machinePos) {
		super.init(world, machinePos);
		this.oneOperationCost = 100; // 1 Coal = 16 crafting operations ?
		this.startPos = BlockPosUtil.copyPos(getMachinePos());
		this.chestPosInput = getMachinePos().up();
		this.chestPosOutput = getMachinePos().down(2);

		try {
			this.inventoryInput = new InventoryWrapper(world, chestPosInput);
			this.inventoryOutput = new InventoryWrapper(world, chestPosOutput);
		} catch (NotInventoryException e) {
			VanillaMagic.log(Level.ERROR, this.getClass().getSimpleName()
					+ " - error when converting to IInventory at position: " + e.position.toString());
		}
	}

	public void initContainer() {
		BlockPos[][] inventoryPosMatrix = QuestAutocrafting.buildInventoryMatrix(getPos());
		IInventory[][] inventoryMatrix = QuestAutocrafting.buildIInventoryMatrix(world, inventoryPosMatrix);
		ItemStack[][] stackMatrix = QuestAutocrafting.buildStackMatrix(inventoryMatrix, this.currentlyCraftingSlot);

		this.container = new ContainerAutocrafting(world, stackMatrix, this);
	}

	public boolean checkSurroundings() {
		return QuestAutocrafting.isConstructionComplete(this.world, getMachinePos());
	}

	public void doWork() {
		if (inventoryOutputHasSpace()) {
			initContainer();

			for (int i = 0; i < 4; ++i) {
				boolean crafted = this.container.craft();

				if (crafted) {
					this.container.outputResult(this.inventoryOutput.getInventory());
					this.container.removeStacks(QuestAutocrafting.buildIInventoryMatrix(world,
							QuestAutocrafting.buildInventoryMatrix(getMachinePos())));
					return;
				}

				this.container.rotateMatrix();
			}

			decreaseTicks();
		}

		EventUtil.postEvent(new EventAutocrafting.Work((IAutocrafting) this, world, pos));
	}

	public EnumFacing getOutputFacing() {
		return EnumFacing.DOWN;
	}

	/**
	 * @see IAutocrafting#setCurrentlyCraftingSlot(int)
	 */
	public void setCurrentCraftingSlot(int slot) {
		this.currentlyCraftingSlot = slot;
	}

	/**
	 * @see IAutocrafting#getCurrentlyCraftingSlot()
	 */
	public int getCurrentCraftingSlot() {
		return this.currentlyCraftingSlot;
	}

	/**
	 * @see IAutocrafting#getDefaultCraftingSlot()
	 */
	public int getDefaultCraftingSlot() {
		return this.defaultCraftingSlot;
	}

	/**
	 * @see IAutocrafting#getDefaultMaxCraftingSlot()
	 */
	public int getDefaultMaxCraftingSlot() {
		return this.defaultMaxCraftingSlot;
	}

	public List<String> getAdditionalInfo() {
		List<String> info = super.getAdditionalInfo();
		info.add("Currently selected slot for crafting: " + this.currentlyCraftingSlot);
		return info;
	}

	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = super.serializeNBT();
		tag.setInteger(NBTUtil.NBT_CRAFTING_SLOT, this.currentlyCraftingSlot);
		return tag;
	}

	public void deserializeNBT(NBTTagCompound compound) {
		super.deserializeNBT(compound);
		this.currentlyCraftingSlot = compound.getInteger(NBTUtil.NBT_CRAFTING_SLOT);
	}
}