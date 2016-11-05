package seia.vanillamagic.fake;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FakePlayerVM extends FakePlayer
{
	public FakePlayerVM(World world, BlockPos pos, GameProfile profile) 
	{
		super(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(world.provider.getDimension()), profile);
		posX = pos.getX() + 0.5;
		posY = pos.getY() + 0.5;
		posZ = pos.getZ() + 0.5;
		// ItemInWorldManager will access this field directly and can crash
		connection = new FakeNetHandlerPlayServer(this);
	}

	// These do things with packets...which crash since the net handler is null. Potion effects are not needed anyways.
	protected void onNewPotionEffect(PotionEffect potionEffect) 
	{
	}

	protected void onChangedPotionEffect(PotionEffect potionEffect, boolean b) 
	{
	}

	protected void onFinishedPotionEffect(PotionEffect potionEffect) 
	{
	}
	  
	protected void playEquipSound(@Nullable ItemStack stack) 
	{  
	}
}