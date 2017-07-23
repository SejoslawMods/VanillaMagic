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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.event.EventPlayerUseCauldron;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.OreMultiplierUtil;
import seia.vanillamagic.util.SmeltingUtil;

public class QuestOreMultiplier extends Quest
{
	protected int multiplier;
	protected IWand requiredMinimalWand;
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		this.multiplier = jo.get("multiplier").getAsInt();
		this.requiredMinimalWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
	}
	
	public int getMultiplier()
	{
		return multiplier;
	}
	
	public IWand getRequiredWand()
	{
		return requiredMinimalWand;
	}
	
	@SubscribeEvent
	public void doubleOre(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		BlockPos cauldronPos = event.getPos();
		// player has got required wand in hand
		if (WandRegistry.isWandInMainHandRight(player, requiredMinimalWand.getWandID()))
		{
			// check if  player has the "fuel" in offHand
			ItemStack fuelOffHand = player.getHeldItemOffhand();
			if (ItemStackUtil.isNullStack(fuelOffHand)) return;
			
			if (SmeltingUtil.isItemFuel(fuelOffHand))
			{
				World world = player.world;
				// is right-clicking on Cauldron
				if (world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
				{
					// is altair build correct
					if (OreMultiplierUtil.check(world, cauldronPos))
					{
						List<EntityItem> oresInCauldron = SmeltingUtil.getOresInCauldron(world, cauldronPos);
						if (oresInCauldron.size() > 0)
						{
							checkQuestProgress(player);
							
							if (hasQuest(player))
							{
								if (!MinecraftForge.EVENT_BUS.post(new EventPlayerUseCauldron.OreMultiplier(
										player, world, cauldronPos, oresInCauldron)))
								{
									multiply(player, oresInCauldron, cauldronPos);
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void multiply(EntityPlayer player, List<EntityItem> oresInCauldron, BlockPos cauldronPos)
	{
		List<EntityItem> smeltingResult = SmeltingUtil.countAndSmelt_OneByOneItemFromOffHand(player, oresInCauldron, cauldronPos.offset(EnumFacing.UP), this, false);
		if (smeltingResult != null)
		{
			World world = player.world;
			for(int i = 0; i < multiplier; ++i)
				for(int j = 0; j < smeltingResult.size(); ++j)
					world.spawnEntity(EntityUtil.copyItem(smeltingResult.get(j)));
			world.updateEntities();
		}
	}
}