package seia.vanillamagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import seia.vanillamagic.api.quest.IQuest;

/**
 * Various Quest-related utilities.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class QuestUtil {
	private QuestUtil() {
	}

	/**
	 * @return Returns TRUE if the Player has already finished checking Quest.
	 */
	public static boolean hasQuestUnlocked(EntityPlayer player, IQuest quest) {
		if (quest == null) {
			return true;
		}

		StatBase stat = quest.getQuestData();
		int statValue = 0;

		if (EntityUtil.isSinglePlayer(player)) {
			statValue = EntityUtil.toSinglePlayer(player).getStatFileWriter().readStat(stat);
		} else if (EntityUtil.isMultiPlayer(player)) {
			statValue = EntityUtil.toMultiPlayer(player).getStatFile().readStat(stat);
		}

		return statValue > 0;
	}

	/**
	 * @return Returns TRUE if the given Player can unlock the given Quest.
	 */
	public static boolean canUnlockQuest(EntityPlayer player, IQuest quest) {
		return quest.canPlayerGetQuest(player);
	}

	@SideOnly(Side.CLIENT)
	public static int countRequirementsUntilAvailable(EntityPlayer player, IQuest quest) {
		if (hasQuestUnlocked(player, quest)) {
			return 0;
		}

		int i = 0;

		for (IQuest q = quest.getParent(); q != null && !hasQuestUnlocked(player, quest); ++i) {
			q = q.getParent();
		}

		return i;
	}

	/**
	 * Add specified Quest for specified Player
	 */
	public static void addStat(EntityPlayer player, IQuest quest) {
		if (EntityUtil.isSinglePlayer(player)) {
			EntityUtil.toSinglePlayer(player).addStat(quest.getQuestData());
			return;
		}

		if (EntityUtil.isMultiPlayer(player)) {
			EntityPlayerMP playerMP = EntityUtil.toMultiPlayer(player);
			playerMP.addStat(quest.getQuestData());
			playerMP.getStatFile().sendStats(playerMP);

			String message = TextUtil.translateToLocal("quest.achieved") + ": " + quest.getQuestName();
			EntityUtil.addChatComponentMessageNoSpam(playerMP, message);
		}
	}

	/**
	 * @return Returns the StatName of the given Quest.
	 */
	public static ITextComponent getStatName(IQuest quest) {
		return (quest != null ? quest.getQuestData().getStatName() : TextUtil.wrap(""));
	}
}