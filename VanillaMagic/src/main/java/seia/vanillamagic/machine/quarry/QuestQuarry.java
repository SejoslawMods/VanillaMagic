package seia.vanillamagic.machine.quarry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.quest.QuestMachineActivate;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestQuarry extends QuestMachineActivate
{
	public QuestQuarry(Quest required, int posX, int posY, String questName, String uniqueName) 
	{
		super(required, posX, posY, new ItemStack(Items.CAULDRON), questName, uniqueName,
				null, EnumWand.BLAZE_ROD.wandItemStack.copy());
	}

	@SubscribeEvent
	public void startQuarry(RightClickBlock event)
	{
		BlockPos quarryPos = event.getPos();
		EntityPlayer player = event.getEntityPlayer();
		ItemStack itemInHand = player.getHeldItemMainhand();
		World world = player.worldObj;
		try
		{
			if(!player.isSneaking())
			{
				return;
			}
			if(itemInHand.getItem().equals(EnumWand.BLAZE_ROD.wandItemStack.copy().getItem()))
			{
				BlockPos diamondBlockPos = new BlockPos(quarryPos.getX() + 1, quarryPos.getY(), quarryPos.getZ());
				if(!Block.isEqualTo(world.getBlockState(diamondBlockPos).getBlock(), Blocks.DIAMOND_BLOCK))
				{
					BlockPos redstoneBlockPos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() - 1);
					if(!Block.isEqualTo(world.getBlockState(redstoneBlockPos).getBlock(), Blocks.REDSTONE_BLOCK))
					{
						return;
					}
				}
				// Should now throw exception from constructor if blocks are wrong.
				TileQuarry tileQuarry = new TileQuarry(quarryPos, player, this.mustHaveMainHand);
				if(tileQuarry.isComplete())
				{
					if(!player.hasAchievement(achievement))
					{
						player.addStat(achievement, 1);
					}
					if(player.hasAchievement(achievement))
					{
						tileQuarry.showBoundingBox();
						TileEntity exist = world.getTileEntity(quarryPos);
						if(exist == null)
						{
							world.addTileEntity(tileQuarry);
							world.setTileEntity(tileQuarry.getMachinePos(), tileQuarry);
							System.out.println("Quarry registered at:");
							BlockPosHelper.printCoords(tileQuarry.getMachinePos());
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Incorrect Quarry placed on:");
			BlockPosHelper.printCoords(quarryPos);
		}
	}

	@SubscribeEvent
	public void stopQuarry(BreakEvent event)
	{
		BlockPos quarryPos = event.getPos();
		EntityPlayer player = event.getPlayer();
		World world = player.worldObj;
		Block quarryBlock = world.getBlockState(quarryPos).getBlock();
		try
		{
			BlockPos cauldronPos = null;
			if(quarryBlock instanceof BlockCauldron)
			{
				cauldronPos = quarryPos;
			}
			else if(Block.isEqualTo(quarryBlock, Blocks.REDSTONE_BLOCK))
			{
				cauldronPos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() + 1);
			}
			else if(Block.isEqualTo(quarryBlock, Blocks.DIAMOND_BLOCK))
			{
				cauldronPos = new BlockPos(quarryPos.getX() - 1, quarryPos.getY(), quarryPos.getZ());
			}
			
			if(cauldronPos != null)
			{
				BlockPos redstonePos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() - 1);
				Block redstoneBlock = world.getBlockState(redstonePos).getBlock();
				if(Block.isEqualTo(redstoneBlock, Blocks.REDSTONE_BLOCK))
				{
					BlockPos diamondBlockPos = new BlockPos(quarryPos.getX() + 1, quarryPos.getY(), quarryPos.getZ());
					Block diamondBlock = world.getBlockState(diamondBlockPos).getBlock();
					if(Block.isEqualTo(diamondBlock, Blocks.DIAMOND_BLOCK))
					{
						for(int i = 0; i < world.tickableTileEntities.size(); i++)
						{
							TileEntity tile = world.tickableTileEntities.get(i);
							if(tile instanceof TileQuarry)
							{
								if(BlockPosHelper.isSameBlockPos(tile.getPos(), cauldronPos))
								{
									world.tickableTileEntities.remove(i);
									world.removeTileEntity(cauldronPos);
									System.out.println("Quarry removed at:");
									BlockPosHelper.printCoords(cauldronPos);
								}
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Incorrect Quarry broke on:");
			BlockPosHelper.printCoords(quarryBlock, quarryPos);
		}
	}
}