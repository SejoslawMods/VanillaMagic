package seia.vanillamagic.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntitySpellTeleport extends EntitySpell
{
	public EntitySpellTeleport(World worldIn, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ) 
	{
		super(worldIn, caster, accelX, accelY, accelZ);
	}

	@Override
	protected void onImpact(RayTraceResult result) 
	{
		EntityLivingBase caster = this.castingEntity;
        if (result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos resultBlockPos = result.getBlockPos();
            TileEntity resultTileEntity = this.worldObj.getTileEntity(resultBlockPos);
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
        {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
        }
        if (!this.worldObj.isRemote)
        {
        	if(result.entityHit == null)
        	{
        		BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
        		if (this.worldObj.isAirBlock(blockpos))
                {
        			if (caster instanceof EntityPlayerMP)
        			{
        				EntityPlayerMP casterMP = (EntityPlayerMP)caster;
        				if (casterMP.connection.getNetworkManager().isChannelOpen() && 
        						casterMP.worldObj == this.worldObj && 
        						!casterMP.isPlayerSleeping())
        				{
        					if (caster.isRiding())
                        	{
                        		caster.dismountRidingEntity();
                        	}
                			caster.setPositionAndUpdate(this.posX, this.posY + 1, this.posZ);
                        	caster.fallDistance = 0.0F;
        				}
        			}
        			else if(caster != null)
        			{
        				caster.setPositionAndUpdate(this.posX, this.posY + 1, this.posZ);
                    	caster.fallDistance = 0.0F;
        			}
                }
        	}
        	this.setDead();
        }
	}
	
	/**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        EntityLivingBase caster = this.castingEntity;
        if (caster != null && caster instanceof EntityPlayer && !caster.isEntityAlive())
        {
            this.setDead();
        }
        else
        {
            super.onUpdate();
        }
    }
}