package seia.vanillamagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import seia.vanillamagic.api.quest.IQuest;

/**
 * Various Quest-related utilities.
 */
public class QuestUtil 
{
	private QuestUtil()
	{
	}
	
	/**
	 * @return Returns TRUE if the Player has already finished checking Quest.
	 */
	public static boolean hasQuestUnlocked(EntityPlayer player, IQuest quest)
	{
		// Always has a NULL quest unlocked
		if (quest == null) return true;
		
		StatBase stat = quest.getQuestData();
		int statValue = 0;
		
		if (EntityUtil.isSinglePlayer(player))
			statValue = EntityUtil.toSinglePlayer(player).getStatFileWriter().readStat(stat);
		else if (EntityUtil.isMultiPlayer(player))
			statValue = EntityUtil.toMultiPlayer(player).getStatFile().readStat(stat);
		
		return statValue > 0;
	}
	
	/**
	 * @return Returns TRUE if the given Player can unlock the given Quest.
	 */
	public static boolean canUnlockQuest(EntityPlayer player, IQuest quest)
	{
		return quest.canPlayerGetQuest(player);
	}
	
	@SideOnly(Side.CLIENT)
	public static int countRequirementsUntilAvailable(EntityPlayer player, IQuest quest)
	{
		if (hasQuestUnlocked(player, quest)) return 0;
        else
        {
            int i = 0;
            for (IQuest q = quest.getParent(); q != null && !hasQuestUnlocked(player, quest); ++i) q = q.getParent();
            return i;
        }
	}
	
	/**
	 * Add specified Quest for specified Player
	 */
	public static void addStat(EntityPlayer player, IQuest quest)
	{
		if (EntityUtil.isSinglePlayer(player)) EntityUtil.toSinglePlayer(player).addStat(quest.getQuestData());
		else if (EntityUtil.isMultiPlayer(player)) 
		{
			EntityUtil.toMultiPlayer(player).addStat(quest.getQuestData());
			String message = TextUtil.translateToLocal("quest.achieved") + ": " + quest.getQuestName();
			EntityUtil.addChatComponentMessageNoSpam(player, message);
		}
	}
}