package seia.vanillamagic.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import seia.vanillamagic.api.util.VectorWrapper;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.util.BlockPosUtil;

/**
 * Class which describes the Spell which Freeze Water.
 */
public class EntitySpellFreezeLiquid extends EntitySpell
{
	public BlockPos circleCenter;
	
	public final Block replacer;
	public final BlockLiquid[] toReplace;
	public final int circleRadius;
	
	public EntitySpellFreezeLiquid(World world, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ,
			Block replacer, BlockLiquid[] toReplace, int circleRadius) 
	{
		super(world, caster, accelX, accelY, accelZ);
		this.replacer = replacer;
		this.toReplace = toReplace;
		this.circleRadius = circleRadius;
	}
	
	/**
	 * Run when this Entity hit something.
	 */
	protected void onImpact(RayTraceResult result) 
	{
	}
	
	/**
	 * Run when this Entity is in water.
	 */
	public void inWater()
	{
		if (circleCenter == null)
		{
			Vector3D casterLookVec = VectorWrapper.wrap(castingEntity.getLookVec());
			circleCenter = new BlockPos(this.posX - casterLookVec.getX(), this.posY - casterLookVec.getY(), this.posZ - casterLookVec.getZ());
			List<BlockPos> blockPosToReplace = BlockPosUtil.getBlockPos3x3XZAroundCenterWithCenter(circleCenter.getX(), circleCenter.getY(), circleCenter.getZ());
			for (int i = 0; i < blockPosToReplace.size(); ++i)
			{
				BlockPos currentPos = blockPosToReplace.get(i);
				Block blockAtPos = world.getBlockState(currentPos).getBlock();
				if (blockAtPos instanceof BlockLiquid)
				{
					BlockLiquid liquidAtPos = (BlockLiquid) blockAtPos;
					for (int j = 0; j < toReplace.length; ++j)
						if (Block.getIdFromBlock(liquidAtPos) == Block.getIdFromBlock(toReplace[j]))
							world.setBlockState(currentPos, replacer.getDefaultState());
				}
			}
		}
	}
}