package seia.vanillamagic.quest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.util.VectorWrapper;
import seia.vanillamagic.api.util.VectorWrapper.Vector3D;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.util.ItemStackUtil;

public class QuestShootWitherSkull extends Quest 
{
	private final ItemStack shouldHaveLeftHand = ItemStackUtil.getHead(1, 1);
	private final ItemStack shouldHaveRightHand = WandRegistry.WAND_NETHER_STAR.getWandStack();
	
	@SubscribeEvent
	public void shootWitherSkull(RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack leftHand = player.getHeldItemOffhand();
		if (ItemStack.areItemsEqual(leftHand, shouldHaveLeftHand))
		{
			ItemStack rightHand = player.getHeldItemMainhand();
			if (ItemStack.areItemsEqual(rightHand, shouldHaveRightHand))
			{
				if (!hasQuest(player)) addStat(player);
				
				if (hasQuest(player))
				{
					World world = event.getWorld();
					world.playEvent((EntityPlayer)null, 1024, new BlockPos(player), 0);
					Vector3D lookingAt = VectorWrapper.wrap(player.getLookVec());
					double accelX = lookingAt.getX();
					double accelY = lookingAt.getY();
					double accelZ = lookingAt.getZ();
					EntityWitherSkull entityWitherSkull = new EntityWitherSkull(world, 
							player.posX + accelX, 
							player.posY + 1.5D + accelY, 
							player.posZ + accelZ, 
							accelX, accelY, accelZ);
					entityWitherSkull.shootingEntity = player;
					entityWitherSkull.motionX = 0.0D;
					entityWitherSkull.motionY = 0.0D;
					entityWitherSkull.motionZ = 0.0D;
					double d0 = (double)MathHelper.sqrt(accelX * accelX + 
							accelY * accelY + 
							accelZ * accelZ);
					entityWitherSkull.accelerationX = accelX / d0 * 0.1D;
					entityWitherSkull.accelerationY = accelY / d0 * 0.1D;
					entityWitherSkull.accelerationZ = accelZ / d0 * 0.1D;
					world.spawnEntity(entityWitherSkull);
					world.updateEntities();
					
					ItemStack offHand = player.getHeldItemOffhand();
					ItemStackUtil.decreaseStackSize(offHand, 1);
					if (ItemStackUtil.getStackSize(offHand) <= 0) player.inventory.deleteStack(offHand);
				}
			}
		}
	}
}