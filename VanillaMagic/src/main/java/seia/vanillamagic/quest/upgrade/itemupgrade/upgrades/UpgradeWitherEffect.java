package seia.vanillamagic.quest.upgrade.itemupgrade.upgrades;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.upgrade.itemupgrade.ItemUpgradeBase;
import seia.vanillamagic.util.ItemStackHelper;
import seia.vanillamagic.util.TextHelper;

/**
 * Class which defines a Wither Effect for Sword
 */
public class UpgradeWitherEffect extends ItemUpgradeBase
{
	public ItemStack getIngredient() 
	{
		return ItemStackHelper.getHead(1, 1); // 1x WIther Skeleton Head
	}
	
	public String getUniqueNBTTag() 
	{
		return "NBT_UPGRADE_WITHER_EFFECT";
	}
	
	public String getUpgradeName() 
	{
		return "Wither Effect";
	}
	
	public String getTextColor()
	{
		return TextHelper.COLOR_GREY;
	}
	
	/**
	 * When Entity was hit with this upgrade. Add Wither Effect to it.
	 */
	@SubscribeEvent
	public void onAttack(AttackEntityEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		// if for some reason Player is null, quit.
		if(player == null) return; 
		// If item does not contains required tag, quit.
		if(!containsTag(player.getHeldItemMainhand())) return; 
		else
		{
			Entity hittedEntity = event.getTarget();
			World world = player.getEntityWorld();
			int multiplier = 1;
			
			if(world.getDifficulty() == EnumDifficulty.NORMAL) 		multiplier = 10;
			else if(world.getDifficulty() == EnumDifficulty.HARD)	multiplier = 40;
			
			if(hittedEntity instanceof EntityLivingBase)
				((EntityLivingBase) hittedEntity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * multiplier, 1));
		}
	}
}