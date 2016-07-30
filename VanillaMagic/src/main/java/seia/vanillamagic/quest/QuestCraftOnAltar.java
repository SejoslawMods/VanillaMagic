package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.AltarChecker;
import seia.vanillamagic.utils.CauldronHelper;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestCraftOnAltar extends Quest
{
	public final ItemStack[] ingredients; // each ItemStack is a different Item
	public final ItemStack result;
	public final int requiredAltarTier;
	public final EnumWand requiredMinimalWand;
	
	public QuestCraftOnAltar(Achievement required, int posX, int posY, String questName, String uniqueName, 
			ItemStack[] ingredients, ItemStack result, int requiredAltarTier, EnumWand requiredMinimalWand)
	{
		this(required, posX, posY, result.getItem(), questName, uniqueName, 
				ingredients, result, requiredAltarTier, requiredMinimalWand);
	}
	
	public QuestCraftOnAltar(Achievement required, int posX, int posY, Item itemIcon, String questName, String uniqueName, 
			ItemStack[] ingredients, ItemStack result, int requiredAltarTier, EnumWand requiredMinimalWand) 
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
		try
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
									Block.spawnAsEntity(world, newItemPos, result);
									world.updateEntities();
									return;
								}
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
}