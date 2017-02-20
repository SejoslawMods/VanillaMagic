package seia.vanillamagic.quest;

import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.event.EventMoveBlock;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.quest.QuestMoveBlockRegistry;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.mobspawnerdrop.MobSpawnerRegistry;
import seia.vanillamagic.util.ItemStackHelper;
import seia.vanillamagic.util.NBTHelper;

public class QuestMoveBlock extends Quest
{
	/*
	 * Stack offHand must be an item that has maxStackSize = 1
	 * If it is a book, it must be a renamed book.
	 */
	protected ItemStack requiredStackOffHand;
	protected IWand requiredWand;
	
	public void readData(JsonObject jo)
	{
		this.requiredStackOffHand = ItemStackHelper.getItemStackFromJSON(jo.get("requiredStackOffHand").getAsJsonObject());
		this.requiredWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
		this.icon = requiredStackOffHand.copy();
		super.readData(jo);
	}
	
	public ItemStack getRequiredStackOffHand()
	{
		return requiredStackOffHand;
	}
	
	public IWand getRequiredWand()
	{
		return requiredWand;
	}
	
	/**
	 * This event is fired twice. And I want it to fire only once.
	 */
	int clickedTimes = 0;
	@SubscribeEvent
	public void onRightClick(RightClickBlock event) 
			throws ReflectiveOperationException, RuntimeException
	{
		EntityPlayer player = event.getEntityPlayer();
		BlockPos wantedBlockPos = event.getPos();
		World world = player.world;
		ItemStack mainHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(mainHand))
		{
			return;
		}
		IWand wandPlayerHand = WandRegistry.getWandByItemStack(mainHand);
		if(wandPlayerHand == null)
		{
			return;
		}
		if(WandRegistry.areWandsEqual(requiredWand, wandPlayerHand))
		{
			if(player.isSneaking())
			{
				ItemStack stackOffHand = player.getHeldItemOffhand();
				if(ItemStackHelper.isNullStack(stackOffHand))
				{
					return;
				}
				if(ItemStack.areItemsEqual(requiredStackOffHand, stackOffHand))
				{
					if(ItemStackHelper.getStackSize(stackOffHand) == ItemStackHelper.getStackSize(requiredStackOffHand))
					{
						if(canPlayerGetAchievement(player))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							if(clickedTimes > 0)
							{
								clickedTimes = 0;
								return;
							}
//							handleSave(world, player, wantedBlockPos, event.getFace());
//							clickedTimes++;
							if(!MinecraftForge.EVENT_BUS.post(new EventMoveBlock.Save(
									world, player, wantedBlockPos, event.getFace())))
							{
								handleSave(world, player, wantedBlockPos, event.getFace());
								clickedTimes++;
							}
						}
					}
					return;
				}
				NBTTagCompound stackTag = stackOffHand.getTagCompound();
				if(stackTag == null)
				{
					return;
				}
				if(stackTag.hasKey(NBTHelper.NBT_TAG_COMPOUND_NAME))
				{
					if(clickedTimes > 0)
					{
						clickedTimes = 0;
						return;
					}
//					handleLoad(world, player, wantedBlockPos, event.getFace());
//					clickedTimes++;
					if(!MinecraftForge.EVENT_BUS.post(new EventMoveBlock.Load(
							world, player, wantedBlockPos, event.getFace())))
					{
						handleLoad(world, player, wantedBlockPos, event.getFace());
						clickedTimes++;
					}
				}
			}
		}
	}
	
	public void handleSave(World world, EntityPlayer player, BlockPos wantedBlockPos, EnumFacing hittedFace)
	{
		Block wantedBlock = world.getBlockState(wantedBlockPos).getBlock();
		if(VMConfig.enableMoveBlockBlacklist)
		{
			if(QuestMoveBlockRegistry.isBlockOnMoveBlockBlacklist(wantedBlock)) // If Block is blacklisted than don't save it 
			{
				return;
			}
		}
		ItemStack stackOffHand = new ItemStack(Items.ENCHANTED_BOOK);
		stackOffHand.setStackDisplayName("QuestBook");
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();
		// Save to ItemStack
		if(!stackTagCompound.hasKey(NBTHelper.NBT_TAG_COMPOUND_NAME))
		{
			NBTTagCompound questTag = new NBTTagCompound();
//			Block wantedBlock = world.getBlockState(wantedBlockPos).getBlock();
			int blockID = Block.REGISTRY.getIDForObject(wantedBlock);
			int meta = wantedBlock.getMetaFromState(world.getBlockState(wantedBlockPos));
			questTag.setInteger(NBTHelper.NBT_BLOCK_ID, blockID);
			questTag.setInteger(NBTHelper.NBT_BLOCK_META, meta);
			questTag = NBTHelper.setBlockPosDataToNBT(questTag, wantedBlockPos, world);
			stackOffHand.setStackDisplayName("Saved block: " + wantedBlock.getLocalizedName());
			TileEntity tileEntity = world.getTileEntity(wantedBlockPos);
			if(tileEntity instanceof TileEntityMobSpawner)
			{
				TileEntityMobSpawner tileMS = (TileEntityMobSpawner) tileEntity;
				questTag = tileEntity.writeToNBT(questTag);
				stackOffHand.setStackDisplayName("[TileEntity] " + stackOffHand.getDisplayName());
				String name = MobSpawnerRegistry.getNameFromBaseLogic(tileMS.getSpawnerBaseLogic());
				stackOffHand.setStackDisplayName(stackOffHand.getDisplayName() + " - " + name);
			}
			else if(tileEntity != null)
			{
				questTag = tileEntity.writeToNBT(questTag);
				stackOffHand.setStackDisplayName("[TileEntity] " + stackOffHand.getDisplayName());
			}
			world.removeTileEntity(wantedBlockPos);
			questTag.removeTag("x");
			questTag.removeTag("y");
			questTag.removeTag("z");
			stackTagCompound.setTag(NBTHelper.NBT_TAG_COMPOUND_NAME, questTag);
			player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stackOffHand);
			world.setBlockToAir(wantedBlockPos);
		}
	}
	
	public void handleLoad(World world, EntityPlayer player, BlockPos wantedBlockPos, EnumFacing hittedFace)
	{
		ItemStack stackOffHand = player.getHeldItemOffhand();
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();
		if(!stackTagCompound.hasKey(NBTHelper.NBT_TAG_COMPOUND_NAME))
		{
			return;
		}
		NBTTagCompound questTag = stackTagCompound.getCompoundTag(NBTHelper.NBT_TAG_COMPOUND_NAME);
		/*
		 * Unload from ItemStack 
		 * (place block) 
		 * (remove offHandItem -> place in offHand new ItemStack from the requiredStackOffHand but with stackSize 1)
		 */
		wantedBlockPos = wantedBlockPos.offset(hittedFace);
		if(world.isAirBlock(wantedBlockPos))
		{
			Block readdedBlock = Block.REGISTRY.getObjectById(questTag.getInteger(NBTHelper.NBT_BLOCK_ID));
			if(readdedBlock != null)
			{
				world.setBlockState(wantedBlockPos, readdedBlock.getStateFromMeta(questTag.getInteger(NBTHelper.NBT_BLOCK_META)));
				//world.notifyBlockOfStateChange(wantedBlockPos, readdedBlock);
				world.notifyNeighborsOfStateChange(wantedBlockPos, readdedBlock, true); // TODO: ???
			}
			TileEntity tile = world.getTileEntity(wantedBlockPos);
			if(tile != null)
			{
				questTag.setInteger("x", wantedBlockPos.getX());
				questTag.setInteger("y", wantedBlockPos.getY());
				questTag.setInteger("z", wantedBlockPos.getZ());
				tile.readFromNBT(questTag);
			}
			ItemStack newOffHand = requiredStackOffHand.copy();
			player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, newOffHand);
		}
	}
}