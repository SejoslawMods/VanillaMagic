package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.magic.spell.SpellRegistry;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.ListUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestSummonHorde extends Quest {
	protected int level; // it will tell how many monsters will be summoned
	protected int range = 10; // range in blocks
	protected int verticalRange = 1; // vertical range in blocks
	// the number is amount of blocks in which the enemy can spawn,
	// requiredDistanceToPlayer away from Player
	protected double requiredDistanceToPlayer = range - 2;
	protected ItemStack requiredLeftHand;

	public void readData(JsonObject jo) {
		this.level = jo.get("level").getAsInt();
		this.requiredLeftHand = ItemStackUtil.getItemStackFromJSON(jo.get("requiredLeftHand").getAsJsonObject());
		this.icon = requiredLeftHand.copy();
		super.readData(jo);
	}

	@SubscribeEvent
	public void spawnHorde(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = player.world;
		ItemStack rightHand = player.getHeldItemMainhand();
		ItemStack leftHand = player.getHeldItemOffhand();

		if (ItemStackUtil.isNullStack(rightHand) || ItemStackUtil.isNullStack(leftHand)) {
			return;
		}

		if (!WandRegistry.areWandsEqual(rightHand, WandRegistry.WAND_NETHER_STAR.getWandStack())
				|| !ItemStack.areItemsEqual(leftHand, requiredLeftHand)
				|| (ItemStackUtil.getStackSize(leftHand) != ItemStackUtil.getStackSize(requiredLeftHand))) {
			return;
		}

		checkQuestProgress(player);

		if (!hasQuest(player)) {
			return;
		}

		EntityUtil.addChatComponentMessageNoSpam(player,
				player.getDisplayNameString() + " summoned horde lvl: " + this.level + ". Prepare to DIE !!!");
		spawnHorde(player, world);
		ItemStackUtil.decreaseStackSize(leftHand, ItemStackUtil.getStackSize(requiredLeftHand));
	}

	public void spawnHorde(EntityPlayer player, World world) {
		int posX = (int) Math.round(player.posX - 0.5f);
		int posY = (int) player.posY;
		int posZ = (int) Math.round(player.posZ - 0.5f);

		for (int i = 0; i < level; ++i) {
			for (int ix = posX - range; ix <= posX + range; ++ix) {
				for (int iz = posZ - range; iz <= posZ + range; ++iz) {
					for (int iy = posY - verticalRange; iy <= posY + verticalRange; ++iy) {
						BlockPos spawnPos = new BlockPos(ix, iy, iz);
						double distanceToPlayer = spawnPos.getDistance(posX, posY, posZ);

						if ((distanceToPlayer >= requiredDistanceToPlayer) && (world.rand.nextInt(50) == 0)) {
							spawn(player, world, spawnPos);
						}
					}
				}
			}
		}
	}

	/**
	 * Spawn single enemy.
	 */
	public void spawn(EntityPlayer player, World world, BlockPos spawnPos) {
		int randID = ListUtil.getRandomObjectFromTab(
				SpellRegistry.getSummonMobSpellIDsWithoutSpecific(SpellRegistry.SPELL_SUMMON_WITHER.getSpellID()));
		SpellRegistry.castSummonMob(player, world, spawnPos, EnumFacing.UP, randID, true);
	}
}