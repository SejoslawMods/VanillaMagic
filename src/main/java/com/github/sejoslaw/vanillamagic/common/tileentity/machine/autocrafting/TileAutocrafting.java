package com.github.sejoslaw.vanillamagic.common.tileentity.machine.autocrafting;

import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.InventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.IAutocrafting;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.TileMachine;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import com.github.sejoslaw.vanillamagic.core.VMTileEntities;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileAutocrafting extends TileMachine implements IAutocrafting {
	public static final String REGISTRY_NAME = TileAutocrafting.class.getName();

	private ContainerAutocrafting container;
	private int currentlyCraftingSlot = 0;

	private final int defaultCraftingSlot = 0;
	private final int defaultMaxCraftingSlot = 4;

	public TileAutocrafting() {
		super(VMTileEntities.AUTO_CRAFTING);
	}

	public void init() {
		super.init();

		this.oneOperationCost = 100; // 1 Coal = 16 crafting operations ?
		this.startPos = this.pos;
		this.chestPosInput = this.pos.up();
		this.chestPosOutput = this.pos.down(2);

		try {
			this.inventoryInput = new InventoryWrapper(world, chestPosInput);
			this.inventoryOutput = new InventoryWrapper(world, chestPosOutput);
		} catch (NotInventoryException e) {
			VMLogger.log(Level.ERROR, this.getClass().getSimpleName() + " - error when converting to IInventory at position: " + e.position.toString());
		}
	}

	public void initContainer() {
		BlockPos[][] inventoryPosMatrix = QuestAutocrafting.buildInventoryMatrix(getPos());
		IInventory[][] inventoryMatrix = QuestAutocrafting.buildIInventoryMatrix(world, inventoryPosMatrix);
		ItemStack[][] stackMatrix = QuestAutocrafting.buildStackMatrix(inventoryMatrix, this.currentlyCraftingSlot);

		this.container = new ContainerAutocrafting(world, stackMatrix, this);
	}

	public boolean checkSurroundings() {
		return QuestAutocrafting.isConstructionComplete(this.world, this.pos);
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

	public Direction getOutputDirection() {
		return Direction.DOWN;
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

	public CompoundNBT serializeNBT() {
		CompoundNBT tag = super.serializeNBT();
		tag.setInteger(NBTUtil.NBT_CRAFTING_SLOT, this.currentlyCraftingSlot);
		return tag;
	}

	public void deserializeNBT(CompoundNBT compound) {
		super.deserializeNBT(compound);
		this.currentlyCraftingSlot = compound.getInteger(NBTUtil.NBT_CRAFTING_SLOT);
	}
}