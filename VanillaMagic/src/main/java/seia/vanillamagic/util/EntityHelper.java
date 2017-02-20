package seia.vanillamagic.util;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
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
		if(ItemStackHelper.isNullStack(mainHand))
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
		return new EntityItem(original.world, original.posX, original.posY, original.posZ, original.getEntityItem().copy());
	}
	
	public static Vec3d getEyePosition(EntityPlayer player)
	{
		double posX = player.posX;
		double posY = player.posY;
		double posZ = player.posZ;
		if(player.world.isRemote) 
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
		return player.getLookVec();
	}
	
	/**
	 * knocks back "toKnockBack" entity
	 */
	public static void knockBack(Entity user, Entity toKnockBack, float strenght, double xRatio, double zRatio)
	{
		toKnockBack.isAirBorne = true;
		float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
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
		player.sendStatusMessage(new TextComponentString(TextHelper.getVanillaMagicInfo(msg)), false); // TODO: What is this boolean ?
	}

	public static void removeEntities(World world, List<EntityItem> itemsInCauldron)
	{
		for(EntityItem ei : itemsInCauldron)
		{
			world.removeEntity(ei);
		}
	}
	
	public static void addRandomArmorToEntity(Entity entity) 
	{
		addRandomItemToSlot(entity, EntityEquipmentSlot.CHEST);
		addRandomItemToSlot(entity, EntityEquipmentSlot.FEET);
		addRandomItemToSlot(entity, EntityEquipmentSlot.HEAD);
		addRandomItemToSlot(entity, EntityEquipmentSlot.LEGS);
		addRandomItemToSlot(entity, EntityEquipmentSlot.MAINHAND);
	}
	
	public static Entity addRandomItemToSlot(Entity entity, EntityEquipmentSlot slot) 
	{
		entity.setItemStackToSlot(slot, EntityHelper.getRandomItemForSlot(slot));
		return entity;
	}
	
	public static ItemStack getRandomItemForSlot(EntityEquipmentSlot slot) 
	{
		Item item = null;
		if(slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND)
		{
			item = ListHelper.getRandomObjectFromList(EquipmentHelper.WEAPONS);
		}
		else if(slot == EntityEquipmentSlot.CHEST)
		{
			item = ListHelper.getRandomObjectFromList(EquipmentHelper.CHESTPLATES);
		}
		else if(slot == EntityEquipmentSlot.FEET)
		{
			item = ListHelper.getRandomObjectFromList(EquipmentHelper.BOOTS);
		}
		else if(slot == EntityEquipmentSlot.HEAD)
		{
			item = ListHelper.getRandomObjectFromList(EquipmentHelper.HELMETS);
		}
		else if(slot == EntityEquipmentSlot.LEGS)
		{
			item = ListHelper.getRandomObjectFromList(EquipmentHelper.LEGGINGS);
		}
		return new ItemStack(item);
	}

	// TODO: Fix long range Fangs attaks.
	@Nullable
	public static Entity getClosestLookingAt(Entity looking, double distance)
	{
		double checkingDistance = 0;
		while(checkingDistance < distance)
		{
			RayTraceResult result = looking.rayTrace(checkingDistance, 0.1F);
			if(result != null)
			{
				if(result.typeOfHit == Type.ENTITY)
				{
					return result.entityHit;
				}
				else if(result.typeOfHit == Type.BLOCK)
				{
					return null;
				}
			}
			checkingDistance += 1.0D;
		}
		return null;
	}
	
	@Nullable
	public static RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids)
	{
		float pitch = playerIn.rotationPitch;
		float yaw = playerIn.rotationYaw;
		double x = playerIn.posX;
		double y = playerIn.posY + (double)playerIn.getEyeHeight();
		double z = playerIn.posZ;
		Vec3d vec3d = new Vec3d(x, y, z);
		float f2 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		float f4 = -MathHelper.cos(-pitch * 0.017453292F);
		float f5 = MathHelper.sin(-pitch * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = 1000.0D;
		Vec3d vec3d1 = vec3d.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
		return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}
}