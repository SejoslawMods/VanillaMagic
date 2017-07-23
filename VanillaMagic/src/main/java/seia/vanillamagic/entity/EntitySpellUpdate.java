package seia.vanillamagic.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Middle class which holds updated onUpdate function.
 */
public abstract class EntitySpellUpdate extends EntitySpell
{
	public EntitySpellUpdate(World world, EntityLivingBase caster, double accelX, double accelY, double accelZ) 
	{
		super(world, caster, accelX, accelY, accelZ);
	}
	
	/**
	 * Called to update the Entity's position / logic.
	 */
	public void onUpdate()
	{
		if (this.castingEntity != null && this.castingEntity instanceof EntityPlayer && !this.castingEntity.isEntityAlive()) this.setDead();
		else super.onUpdate();
	}
}