package seia.vanillamagic.machine.quarry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
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
				TileQuarry tileQuarry = new TileQuarry();
				if(!player.hasAchievement(achievement))
				{
					player.addStat(achievement, 1);
				}
				if(player.hasAchievement(achievement))
				{
					tileQuarry.showBoundingBox();
					tileQuarry.init(world, quarryPos);
					CustomTileEntityHandler.INSTANCE.addCustomTileEntity(tileQuarry, player.dimension);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			BlockPosHelper.printCoords("Incorrect Quarry placed on:", quarryPos);
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
						CustomTileEntityHandler.INSTANCE.removeCustomTileEntityAtPos(world, cauldronPos, player.dimension);
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