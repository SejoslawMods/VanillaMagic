package seia.vanillamagic.quest;

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import seia.vanillamagic.utils.ItemStackHelper;

/** 
 * Base class for all the quests.<br>
 * Each Quest MUST overrides {@link readData} if it adds additional data.
 */
public abstract class Quest
{
	public Achievement achievement;
	public Achievement required;
	public Quest requiredQuest;
	public int posX, posY;
	public ItemStack icon;
	public String questName, uniqueName;
	
	@Nullable
	public Quest[] additionalRequiredQuests;
	
	public void readData(JsonObject jo)
	{
		this.requiredQuest = QuestList.QUESTS_MAP.get(jo.get("requiredQuest"));
		this.required = (this.requiredQuest == null ? null : this.requiredQuest.achievement);
		this.questName = jo.get("questName").getAsString();
		this.uniqueName = jo.get("uniqueName").getAsString();
		{
			// Quest position on the screen
			int tmpX = jo.get("posX").getAsInt();
			this.posX = (this.requiredQuest != null ? (this.requiredQuest.posX + tmpX) : tmpX);
			int tmpY = jo.get("posY").getAsInt();
			this.posY = (this.requiredQuest != null ? (this.requiredQuest.posY + tmpY) : tmpY);
		}
		{
			// Quest icon
//			JsonObject icon = jo.get("icon").getAsJsonObject();
			this.icon = ItemStackHelper.getItemStackFromJSON(jo.get("icon").getAsJsonObject());
//			int id = icon.get("id").getAsInt();
//			int stackSize = icon.get("stackSize").getAsInt();
//			int meta = icon.get("meta").getAsInt();
//			Item item = null;
//			Block block = null;
//			try
//			{
//				item = Item.getItemById(id);
//			}
//			catch(Exception e)
//			{
//				block = Block.getBlockById(id);
//			}
//			
//			if(item == null)
//			{
//				this.icon = new ItemStack(block, stackSize, meta);
//			}
//			else
//			{
//				this.icon = new ItemStack(item, stackSize, meta);
//			}
		}
		// Additional Quests
		if(jo.has("additionalRequiredQuests"))
		{
			JsonObject additionalRequiredQuests = jo.get("additionalRequiredQuests").getAsJsonObject();
			Set<Entry<String, JsonElement>> set = additionalRequiredQuests.entrySet();
			Quest[] requiredQuestsTable = new Quest[set.size()];
			int index = 0;
			for(Entry<String, JsonElement> q : set)
			{
				requiredQuestsTable[index] = QuestList.QUESTS_MAP.get(q.getValue().getAsString());
				index++;
			}
			this.additionalRequiredQuests = requiredQuestsTable;
		}
		// Building Achievement
		this.achievement = new Achievement(this.questName, 
				this.uniqueName, 
				this.posX, 
				this.posY, 
				this.icon, 
				this.required)
				.registerStat();
		// Registering Quest
		QuestList.addQuest(this);
	}
	
//	private Quest()
//	{
//		achievement = null;
//		required = null;
//		requiredQuest = null;
//		posX = 0;
//		posY = 0;
//		icon = null;
//		questName = "";
//		uniqueName = "";
//		additionalRequiredQuests = null;
//	}
//	
//	public Quest(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName)
//	{
//		this(required, posX, posY, icon, questName, uniqueName, null);
//	}
//	
//	public Quest(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName, Quest[] additionalRequiredQuests)
//	{
//		this.requiredQuest = required;
//		this.required = required.achievement;
//		this.posX = required.posX + posX;
//		this.posY = required.posY + posY;
//		this.icon = icon;
//		this.questName = questName;
//		this.uniqueName = uniqueName;
//		this.additionalRequiredQuests = additionalRequiredQuests;
//		
//		this.achievement = new Achievement(this.questName, 
//				this.uniqueName, 
//				this.posX, 
//				this.posY, 
//				this.icon, 
//				this.required)
//				.registerStat();
//		
//		QuestList.QUESTS.add(this);
//	}
//	
//	/**
//	 * This is only for the first Achievement.
//	 * Try to use the other Constructor.
//	 */
//	public Quest(Achievement required, int posX, int posY, ItemStack icon, String questName, String uniqueName)
//	{
//		this.requiredQuest = null;
//		this.required = required;
//		this.posX = 0 + posX;
//		this.posY = 0 + posY;
//		this.icon = icon;
//		this.questName = questName;
//		this.uniqueName = uniqueName;
//		this.additionalRequiredQuests = null;
//		
//		this.achievement = new Achievement(this.questName, 
//				this.uniqueName, 
//				this.posX, 
//				this.posY, 
//				this.icon, 
//				this.required)
//				.registerStat();
//		
//		QuestList.QUESTS.add(this);
//	}
	
	public boolean canPlayerGetAchievement(EntityPlayer player)
	{
		if(player.hasAchievement(required))
		{
			// Player need additional quests to be completed.
			if(additionalRequiredQuests != null)
			{
				for(Quest quest : additionalRequiredQuests)
				{
					if(!player.hasAchievement(quest.achievement))
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean hasAdditionalQuests()
	{
		return this.additionalRequiredQuests != null;
	}
	
	public boolean finishedAdditionalQuests(EntityPlayer player)
	{
		if(hasAdditionalQuests())
		{
			for(Quest quest : additionalRequiredQuests)
			{
				if(!player.hasAchievement(quest.achievement))
				{
					return false;
				}
			}
		}
		return true;
	}
}