package seia.vanillamagic.quest;

import java.util.List;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.AltarChecker;
import seia.vanillamagic.utils.SmeltingHelper;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestSmeltOnAltar extends Quest
{
	// in 200 ticks You will smelt 1 item in Furnace
	public static final int ONE_ITEM_SMELT_TICKS = 200;
	
	public final int requiredAltarTier;
	public final EnumWand requiredMinimalWand;
	
	public QuestSmeltOnAltar(Quest required, int posX, int posY, 
			ItemStack itemIcon, String questName, String uniqueName,
			int requiredAltarTier, EnumWand requiredMinimalWand)
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
		this.requiredAltarTier = requiredAltarTier;
		this.requiredMinimalWand = requiredMinimalWand;
	}
	
	@SubscribeEvent
	public void craftOnAltar(RightClickBlock event)
	{
		try
		{
			EntityPlayer player = event.getEntityPlayer();
			BlockPos cauldronPos = event.getPos();
			
			// player has got required wand in hand
			if(EnumWand.isWandInMainHandRight(player, requiredMinimalWand.wandTier))
			{
				// check if player has the "fuel" in offHand
				ItemStack fuelOffHand = player.getHeldItemOffhand();
				if(SmeltingHelper.isItemFuel(fuelOffHand))
				{
					World world = player.worldObj;
					// is right-clicking on Cauldron
					if(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
					{
						// is altair build correct
						if(AltarChecker.checkAltarTier(world, cauldronPos, requiredAltarTier))
						{
							List<EntityItem> itemsToSmelt = SmeltingHelper.getSmeltable(world, cauldronPos);
							if(itemsToSmelt.size() > 0)
							{
								countAndSmelt(player, itemsToSmelt, cauldronPos);
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	// TODO: better idea
	// currently will consume whole stack in offhand for 1 operation
	public void countAndSmelt(EntityPlayer caster, List<EntityItem> itemsToSmelt, BlockPos cauldronPos)
	{
		if(!caster.hasAchievement(achievement))
		{
			caster.addStat(achievement, 1);
		}
		if(caster.hasAchievement(achievement))
		{
			World world = caster.worldObj;
			int ticks = 0;
			ticks += SmeltingHelper.countTicks(caster.getHeldItemOffhand()); // value for the whole stack
			caster.getHeldItemOffhand().stackSize = 0;
			for(int i = 0; i < itemsToSmelt.size(); i++)
			{
				EntityItem entityItemToSmelt = itemsToSmelt.get(i);
				ItemStack entityItemToSmeltStack = entityItemToSmelt.getEntityItem();
				int entityItemToSmeltStackSize = entityItemToSmeltStack.stackSize;
				int ticksToSmeltStack = entityItemToSmeltStackSize * ONE_ITEM_SMELT_TICKS;
				ItemStack smeltResult = null;
				EntityItem smeltResultEntityItem = null;
				// will smelt whole stack
				if(ticks >= ticksToSmeltStack)
				{
					smeltResult = SmeltingHelper.getSmeltingResultAsNewStack(entityItemToSmeltStack);
					smeltResult.stackSize = entityItemToSmeltStack.stackSize;
					smeltResultEntityItem = new EntityItem(world, cauldronPos.getX(), cauldronPos.getY() + 1, cauldronPos.getZ(), smeltResult);
					world.removeEntity(entityItemToSmelt);
				}
				else if(ticks >= ONE_ITEM_SMELT_TICKS)// won't smelt whole stack, we need to count how many we can smelt
				{
					int howManyCanSmelt = ticks / ONE_ITEM_SMELT_TICKS;
					entityItemToSmeltStack.stackSize -= howManyCanSmelt;
					smeltResult = SmeltingHelper.getSmeltingResultAsNewStack(entityItemToSmeltStack);
					smeltResult.stackSize = howManyCanSmelt;
					smeltResultEntityItem = new EntityItem(world, cauldronPos.getX(), cauldronPos.getY() + 1, cauldronPos.getZ(), smeltResult);
				}
				else // if(ticks < ONE_ITEM_SMELT_TICKS), we can't smelt any more items so let's just break
				{
					break;
				}
				world.spawnEntityInWorld(smeltResultEntityItem);
				ticks -= ticksToSmeltStack;
				//TODO: Fix the experience
				int experienceToAdd = SmeltingHelper.getExperienceToAddFromWholeStack(entityItemToSmeltStack);
				caster.addExperience(experienceToAdd);
			}
			world.updateEntities();
		}
	}
}