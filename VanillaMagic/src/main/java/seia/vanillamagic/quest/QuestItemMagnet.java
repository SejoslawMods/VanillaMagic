package seia.vanillamagic.quest;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.util.VectorWrapper;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.config.VMConfig;

public class QuestItemMagnet extends Quest
{
	public final ItemStack star = new ItemStack(Items.NETHER_STAR);
	public final int range = VMConfig.ITEM_MAGNET_RANGE;
	public final int maxPulledItems = VMConfig.ITEM_MAGNET_PULLED_ITEMS;
	
	@SubscribeEvent
	public void playerTick(LivingUpdateEvent event)
	{
		if (event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if (playerHasRightItemsInInventory(player))
			{
				checkQuestProgress(player);
				
				if (hasQuest(player))
				{
					double x = player.posX;
					double y = player.posY + 0.75;
					double z = player.posZ;
					List<EntityItem> items = player.world.getEntitiesWithinAABB(EntityItem.class, 
							new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range));
					int pulledItems = 0;
					for (EntityItem item : items)
					{
						if (pulledItems > maxPulledItems) break;
						setEntityMotionFromVector(item, VectorWrapper.wrap(x, y, z), 0.45F);
						pulledItems++;
					}
				}
			}
		}
	}
	
	public boolean playerHasRightItemsInInventory(EntityPlayer player)
	{
		boolean star1 = false;
		boolean star2 = false;
		NonNullList<ItemStack> inventory = player.inventory.mainInventory;
		for (int i = 0; i < inventory.size(); ++i)
		{
			if (ItemStack.areItemStacksEqual(star, inventory.get(i)))
			{
				if (star1 == true)
				{
					star2 = true;
					break;
				}
				else
					star1 = true;
			}
		}
		
		if ((star1 == true) && (star2 == true)) return true;
		return false;
	}
	
	public double mag(Vector3D vec)
	{
		return Math.sqrt(vec.getX() * vec.getX() + vec.getY() * vec.getY() + vec.getZ() * vec.getZ());
	}
	
	public void setEntityMotionFromVector(Entity entity, Vector3D originalPosVector, float modifier)
	{
		Vector3D entityVector = VectorWrapper.wrap(entity.posX, entity.posY - entity.getYOffset() + entity.height / 2, entity.posZ);
		Vector3D finalVector = originalPosVector.subtract(entityVector);
		if (mag(finalVector) > 1) finalVector = finalVector.normalize();
		entity.motionX = finalVector.getX() * modifier;
		entity.motionY = finalVector.getY() * modifier;
		entity.motionZ = finalVector.getZ() * modifier;
	}
}