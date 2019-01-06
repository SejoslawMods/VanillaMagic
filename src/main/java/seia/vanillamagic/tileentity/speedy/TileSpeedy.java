package seia.vanillamagic.tileentity.speedy;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.event.EventSpeedy;
import seia.vanillamagic.api.exception.NotInventoryException;
import seia.vanillamagic.api.inventory.IInventoryWrapper;
import seia.vanillamagic.api.inventory.InventoryWrapper;
import seia.vanillamagic.api.tileentity.speedy.ISpeedy;
import seia.vanillamagic.api.util.Box;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.EventUtil;
import seia.vanillamagic.util.NBTUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileSpeedy extends CustomTileEntity implements ISpeedy {
	public static final String REGISTRY_NAME = TileSpeedy.class.getName();

	/**
	 * How many ticks in one block.
	 */
	protected int ticks;
	/**
	 * Working space box.
	 * 
	 * @see {@link Box}
	 */
	protected Box radiusBox;
	/**
	 * Size of the Speedy.
	 */
	protected int size;

	public void init(World world, BlockPos pos) {
		super.init(world, pos);

		this.ticks = VMConfig.TILE_SPEEDY_TICKS;
		this.size = VMConfig.TILE_SPEEDY_SIZE;

		this.setBox();
	}

	private void setBox() {
		this.radiusBox = new Box(this.pos);

		this.radiusBox.resizeX(this.size);
		this.radiusBox.resizeY(1);
		this.radiusBox.resizeZ(this.size);
	}

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public Box getRadiusBox() {
		return radiusBox;
	}

	public void setRadiusBox(Box radiusBox) {
		this.radiusBox = radiusBox;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public IInventoryWrapper getInventoryWithCrystal() {
		try {
			return new InventoryWrapper(world, getPos().offset(EnumFacing.UP));
		} catch (NotInventoryException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean containsCrystal() {
		IInventory inv = getInventoryWithCrystal().getInventory();

		if (inv != null) {
			for (int i = 0; i < inv.getSizeInventory(); ++i) {
				ItemStack stack = inv.getStackInSlot(i);

				if (VanillaMagicItems.isCustomItem(stack, VanillaMagicItems.ACCELERATION_CRYSTAL)) {
					return true;
				}
			}
		}

		return false;
	}

	public void update() {
		if (this.ticks <= 0) {
			return;
		}

		if (this.containsCrystal()) {
			this.tickNeighbors();
		}

		EventUtil.postEvent(new EventSpeedy((ISpeedy) this, world, pos));
	}

	private void tickNeighbors() {
		for (int x = radiusBox.xMin; x <= radiusBox.xMax; ++x) {
			for (int y = radiusBox.yMin; y <= radiusBox.yMax; ++y) {
				for (int z = radiusBox.zMin; z <= radiusBox.zMax; ++z) {
					this.tickBlock(new BlockPos(x, y, z));
				}
			}
		}
	}

	private void tickBlock(BlockPos pos) {
		if (!world.isAirBlock(pos)) {
			IBlockState blockState = this.world.getBlockState(pos);
			Block block = blockState.getBlock();
			TileEntity tile = world.getTileEntity(pos);
			Random rand = new Random();

			for (int i = 0; i < ticks; ++i) {
				if (tile == null) {
					block.updateTick(world, pos, blockState, rand);
					world.spawnParticle(EnumParticleTypes.END_ROD, pos.getX(), pos.getY(), pos.getZ(), 100, 100, 100,
							new int[] {});
				} else if (tile instanceof ITickable) {
					((ITickable) tile).update();
					world.spawnParticle(EnumParticleTypes.END_ROD, pos.getX(), pos.getY(), pos.getZ(), 100, 100, 100,
							new int[] {});
				}
			}
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger(NBTUtil.NBT_SPEEDY_TICKS, ticks);
		return tag;
	}

	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.ticks = tag.getInteger(NBTUtil.NBT_SPEEDY_TICKS);
	}

	public List<String> getAdditionalInfo() {
		List<String> list = super.getAdditionalInfo();
		list.add("Size: " + size);
		list.add("Number of ticks to boost: " + ticks);
		list.add("Min pos: X=" + radiusBox.xMin + ", Y=" + radiusBox.yMin + ", Z=" + radiusBox.zMin);
		list.add("Max pos: X=" + radiusBox.xMax + ", Y=" + radiusBox.yMax + ", Z=" + radiusBox.zMax);
		return list;
	}
}