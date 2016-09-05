package seia.vanillamagic.quest;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.utils.ItemStackHelper;
import seia.vanillamagic.utils.NBTHelper;

public class QuestCaptureEntity extends Quest
{
	public ItemStack requiredStackOffHand;
	public EnumWand requiredWand;
	
//	public QuestCaptureEntity(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName,
//			ItemStack requiredStackOffHand, EnumWand requiredWand)
//	{
//		super(required, posX, posY, icon, questName, uniqueName);
//		this.requiredStackOffHand = requiredStackOffHand;
//		this.requiredWand = requiredWand;
//	}
	
	public void readData(JsonObject jo)
	{
		this.requiredStackOffHand = ItemStackHelper.getItemStackFromJSON(jo.get("requiredStackOffHand").getAsJsonObject());
		this.requiredWand = EnumWand.getWandByTier(jo.get("wandTier").getAsInt());
		this.icon = requiredStackOffHand.copy();
		super.readData(jo);
	}
	
	int clickedTimes = 0;
	@SubscribeEvent
	public void captureEntity(EntityInteract event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;
		Entity target = event.getTarget();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(rightHand == null)
		{
			return;
		}
		EnumWand wandPlayerHand = EnumWand.getWandByItemStack(rightHand);
		if(wandPlayerHand == null)
		{
			return;
		}
		if(EnumWand.areWandsEqual(requiredWand, wandPlayerHand))
		{
			if(player.isSneaking())
			{
				ItemStack stackOffHand = player.getHeldItemOffhand();
				if(ItemStack.areItemsEqual(requiredStackOffHand, stackOffHand))
				{
					if(stackOffHand.stackSize == requiredStackOffHand.stackSize)
					{
						if(!player.hasAchievement(achievement))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							if(clickedTimes > 0)
							{
								clickedTimes = 0;
								return;
							}
							if(target instanceof EntityPlayer)
							{
								event.setCanceled(true);
								return;
							}
							handleCapture(world, player, target);
							clickedTimes++;
						}
					}
				}
			}
		}
	}
	
	int clickedTimesFree = 0;
	@SubscribeEvent
	public void respawnEntity(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;
		BlockPos respawnPos = event.getPos();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(rightHand == null)
		{
			return;
		}
		EnumWand wandPlayerHand = EnumWand.getWandByItemStack(rightHand);
		if(wandPlayerHand == null)
		{
			return;
		}
		if(EnumWand.areWandsEqual(requiredWand, wandPlayerHand))
		{
			if(player.isSneaking())
			{
				ItemStack stackOffHand = player.getHeldItemOffhand();
				if(stackOffHand == null)
				{
					return;
				}
				NBTTagCompound stackTag = stackOffHand.getTagCompound();
				if(stackTag == null)
				{
					return;
				}
				if(stackTag.hasKey(NBTHelper.NBT_TAG_COMPOUND_ENTITY))
				{
					if(clickedTimesFree > 0)
					{
						clickedTimesFree = 0;
						return;
					}
					handleRespawn(world, player, stackOffHand, respawnPos, event.getFace());
					clickedTimesFree++;
				}
			}
		}
	}
	
	public void handleCapture(World world, EntityPlayer player, Entity target) 
	{
		if(world.isRemote)
		{
			return;
		}
		// changing name just to force Minecraft to let this item have NBTTagCompound
		player.getHeldItemOffhand().setStackDisplayName("EntityBook");
		// any item will be replaced to Enchanted Book
		ItemStack stackOffHand = ItemStackHelper.replaceItemInStack(player.getHeldItemOffhand(), Items.ENCHANTED_BOOK);
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();
		// Save to ItemStack
		if(!stackTagCompound.hasKey(NBTHelper.NBT_TAG_COMPOUND_ENTITY))
		{
			NBTTagCompound entityTag = new NBTTagCompound();
			target.writeToNBT(entityTag);
			stackTagCompound.setTag(NBTHelper.NBT_TAG_COMPOUND_ENTITY, entityTag);
			stackTagCompound.setString(NBTHelper.NBT_ENTITY_TYPE, target.getClass().getCanonicalName());
			stackOffHand.setStackDisplayName("Captured Entity: " + target.getName());
			world.removeEntity(target);
		}
		player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stackOffHand);
	}
	
	public void handleRespawn(World world, EntityPlayer player, ItemStack stackOffHand, BlockPos respawnPos, EnumFacing face) 
	{
		if(world.isRemote)
		{
			return;
		}
		respawnPos = respawnPos.offset(face);
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();
		if(stackTagCompound.hasKey(NBTHelper.NBT_TAG_COMPOUND_ENTITY))
		{
			NBTTagCompound entityTag = stackTagCompound.getCompoundTag(NBTHelper.NBT_TAG_COMPOUND_ENTITY);
			String type = stackTagCompound.getString(NBTHelper.NBT_ENTITY_TYPE);
			Entity entity = createEntity(player, world, type);
			if(entity == null)
			{
				return;
			}
			entity.readFromNBT(entityTag);
			entity.setLocationAndAngles(respawnPos.getX() + 0.5D, respawnPos.getY() + 0.5d, respawnPos.getZ() + 0.5D, 0, 0);
			world.spawnEntityInWorld(entity);
			ItemStack newOffHand = requiredStackOffHand.copy();
			player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, null);
			player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, newOffHand);
		}
	}
	
	@Nullable
	public Entity createEntity(EntityPlayer player, World world, String type)
	{
		Entity entity = null;
		try
		{
			entity = (Entity) Class.forName(type).getConstructor(World.class).newInstance(world);
		}
		catch(Exception e)
		{
		}
		return entity;
	}
}