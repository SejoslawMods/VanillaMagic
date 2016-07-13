package seia.vanillamagic.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntitySpell extends Entity
{
	private int xTile = 0;
    private int yTile = 0;
    private int zTile = 0;
    private Block inTile;
    private boolean inGround;
    public EntityLivingBase castingEntity;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;

    public EntitySpell(World world, EntityLivingBase caster, 
    		double accelX, double accelY, double accelZ)
    {
        super(world);
        this.castingEntity = caster;
        this.setSize(1.0F, 1.0F);
        this.setLocationAndAngles(
        		caster.posX + accelX, // in front of face = accelX
        		caster.posY + 1.5D + accelY, // in front of face = accelY
        		caster.posZ + accelZ, // in front of face = accelZ
        		caster.rotationYaw, 
        		caster.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        double d0 = (double)MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.accelerationX = accelX / d0 * 0.1D;
        this.accelerationY = accelY / d0 * 0.1D;
        this.accelerationZ = accelZ / d0 * 0.1D;
    }
	
	protected void entityInit()
    {
    }
	
	/**
     * Checks if the entity is in range to render.
     */
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
        if (Double.isNaN(d0))
        {
            d0 = 4.0D;
        }
        d0 = d0 * 64.0D;
        return distance < d0 * d0;
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.worldObj.isRemote || (this.castingEntity == null || !this.castingEntity.isDead) && this.worldObj.isBlockLoaded(new BlockPos(this)))
        {
            super.onUpdate();
            if (this.inGround)
            {
                if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
                {
                    ++this.ticksAlive;
                    if (this.ticksAlive == 600)
                    {
                        this.setDead();
                    }
                    return;
                }
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksAlive = 0;
                this.ticksInAir = 0;
            }
            else
            {
                ++this.ticksInAir;
            }
            RayTraceResult rayTraceResult = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, this.castingEntity);
            if (rayTraceResult != null)
            {
            	this.onImpact(rayTraceResult);
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            float motionFactor = this.getMotionFactor();
            if (this.isInWater())
            {
                for (int i = 0; i < 4; ++i)
                {
                    float f1 = 0.25F;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                motionFactor = 0.8F;
            }
            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double)motionFactor;
            this.motionY *= (double)motionFactor;
            this.motionZ *= (double)motionFactor;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
        else
        {
            this.setDead();
        }
    }
    
    /**
     * Return the motion factor for this projectile. The factor is multiplied by the original motion.
     */
    protected float getMotionFactor()
    {
        return 0.95F;
    }
    
    /**
     * Called when this EntitySpell hits a block or entity.
     */
    protected abstract void onImpact(RayTraceResult result);
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("xTile", this.xTile);
        compound.setInteger("yTile", this.yTile);
        compound.setInteger("zTile", this.zTile);
        ResourceLocation resourceLocation = (ResourceLocation)Block.REGISTRY.getNameForObject(this.inTile);
        compound.setString("inTile", resourceLocation == null ? "" : resourceLocation.toString());
        compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        compound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
        compound.setTag("power", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
        compound.setInteger("life", this.ticksAlive);
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        this.xTile = compound.getInteger("xTile");
        this.yTile = compound.getInteger("yTile");
        this.zTile = compound.getInteger("zTile");
        if (compound.hasKey("inTile", 8))
        {
            this.inTile = Block.getBlockFromName(compound.getString("inTile"));
        }
        else
        {
            this.inTile = Block.getBlockById(compound.getByte("inTile") & 255);
        }
        this.inGround = compound.getByte("inGround") == 1;
        if (compound.hasKey("power", 9))
        {
            NBTTagList nbtTagListPower = compound.getTagList("power", 6);
            if (nbtTagListPower.tagCount() == 3)
            {
                this.accelerationX = nbtTagListPower.getDoubleAt(0);
                this.accelerationY = nbtTagListPower.getDoubleAt(1);
                this.accelerationZ = nbtTagListPower.getDoubleAt(2);
            }
        }
        this.ticksAlive = compound.getInteger("life");
        if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3)
        {
            NBTTagList nbtTagListDirection = compound.getTagList("direction", 6);
            this.motionX = nbtTagListDirection.getDoubleAt(0);
            this.motionY = nbtTagListDirection.getDoubleAt(1);
            this.motionZ = nbtTagListDirection.getDoubleAt(2);
        }
        else
        {
            this.setDead();
        }
    }
    
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return true;
    }

    public float getCollisionBorderSize()
    {
        return 1.0F;
    }
}