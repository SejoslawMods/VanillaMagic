package seia.vanillamagic.quest.autocrafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import seia.vanillamagic.utils.InventoryHelper;

public class AutocraftingHandler 
{
	public static final AutocraftingHandler INSTANCE = new AutocraftingHandler();
	
	private AutocraftingHandler()
	{
	}
	
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void autocraft(WorldTickEvent event)
	{
		World world = event.world;
		if(world.isRemote)
		{
			return;
		}
		if(world.tickableTileEntities.size() > 0)
		{
			for(TileEntity tile : world.tickableTileEntities)
			{
				if(tile instanceof IHopper)
				{
					IHopper iHopper = (IHopper) tile;
					BlockPos hopperPos = new BlockPos(iHopper.getXPos(), iHopper.getYPos(), iHopper.getZPos());
					BlockPos cauldronPos = hopperPos.offset(EnumFacing.UP, 3);
					if(QuestAutocrafting.isConstructionComplete(world, cauldronPos))
					{
						TileEntityHopper tileHop = (TileEntityHopper) world.getTileEntity(hopperPos);
						if(!InventoryHelper.isInventoryFull(tileHop, EnumFacing.DOWN))
						{
							BlockPos[][] inventoryPosMatrix = buildInventoryMatrix(cauldronPos);
							IInventory[][] inventoryMatrix = buildIInventoryMatrix(world, inventoryPosMatrix);
							ItemStack[][] stackMatrix = buildStackMatrix(inventoryMatrix);
							ContainerAutocrafting conAuto = new ContainerAutocrafting(world, stackMatrix);
							for(int i = 0; i < 4; i++)
							{
								boolean crafted = conAuto.craft();
								if(crafted)
								{
									conAuto.removeStacks(inventoryMatrix);
									conAuto.outputResult(iHopper);
									return;
								}
								conAuto.rotateMatrix();
							}
						}
					}
				}
			}
		}
	}

	public ItemStack[][] buildStackMatrix(IInventory[][] inventoryMatrix) 
	{
		int size = inventoryMatrix.length;
		ItemStack[][] stackMat = new ItemStack[size][size];
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				stackMat[i][j] = inventoryMatrix[i][j].getStackInSlot(0);
			}
		}
		return stackMat;
	}

	public IInventory[][] buildIInventoryMatrix(World world, BlockPos[][] inventoryPosMatrix) 
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

	public BlockPos[][] buildInventoryMatrix(BlockPos cauldronPos) 
	{
		BlockPos leftTop = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - 2, cauldronPos.getZ() + 2);
		BlockPos top = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - 2, cauldronPos.getZ() + 2);
		BlockPos rightTop = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - 2, cauldronPos.getZ() + 2);
		BlockPos right = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - 2, cauldronPos.getZ());
		BlockPos rightBottom = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - 2, cauldronPos.getZ() - 2);
		BlockPos bottom = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - 2, cauldronPos.getZ() - 2);
		BlockPos leftBottom = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - 2, cauldronPos.getZ() - 2);
		BlockPos left = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - 2, cauldronPos.getZ());
		BlockPos middle = cauldronPos.offset(EnumFacing.DOWN, 2);
		
		return new BlockPos[][]{
			new BlockPos[]{ leftTop, top, rightTop },
			new BlockPos[]{ left, middle, right },
			new BlockPos[]{ leftBottom, bottom, rightBottom }
		};
	}
}