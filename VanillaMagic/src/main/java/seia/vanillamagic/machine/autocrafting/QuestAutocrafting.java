package seia.vanillamagic.machine.autocrafting;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.WorldHelper;
import seia.vanillamagic.utils.spell.EnumWand;

public class QuestAutocrafting extends Quest
{
	private static int iinvDown = 3;
	
	public QuestAutocrafting(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName) 
	{
		super(required, posX, posY, icon, questName, uniqueName);
	}
	
	@SubscribeEvent
	public void addAutocrafting(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;
		if(EnumWand.areWandsEqual(EnumWand.BLAZE_ROD.wandItemStack, player.getHeldItemMainhand()))
		{
			if(player.isSneaking())
			{
				BlockPos workbenchPos = event.getPos();
				BlockPos cauldronPos = workbenchPos.up();
				if(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
				{
					if(isConstructionComplete(world, cauldronPos))
					{
						if(!player.hasAchievement(achievement))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							if(player.getHeldItemOffhand() != null)
							{
								return;
							}
							TileAutocrafting tile = new TileAutocrafting();
							tile.init(world, cauldronPos);
							CustomTileEntityHandler.INSTANCE.addCustomTileEntity(tile, WorldHelper.getDimensionID(world));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void deleteAutocrafting(BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		World world = player.worldObj;
		BlockPos cauldronPos = event.getPos();
		Block cauldron = world.getBlockState(cauldronPos).getBlock();
		if(cauldron instanceof BlockCauldron)
		{
			BlockPos workbenchPos = cauldronPos.down();
			Block workbench = world.getBlockState(workbenchPos).getBlock();
			if(workbench instanceof BlockWorkbench)
			{
				CustomTileEntityHandler.INSTANCE.removeCustomTileEntityAtPos(world, cauldronPos, WorldHelper.getDimensionID(world));
			}
		}
	}
	
	public static ItemStack[][] buildStackMatrix(IInventory[][] inventoryMatrix, int slot) 
	{
		int size = inventoryMatrix.length;
		ItemStack[][] stackMat = new ItemStack[size][size];
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				stackMat[i][j] = inventoryMatrix[i][j].getStackInSlot(slot);
			}
		}
		return stackMat;
	}

	public static IInventory[][] buildIInventoryMatrix(World world, BlockPos[][] inventoryPosMatrix) 
	{
		int size = inventoryPosMatrix.length;
		IInventory[][] invMat = new IInventory[size][size];
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				TileEntity tile = world.getTileEntity(inventoryPosMatrix[i][j]);
				if(tile instanceof IInventory)
				{
					invMat[i][j] = (IInventory) tile;
				}
			}
		}
		return invMat;
	}

	public static BlockPos[][] buildInventoryMatrix(BlockPos cauldronPos) 
	{
		BlockPos leftTop = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ() + 2);
		BlockPos top = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - iinvDown, cauldronPos.getZ() + 2);
		BlockPos rightTop = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ() + 2);
		BlockPos right = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ());
		BlockPos rightBottom = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ() - 2);
		BlockPos bottom = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - iinvDown, cauldronPos.getZ() - 2);
		BlockPos leftBottom = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ() - 2);
		BlockPos left = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ());
		BlockPos middle = cauldronPos.offset(EnumFacing.DOWN, iinvDown);
		
		return new BlockPos[][]{
			new BlockPos[]{ leftTop, top, rightTop },
			new BlockPos[]{ left, middle, right },
			new BlockPos[]{ leftBottom, bottom, rightBottom }
		};
	}
	
	public static boolean isConstructionComplete(World world, BlockPos cauldronPos)
	{
		boolean checkBasics = false;
		if(world.getTileEntity(cauldronPos.offset(EnumFacing.DOWN, iinvDown)) instanceof IInventory)
		{
			if(world.getTileEntity(cauldronPos.offset(EnumFacing.DOWN, 2)) instanceof IHopper)
			{
				if(world.getBlockState(cauldronPos.offset(EnumFacing.DOWN)).getBlock() instanceof BlockWorkbench)
				{
					checkBasics = true;
				}
			}
		}
		if(checkBasics)
		{
			BlockPos leftTop = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ() + 2);
			BlockPos top = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - iinvDown, cauldronPos.getZ() + 2);
			BlockPos rightTop = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ() + 2);
			BlockPos right = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ());
			BlockPos rightBottom = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ() - 2);
			BlockPos bottom = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - iinvDown, cauldronPos.getZ() - 2);
			BlockPos leftBottom = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ() - 2);
			BlockPos left = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - iinvDown, cauldronPos.getZ());
			
			if(world.getTileEntity(leftTop) instanceof IInventory)
			{
				if(world.getTileEntity(top) instanceof IInventory)
				{
					if(world.getTileEntity(rightTop) instanceof IInventory)
					{
						if(world.getTileEntity(right) instanceof IInventory)
						{
							if(world.getTileEntity(rightBottom) instanceof IInventory)
							{
								if(world.getTileEntity(bottom) instanceof IInventory)
								{
									if(world.getTileEntity(leftBottom) instanceof IInventory)
									{
										if(world.getTileEntity(left) instanceof IInventory)
										{
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}