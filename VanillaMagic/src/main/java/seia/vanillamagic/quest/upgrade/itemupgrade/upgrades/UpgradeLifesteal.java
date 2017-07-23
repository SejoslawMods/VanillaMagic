package seia.vanillamagic.quest.upgrade.itemupgrade.upgrades;

import java.util.Collection;

import com.google.common.collect.Multimap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.util.TextUtil;

/**
 * Class which represents Lifesteal upgrade for Swords
 */
public class UpgradeLifesteal extends UpgradeSword
{
	public ItemStack getIngredient() 
	{
		return new ItemStack(Items.GOLDEN_APPLE);
	}
	
	public String getUniqueNBTTag() 
	{
		return "NBT_UPGRADE_LIFESTEAL";
	}
	
	public String getUpgradeName() 
	{
		return "Lifesteal";
	}
	
	public String getTextColor()
	{
		return TextUtil.COLOR_GREEN;
	}
	
	public void onAttack(EntityPlayer player, Entity target) 
	{
		ItemStack playerMainHandStack = player.getHeldItemMainhand();
		// Attributes 
		Multimap<String, AttributeModifier> attributes = playerMainHandStack.getItem().getAttributeModifiers(EntityEquipmentSlot.MAINHAND, playerMainHandStack);
		// Used attribute
		String attributeName = SharedMonsterAttributes.ATTACK_DAMAGE.getName();
		// All modifiers for this name
		Collection<AttributeModifier> modifiers = attributes.get(attributeName);
		
		double amount = 0;
		for (AttributeModifier am : modifiers) amount += am.getAmount() / 2; // to make it not too OP
		player.heal((float) amount);
	}
}