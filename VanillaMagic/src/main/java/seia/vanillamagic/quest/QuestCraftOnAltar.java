package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.utils.AltarChecker;
import seia.vanillamagic.utils.CauldronHelper;

public class QuestCraftOnAltar extends Quest
{
	/*
	 * Each ItemStack is a different Item
	 * Instead of doing 1x Coal + 1x Coal, do 2x Coal -> 2 will be stackSize
	 */
	public final ItemStack[] ingredients;
	public final ItemStack[] result;
	public final int requiredAltarTier;
	public final EnumWand requiredMinimalWand;
	
	public QuestCraftOnAltar(Quest required, int posX, int posY, String questName, String uniqueName, 
			ItemStack[] ingredients, ItemStack[] result, int requiredAltarTier, EnumWand requiredMinimalWand) 
	{
		this(required, posX, posY, result[0], questName, uniqueName,
				ingredients, result, requiredAltarTier, requiredMinimalWand);
	}

	public QuestCraftOnAltar(Quest required, int posX, int posY, ItemStack itemIcon, String questName, String uniqueName, 
			ItemStack[] ingredients, ItemStack[] result, int requiredAltarTier, EnumWand requiredMinimalWand) 
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
		this.ingredients = ingredients;
		this.result = result;
		this.requiredAltarTier = requiredAltarTier;
		this.requiredMinimalWand = requiredMinimalWand;
	}
	
	public int getIngredientsStackSize()
	{
		int stackSize = 0;
		for(int i = 0; i < ingredients.length; i++)
		{
			stackSize += ingredients[i].stackSize;
		}
		return stackSize;
	}
	
	public int getIngredientsInCauldronStackSize(List<EntityItem> entitiesInCauldron)
	{
		int stackSize = 0;
		for(int i = 0; i < entitiesInCauldron.size(); i++)
		{
			stackSize += entitiesInCauldron.get(i).getEntityItem().stackSize;
		}
		return stackSize;
	}
	
	@SubscribeEvent
	public void craftOnAltar(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		BlockPos cauldronPos = event.getPos();
		// player has got required wand in hand
		if(EnumWand.isWandInMainHandRight(player, requiredMinimalWand.wandTier))
		{
			World world = player.worldObj;
			// is right-clicking on Cauldron
			if(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
			{
				// is altair build correct
				if(AltarChecker.checkAltarTier(world, cauldronPos, requiredAltarTier))
				{
					List<EntityItem> entitiesInCauldron = CauldronHelper.getItemsInCauldron(world, cauldronPos);
					// there is a right amount of items in Cauldron but are they the right items ?
					int ingredientsStackSize = getIngredientsStackSize();
					int ingredientsInCauldronStackSize = getIngredientsInCauldronStackSize(entitiesInCauldron);
					if(ingredientsStackSize == ingredientsInCauldronStackSize)
					{
						List<EntityItem> alreadyCheckedEntityItems = new ArrayList<EntityItem>(); // items to be deleted later
						for(int i = 0; i < ingredients.length; i++)
						{
							ItemStack currentlyCheckedIngredient = ingredients[i];
							for(int j = 0; j < entitiesInCauldron.size(); j++)
							{
								EntityItem currentlyCheckedEntityItem = entitiesInCauldron.get(j);
								if(ItemStack.areItemStacksEqual(currentlyCheckedIngredient, currentlyCheckedEntityItem.getEntityItem()))
								{
									alreadyCheckedEntityItems.add(currentlyCheckedEntityItem);
									break;
								}
							}
						}
						// the amount of items in Cauldron was right and the items themselfs were right
						if(ingredients.length == alreadyCheckedEntityItems.size())
						{
							if(!player.hasAchievement(achievement))
							{
								player.addStat(achievement, 1);
							}
							if(player.hasAchievement(achievement))
							{
								for(int i = 0; i < alreadyCheckedEntityItems.size(); i++)
								{
									world.removeEntity(alreadyCheckedEntityItems.get(i));
								}
								BlockPos newItemPos = new BlockPos(cauldronPos.getX(), cauldronPos.getY() + 1, cauldronPos.getZ());
								// Spawn all the results
								for(int i = 0; i < result.length; i++)
								{
									Block.spawnAsEntity(world, newItemPos, result[i].copy());
								}
								world.updateEntities();
							}
						}
					}
				}
			}
		}
	}
}