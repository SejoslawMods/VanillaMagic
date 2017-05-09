package seia.vanillamagic.quest.upgrade.itemupgrade.upgrades;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.util.ItemStackHelper;
import seia.vanillamagic.util.TextHelper;
import seia.vanillamagic.util.WeatherHelper;

/**
 * Class which defines Thor upgrade for Sword. (thunder enemy on hit)
 */
public class UpgradeThor extends UpgradeSword 
{
	public ItemStack getIngredient() 
	{
		return ItemStackHelper.getHead(1, 4);
	}
	
	public String getUniqueNBTTag() 
	{
		return "NBT_UPGRADE_THOR";
	}
	
	public String getUpgradeName() 
	{
		return "Thor " + "\u26A1";
	}
	
	public String getTextColor()
	{
		return TextHelper.COLOR_BLUE;
	}
	
	public void onAttack(EntityPlayer player, Entity target) 
	{
		if(target instanceof EntityLivingBase)
		{
			WeatherHelper.spawnLightningBolt(player.world, target.posX, target.posY, target.posZ);
		}
	}
}