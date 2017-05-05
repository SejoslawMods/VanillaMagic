package seia.vanillamagic.quest.mobspawnerdrop;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.ItemStackHelper;

public class QuestMobSpawnerDrop extends Quest
{
	/**
	 * When MobSPawner is broke drop:
	 * 1. MobSpawner in form of block
	 * 2. Enchanted Book with Entity from spawner.
	 */
	@SubscribeEvent
	public void onMobSpawnerBreak(BreakEvent event)
	{
		IBlockState spawnerState = event.getState();
		Block spawnerBlock = spawnerState.getBlock();
		if(spawnerBlock instanceof BlockMobSpawner) // if we broke MobSpawner
		{
			EntityPlayer player = event.getPlayer();
			if(!player.hasAchievement(achievement))
			{
				player.addStat(achievement, 1);
			}
			if(player.hasAchievement(achievement))
			{
				BlockPos spawnerPos = event.getPos();
				World world = event.getWorld();
				TileEntity tile = world.getTileEntity(spawnerPos);
				if(tile == null) // just for safety purposes
				{
					return;
				}
				if(tile instanceof TileEntityMobSpawner)
				{
					TileEntityMobSpawner tileMobSpawner = (TileEntityMobSpawner) tile;
					// Spawn Block Spawner as EntityItem
					ItemStack spawnerStack = new ItemStack(spawnerBlock);
					EntityItem spawnerEI = new EntityItem(world, 
							spawnerPos.getX() + 0.5D, 
							spawnerPos.getY(), 
							spawnerPos.getZ() + 0.5D, 
							spawnerStack);
					world.spawnEntity(spawnerEI);
					// Spawn Enchanted Book with TileEntityMobSpawner data as EntityItem
					ItemStack bookWithData = MobSpawnerRegistry.getStackFromTile(tileMobSpawner);
					EntityItem spawnerBook = new EntityItem(world, 
							spawnerPos.getX() + 0.5D, 
							spawnerPos.getY(), 
							spawnerPos.getZ() + 0.5D, 
							bookWithData);
					world.spawnEntity(spawnerBook);
					world.removeTileEntity(spawnerPos);
				}
			}
		}
	}
	
	/**
	 * On right-click MobSpawner with Entity book, change MobSpanwer Entity id to the id form book.
	 */
	@SubscribeEvent
	public void onRightClickWithBook(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		if(!player.hasAchievement(achievement))
		{
			return;
		}
		ItemStack rightHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(rightHand))
		{
			return;
		}
		NBTTagCompound stackTag = rightHand.getTagCompound();
		if(stackTag == null)
		{
			return;
		}
		if(stackTag.hasKey(MobSpawnerRegistry.NBT_MOB_SPAWNER_DATA))
		{
			String id = stackTag.getString(MobSpawnerRegistry.NBT_ID); // Cow, Creeper, Pig, etc.
			World world = event.getWorld();
			BlockPos spawnerPos = event.getPos();
			TileEntity tile = world.getTileEntity(spawnerPos);
			if(tile == null)
			{
				return;
			}
			if(tile instanceof TileEntityMobSpawner)
			{
				TileEntityMobSpawner tileMS = (TileEntityMobSpawner) tile;
				MobSpawnerRegistry.setID(tileMS, id, world, event.getPos());
				player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStackHelper.NULL_STACK);
			}
		}
	}
}