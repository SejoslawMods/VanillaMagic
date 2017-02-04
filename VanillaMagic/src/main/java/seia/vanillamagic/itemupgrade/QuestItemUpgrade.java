package seia.vanillamagic.itemupgrade;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.item.itemupgrade.IItemUpgrade;
import seia.vanillamagic.itemupgrade.ItemUpgradeRegistry.ItemEntry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.util.CauldronHelper;
import seia.vanillamagic.util.ItemStackHelper;

public class QuestItemUpgrade extends Quest
{
	int times = 0;
	@SubscribeEvent
	public void craftUpgrade(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
//		if(world.isRemote)
//		{
//			return;
//		}
		
		if(times == 0)
		{
			times++;
		}
		else
		{
			times = 0;
			return;
		}
		BlockPos clickedPos = event.getPos();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(rightHand))
		{
			return;
		}
		if(EnumWand.areWandsEqual(EnumWand.BLAZE_ROD.wandItemStack, rightHand))
		{
			IBlockState clickedBlock = world.getBlockState(clickedPos);
			if(clickedBlock.getBlock() instanceof BlockCauldron)
			{
				List<EntityItem> inCauldron = CauldronHelper.getItemsInCauldron(world, clickedPos);
				ItemStack base = getBaseStack(inCauldron);
				if(base == null)
				{
					return;
				}
				if(ItemStackHelper.isNullStack(base))
				{
					return;
				}
				if(!canGetUpgrade(base))
				{
					return;
				}
				ItemStack ingredient = getIngredient(inCauldron);
				if(ItemStackHelper.isNullStack(ingredient))
				{
					return;
				}
				ItemStack craftingResult = ItemUpgradeRegistry.getResult(base, ingredient);
				if(ItemStackHelper.isNullStack(craftingResult))
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
					world.spawnEntity(craftingResultEntity);
					// Particle + sound
					{
						world.playSound((double)clickedPos.getX() + 0.5D, (double)clickedPos.getY(), (double)clickedPos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
						Random rand = new Random();
						double particleX = (double)clickedPos.getX() + 0.5D;
						double particleY = (double)clickedPos.getY() + rand.nextDouble() * 6.0D / 16.0D;
						double particleZ = (double)clickedPos.getZ() + 0.5D;
						double randomPos = rand.nextDouble() * 0.6D - 0.3D;
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX - 0.52D, particleY, particleZ + randomPos, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnParticle(EnumParticleTypes.FLAME, particleX - 0.52D, particleY, particleZ + randomPos, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX + 0.52D, particleY, particleZ + randomPos, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnParticle(EnumParticleTypes.FLAME, particleX + 0.52D, particleY, particleZ + randomPos, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX + randomPos, particleY, particleZ - 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnParticle(EnumParticleTypes.FLAME, particleX + randomPos, particleY, particleZ - 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particleX + randomPos, particleY, particleZ + 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
						world.spawnParticle(EnumParticleTypes.FLAME, particleX + randomPos, particleY, particleZ + 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
					}
				}
			}
		}
	}

	public boolean canGetUpgrade(ItemStack base) 
	{
		base.setStackDisplayName(base.getDisplayName() + " ");
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
		for(ItemEntry ie : ItemUpgradeRegistry.getBaseItems())
		{
			if(entityItem.getItem() == ie.item)
			{
				if(ItemStackHelper.getStackSize(entityItem) == ItemStackHelper.getStackSize(ie.stack))
				{
					return true;
				}
			}
		}
		return false;
	}
}