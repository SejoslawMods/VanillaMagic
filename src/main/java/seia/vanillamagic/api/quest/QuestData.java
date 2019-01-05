package seia.vanillamagic.api.quest;

import net.minecraft.stats.StatBase;
import net.minecraft.util.text.ITextComponent;

/**
 * Holds data for Minecraft statistics about single Quest.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestData extends StatBase {
	/**
	 * Quest connected with this QuestData.
	 */
	private final IQuest quest;

	/**
	 * @param questUniqueId -> "vanillamagic:" + questUniqueName
	 * @param statNameIn
	 */
	public QuestData(String questUniqueId, ITextComponent statNameIn, IQuest connectedQuest) {
		super(questUniqueId, statNameIn);
		this.quest = connectedQuest;
	}

	/**
	 * @return Returns the unique id of this stat.
	 */
	public String getStatId() {
		return this.statId;
	}

	/**
	 * @return Returns the stat name.
	 */
	public ITextComponent getStatName() {
		return super.getStatName();
	}

	/**
	 * @return Returns Quest connected with this QuestData.
	 */
	public IQuest getQuest() {
		return this.quest;
	}
}