package seia.vanillamagic.item.thecrystalofmothernature;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.ItemStackHelper;

public class QuestMotherNatureCrystal extends Quest
{
	@SubscribeEvent
	public void onHoldInLeftHandTick(PlayerTickEvent event) // left hand
	{
		EntityPlayer player = event.player;
		World world = player.world;
		ItemStack leftHand = player.getHeldItemOffhand();
		if(ItemStackHelper.isNullStack(leftHand))
		{
			return;
		}
		NBTTagCompound stackTag = leftHand.getTagCompound();
		if(stackTag == null)
		{
			return;
		}
		if(VanillaMagicItems.isCustomItem(leftHand, VanillaMagicItems.MOTHER_NATURE_CRYSTAL))
		{
			onTickUpdate(leftHand, world, player);
		}
	}
	
	public void onTickUpdate(ItemStack leftHand, World world, EntityPlayer player) 
	{
		int range = 10;
		int verticalRange = 3;
		int posX = (int) Math.round(player.posX - 0.5f);
		int posY = (int) player.posY;
		int posZ = (int) Math.round(player.posZ - 0.5f);
		for(int ix = posX - range; ix <= posX + range; ++ix)
		{
			for(int iz = posZ - range; iz <= posZ + range; ++iz)
			{
				for(int iy = posY - verticalRange; iy <= posY + verticalRange; ++iy)
				{
					BlockPos blockPos = new BlockPos(ix, iy, iz);
					Block block = world.getBlockState(blockPos).getBlock();
					if(block instanceof IPlantable || block instanceof IGrowable)
					{
						if(world.rand.nextInt(50) == 0)
						{
							IBlockState preBlockState = world.getBlockState(blockPos);
							block.updateTick(world, blockPos, world.getBlockState(blockPos), world.rand);
							IBlockState newState = world.getBlockState(blockPos);
							if(!newState.equals(preBlockState))
							{
								world.playEvent(2001, blockPos, Block.getIdFromBlock(newState.getBlock()) + (newState.getBlock().getMetaFromState(newState) << 12));
							}
						}
					}
				}
			}
		}
	}

	int countTicks = 0;
	@SubscribeEvent
	public void onCrystalUse(RightClickBlock event) // right hand
	{
		if(countTicks == 0)
		{
			countTicks++;
		}
		else
		{
			countTicks = 0;
			return;
		}
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos clickedPos = event.getPos();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(ItemStackHelper.isNullStack(rightHand))
		{
			return;
		}
		NBTTagCompound stackTag = rightHand.getTagCompound();
		if(stackTag == null)
		{
			return;
		}
		if(VanillaMagicItems.isCustomItem(rightHand, VanillaMagicItems.MOTHER_NATURE_CRYSTAL))
		{
			if(!player.hasAchievement(achievement))
			{
				player.addStat(achievement, 1);
			}
			if(player.hasAchievement(achievement))
			{
				if(applyBonemeal(rightHand, world, clickedPos))
				{
					IBlockState state = world.getBlockState(clickedPos);
					world.playEvent(2001, clickedPos, Block.getIdFromBlock(state.getBlock()) + (state.getBlock().getMetaFromState(state) << 12));
				}
			}
		}
	}

	public boolean applyBonemeal(ItemStack rightHand, World world, BlockPos clickedPos) 
	{
		return world instanceof WorldServer && applyBonemeal(rightHand, world, clickedPos, FakePlayerFactory.getMinecraft((WorldServer) world));
	}

	public boolean applyBonemeal(ItemStack rightHand, World world, BlockPos clickedPos, FakePlayer fakePlayer) 
	{
		IBlockState iblockstate = world.getBlockState(clickedPos);
		int hook = ForgeEventFactory.onApplyBonemeal(fakePlayer, world, clickedPos, iblockstate, rightHand);
		if(hook != 0)
		{
			return hook > 0;
		}
		if(iblockstate.getBlock() instanceof IGrowable)
		{
			IGrowable igrowable = (IGrowable) iblockstate.getBlock();
			if(igrowable.canGrow(world, clickedPos, iblockstate, world.isRemote))
			{
				if(!world.isRemote)
				{
					if(igrowable.canUseBonemeal(world, world.rand, clickedPos, iblockstate))
					{
						igrowable.grow(world, world.rand, clickedPos, iblockstate);
					}
				}
				return true;
			}
		}
		return false;
	}
}