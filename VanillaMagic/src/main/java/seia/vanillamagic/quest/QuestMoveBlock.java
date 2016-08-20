package seia.vanillamagic.quest;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.ItemStackHelper;
import seia.vanillamagic.utils.NBTHelper;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestMoveBlock extends Quest
{
	/*
	 * Stack offHand must be an item that has maxStackSize = 1
	 * If it is a book, it must be a renamed book.
	 */
	public final ItemStack requiredStackOffHand;
	public final EnumWand requiredWand;

	public QuestMoveBlock(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName, 
			ItemStack requiredStackOffHand, EnumWand requiredWand)
	{
		super(required, posX, posY, icon, questName, uniqueName);
		this.requiredStackOffHand = requiredStackOffHand;
		this.requiredWand = requiredWand;
	}
	
	/**
	 * This event is fired twice. And I want it to fire only once.
	 */
	int clickedTimes = 0;
	@SubscribeEvent
	public void onRightClick(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		BlockPos wantedBlockPos = event.getPos();
		World world = player.worldObj;
		try
		{
			EnumWand wandPlayerHand = EnumWand.getWandByItemStack(player.getHeldItemMainhand());
			if(EnumWand.areWandsEqual(requiredWand, wandPlayerHand))
			{
				if(player.isSneaking())
				{
					ItemStack stackOffHand = player.getHeldItemOffhand();
					if(ItemStack.areItemsEqual(requiredStackOffHand, stackOffHand))
					{
						if(!player.hasAchievement(achievement))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							/*
							 * can be write to ONLY one item
							 * IGNORES THE offHandRequiredStack.stackSize
							 */
							if(stackOffHand.stackSize == 1)
							{
								if(clickedTimes > 0)
								{
									clickedTimes = 0;
									return;
								}
								handleSave(world, player, wantedBlockPos, event.getFace());
								clickedTimes++;
							}
						}
						return;
					}
					NBTTagCompound stackTag = stackOffHand.getTagCompound();
					if(stackTag.hasKey(NBTHelper.NBT_TAG_COMPOUND_NAME))
					{
						if(clickedTimes > 0)
						{
							clickedTimes = 0;
							return;
						}
						handleLoad(world, player, wantedBlockPos, event.getFace());
						clickedTimes++;
					}
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	public void handleSave(World world, EntityPlayer player, BlockPos wantedBlockPos, EnumFacing hittedFace)
	{
		// changing name just to force Minecraft to let this item have NBTTagCompound
		player.getHeldItemOffhand().setStackDisplayName("QuestBook");
		// any item will be replaced to Enchanted Book
		ItemStack stackOffHand = ItemStackHelper.replaceItemInStack(player.getHeldItemOffhand(), Items.ENCHANTED_BOOK);
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();
		// Save to ItemStack
		if(!stackTagCompound.hasKey(NBTHelper.NBT_TAG_COMPOUND_NAME))
		{
			NBTTagCompound questTag = new NBTTagCompound();
			Block wantedBlock = world.getBlockState(wantedBlockPos).getBlock();
			int blockID = Block.REGISTRY.getIDForObject(wantedBlock);
			int meta = wantedBlock.getMetaFromState(world.getBlockState(wantedBlockPos));
			questTag.setInteger(NBTHelper.NBT_BLOCK_ID, blockID);
			questTag.setInteger(NBTHelper.NBT_BLOCK_META, meta);
			questTag = NBTHelper.setBlockPosDataToNBT(wantedBlockPos, questTag);
			stackOffHand.setStackDisplayName("Saved block: " + wantedBlock.getLocalizedName());
			TileEntity tileEntity = world.getTileEntity(wantedBlockPos);
			if(tileEntity != null)
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
				world.notifyBlockOfStateChange(wantedBlockPos, readdedBlock);
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
			newOffHand.stackSize = 1;
			player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, null);
			player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, newOffHand);
			return;
		}
	}
}