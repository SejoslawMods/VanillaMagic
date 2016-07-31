package seia.vanillamagic.quest;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestArrowMachineGun extends Quest
{
	public QuestArrowMachineGun(Quest required, int posX, int posY, ItemStack itemIcon, String questName,
			String uniqueName) 
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
	}
	
	@SubscribeEvent
	public void onRightClick(RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack leftHand = player.getHeldItemOffhand();
		ItemStack rightHand = player.getHeldItemMainhand();
		World world = player.worldObj;
		try
		{
			if(leftHand.getItem().equals(Items.ARROW) ||
					leftHand.getItem().equals(Items.TIPPED_ARROW))
			{
				if(rightHand.getItem().equals(EnumWand.NETHER_STAR.wandItemStack.getItem()))
				{
					if(!player.hasAchievement(achievement))
					{
						player.addStat(achievement, 1);
					}
					else if(player.hasAchievement(achievement))
					{
						EntityTippedArrow entityTippedArrow = new EntityTippedArrow(world, player);
						entityTippedArrow.setPotionEffect(leftHand);
						entityTippedArrow.setAim(player, 
								player.rotationPitch,
								player.rotationYaw,
								0.0F,
								3.0F,
								1.0F);
						entityTippedArrow.setIsCritical(true);
						int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, leftHand);
						if (j > 0)
						{
							entityTippedArrow.setDamage(entityTippedArrow.getDamage() + (double)j * 0.5D + 0.5D);
						}
						int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, leftHand);
						if (k > 0)
						{
							entityTippedArrow.setKnockbackStrength(k);
						}
						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, leftHand) > 0)
						{
							entityTippedArrow.setFire(100);
						}
						leftHand.damageItem(1, player);
						world.spawnEntityInWorld(entityTippedArrow);
						world.playSound((EntityPlayer)null, 
								player.posX, 
								player.posY, 
								player.posZ, 
								SoundEvents.ENTITY_ARROW_SHOOT, 
								SoundCategory.NEUTRAL, 
								1.0F, 
								1.0F / (new Random().nextFloat() * 0.4F + 1.2F) + 0.5F);
						if(leftHand.stackSize > 0)
						{
							--leftHand.stackSize;
							if(leftHand.stackSize == 0)
							{
								player.inventory.deleteStack(leftHand);
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
}