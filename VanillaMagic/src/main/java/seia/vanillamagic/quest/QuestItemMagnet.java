package seia.vanillamagic.quest;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class QuestItemMagnet extends Quest
{
	public final ItemStack star = new ItemStack(Items.NETHER_STAR);
	public final int range = 6;
	public final int maxPulledItems = 200;

	public QuestItemMagnet(Quest required, int posX, int posY, ItemStack itemIcon, String questName, String uniqueName) 
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
	}
	
	@SubscribeEvent
	public void playerTick(LivingUpdateEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if(playerHasRightItemsInInventory(player))
			{
				if(!player.hasAchievement(achievement))
				{
					player.addStat(achievement, 1);
				}
				if(player.hasAchievement(achievement))
				{
					double x = player.posX;
					double y = player.posY + 0.75;
					double z = player.posZ;
					List<EntityItem> items = player.worldObj.getEntitiesWithinAABB(EntityItem.class, 
							new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
					int pulledItems = 0;
					for(EntityItem item : items)
					{
						if(pulledItems > maxPulledItems)
						{
							break;
						}
						setEntityMotionFromVector(item, new Vec3d(x, y, z), 0.45F);
						pulledItems++;
					}
				}
			}
		}
	}
	
	// TODO: Currently only for 1x Nether Star + 1x Nether Star
	public boolean playerHasRightItemsInInventory(EntityPlayer player)
	{
		boolean star1 = false;
		boolean star2 = false;
		ItemStack[] inventory = player.inventory.mainInventory;
		for(int i = 0; i < inventory.length; i++)
		{
			if(ItemStack.areItemStacksEqual(star, inventory[i]))
			{
				if(star1 == true)
				{
					star2 = true;
					break;
				}
				else
				{
					star1 = true;
				}
			}
		}
		if((star1 == true) && (star2 == true))
		{
			return true;
		}
		return false;
	}
	
	public double mag(Vec3d vec)
	{
		return Math.sqrt(vec.xCoord * vec.xCoord + vec.yCoord * vec.yCoord + vec.zCoord * vec.zCoord);
	}
	
	public void setEntityMotionFromVector(Entity entity, Vec3d originalPosVector, float modifier)
	{
		Vec3d entityVector = new Vec3d(entity.posX, entity.posY - entity.getYOffset() + entity.height / 2, entity.posZ);
		Vec3d finalVector = originalPosVector.subtract(entityVector);
		if(mag(finalVector) > 1)
		{
			finalVector = finalVector.normalize();
		}
		entity.motionX = finalVector.xCoord * modifier;
		entity.motionY = finalVector.yCoord * modifier;
		entity.motionZ = finalVector.zCoord * modifier;
	}
}