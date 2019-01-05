package seia.vanillamagic.quest;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.event.EventPlayerUseCauldron;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.util.AltarUtil;
import seia.vanillamagic.util.EventUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.SmeltingUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestSmeltOnAltar extends Quest {
	// It takes 200 ticks to smelt 1 item in Furnace.
	public static final int ONE_ITEM_SMELT_TICKS = 200;

	protected int requiredAltarTier;
	protected IWand requiredMinimalWand;

	public void readData(JsonObject jo) {
		super.readData(jo);
		this.requiredAltarTier = jo.get("requiredAltarTier").getAsInt();
		this.requiredMinimalWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
	}

	public int getRequiredAltarTier() {
		return requiredAltarTier;
	}

	public IWand getRequiredWand() {
		return requiredMinimalWand;
	}

	@SubscribeEvent
	public void smeltOnAltar(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		BlockPos cauldronPos = event.getPos();

		if (!WandRegistry.isWandInMainHandRight(player, requiredMinimalWand.getWandID())) {
			return;
		}

		ItemStack fuelOffHand = player.getHeldItemOffhand();

		if (ItemStackUtil.isNullStack(fuelOffHand) || !SmeltingUtil.isItemFuel(fuelOffHand)) {
			return;
		}

		World world = player.world;

		if (!(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
				|| !AltarUtil.checkAltarTier(world, cauldronPos, requiredAltarTier)) {
			return;
		}

		List<EntityItem> itemsToSmelt = SmeltingUtil.getSmeltable(world, cauldronPos);

		if (itemsToSmelt.size() <= 0) {
			return;
		}

		checkQuestProgress(player);

		if (!hasQuest(player) || EventUtil
				.postEvent(new EventPlayerUseCauldron.SmeltOnAltar(player, world, cauldronPos, itemsToSmelt))) {
			return;
		}

		SmeltingUtil.countAndSmelt_OneByOneItemFromOffHand(player, itemsToSmelt, cauldronPos.offset(EnumFacing.UP),
				this, true);
	}
}