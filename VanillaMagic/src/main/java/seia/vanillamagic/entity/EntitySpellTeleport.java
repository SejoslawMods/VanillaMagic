package seia.vanillamagic.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventTeleportEntity;

/**
 * Class which defines the Teleport Spell.
 */
public class EntitySpellTeleport extends EntitySpellUpdate
{
	public EntitySpellTeleport(World worldIn, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ) 
	{
		super(worldIn, caster, accelX, accelY, accelZ);
	}
	
	/**
	 * Run when Entity collide with something.
	 */
	protected void onImpact(RayTraceResult result) 
	{
		EntityLivingBase caster = this.castingEntity;
		if (result.typeOfHit == RayTraceResult.Type.ENTITY)
		{
			if (!MinecraftForge.EVENT_BUS.post(new EventTeleportEntity(caster, new BlockPos(result.entityHit.posX, result.entityHit.posY, result.entityHit.posZ))))
			{
				caster.setPositionAndUpdate(result.entityHit.posX, result.entityHit.posY, result.entityHit.posZ);
				caster.fallDistance = 0.0F;
				return;
			}
		}
		if (result.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			BlockPos resultBlockPos = result.getBlockPos();
			TileEntity resultTileEntity = this.world.getTileEntity(resultBlockPos);
			if (resultTileEntity instanceof TileEntityEndGateway)
			{
				TileEntityEndGateway tileEntityEndGate = (TileEntityEndGateway)resultTileEntity;
				if (caster != null)
				{
					tileEntityEndGate.teleportEntity(caster);
					this.setDead();
					return;
				}
				tileEntityEndGate.teleportEntity(this);
				return;
			}
		}
		
		for (int i = 0; i < 32; ++i)
			this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
		
		if (!this.world.isRemote)
		{
			if (result.entityHit == null)
			{
				BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
				if (this.world.isAirBlock(blockpos))
				{
					if (caster instanceof EntityPlayerMP)
					{
						EntityPlayerMP casterMP = (EntityPlayerMP)caster;
						if (casterMP.connection.getNetworkManager().isChannelOpen() && 
								casterMP.world == this.world && 
								!casterMP.isPlayerSleeping())
						{
							if (caster.isRiding()) caster.dismountRidingEntity();
							
							if (!MinecraftForge.EVENT_BUS.post(new EventTeleportEntity(caster, new BlockPos(this.posX, this.posY + 1, this.posZ))))
							{
								caster.setPositionAndUpdate(this.posX, this.posY + 1, this.posZ);
								caster.fallDistance = 0.0F;
							}
						}
					}
					else if (caster != null)
					{
						if (!MinecraftForge.EVENT_BUS.post(new EventTeleportEntity(caster, new BlockPos(this.posX, this.posY + 1, this.posZ))))
						{
							caster.setPositionAndUpdate(this.posX, this.posY + 1, this.posZ);
							caster.fallDistance = 0.0F;
						}
					}
				}
			}
			this.setDead();
		}
	}
}