package seia.vanillamagic.utils.spell;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import seia.vanillamagic.entity.EntitySpellFreezeLiquid;
import seia.vanillamagic.entity.EntitySpellPull;
import seia.vanillamagic.entity.EntitySpellSummonLightningBolt;
import seia.vanillamagic.entity.EntitySpellTeleport;
import seia.vanillamagic.entity.meteor.EntitySpellSummonMeteor;
import seia.vanillamagic.utils.EntityHelper;
import seia.vanillamagic.utils.spell.teleport.TeleportHelper;

/*
 * The work of each spell.
 * When using any of those methods we are sure that the Caster has got the right wand in hand.
 */
public class SpellHelper
{
	/**
	 * @return True - if the spell was casted correctly
	 */
	public static boolean castSpellById(int spellID, EntityPlayer caster, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(spellID == EnumSpell.LIGHTER.spellID)
		{
			return spellLighter(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.SMALL_FIREBALL.spellID)
		{
			return spellSmallFireball(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.LARGE_FIREBALL.spellID)
		{
			return spellLargeFireball(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.TELEPORT.spellID)
		{
			return spellTeleport(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.METEOR.spellID)
		{
			return spellSummonMeteor(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.LIGHTNING_BOLT.spellID)
		{
			return spellSummonLightningBolt(caster, pos, face, hitVec);
		}
		else if(EnumSpell.isSpellSummonAnimal(spellID))
		{
			return spellSummonAnimal(caster, pos, face, hitVec, spellID);
		}
		else if(EnumSpell.isSpellSummonMob(spellID))
		{
			return spellSummonMob(caster, pos, face, hitVec, spellID);
		}
		else if(spellID == EnumSpell.FUS_RO_DAH.spellID)
		{
			return spellFusRoDah(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.TELEPORT_TO_NETHER.spellID)
		{
			return spellTeleportToNether(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.TELEPORT_TO_END.spellID)
		{
			return spellTeleportToEnd(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.MOVE_IN_AIR.spellID)
		{
			return spellMoveInAir(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.PULL_ENTITY_TO_PLAYER.spellID)
		{
			return spellPullEntityToPlayer(caster, pos, face, hitVec);
		}
		else if(spellID == EnumSpell.WATER_FREEZE.spellID)
		{
			return spellFreezeWater3x3(caster, pos, face, hitVec);
		}
		return false;
	}
	
	/*
	 * Flint and Steal Clone
	 */
	public static boolean spellLighter(EntityPlayer caster, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos != null) // RightClickBlock
		{
			World world = caster.worldObj;
			pos = pos.offset(face);
			if (world.isAirBlock(pos))
			{
				world.playSound(caster, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
				world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Feel like Blaze
	 */
	public static boolean spellSmallFireball(EntityPlayer caster, 
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null) // casting while NOT looking at block
		{
			World world = caster.worldObj;
			world.playEvent(caster, 1018, new BlockPos((int)caster.posX, (int)caster.posY, (int)caster.posZ), 0);
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySmallFireball fireball = new EntitySmallFireball(world, 
					caster.posX + accelX, 
					caster.posY + 1.5D + accelY, 
					caster.posZ + accelZ, 
					accelX, accelY, accelZ);
			fireball.shootingEntity = caster;
			fireball.motionX = 0.0D;
			fireball.motionY = 0.0D;
			fireball.motionZ = 0.0D;
			double d0 = (double)MathHelper.sqrt_double(accelX * accelX + 
					accelY * accelY + 
					accelZ * accelZ);
			fireball.accelerationX = accelX / d0 * 0.1D;
			fireball.accelerationY = accelY / d0 * 0.1D;
			fireball.accelerationZ = accelZ / d0 * 0.1D;
			world.spawnEntityInWorld(fireball);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Feel like Ghast
	 */
	public static boolean spellLargeFireball(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null) // casting while NOT looking at block
		{
			World world = caster.worldObj;
			world.playEvent(caster, 1016, new BlockPos((int)caster.posX, (int)caster.posY, (int)caster.posZ), 0);
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntityLargeFireball fireball = new EntityLargeFireball(world, 
					caster.posX + accelX, 
					caster.posY + 1.5D + accelY, 
					caster.posZ + accelZ,
					accelX, accelY, accelZ);
			fireball.shootingEntity = caster;
			fireball.motionX = 0.0D;
			fireball.motionY = 0.0D;
			fireball.motionZ = 0.0D;
			double d0 = (double)MathHelper.sqrt_double(accelX * accelX + 
					accelY * accelY + 
					accelZ * accelZ);
			fireball.accelerationX = accelX / d0 * 0.1D;
			fireball.accelerationY = accelY / d0 * 0.1D;
			fireball.accelerationZ = accelZ / d0 * 0.1D;
			world.spawnEntityInWorld(fireball);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Teleportation !!!
	 */
	public static boolean spellTeleport(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			World world = caster.worldObj;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellTeleport spellTeleport = new EntitySpellTeleport(world, caster, 
					accelX, accelY, accelZ);
			world.spawnEntityInWorld(spellTeleport);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Meteor !!!
	 */
	public static boolean spellSummonMeteor(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			World world = caster.worldObj;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellSummonMeteor spellMeteor = new EntitySpellSummonMeteor(world, caster, 
					accelX, accelY, accelZ);
			world.spawnEntityInWorld(spellMeteor);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Thunder !!!
	 */
	public static boolean spellSummonLightningBolt(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			World world = caster.worldObj;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellSummonLightningBolt spellLightningBolt = new EntitySpellSummonLightningBolt(world,
					caster, accelX, accelY, accelZ);
			world.spawnEntityInWorld(spellLightningBolt);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Summon Animal
	 */
	public static boolean spellSummonAnimal(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec,
			int spellID)
	{
		if(pos != null)
		{
			World world = caster.worldObj;
			BlockPos spawnPos = pos.offset(face);
			EntityAgeable entityAgeable = null;
			if(spellID == EnumSpell.SUMMON_CHICKEN.spellID)
			{
				entityAgeable = new EntityChicken(world);
			}
			else if(spellID == EnumSpell.SUMMON_PIG.spellID)
			{
				entityAgeable = new EntityPig(world);
			}
			else if(spellID == EnumSpell.SUMMON_COW.spellID)
			{
				entityAgeable = new EntityCow(world);
			}
			else if(spellID == EnumSpell.SUMMON_MOOSHROOM.spellID)
			{
				entityAgeable = new EntityMooshroom(world);
			}
			else if(spellID == EnumSpell.SUMMON_SHEEP.spellID)
			{
				entityAgeable = new EntitySheep(world);
			}
			else if(spellID == EnumSpell.SUMMON_WOLF.spellID)
			{
				entityAgeable = new EntityWolf(world);
			}
			else if(spellID == EnumSpell.SUMMON_RABBIT.spellID)
			{
				entityAgeable = new EntityRabbit(world);
			}
			else if(spellID == EnumSpell.SUMMON_HORSE.spellID)
			{
				entityAgeable = new EntityHorse(world);
			}
			else if(spellID == EnumSpell.SUMMON_VILLAGER.spellID)
			{
				entityAgeable = new EntityVillager(world);
			}
			
			if(entityAgeable != null)
			{
				entityAgeable.setGrowingAge(1);
				entityAgeable.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), caster.rotationYaw, 0.0F);
				world.spawnEntityInWorld(entityAgeable);
				world.updateEntities();
				return true;
			}
			else
			{
				System.out.println("Wrong spellID. (spellID = " + spellID + ")");
			}
		}
		return false;
	}
	
	/*
	 * Summon Mob
	 */
	public static boolean spellSummonMob(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec,
			int spellID)
	{
		if(pos != null)
		{
			World world = caster.worldObj;
			BlockPos spawnPos = pos.offset(face);
			EntityLiving entityMob = null;
			if(spellID == EnumSpell.SUMMON_ZOMBIE.spellID)
			{
				entityMob = new EntityZombie(world);
			}
			else if(spellID == EnumSpell.SUMMON_CREEPER.spellID)
			{
				entityMob = new EntityCreeper(world);
			}
			else if(spellID == EnumSpell.SUMMON_SKELETON.spellID)
			{
				entityMob = new EntitySkeleton(world);
				entityMob.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
			}
			else if(spellID == EnumSpell.SUMMON_BLAZE.spellID)
			{
				entityMob = new EntityBlaze(world);
			}
			else if(spellID == EnumSpell.SUMMON_MAGMA_CUBE.spellID)
			{
				entityMob = new EntityMagmaCube(world);
			}
			else if(spellID == EnumSpell.SUMMON_GHAST.spellID)
			{
				entityMob = new EntityGhast(world);
			}
			else if(spellID == EnumSpell.SUMMON_ENDERMAN.spellID)
			{
				entityMob = new EntityEnderman(world);
			}
			else if(spellID == EnumSpell.SUMMON_SPIDER.spellID)
			{
				entityMob = new EntitySpider(world);
			}
			else if(spellID == EnumSpell.SUMMON_SLIME.spellID)
			{
				entityMob = new EntitySlime(world);
			}
			else if(spellID == EnumSpell.SUMMON_WITCH.spellID)
			{
				entityMob = new EntityWitch(world);
			}
			
			if(entityMob != null)
			{
				entityMob.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), caster.rotationYaw, 0.0F);
				world.spawnEntityInWorld(entityMob);
				world.updateEntities();
				return true;
			}
			else
			{
				System.out.println("Wrong spellID. (spellID = " + spellID + ")");
			}
		}
		return false;
	}
	
	/*
	 * Fus-Ro-Dah !!!
	 */
	public static boolean spellFusRoDah(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			// it will be (2 *SIZE)x(2 *SIZE)x(2 * SIZE) area of effect
			int SIZE = 8;
			float strenght = 2.0f;
			double casterX = caster.posX;
			double casterY = caster.posY;
			double casterZ = caster.posZ;
			BlockPos casterPos = new BlockPos(casterX, casterY, casterZ);
			AxisAlignedBB aabb = new AxisAlignedBB(casterPos);
			aabb = aabb.expand(SIZE, SIZE, SIZE);
			World world = caster.worldObj;
			List<Entity> entitiesInAABB = world.getEntitiesWithinAABBExcludingEntity(caster, aabb);
			// TODO: Currently will work on ALL EntityLivingBase
			for(Entity entity : entitiesInAABB)
			{
				if(entity instanceof EntityLivingBase)
				{
					EntityHelper.knockBack(caster, entity, strenght);
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean spellTeleportToNether(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		try
		{
			if(caster.dimension == 0)
			{
				TeleportHelper.changePlayerDimensionWithoutPortal(caster, -1);
				return true;
			}
			else if(caster.dimension == -1)
			{
				TeleportHelper.changePlayerDimensionWithoutPortal(caster, 0);
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean spellTeleportToEnd(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		try
		{
			if(caster.dimension == 0)
			{
				caster.changeDimension(1);
				return true;
			}
			else if(caster.dimension == 1)
			{
				if(caster.hasAchievement(AchievementList.THE_END2))
				{
					World world = caster.worldObj;
					List<Entity> entities = world.loadedEntityList;
					for(int i = 0; i < entities.size(); i++)
					{
						if(entities.get(i) instanceof EntityDragon)
						{
							return false;
						}
					}
					TeleportHelper.changePlayerDimensionWithoutPortal(caster, 0);
					return true;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * Move in air
	 */
	public static boolean spellMoveInAir(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		World world = caster.worldObj;
		double distance = 10;
		Vec3d casterLookVec = caster.getLookVec();
		Potion potionSpeed = Potion.getPotionById(1);
		if(caster.isPotionActive(potionSpeed)) // speed potion
		{
			int amplifier = caster.getActivePotionEffect(potionSpeed).getAmplifier();
			distance += (1 + amplifier) * (0.35);
		}
		// will teleport caster to the farthest blockPos between casterPos and 'distance'
		for(double i = distance; i > 0; i -= 1.0D)
		{
			double newPosX = caster.posX + casterLookVec.xCoord * i;
			double newPosY = caster.posY + casterLookVec.yCoord * i;
			double newPosZ = caster.posZ + casterLookVec.zCoord * i;
			BlockPos newPos = new BlockPos(newPosX, newPosY, newPosZ);
			BlockPos newPosHead = new BlockPos(newPosX, newPosY + 1, newPosZ);
			if(newPosY > 0)
			{
				if(world.isAirBlock(newPos) && 
						world.isAirBlock(newPosHead))
				{
					caster.setPositionAndUpdate(newPosX, newPosY, newPosZ);
					caster.fallDistance = 0.0F;
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * Pull Entity to Player
	 */
	public static boolean spellPullEntityToPlayer(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			World world = caster.worldObj;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellPull spellLightningBolt = new EntitySpellPull(world,
					caster, accelX, accelY, accelZ,
					caster.getPosition());
			world.spawnEntityInWorld(spellLightningBolt);
			world.updateEntities();
			return true;
		}
		return false;
	}
	
	/*
	 * Freeze Water 3x3
	 */
	public static boolean spellFreezeWater3x3(EntityPlayer caster,
			BlockPos pos, EnumFacing face, Vec3d hitVec)
	{
		if(pos == null)
		{
			World world = caster.worldObj;
			Vec3d lookingAt = caster.getLookVec();
			double accelX = lookingAt.xCoord;
			double accelY = lookingAt.yCoord;
			double accelZ = lookingAt.zCoord;
			EntitySpellFreezeLiquid spellLightningBolt = new EntitySpellFreezeLiquid(world,
					caster, accelX, accelY, accelZ,
					Blocks.ICE, new BlockLiquid[]{Blocks.WATER, Blocks.FLOWING_WATER}, 5);
			world.spawnEntityInWorld(spellLightningBolt);
			world.updateEntities();
			return true;
		}
		return false;
	}
}