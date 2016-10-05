package seia.vanillamagic.api.quest;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import seia.vanillamagic.api.util.Point;

/**
 * This is the base of the Quest System.<br>
 * Each Quest must implements this interface to be registered from achievements.json file.
 */
public interface IQuest
{
	/**
	 * This method is used to read data from the achievements.json<br>
	 * It will create an instance of this Quest and add it to QuestList.
	 */
	void readData(JsonObject jo);
	
	/**
	 * Returns TRUE if the given {@link EntityPlayer} can get this {@link Achievement}
	 */
	 boolean canPlayerGetAchievement(EntityPlayer player);
	 
	 /**
	  * Returns TRUE if this Quest needs any additional Quest to be achieved.
	  */
	 boolean hasAdditionalQuests();
	 
	 /**
	  * Returns TRUE if the given {@link EntityPlayer} achieved all the additional Quests.
	  */
	 boolean finishedAdditionalQuests(EntityPlayer player);
	 
	 /**
	  * Returns the {@link Achievement} connected to this Quest.
	  */
	 Achievement getAchievement();
	 
	 /**
	  * Returns the {@link Achievement} that s required before this one.
	  */
	 Achievement getRequiredAchievement();
		
	 /**
	  * Returns the full Quest that is required before this one.
	  */
	 IQuest getRequiredQuest();
		
	 /**
	  * Returns position as {@link Point} in 2D.<br>
	  * Any changes to this point will NOT affect the position.
	  */
	 Point getPosition();
		
	 /**
	  * Returns the {@link ItemStack} that will be displayed as Icon for this Achievement.
	  */
	 ItemStack getIcon();
		
	 /**
	  * Returns the Quest name as a readable string.
	  */
	 String getQuestName();
		
	 /**
	  * Returns the Quest unique name.
	  */
	 String getUniqueName();
		
	 /**
	  * Returns an array of Quests that must be achieved before this Quest can be achieve.
	  */
	 IQuest[] getAdditionalRequiredQuests();
}