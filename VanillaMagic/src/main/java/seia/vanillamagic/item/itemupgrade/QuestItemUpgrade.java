package seia.vanillamagic.item.itemupgrade;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry.ItemEntry;
import seia.vanillamagic.item.itemupgrade.upgrade.IItemUpgrade;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.util.CauldronHelper;

public class QuestItemUpgrade extends Quest
{
	// TODO: Fix crafting upgrade
	@SubscribeEvent
	public void craftUpgrade(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos clickedPos = event.getPos();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(rightHand == null)
		{
			return;
		}
		if(EnumWand.areWandsEqual(EnumWand.BLAZE_ROD.wandItemStack, rightHand))
		{
			IBlockState clickedBlock = world.getBlockState(clickedPos);
			if(Block.isEqualTo(clickedBlock.getBlock(), Blocks.CAULDRON))
			{
				List<EntityItem> inCauldron = CauldronHelper.getItemsInCauldron(world, clickedPos);
				ItemStack base = getBaseStack(inCauldron);
				if(base == null)
				{
					return;
				}
				if(!canGetUpgrade(base))
				{
					return;
				}
				ItemStack ingredient = getIngredient(inCauldron);
				if(ingredient == null)
				{
					return;
				}
				ItemStack craftingResult = ItemUpgradeRegistry.INSTANCE.getResult(base, ingredient);
				if(craftingResult == null)
				{
					return;
				}
				if(!player.hasAchievement(achievement))
				{
					player.addStat(achievement, 1);
				}
				else if(player.hasAchievement(achievement))
				{
					for(EntityItem ei : inCauldron)
					{
						world.removeEntity(ei);
					}
					EntityItem craftingResultEntity = new EntityItem(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(), craftingResult);
					world.spawnEntityInWorld(craftingResultEntity);
				}
			}
		}
	}

	public boolean canGetUpgrade(ItemStack base) 
	{
		NBTTagCompound tag = base.getTagCompound();
		if(tag == null)
		{
			return false;
		}
		return !tag.getBoolean(IItemUpgrade.NBT_ITEM_CONTAINS_UPGRADE);
	}

	@Nullable
	public ItemStack getIngredient(List<EntityItem> inCauldron) 
	{
		for(EntityItem ei : inCauldron)
		{
			if(!isBaseItem(ei.getEntityItem()))
			{
				return ei.getEntityItem();
			}
		}
		return null;
	}

	@Nullable
	public ItemStack getBaseStack(List<EntityItem> inCauldron) 
	{
		for(EntityItem ei : inCauldron)
		{
			if(isBaseItem(ei.getEntityItem()))
			{
				return ei.getEntityItem();
			}
		}
		return null;
	}

	public boolean isBaseItem(ItemStack entityItem) 
	{
		for(ItemEntry ie : ItemUpgradeRegistry.INSTANCE.getBaseItems())
		{
			if(entityItem.getItem() == ie.item)
			{
				if(entityItem.stackSize == ie.stack.stackSize)
				{
					return true;
				}
			}
		}
		return false;
	}
}