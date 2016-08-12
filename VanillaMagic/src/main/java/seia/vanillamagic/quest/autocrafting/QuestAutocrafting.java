package seia.vanillamagic.quest.autocrafting;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.EntityHelper;

public class QuestAutocrafting extends Quest
{
	public QuestAutocrafting(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName) 
	{
		super(required, posX, posY, icon, questName, uniqueName);
	}
	
	@SubscribeEvent
	public void earnAchievement(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;
		if(EntityHelper.hasPlayerCraftingTableInMainHand(player))
		{
			if(player.isSneaking())
			{
				BlockPos cauldronPos = event.getPos();
				if(world.getBlockState(cauldronPos).getBlock() instanceof BlockCauldron)
				{
					if(isConstructionComplete(world, cauldronPos))
					{
						if(!player.hasAchievement(achievement))
						{
							player.addStat(achievement, 1);
						}
					}
				}
			}
		}
	}
	
	public static boolean isConstructionComplete(World world, BlockPos cauldronPos)
	{
		boolean checkBasics = false;
		if(world.getTileEntity(cauldronPos.offset(EnumFacing.DOWN, 2)) instanceof IInventory)
		{
			if(world.getTileEntity(cauldronPos.offset(EnumFacing.DOWN, 3)) instanceof IHopper)
			{
				if(world.getBlockState(cauldronPos.offset(EnumFacing.DOWN)).getBlock() instanceof BlockWorkbench)
				{
					checkBasics = true;
				}
			}
		}
		if(checkBasics)
		{
			BlockPos leftTop = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - 2, cauldronPos.getZ() + 2);
			BlockPos top = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - 2, cauldronPos.getZ() + 2);
			BlockPos rightTop = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - 2, cauldronPos.getZ() + 2);
			BlockPos right = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - 2, cauldronPos.getZ());
			BlockPos rightBottom = new BlockPos(cauldronPos.getX() + 2, cauldronPos.getY() - 2, cauldronPos.getZ() - 2);
			BlockPos bottom = new BlockPos(cauldronPos.getX(), cauldronPos.getY() - 2, cauldronPos.getZ() - 2);
			BlockPos leftBottom = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - 2, cauldronPos.getZ() - 2);
			BlockPos left = new BlockPos(cauldronPos.getX() - 2, cauldronPos.getY() - 2, cauldronPos.getZ());
			
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