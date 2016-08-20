package seia.vanillamagic.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class CustomTileEntity extends TileEntity
{
	public void init(World world, BlockPos pos)
	{
		this.worldObj = world;
		this.pos = pos;
	}
}