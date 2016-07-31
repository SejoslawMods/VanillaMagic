package seia.vanillamagic.quest;

import net.minecraft.item.ItemStack;

//TODO; add the 3 walls 3x3 checker and register achievement
public class QuestBuildEnchantingSanctuary extends Quest
{
	public QuestBuildEnchantingSanctuary(Quest required, int posX, int posY, ItemStack itemIcon, String questName, String uniqueName)
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
	}
}