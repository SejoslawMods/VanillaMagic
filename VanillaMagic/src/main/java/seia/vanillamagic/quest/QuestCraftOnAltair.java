package seia.vanillamagic.quest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.AltairChecker;

public class QuestCraftOnAltair extends Quest
{
	private ItemStack[] ingredients;
	private ItemStack result;
	private int requiredAltairTier;
	
	public QuestCraftOnAltair(Achievement required, int posX, int posY, String questName, String uniqueName, ItemStack[] ingredients, ItemStack result, int requiredAltairTier)
	{
		this(required, posX, posY, result.getItem(), questName, uniqueName, ingredients, result, requiredAltairTier);
	}
	
	public QuestCraftOnAltair(Achievement required, int posX, int posY, Item itemIcon, String questName, String uniqueName, ItemStack[] ingredients, ItemStack result, int requiredAltairTier) 
	{
		super(required, posX, posY, itemIcon, questName, uniqueName);
		this.ingredients = ingredients;
		this.result = result;
		this.requiredAltairTier = requiredAltairTier;
	}
	
	public ItemStack[] getIngredients()
	{
		return ingredients;
	}
	
	public ItemStack getResult()
	{
		return result;
	}
	
	public int getRequiredAltairTier()
	{
		return requiredAltairTier;
	}
	
	@SubscribeEvent
	public void craftOnAltair(RightClickBlock event)
	{
		try
		{
			EntityPlayer player = event.getEntityPlayer();
			BlockPos cauldronPos = event.getPos();
			
			// player has got stick in hand
			if(Items.STICK.equals(player.getHeldItemMainhand().getItem()))
			{
				World world = player.worldObj;
				// is right-clicking on Cauldron
				if(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
				{
					// is altair build correct
					if(AltairChecker.checkAltairTier(world, cauldronPos, requiredAltairTier))
					{
						// all entities on World
						List<Entity> loadedEntities = world.loadedEntityList;
						int howManyCorrect = 0;
						List<EntityItem> itemsToDelete = new ArrayList<EntityItem>();
						for(int i = 0; i < ingredients.length; i++)
						{
							for(int j = 0; j < loadedEntities.size(); j++)
							{
								if(loadedEntities.get(j) instanceof EntityItem)
								{
									int ingredientID = Item.getIdFromItem(ingredients[i].getItem());
									EntityItem loadedEntityItem = (EntityItem) loadedEntities.get(j);
									int entityItemID = Item.getIdFromItem(loadedEntityItem.getEntityItem().getItem());
									if(ingredientID == entityItemID)
									{
										BlockPos loadedEntityItemPos = new BlockPos(loadedEntityItem.posX, loadedEntityItem.posY, loadedEntityItem.posZ);
										if((cauldronPos.getX() == loadedEntityItemPos.getX()) &&
												(cauldronPos.getY() == loadedEntityItemPos.getY()) &&
												(cauldronPos.getZ() == loadedEntityItemPos.getZ()))
										{
											itemsToDelete.add(loadedEntityItem);
											howManyCorrect++;
											if(howManyCorrect == ingredients.length)
											{
												if(!player.hasAchievement(achievement))
												{
													player.addStat(achievement, 1);
													return;
												}
												else if(player.hasAchievement(achievement))
												{
													for(int k = 0; k < itemsToDelete.size(); k++)
													{
														world.removeEntity(itemsToDelete.get(k));
													}
													BlockPos newItemPos = new BlockPos(cauldronPos.getX(), cauldronPos.getY() + 1, cauldronPos.getZ());
													Block.spawnAsEntity(world, newItemPos, new ItemStack(result.getItem()));
													world.updateEntities();
													return;
												}
											}
										}
									}
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