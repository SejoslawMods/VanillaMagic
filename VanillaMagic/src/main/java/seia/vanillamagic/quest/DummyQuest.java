package seia.vanillamagic.quest;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.api.quest.QuestData;

/**
 * Dummy Quest for testing Quest rendering.
 */
public class DummyQuest extends Quest
{
	public DummyQuest()
	{
		this.requiredQuest = null;
		this.posX = 0;
		this.posY = 0;
		this.icon = new ItemStack(Blocks.DIAMOND_BLOCK);
		this.questName = "Dummy Quest";
		this.uniqueName = "dummyQuest";
		this.questTitle = this.questName;
		this.questDescription = "Description is something good to have";
		this.questData = new QuestData("vanillamagic:" + this.getUniqueName(),
				new TextComponentTranslation("achievement." + this.getUniqueName(), new Object[0]),
				this);
	}
	
	public IQuest getParent() 
	{
		return null;
	}
}