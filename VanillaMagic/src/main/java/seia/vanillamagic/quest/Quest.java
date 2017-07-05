package seia.vanillamagic.quest;

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import seia.vanillamagic.api.quest.IQuest;
import seia.vanillamagic.api.quest.QuestData;
import seia.vanillamagic.api.quest.QuestList;
import seia.vanillamagic.api.util.Point;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.QuestUtil;
import seia.vanillamagic.util.TextUtil;

/** 
 * Base class for all the quests.<br>
 * Each Quest MUST overrides {@link readData} if it adds additional data.
 */
public abstract class Quest implements IQuest
{
	/**
	 * Quest that is required for this Quest to be completed.
	 */
	protected IQuest requiredQuest;
	/**
	 * Position of this Quest on Quest Page.
	 */
	protected int posX, posY;
	/**
	 * Icon of this Quest on Quest Page.
	 */
	protected ItemStack icon;
	/**
	 * 1. Name of the Quest - to be displayed.
	 * 2. Unique name of this Quest.
	 * 3. Quest Title
	 * 4. Quest Description
	 */
	protected String questName, uniqueName, questTitle, questDescription;
	/**
	 * Quest data for Minecraft statistics.
	 */
	protected QuestData questData;
	
	/**
	 * Array of additional Quests which should be completed to get this Quest.
	 */
	@Nullable
	IQuest[] additionalRequiredQuests;
	
	public void readData(JsonObject jo)
	{
		this.requiredQuest = QuestList.get(jo.get("requiredQuest").getAsString());
		//this.required = (this.requiredQuest == null ? null : this.requiredQuest.getAdvancement());
		if (jo.has("questName"))
		{
			this.questName = jo.get("questName").getAsString();
		}
		if (jo.has("uniqueName"))
		{
			this.uniqueName = jo.get("uniqueName").getAsString();
		}
		// Quest position on the screen
		int tmpX = jo.get("posX").getAsInt();
		this.posX = (this.requiredQuest != null ? (this.requiredQuest.getPosition().getX() + tmpX) : tmpX);
		int tmpY = jo.get("posY").getAsInt();
		this.posY = (this.requiredQuest != null ? (this.requiredQuest.getPosition().getY() + tmpY) : tmpY);
		if (jo.has("icon"))
		{
			this.icon = ItemStackUtil.getItemStackFromJSON(jo.get("icon").getAsJsonObject());
		}
		// Additional Quests
		if (jo.has("additionalRequiredQuests"))
		{
			JsonObject additionalRequiredQuests = jo.get("additionalRequiredQuests").getAsJsonObject();
			Set<Entry<String, JsonElement>> set = additionalRequiredQuests.entrySet();
			IQuest[] requiredQuestsTable = new IQuest[set.size()];
			int index = 0;
			for (Entry<String, JsonElement> q : set)
			{
				requiredQuestsTable[index] = QuestList.get(q.getValue().getAsString());
				index++;
			}
			this.additionalRequiredQuests = requiredQuestsTable;
		}
		this.questTitle = "achievement." + this.uniqueName;
		this.questDescription = "achievement." + this.uniqueName + ".desc";
		// Build QuestData
		this.questData = new QuestData("vanillamagic:" + this.uniqueName,
				new TextComponentTranslation("achievement." + this.uniqueName, new Object[0]),
				this);
		this.questData.registerStat();
		/*
		this.achievement = new Advancement("vanillamagic:" + this.uniqueName, 
				this.uniqueName, 
				this.posX, 
				this.posY, 
				this.icon, 
				this.required)
				.registerStat();
		*/
		// Registering Quest - this method should ONLY be called here
		QuestList.addQuest(this);
	}
	
	/**
	 * @return Returns TRUE if checking Player can get this Quest.
	 */
	public boolean canPlayerGetQuest(EntityPlayer player)
	{
		if (QuestUtil.hasQuestUnlocked(player, this.requiredQuest))
		{
			if (hasAdditionalQuests())
			{
				if (finishedAdditionalQuests(player))
					return true;
			}
			else
				return true;
		}
		return false;
	}
	
	/**
	 * @return Returns TRUE if this Quest has any additional Quests that need to be achieved before this.
	 */
	public boolean hasAdditionalQuests()
	{
		return additionalRequiredQuests != null;
	}
	
	public boolean finishedAdditionalQuests(EntityPlayer player)
	{
		if (hasAdditionalQuests())
		{
			for (IQuest quest : additionalRequiredQuests)
			{
				if (!QuestUtil.hasQuestUnlocked(player, quest))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * =============================================================== GETTERS ================================================================
	 */
	
	public IQuest getRequiredQuest()
	{
		return requiredQuest;
	}
	
	public Point getPosition()
	{
		return new Point(posX, posY);
	}
	
	public ItemStack getIcon()
	{
		return icon;
	}
	
	public String getQuestName()
	{
		return questName;
	}
	
	public String getQuestDesc()
	{
		return TextUtil.translateToLocal(this.questDescription);
	}
	
	public String getUniqueName()
	{
		return uniqueName;
	}
	
	public IQuest[] getAdditionalRequiredQuests()
	{
		return additionalRequiredQuests;
	}
	
	public QuestData getQuestData()
	{
		return this.questData;
	}
}
