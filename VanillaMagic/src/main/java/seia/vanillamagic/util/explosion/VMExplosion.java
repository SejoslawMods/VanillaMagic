package seia.vanillamagic.util.explosion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Explosion object itself.
 * This is where all the crazy mathematics is.
 */
public class VMExplosion extends Explosion
{
	private final World _world;
	private final Entity _exploder;
	private final double _explosionX;
	private final double _explosionY;
	private final double _explosionZ;
	private final int _mapHeight;
	private final float _power;
	private final float _explosionDropRate = VMConfig.BASIC_METEOR_EXPLOSION_DROP_RATE;
	  
	private final Random _random = new Random();
	private final double _maxDistance;
	private final int _areaSize;
	private final int _areaX;
	private final int _areaZ;
	private final DamageSource _damageSource;
	private final List<EntityDamage> _entitiesInRange = new ArrayList<>();
	private final long[][] _destroyedBlockPositions;
	private ChunkCache _chunkCache;
	  
	private static class XZposition
	{
		int x;
		int z;
		
		XZposition(int x, int z)
		{
			this.x = x;
			this.z = z;
		}
	    
		public boolean equals(Object obj)
		{
			if ((obj instanceof XZposition))
			{
				XZposition xZposition = (XZposition) obj;	        
				return (xZposition.x == this.x) && (xZposition.z == this.z);
			}
			return false;
		}
		
		public int hashCode()
		{
			return this.x * 31 ^ this.z;
		}
	}
	  
	private static class DropData
	{
		int n;
		int maxY;
	    
		DropData(int n1, int y)
		{
			this.n = n1;
			this.maxY = y;
		}
	    
		public DropData add(int n1, int y)
		{
			this.n += n1;
			if (y > this.maxY) this.maxY = y;
			return this;
		}
	}
	
	private static class EntityDamage
	{
		final Entity entity;
		final int distance;
		double health;
		double damage;
		double motionX;
		double motionY;
		double motionZ;
		
		EntityDamage(Entity entity, int distance, double health)
		{
			this.entity = entity;
			this.distance = distance;
			this.health = health;
		}
	}
	
	public VMExplosion(World world, Entity entity, double x, double y, double z, float power, boolean flaming, boolean smoking)
	{
		super(world, entity, x, y, z, power, flaming, smoking);
		
		this._world = world;
		this._exploder = entity;
		this._explosionX = x;
		this._explosionY = y;
		this._explosionZ = z;
		this._mapHeight = world.getHeight();
		this._power = power;
	    
		this._maxDistance = (this._power / 0.4D);
		int maxDistanceInt = (int)Math.ceil(this._maxDistance);
		this._areaSize = (maxDistanceInt * 2);
		this._areaX = (ExplosionUtil.roundToNegInf(x) - maxDistanceInt);
		this._areaZ = (ExplosionUtil.roundToNegInf(z) - maxDistanceInt);
		this._damageSource = DamageSource.causeExplosionDamage(this);
		this._destroyedBlockPositions = new long[this._mapHeight][];
	}
	  
	@SuppressWarnings({ "deprecation" })
	public void doExplosion()
	{
		if (this._power <= 0.0F) return;
		
		int range = this._areaSize / 2;
		BlockPos pos = new BlockPos(getPosition());
		BlockPos start = pos.add(-range, -range, -range);
		BlockPos end = pos.add(range, range, range);
		
		this._chunkCache = new ChunkCache(this._world, start, end, 0);
		
		List<Entity> entities = this._world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(start, end));
		for (Entity entity : entities) 
		{
			if (((entity instanceof EntityLivingBase)) || ((entity instanceof EntityItem)))
			{
				int distance = (int)(ExplosionUtil.square(entity.posX - this._explosionX) + ExplosionUtil.square(entity.posY - this._explosionY) + ExplosionUtil.square(entity.posZ - this._explosionZ));
				double health = getEntityHealth(entity);
				this._entitiesInRange.add(new EntityDamage(entity, distance, health));
			}
		}
		boolean entitiesAreInRange = !this._entitiesInRange.isEmpty();
		if (entitiesAreInRange) 
		{
			Collections.sort(this._entitiesInRange, new Comparator<Object>()
			{
				public int compare(Object a, Object b)
				{
					return ((VMExplosion.EntityDamage) a).distance - ((VMExplosion.EntityDamage) b).distance;
				}
			});
		}
		int steps = (int)Math.ceil(3.141592653589793D / Math.atan(1.0D / this._maxDistance));
		BlockPos.MutableBlockPos tmpPos = new BlockPos.MutableBlockPos();
		for (int phi_n = 0; phi_n < 2 * steps; ++phi_n) 
		{
			for (int theta_n = 0; theta_n < steps; ++theta_n)
			{
				double phi = 6.283185307179586D / steps * phi_n;
				double theta = 3.141592653589793D / steps * theta_n;    
				shootRay(this._explosionX, this._explosionY, this._explosionZ, phi, theta, this._power, (entitiesAreInRange) && (phi_n % 8 == 0) && (theta_n % 8 == 0), tmpPos);
			}
		}
		for (Iterator<EntityDamage> phi_n = this._entitiesInRange.iterator(); phi_n.hasNext();)
		{
			EntityDamage entry = (EntityDamage) phi_n.next();
			Entity entity = entry.entity;
			
			entity.attackEntityFrom(this._damageSource, (float)entry.damage);
			double motionSq = ExplosionUtil.square(entry.motionX) + ExplosionUtil.square(entity.motionY) + ExplosionUtil.square(entity.motionZ);
			double reduction = motionSq > 3600.0D ? Math.sqrt(3600.0D / motionSq) : 1.0D;
			
			entity.motionX += entry.motionX * reduction;
			entity.motionY += entry.motionY * reduction;
			entity.motionZ += entry.motionZ * reduction;
		}
		
		Random rng = this._world.rand;
		boolean doDrops = this._world.getGameRules().getBoolean("doTileDrops");
		Map<XZposition, Map<ItemStack, DropData>> blocksToDrop = new HashMap<XZposition, Map<ItemStack, DropData>>();
		
		this._world.playSound(null, this._explosionX, this._explosionY, this._explosionZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (rng.nextFloat() - rng.nextFloat()) * 0.2F) * 0.7F);
		int realIndex;
		for (int y = 0; y < this._destroyedBlockPositions.length; ++y)
		{
			long[] bitSet = this._destroyedBlockPositions[y];
			if (bitSet != null)
			{
				int index = -2;
				while ((index = nextSetIndex(index + 2, bitSet, 2)) != -1)
				{
					realIndex = index / 2;
					int z = realIndex / this._areaSize;
					int x = realIndex - z * this._areaSize;
					
					x += this._areaX;
					z += this._areaZ;
					tmpPos.setPos(x, y, z);
					
					IBlockState state = this._chunkCache.getBlockState(tmpPos);
					Block block = state.getBlock();
					if ((this._power >= 20.0F) || (
							(doDrops) && 
							(block.canDropFromExplosion(this)) && 
							(getAtIndex(index, bitSet, 2) == 1))) 
					{
						for (ItemStack stack : state.getBlock().getDrops(this._world, tmpPos, state, 0)) 
						{
							if (rng.nextFloat() <= this._explosionDropRate)
							{
								XZposition xZposition = new XZposition(x / 2, z / 2);
								Map<ItemStack, DropData> map = (Map<ItemStack, DropData>)blocksToDrop.get(xZposition);
								if (map == null)
								{
									map = new HashMap<ItemStack, DropData>();
									blocksToDrop.put(xZposition, map);
								}
								DropData data = map.get(stack);
								if (data == null)
								{
									data = new DropData(ItemStackUtil.getStackSize(stack), y);
									map.put(stack.copy(), data);
								}
								else
									data.add(ItemStackUtil.getStackSize(stack), y);
							}
						}
					}
					block.onBlockExploded(this._world, tmpPos, this);
				}
			}
		}
		XZposition xZposition;
		for (Map.Entry<XZposition, Map<ItemStack, DropData>> entry1 : blocksToDrop.entrySet())
		{
			xZposition = entry1.getKey();
			for (Map.Entry<ItemStack, DropData> entry2 : entry1.getValue().entrySet())
			{
				ItemStack isw = entry2.getKey();
				int count = entry2.getValue().n;
				while (count > 0)
				{
					int stackSize = Math.min(count, 64);
					EntityItem entityitem = new EntityItem(this._world, (xZposition.x + this._world.rand.nextFloat()) * 2.0F, ((DropData)entry2.getValue()).maxY + 0.5D, (xZposition.z + this._world.rand.nextFloat()) * 2.0F, isw);
					entityitem.setDefaultPickupDelay();
					this._world.spawnEntity(entityitem);
					count -= stackSize;
				}
			}
		}
	}
	  
	public void destroy(int x, int y, int z, boolean noDrop)
	{
		destroyUnchecked(x, y, z, noDrop);
	}
	  
	private void destroyUnchecked(int x, int y, int z, boolean noDrop)
	{
		int index = (z - this._areaZ) * this._areaSize + (x - this._areaX);
		index *= 2;
		
		long[] array = this._destroyedBlockPositions[y];
		if (array == null)
		{
			array = makeArray(ExplosionUtil.square(this._areaSize), 2);
			this._destroyedBlockPositions[y] = array;
		}
		
		if (noDrop) setAtIndex(index, array, 3);
		else setAtIndex(index, array, 1);
	}
	  
	private void shootRay(double x, double y, double z, double phi, double theta, double power1, boolean killEntities, BlockPos.MutableBlockPos tmpPos)
	{
		double deltaX = Math.sin(theta) * Math.cos(phi);
		double deltaY = Math.cos(theta);
		double deltaZ = Math.sin(theta) * Math.sin(phi);
		for (int step = 0;; step++)
		{
			int blockY = ExplosionUtil.roundToNegInf(y);
			if ((blockY < 0) || (blockY >= this._mapHeight)) break;
			
			int blockX = ExplosionUtil.roundToNegInf(x);
			int blockZ = ExplosionUtil.roundToNegInf(z);
			
			tmpPos.setPos(blockX, blockY, blockZ);
			IBlockState state = this._chunkCache.getBlockState(tmpPos);
			Block block = state.getBlock();
			double absorption = getAbsorption(block, tmpPos);
			if (absorption < 0.0D) break;
			
			if (absorption > 1000.0D) absorption = 0.5D;
			else
			{
				if (absorption > power1) break;
				
				if((block == Blocks.STONE) || ((block != Blocks.AIR) && (!block.isAir(state, this._world, tmpPos)))) 
					destroyUnchecked(blockX, blockY, blockZ, power1 > 8.0D);
			}
			
			if ((killEntities) && ((step + 4) % 8 == 0)) 
				if ((!this._entitiesInRange.isEmpty()) && (power1 >= 0.25D)) 
					damageEntities(x, y, z, step, power1);
			
			if (absorption > 10.0D) 
				for (int i = 0; i < 5; ++i) 
					shootRay(x, y, z, this._random.nextDouble() * 2.0D * 3.141592653589793D, this._random.nextDouble() * 3.141592653589793D, absorption * 0.4D, false, tmpPos);
			
			power1 -= absorption;
			
			x += deltaX;
			y += deltaY;
			z += deltaZ;
		}
	}
	  
	private double getAbsorption(Block block, BlockPos pos)
	{
		double ret = 0.5D;
		if ((block == Blocks.AIR) || (block.isAir(block.getDefaultState(), this._world, pos))) return ret;
		
		if (block == Blocks.BEDROCK)
		{
			ret += 800D;
			return ret;
		}
		
		if ((block == Blocks.WATER) || (block == Blocks.FLOWING_WATER)) ret += 1.0D;
		else
		{
			float resistance = block.getExplosionResistance(this._world, pos, this._exploder, this);
			if (resistance < 0.0F) return resistance;
//			double extra = (resistance + 4.0F) * 0.3D;
//			ret += extra * 6.0D;
		}
		return ret;
	}
	  
	private void damageEntities(double x, double y, double z, int step, double power)
	{
		int index;
		if (step != 4)
		{
			int distanceMin = ExplosionUtil.square(step - 5);
			int indexStart = 0;
			int indexEnd = this._entitiesInRange.size() - 1;
			do
			{
				index = (indexStart + indexEnd) / 2;
				int distance = this._entitiesInRange.get(index).distance;
				
				if (distance < distanceMin) indexStart = index + 1;
				else if(distance > distanceMin) indexEnd = index - 1;
				else indexEnd = index;
			} 
			while (indexStart < indexEnd);
		}
		else
		{
			index = 0;
		}
		int distanceMax = ExplosionUtil.square(step + 5);
		for (int i = index; i < this._entitiesInRange.size(); i++)
		{
			EntityDamage entry = this._entitiesInRange.get(i);
			if (entry.distance >= distanceMax) break;
			
			Entity entity = entry.entity;
			if (ExplosionUtil.square(entity.posX - x) + ExplosionUtil.square(entity.posY - y) + ExplosionUtil.square(entity.posZ - z) <= 25.0D)
			{
				double damage = 4.0D * power;
				
				entry.damage += damage;
				entry.health -= damage;
				
				double dx = entity.posX - this._explosionX;
				double dy = entity.posY - this._explosionY;
				double dz = entity.posZ - this._explosionZ;
				
				double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
				
				entry.motionX += dx / distance * 0.08749999999999999D * power;
				entry.motionY += dy / distance * 0.08749999999999999D * power;
				entry.motionZ += dz / distance * 0.08749999999999999D * power;
				if (entry.health <= 0.0D)
				{
					entity.attackEntityFrom(this._damageSource, (float)entry.damage);
					if (!entity.isEntityAlive())
					{
						this._entitiesInRange.remove(i);
						i--;
					}
				}
			}
		}
	}
	
	private static double getEntityHealth(Entity entity)
	{
		if ((entity instanceof EntityItem)) return 5.0D;
		return (1.0D / 0.0D);
	}
	  
	private static long[] makeArray(int size, int step)
	{
		return new long[(size * step + 8 - step) / 8];
	}
	  
	private static int nextSetIndex(int start, long[] array, int step)
	{
		int offset = start % 8;
		for (int i = start / 8; i < array.length; i++)
		{
			long aval = array[i];
			for (int j = offset; j < 8; j += step)
			{
				int val = (int)(aval >> j & (1 << step) - 1);
				
				if (val != 0) return i * 8 + j;
			}
			offset = 0;
		}
		return -1;
	}
	  
	private static int getAtIndex(int index, long[] array, int step)
	{
		return (int)(array[(index / 8)] >>> index % 8 & (1 << step) - 1);
	}
	  
	private static void setAtIndex(int index, long[] array, int value)
	{
		array[(index / 8)] |= value << index % 8;
	}
}