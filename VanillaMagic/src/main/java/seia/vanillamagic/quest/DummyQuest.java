package seia.vanillamagic.quest;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import seia.vanillamagic.api.quest.QuestData;

/**
 * Dummy Quest for testing Quest rendering.
 */
public class DummyQuest extends Quest
{
	public DummyQuest(Quest required, String questName, String uniqueName, int posX, int posY)
	{
		this.requiredQuest = required;
		this.posX = posX;
		this.posY = posY;
		this.icon = new ItemStack(Blocks.DIAMOND_BLOCK);
		this.questName = questName;
		this.uniqueName = uniqueName;
		this.questTitle = this.questName;
		this.questDescription = "Description is something good to have";
		this.questData = new QuestData("vanillamagic:" + this.getUniqueName(),
				new TextComponentTranslation("quest." + this.getUniqueName(), new Object[0]),
				this);
		this.questData.registerStat();
	}
}