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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.event.EventCaptureEntity;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.NBTUtil;

public class QuestCaptureEntity extends Quest
{
	protected ItemStack requiredStackOffHand;
	protected IWand requiredWand;
	
	public void readData(JsonObject jo)
	{
		this.requiredStackOffHand = ItemStackUtil.getItemStackFromJSON(jo.get("requiredStackOffHand").getAsJsonObject());
		this.requiredWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
		this.icon = requiredStackOffHand.copy();
		super.readData(jo);
	}
	
	public ItemStack getRequiredStackOffHand()
	{
		return requiredStackOffHand;
	}
	
	public IWand getRequirdWand()
	{
		return requiredWand;
	}
	
	int clickedTimes = 0;
	@SubscribeEvent
	public void captureEntity(EntityInteract event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.world;
		Entity target = event.getTarget();
		ItemStack rightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(rightHand)) return;
		
		IWand wandPlayerHand = WandRegistry.getWandByItemStack(rightHand);
		if (wandPlayerHand == null) return;
		
		if (WandRegistry.areWandsEqual(requiredWand, wandPlayerHand))
		{
			if (player.isSneaking())
			{
				ItemStack stackOffHand = player.getHeldItemOffhand();
				if (ItemStack.areItemsEqual(requiredStackOffHand, stackOffHand))
				{
					if (ItemStackUtil.getStackSize(stackOffHand) == ItemStackUtil.getStackSize(requiredStackOffHand))
					{
						checkQuestProgress(player);
						
						if (hasQuest(player))
						{
							if (clickedTimes > 0)
							{
								clickedTimes = 0;
								return;
							}
							if (target instanceof EntityPlayer)
							{
								event.setCanceled(true);
								return;
							}
							if (!MinecraftForge.EVENT_BUS.post(new EventCaptureEntity.Capture(world, player, target))) 
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
		World world = player.world;
		BlockPos respawnPos = event.getPos();
		ItemStack rightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(rightHand)) return;
		
		IWand wandPlayerHand = WandRegistry.getWandByItemStack(rightHand);
		if (wandPlayerHand == null) return;
		
		if (WandRegistry.areWandsEqual(requiredWand, wandPlayerHand))
		{
			if (player.isSneaking())
			{
				ItemStack stackOffHand = player.getHeldItemOffhand();
				if (ItemStackUtil.isNullStack(stackOffHand)) return;
				
				NBTTagCompound stackTag = stackOffHand.getTagCompound();
				if (stackTag == null) return;
				
				if (stackTag.hasKey(NBTUtil.NBT_TAG_COMPOUND_ENTITY))
				{
					if (clickedTimesFree > 0)
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
		if (world.isRemote) return;
		
		ItemStack stackOffHand = new ItemStack(Items.ENCHANTED_BOOK);
		stackOffHand.setStackDisplayName("EntityBook");
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();
		// Save to ItemStack
		if (!stackTagCompound.hasKey(NBTUtil.NBT_TAG_COMPOUND_ENTITY))
		{
			NBTTagCompound entityTag = new NBTTagCompound();
			target.writeToNBT(entityTag);
			stackTagCompound.setTag(NBTUtil.NBT_TAG_COMPOUND_ENTITY, entityTag);
			stackTagCompound.setString(NBTUtil.NBT_ENTITY_TYPE, target.getClass().getCanonicalName());
			stackTagCompound.setString(NBTUtil.NBT_ENTITY_NAME, target.getName());
			stackOffHand.setStackDisplayName("Captured Entity: " + target.getName());
			world.removeEntity(target);
		}
		player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stackOffHand);
	}
	
	public void handleRespawn(World world, EntityPlayer player, ItemStack stackOffHand, BlockPos respawnPos, EnumFacing face) 
	{
		if (world.isRemote) return;
		
		respawnPos = respawnPos.offset(face);
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();
		if (stackTagCompound.hasKey(NBTUtil.NBT_TAG_COMPOUND_ENTITY))
		{
			NBTTagCompound entityTag = stackTagCompound.getCompoundTag(NBTUtil.NBT_TAG_COMPOUND_ENTITY);
			String type = stackTagCompound.getString(NBTUtil.NBT_ENTITY_TYPE);
			Entity entity = createEntity(player, world, type);
			if (entity == null) return;
			
			if (!MinecraftForge.EVENT_BUS.post(new EventCaptureEntity.Respawn(
					world, player, respawnPos, face, entity, type)))
			{
				entity.readFromNBT(entityTag);
				entity.setLocationAndAngles(respawnPos.getX() + 0.5D, respawnPos.getY() + 0.5d, respawnPos.getZ() + 0.5D, 0, 0);
				world.spawnEntity(entity);
				ItemStack newOffHand = requiredStackOffHand.copy();
				player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, newOffHand);
			}
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
			e.printStackTrace();
		}
		return entity;
	}
	
	@SubscribeEvent
	public void addEntityDataTooltip(ItemTooltipEvent event)
	{
		ItemStack entityContainer = event.getItemStack();
		NBTTagCompound entityContainerTag = entityContainer.getTagCompound();
		if (entityContainerTag != null)
		{
			if (entityContainerTag.hasKey(NBTUtil.NBT_TAG_COMPOUND_ENTITY))
			{
				String type = entityContainerTag.getString(NBTUtil.NBT_ENTITY_NAME);
				event.getToolTip().add("Captured Entity: " + type);
			}
		}
	}
}