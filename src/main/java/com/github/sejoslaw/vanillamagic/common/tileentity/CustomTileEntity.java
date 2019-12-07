package com.github.sejoslaw.vanillamagic.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import com.github.sejoslaw.vanillamagic.api.tileentity.CustomTileEntityBase;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.github.sejoslaw.vanillamagic.util.NBTUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class CustomTileEntity extends CustomTileEntityBase {
	public void validate() {
		super.validate();

		if ((!this.world.isRemote) && (this.chunkTicket == null)) {
			Ticket ticket = ForgeChunkManager.requestTicket(VanillaMagic.INSTANCE, this.world,
					ForgeChunkManager.Type.NORMAL);

			if (ticket != null) {
				forceChunkLoading(ticket);
			}
		}
	}

	/**
	 * Try to override {@link #deserializeNBT(CompoundNBT)} instead of this
	 * method.
	 */
	@Deprecated
	public void readFromNBT(CompoundNBT tag) {
		super.readFromNBT(tag);
		NBTUtil.readFromINBTSerializable(this, tag);
	}

	public void deserializeNBT(CompoundNBT tag) {
	}

	/**
	 * Try to override {@link #serializeNBT()} instead of this method.
	 */
	@Deprecated
	public CompoundNBT writeToNBT(CompoundNBT tag) {
		super.writeToNBT(tag);
		tag = NBTUtil.writeToINBTSerializable(this, tag);
		tag.putString(NBTUtil.NBT_CLASS, this.getClass().getName());
		return tag;
	}

	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		return tag;
	}

	public SPacketUpdateTileEntity getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(getPos(), -999, nbt);
	}

	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readFromNBT(pkt.getNbtCompound());
	}

	public CompoundNBT getUpdateTag() {
		return writeToNBT(new CompoundNBT());
	}
}