package seia.vanillamagic.quest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class QuestMonsterSpawnerDropBlock extends Quest
{
	private static String SPAWNER_DATA = "SPAWNER_DATA";
	
//	@SubscribeEvent
	public void onMonsterSpawnerDestroy(BreakEvent event) // spawn BlockMobSpawner as new EntityItem and write data to NBT
	{
		IBlockState spawnerState = event.getState();
		Block spawnerBlock = spawnerState.getBlock();
		if(spawnerBlock instanceof BlockMobSpawner) // if we broke MobSpawner
		{
			BlockPos spawnerPos = event.getPos();
			World world = event.getWorld();
			TileEntityMobSpawner tileMobSpawner = (TileEntityMobSpawner) world.getTileEntity(spawnerPos);
			NBTTagCompound spawnerData = tileMobSpawner.writeToNBT(new NBTTagCompound());
			ItemStack spawnerStack = new ItemStack(spawnerBlock);
			spawnerStack.setStackDisplayName("Mob Spawner");
			spawnerStack.getTagCompound().setTag(SPAWNER_DATA, spawnerData);
			EntityItem spawnerEI = new EntityItem(world, spawnerPos.getX(), spawnerPos.getY(), spawnerPos.getZ(), spawnerStack);
			world.spawnEntityInWorld(spawnerEI);
		}
	}
	
//	@SubscribeEvent
	public void onMonsterSpawnerPlace(PlaceEvent event) // place BlockMobSpawner and read data from NBT
	{
	}
}