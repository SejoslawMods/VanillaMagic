package seia.vanillamagic.quest;

import java.util.List;

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
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.api.quest.QuestMoveBlockRegistry;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.mobspawnerdrop.MobSpawnerRegistry;
import seia.vanillamagic.util.EventUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.NBTUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMoveBlock extends Quest {
	/*
	 * Stack offHand must be an item that has maxStackSize = 1 if it is a book, it
	 * must be a renamed book.
	 */
	protected ItemStack requiredStackOffHand;
	protected IWand requiredWand;

	public void readData(JsonObject jo) {
		this.requiredStackOffHand = ItemStackUtil
				.getItemStackFromJSON(jo.get("requiredStackOffHand").getAsJsonObject());
		this.requiredWand = WandRegistry.getWandByTier(jo.get("wandTier").getAsInt());
		this.icon = requiredStackOffHand.copy();
		super.readData(jo);
	}

	public ItemStack getRequiredStackOffHand() {
		return requiredStackOffHand;
	}

	public IWand getRequiredWand() {
		return requiredWand;
	}

	/**
	 * This event is fired twice. And I want it to fire only once.
	 */
	int clickedTimes = 0;

	@SubscribeEvent
	public void onRightClick(RightClickBlock event) throws ReflectiveOperationException, RuntimeException {
		EntityPlayer player = event.getEntityPlayer();
		BlockPos wantedBlockPos = event.getPos();
		World world = player.world;
		ItemStack mainHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(mainHand)) {
			return;
		}

		IWand wandPlayerHand = WandRegistry.getWandByItemStack(mainHand);
		if ((wandPlayerHand == null) || !WandRegistry.areWandsEqual(requiredWand, wandPlayerHand)
				|| !player.isSneaking()) {
			return;
		}

		ItemStack stackOffHand = player.getHeldItemOffhand();

		if (ItemStackUtil.isNullStack(stackOffHand)) {
			return;
		}

		if (ItemStack.areItemsEqual(requiredStackOffHand, stackOffHand)) {
			if (ItemStackUtil.getStackSize(stackOffHand) == ItemStackUtil.getStackSize(requiredStackOffHand)) {
				checkQuestProgress(player);

				if (hasQuest(player)) {
					if (clickedTimes > 0) {
						clickedTimes = 0;
						return;
					}

					if (!EventUtil.postEvent(
							new BreakEvent(world, wantedBlockPos, world.getBlockState(wantedBlockPos), player))) {
						handleSave(world, player, wantedBlockPos, event.getFace());
						clickedTimes++;
					}
				}
			}
			return;
		}

		NBTTagCompound stackTag = stackOffHand.getTagCompound();
		if ((stackTag == null) || !stackTag.hasKey(NBTUtil.NBT_TAG_COMPOUND_NAME)) {
			return;
		}

		if (clickedTimes > 0) {
			clickedTimes = 0;
			return;
		}

		handleLoad(world, player, wantedBlockPos, event.getFace());
		clickedTimes++;
	}

	public void handleSave(World world, EntityPlayer player, BlockPos wantedBlockPos, EnumFacing hittedFace) {
		Block wantedBlock = world.getBlockState(wantedBlockPos).getBlock();

		if (VMConfig.ENABLE_MOVE_BLOCK_BLACKLIST && QuestMoveBlockRegistry.isBlockOnMoveBlockBlacklist(wantedBlock)) {
			return;
		}

		ItemStack stackOffHand = new ItemStack(Items.ENCHANTED_BOOK);
		stackOffHand.setStackDisplayName("QuestBook");
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();

		if (stackTagCompound.hasKey(NBTUtil.NBT_TAG_COMPOUND_NAME)) {
			return;
		}

		NBTTagCompound questTag = new NBTTagCompound();
		int blockID = Block.REGISTRY.getIDForObject(wantedBlock);
		int meta = wantedBlock.getMetaFromState(world.getBlockState(wantedBlockPos));
		questTag.setInteger(NBTUtil.NBT_BLOCK_ID, blockID);
		questTag.setInteger(NBTUtil.NBT_BLOCK_META, meta);
		questTag = NBTUtil.setBlockPosDataToNBT(questTag, wantedBlockPos, world);
		stackOffHand.setStackDisplayName("Saved block: " + wantedBlock.getLocalizedName());
		TileEntity tileEntity = world.getTileEntity(wantedBlockPos);

		if (tileEntity instanceof TileEntityMobSpawner) {
			TileEntityMobSpawner tileMS = (TileEntityMobSpawner) tileEntity;
			questTag = tileEntity.writeToNBT(questTag);
			stackOffHand.setStackDisplayName("[TileEntity] " + stackOffHand.getDisplayName());
			String name = MobSpawnerRegistry.getNameFromBaseLogic(tileMS.getSpawnerBaseLogic());
			stackOffHand.setStackDisplayName(stackOffHand.getDisplayName() + " - " + name);
		} else if (tileEntity != null) {
			questTag = tileEntity.writeToNBT(questTag);
			stackOffHand.setStackDisplayName("[TileEntity] " + stackOffHand.getDisplayName());
		}

		world.removeTileEntity(wantedBlockPos);
		questTag.removeTag("x");
		questTag.removeTag("y");
		questTag.removeTag("z");
		stackTagCompound.setTag(NBTUtil.NBT_TAG_COMPOUND_NAME, questTag);
		player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stackOffHand);
		world.setBlockToAir(wantedBlockPos);
	}

	public void handleLoad(World world, EntityPlayer player, BlockPos wantedBlockPos, EnumFacing hittedFace) {
		ItemStack stackOffHand = player.getHeldItemOffhand();
		NBTTagCompound stackTagCompound = stackOffHand.getTagCompound();

		if (!stackTagCompound.hasKey(NBTUtil.NBT_TAG_COMPOUND_NAME)) {
			return;
		}

		NBTTagCompound questTag = stackTagCompound.getCompoundTag(NBTUtil.NBT_TAG_COMPOUND_NAME);
		/*
		 * Unload from ItemStack (place block) (remove offHandItem -> place in offHand
		 * new ItemStack from the requiredStackOffHand but with stackSize 1)
		 */
		wantedBlockPos = wantedBlockPos.offset(hittedFace);
		if (!world.isAirBlock(wantedBlockPos)) {
			return;
		}

		Block readdedBlock = Block.REGISTRY.getObjectById(questTag.getInteger(NBTUtil.NBT_BLOCK_ID));

		if (readdedBlock != null) {
			if (EventUtil.postEvent(
					new PlaceEvent(new BlockSnapshot(world, wantedBlockPos, world.getBlockState(wantedBlockPos)),
							world.getBlockState(wantedBlockPos), player, EnumHand.OFF_HAND))) {
				return;
			}

			world.setBlockState(wantedBlockPos,
					readdedBlock.getStateFromMeta(questTag.getInteger(NBTUtil.NBT_BLOCK_META)));
			world.notifyNeighborsOfStateChange(wantedBlockPos, readdedBlock, true); // TODO: ???
		}

		TileEntity tile = world.getTileEntity(wantedBlockPos);

		if (tile != null) {
			questTag.setInteger("x", wantedBlockPos.getX());
			questTag.setInteger("y", wantedBlockPos.getY());
			questTag.setInteger("z", wantedBlockPos.getZ());
			tile.readFromNBT(questTag);
		}

		ItemStack newOffHand = requiredStackOffHand.copy();
		player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, newOffHand);
	}

	@SubscribeEvent
	public void addSavedBlockDataTooltip(ItemTooltipEvent event) {
		ItemStack savedPosBook = event.getItemStack();
		NBTTagCompound savedPosTagCompound = savedPosBook.getTagCompound();

		if ((savedPosTagCompound != null) && savedPosTagCompound.hasKey(NBTUtil.NBT_TAG_COMPOUND_NAME)) {
			NBTTagCompound questTag = savedPosTagCompound.getCompoundTag(NBTUtil.NBT_TAG_COMPOUND_NAME);
			Block readdedBlock = Block.REGISTRY.getObjectById(questTag.getInteger(NBTUtil.NBT_BLOCK_ID));

			if (readdedBlock != null) {
				List<String> blockInfo = event.getToolTip();
				blockInfo.add("Saved Block: " + readdedBlock.getLocalizedName());
				blockInfo.add("Saved Block MetaData: " + questTag.getInteger(NBTUtil.NBT_BLOCK_META));
			}
		}
	}
}