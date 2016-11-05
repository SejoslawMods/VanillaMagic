package seia.vanillamagic.tileentity.machine.farm;

import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import seia.vanillamagic.fake.FakeNetHandlerPlayServer;

public class FakeFarmer extends EntityPlayerMP
{
	private static final UUID uuid = UUID.fromString("c1ddfd7f-120a-4437-8b64-38660d3ec62c");
	
	private static GameProfile DUMMY_PROFILE = new GameProfile(uuid, "[FakeFarmer]");
	
	public FakeFarmer(WorldServer world) 
	{
		super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, DUMMY_PROFILE, new PlayerInteractionManager(world));
		// ItemInWorldManager will access this field directly and can crash
		connection = new FakeNetHandlerPlayServer(this);
	}
	
	public boolean canCommandSenderUseCommand(int permLevel, String commandName) 
	{
		return false;
	}
	
	public void addStat(StatBase stat, int amount) 
	{
	}
	
	public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) 
	{
	}
	
	public boolean isEntityInvulnerable(DamageSource source) 
	{
		return true;
	}
	
	public boolean canAttackPlayer(EntityPlayer player) 
	{
		return false;
	}
	
	public void onDeath(DamageSource source) 
	{
		return;
	}
	
	public void onUpdate() 
	{
		return;
	}
	
	public boolean canPlayerEdit(BlockPos pos, EnumFacing facing, @Nullable ItemStack stack) 
	{
		return true;
	}
	
	public void setWorld(World world) 
	{
		Thread.dumpStack();
	}
}