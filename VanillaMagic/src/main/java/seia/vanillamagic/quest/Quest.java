package seia.vanillamagic.quest;

import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;

public abstract class Quest
{
	protected Achievement achievement;
	protected Achievement required;
	protected int posX, posY;
	protected Item itemIcon;
	protected String questName, uniqueName;
	
	@Deprecated
	public Quest()
	{
	}
	
	public Quest(Achievement required, int posX, int posY, Item itemIcon, String questName, String uniqueName)
	{
		this.required = required;
		this.posX = posX;
		this.posY = posY;
		this.itemIcon = itemIcon;
		this.questName = questName;
		this.uniqueName = uniqueName;
		
		this.achievement = new Achievement(questName, uniqueName, posX, posY, 
				itemIcon, 
				required)
				.registerStat();
		
		QuestList.QUESTS.add(this);
	}
	
	public Achievement getAchievement()
	{
		return achievement;
	}
	
	public Achievement getRequiredAchievement()
	{
		return required;
	}
	
	public int getPosX()
	{
		return posX;
	}
	
	public int getPosY()
	{
		return posY;
	}
	
	public Item getItemIcon()
	{
		return itemIcon;
	}
	
	public String getQuestName()
	{
		return questName;
	}
	
	public String getUniqueName()
	{
		return uniqueName;
	}
}