package seia.vanillamagic.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import seia.vanillamagic.quest.portablecraftingtable.ICraftingTable;

public class EntityHelper 
{
	private EntityHelper()
	{
	}
	
	public static boolean hasPlayerCraftingTableInMainHand(EntityPlayer player)
	{
		ItemStack mainHand = player.getHeldItemMainhand();
		if(mainHand == null)
		{
			return false;
		}
		Block ct = Block.getBlockFromItem(mainHand.getItem());
		if(ct == null)
		{
			return false;
		}
		
		if(Block.isEqualTo(ct, Blocks.CRAFTING_TABLE))
		{
			return true;
		}
		else if(player.getHeldItemMainhand().getItem() instanceof ICraftingTable)
		{
			return ((ICraftingTable) mainHand.getItem()).canOpenGui(player);
		}
		return false;
	}
	
	public static EntityItem copyItem(EntityItem original)
	{
		return new EntityItem(original.worldObj, original.posX, original.posY, original.posZ, original.getEntityItem().copy());
	}
	
	public static Vec3d getEyePosition(EntityPlayer player)
	{
		double posX = player.posX;
		double posY = player.posY;
		double posZ = player.posZ;
		if (player.worldObj.isRemote) 
		{
			posY += player.getEyeHeight() - player.getDefaultEyeHeight();
		} 
		else 
		{
			posY += player.getEyeHeight();
			if(player instanceof EntityPlayerMP && player.isSneaking()) 
			{
				posY -= 0.08;
			}
		}
		return new Vec3d(posX, posY, posZ);
	}
	
	public static Vec3d getLookVec(EntityPlayer player) 
	{
		Vec3d lv = player.getLookVec();
		return new Vec3d(lv.xCoord, lv.yCoord, lv.zCoord);
	}
	
	/**
	 * knocks back "toKnockBack" entity
	 */
	public static void knockBack(Entity user, Entity toKnockBack, float strenght, double xRatio, double zRatio)
	{
		toKnockBack.isAirBorne = true;
		float f = MathHelper.sqrt_double(xRatio * xRatio + zRatio * zRatio);
		toKnockBack.motionX /= 2.0D;
		toKnockBack.motionZ /= 2.0D;
		toKnockBack.motionX -= xRatio / (double)f * (double)strenght;
		toKnockBack.motionZ -= zRatio / (double)f * (double)strenght;
		if(toKnockBack.onGround)
		{
			toKnockBack.motionY /= 2.0D;
			toKnockBack.motionY += (double)strenght;
			if(toKnockBack.motionY > 0.4000000059604645D)
			{
				toKnockBack.motionY = 0.4000000059604645D;
			}
		}
	}
	
	/**
	 * knocks back "toKnockBack" entity
	 */
	public static void knockBack(Entity user, Entity toKnockBack, float strenght)
	{
		double xRatio = user.posX - toKnockBack.posX;
		double zRatio = user.posZ - toKnockBack.posZ;
		knockBack(user, toKnockBack, strenght, xRatio, zRatio);
	}
	
	public static void addChatComponentMessage(EntityPlayer player, String msg)
	{
		player.addChatComponentMessage(new TextComponentString(msg));
	}

	public static void removeEntities(World world, List<EntityItem> itemsInCauldron)
	{
		for(EntityItem ei : itemsInCauldron)
		{
			world.removeEntity(ei);
		}
	}
	
	public static List<EntityLiving> getHostileMobs(World world)
	{
		List<EntityLiving> mobs = new ArrayList<EntityLiving>();
		mobs.add(new EntityZombie(world));
		mobs.add(new EntityCreeper(world));
		mobs.add(new EntityBlaze(world));
		mobs.add(new EntityMagmaCube(world));
		mobs.add(new EntityGhast(world));
		mobs.add(new EntityEnderman(world));
		mobs.add(new EntitySpider(world));
		mobs.add(new EntitySlime(world));
		mobs.add(new EntityWitch(world));
		mobs.add(new EntityPolarBear(world));
		mobs.add(new EntityShulker(world));
		mobs.add(new EntitySilverfish(world));
		mobs.add(new EntityWither(world));
		mobs.add(new EntityGiantZombie(world));
		mobs.add(new EntityGuardian(world));
		EntitySkeleton skeleton = new EntitySkeleton(world);
		skeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
		mobs.add(skeleton);
		EntityPigZombie pigZombie = new EntityPigZombie(world);
		pigZombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
		mobs.add(pigZombie);
		EntityGuardian elder = new EntityGuardian(world);
		elder.setElder();
		mobs.add(elder);
		return mobs;
	}
	
	public static EntityLiving getRandomHostileMob(World world)
	{
		List<EntityLiving> mobs = getHostileMobs(world);
		return ListHelper.getRandomObjectFromlist(mobs);
	}

	@Nullable
	public static ItemStack getRandomItemForSlot(EntityEquipmentSlot slot) 
	{
		Item item = null;
		if(slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND)
		{
			item = ListHelper.getRandomObjectFromlist(EquipmentHelper.INSTANCE.weapons);
		}
		else if(slot == EntityEquipmentSlot.CHEST)
		{
			item = ListHelper.getRandomObjectFromlist(EquipmentHelper.INSTANCE.chestplates);
		}
		else if(slot == EntityEquipmentSlot.FEET)
		{
			item = ListHelper.getRandomObjectFromlist(EquipmentHelper.INSTANCE.boots);
		}
		else if(slot == EntityEquipmentSlot.HEAD)
		{
			item = ListHelper.getRandomObjectFromlist(EquipmentHelper.INSTANCE.helmets);
		}
		else if(slot == EntityEquipmentSlot.LEGS)
		{
			item = ListHelper.getRandomObjectFromlist(EquipmentHelper.INSTANCE.leggings);
		}
		return new ItemStack(item);
	}
}