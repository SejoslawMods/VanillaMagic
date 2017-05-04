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

/**
 * Basic EntitySpell definition.
 * This Spell is fired when Player casts Spell.
 * Works like invisible Ender Pearl which go in the Player looking direction.
 */
public abstract class EntitySpell extends Entity
{
	private int _xTile = 0;
	private int _yTile = 0;
	private int _zTile = 0;
	private Block _inTile;
	private boolean _inGround;
	private int _ticksAlive;
	private int _ticksInAir;
	
	public EntityLivingBase castingEntity; // This is mainly the EntityPlayer
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;
	public boolean spawnParticlesInWater = false;

	public EntitySpell(World world, EntityLivingBase caster, 
			double accelX, double accelY, double accelZ)
	{
		super(world);
		this.castingEntity = caster;
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(
				caster.posX + accelX, // in front of face = accelX
				caster.posY + 1.2D + accelY, // in front of face = accelY
				caster.posZ + accelZ, // in front of face = accelZ
				caster.rotationYaw, 
				caster.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		double d0 = (double)MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * 0.1D;
		this.accelerationY = accelY / d0 * 0.1D;
		this.accelerationZ = accelZ / d0 * 0.1D;
	}
	
	/**
	 * Initialise the Entity
	 */
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
		if(Double.isNaN(d0))
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
		if(this.world.isRemote || (this.castingEntity == null || !this.castingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this)))
		{
			super.onUpdate();
			if(this._inGround)
			{
				if(this.world.getBlockState(new BlockPos(this._xTile, this._yTile, this._zTile)).getBlock() == this._inTile)
				{
					++this._ticksAlive;
					if(this._ticksAlive == 600)
					{
						this.setDead();
					}
					return;
				}
				this._inGround = false;
				this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
				this._ticksAlive = 0;
				this._ticksInAir = 0;
			}
			else
			{
				++this._ticksInAir;
				inAir();
			}
			RayTraceResult rayTraceResult = ProjectileHelper.forwardsRaycast(this, true, this._ticksInAir >= 25, this.castingEntity);
			if(rayTraceResult != null)
			{
				this.onImpact(rayTraceResult);
			}
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			ProjectileHelper.rotateTowardsMovement(this, 0.2F);
			float motionFactor = this.getMotionFactor();
			if(this.isInWater())
			{
				if(spawnParticlesInWater)
				{
					for(int i = 0; i < 4; ++i)
					{
						float f1 = 0.25F;
						this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ, new int[0]);
					}
				}
				inWater();
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
	 * Run when this Entity is in water.
	 * (Spawn water particle, etc.)
	 */
	public void inWater()
	{
	}
    
	/**
	 * Run when this Entity is in air.
	 */
	public void inAir()
	{
	}
    
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("xTile", this._xTile);
		compound.setInteger("yTile", this._yTile);
		compound.setInteger("zTile", this._zTile);
		ResourceLocation resourceLocation = (ResourceLocation)Block.REGISTRY.getNameForObject(this._inTile);
		compound.setString("inTile", resourceLocation == null ? "" : resourceLocation.toString());
		compound.setByte("inGround", (byte)(this._inGround ? 1 : 0));
		compound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
		compound.setTag("power", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
		compound.setInteger("life", this._ticksAlive);
	}
    
	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		this._xTile = compound.getInteger("xTile");
		this._yTile = compound.getInteger("yTile");
		this._zTile = compound.getInteger("zTile");
		if(compound.hasKey("inTile", 8))
		{
			this._inTile = Block.getBlockFromName(compound.getString("inTile"));
		}
		else
		{
			this._inTile = Block.getBlockById(compound.getByte("inTile") & 255);
		}
		this._inGround = compound.getByte("inGround") == 1;
		if(compound.hasKey("power", 9))
		{
			NBTTagList nbtTagListPower = compound.getTagList("power", 6);
			if(nbtTagListPower.tagCount() == 3)
			{
				this.accelerationX = nbtTagListPower.getDoubleAt(0);
				this.accelerationY = nbtTagListPower.getDoubleAt(1);
				this.accelerationZ = nbtTagListPower.getDoubleAt(2);
			}
		}
		this._ticksAlive = compound.getInteger("life");
		if(compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3)
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