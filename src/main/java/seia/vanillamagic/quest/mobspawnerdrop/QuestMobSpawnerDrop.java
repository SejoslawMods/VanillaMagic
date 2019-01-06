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
import seia.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMobSpawnerDrop extends Quest {
	@SubscribeEvent
	public void onMobSpawnerBreak(BreakEvent event) {
		IBlockState spawnerState = event.getState();
		Block spawnerBlock = spawnerState.getBlock();

		if (!(spawnerBlock instanceof BlockMobSpawner)) {
			return;
		}

		EntityPlayer player = event.getPlayer();
		checkQuestProgress(player);

		if (!hasQuest(player)) {
			return;
		}

		BlockPos spawnerPos = event.getPos();
		World world = event.getWorld();
		TileEntity tile = world.getTileEntity(spawnerPos);

		if ((tile == null) || !(tile instanceof TileEntityMobSpawner)) {
			return;
		}

		TileEntityMobSpawner tileMobSpawner = (TileEntityMobSpawner) tile;
		ItemStack spawnerStack = new ItemStack(spawnerBlock);
		EntityItem spawnerEI = new EntityItem(world, spawnerPos.getX() + 0.5D, spawnerPos.getY(),
				spawnerPos.getZ() + 0.5D, spawnerStack);
		world.spawnEntity(spawnerEI);

		ItemStack bookWithData = MobSpawnerRegistry.getStackFromTile(tileMobSpawner);
		EntityItem spawnerBook = new EntityItem(world, spawnerPos.getX() + 0.5D, spawnerPos.getY(),
				spawnerPos.getZ() + 0.5D, bookWithData);
		world.spawnEntity(spawnerBook);
		world.removeTileEntity(spawnerPos);
	}

	@SubscribeEvent
	public void onRightClickWithBook(RightClickBlock event) {
		EntityPlayer player = event.getEntityPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (!hasQuest(player) || ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		NBTTagCompound stackTag = rightHand.getTagCompound();

		if ((stackTag == null) || !stackTag.hasKey(MobSpawnerRegistry.NBT_MOB_SPAWNER_DATA)) {
			return;
		}

		String id = stackTag.getString(MobSpawnerRegistry.NBT_ID);
		World world = event.getWorld();
		BlockPos spawnerPos = event.getPos();
		TileEntity tile = world.getTileEntity(spawnerPos);

		if ((tile == null) || !(tile instanceof TileEntityMobSpawner)) {
			return;
		}

		TileEntityMobSpawner tileMS = (TileEntityMobSpawner) tile;
		MobSpawnerRegistry.setID(tileMS, id, world, event.getPos());
		player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStackUtil.NULL_STACK);
	}
}