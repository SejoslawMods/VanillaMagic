package seia.vanillamagic.quest;

import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;

//TODO; add the 3 walls 3x3 checker and register achievement
public class QuestBuildEnchantingSanctuary extends Quest
{
	public QuestBuildEnchantingSanctuary(Achievement required, int posX, int posY, Item itemIcon, String questName, String uniqueName)
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
	}
}