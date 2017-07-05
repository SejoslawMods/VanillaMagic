package seia.vanillamagic.api.quest;

import net.minecraft.stats.StatBase;
import net.minecraft.util.text.ITextComponent;

/**
 * Holds data for Minecraft statistics.
 */
public class QuestData extends StatBase
{
	/**
	 * Quest connected with this QuestData.
	 */
	private final IQuest _quest;
	
	/**
	 * @param questUniqueId -> "vanillamagic:" + questUniqueName
	 * @param statNameIn
	 */
	public QuestData(String questUniqueId, ITextComponent statNameIn, IQuest quest)
	{
		super(questUniqueId, statNameIn);
		this._quest = quest;
	}
	
	/**
	 * @return Returns the unique id of this stat.
	 */
	public String getStatId()
	{
		return this.statId;
	}
	
	/**
	 * @return Returns the stat name.
	 */
	public ITextComponent getStatName()
	{
		return super.getStatName();
	}
	
	/**
	 * @return Returns Quest connected with this QuestData.
	 */
	public IQuest getQuest()
	{
		return this._quest;
	}
}