package seia.vanillamagic.quest.multimine;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumWand;

public class QuestMineMulti extends Quest
{
	public int radius;
	public int depth;
	public EnumWand requiredWand;
	
//	public QuestMineMulti(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName,
//			int radius, int depth, EnumWand requiredWand) 
//	{
//		super(required, posX, posY, icon, questName, uniqueName);
//		this.radius = radius;
//		this.depth = depth;
//		this.requiredWand = requiredWand;
//	}
	
	public void readData(JsonObject jo)
	{
		super.readData(jo);
		this.radius = jo.get("radius").getAsInt();
		this.depth = jo.get("depth").getAsInt();
		this.requiredWand = EnumWand.getWandByTier(jo.get("wandTier").getAsInt());
	}
	
	public int getMaxDestroyedBlocks()
	{
		return (((2 * radius) + 1) ^ 2) * depth;
	}
	
	@SubscribeEvent
	public void dig(BreakEvent event)
	{
		EntityPlayer player = event.getPlayer();
		World world = player.worldObj;
		BlockPos miningPos = event.getPos();
		if(!player.hasAchievement(achievement))
		{
			player.addStat(achievement, 1);
		}
		if(player.hasAchievement(achievement))
		{
			ItemStack offHand = player.getHeldItemOffhand();
			ItemStack mainHand = player.getHeldItemMainhand();
			if(EnumWand.areWandsEqual(requiredWand, EnumWand.getWandByItemStack(offHand)))
			{
				if(mainHand.getItem() instanceof ItemPickaxe)
				{
					// MultiMining algorithm
					RayTraceResult rayTraceResult = Minecraft.getMinecraft().objectMouseOver;
					EnumFacing sideHit = rayTraceResult.sideHit;
					int xRange = radius;
					int yRange = radius;
					int zRange = depth;
					switch(sideHit)
					{
					case UP:
					case DOWN:
						yRange = depth;
						zRange = radius;
						break;
					case NORTH:
					case SOUTH:
						xRange = radius;
						zRange = depth;
						break;
					case EAST:
					case WEST:
						xRange = depth;
						zRange = radius;
						break;
					}
					int x = miningPos.getX();
					int y = miningPos.getY();
					int z = miningPos.getZ();
					int count = 0;
					int max = getMaxDestroyedBlocks();
					for(int xPos = x - xRange; xPos <= x + xRange; xPos++)
					{
						for(int yPos = y - yRange; yPos <= y + yRange; yPos++)
						{
							for(int zPos = z - zRange; zPos <= z + zRange; zPos++)
							{
								if(count > max)
								{
									return;
								}
								BlockPos pos = new BlockPos(xPos, yPos, zPos);
								//ToolHelper.breakExtraBlock(mainHand, world, player, pos, pos);
								MinecraftForge.EVENT_BUS.register(new MineBlockTask(mainHand, world, player, pos, pos));
								count++;
							}
						}
					}
				}
			}
		}
	}
}