package seia.vanillamagic.utils.spell.teleport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;

public class TeleportHelper 
{
	private TeleportHelper()
	{
	}

	public static void changePlayerDimensionWithoutPortal(EntityPlayer player, int dimension)
	{
		int oldDimension = player.worldObj.provider.getDimension();
		EntityPlayerMP playerMP = (EntityPlayerMP) player;
		MinecraftServer server = playerMP.worldObj.getMinecraftServer();
		WorldServer worldServer = server.worldServerForDimension(dimension);
		MinecraftServer mcServer = worldServer.getMinecraftServer();
		VMTeleporter vmTele = new VMTeleporter(worldServer, player.posX, player.posY, player.posZ);
		boolean has = false;
		for(int i = 0 ; i < worldServer.customTeleporters.size(); i++)
		{
			if(worldServer.customTeleporters.get(i) instanceof VMTeleporter)
			{
				has = true;
			}
		}
		if(!has)
		{
			worldServer.customTeleporters.add(vmTele);
		}
		for(int i = 0 ; i < worldServer.customTeleporters.size(); i++)
		{
			if(worldServer.customTeleporters.get(i) instanceof VMTeleporter)
			{
				transferPlayerToDimension(playerMP, dimension, (VMTeleporter)worldServer.customTeleporters.get(i));
			}
		}
	}
	
	public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, VMTeleporter teleporter)
	{
		MinecraftServer server = ((EntityPlayerMP)player).worldObj.getMinecraftServer();
		WorldServer worldServer = server.worldServerForDimension(dimension);
		MinecraftServer mcServer = worldServer.getMinecraftServer();
		int i = player.dimension;
		WorldServer worldserver = mcServer.worldServerForDimension(player.dimension);
		player.dimension = dimension;
		WorldServer worldserver1 = mcServer.worldServerForDimension(player.dimension);
		player.connection.sendPacket(new SPacketRespawn(player.dimension, worldserver1.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
		worldserver.removeEntityDangerously(player);
		player.isDead = false;
		transferEntityToWorld(player, i, worldserver, worldserver1, teleporter);
		preparePlayer(player, worldserver);
		player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		player.interactionManager.setWorld(worldserver1);
		player.connection.sendPacket(new SPacketPlayerAbilities(player.capabilities));
		updateTimeAndWeatherForPlayer(player, worldserver1, dimension);
		syncPlayerInventory(player);
		for (PotionEffect potioneffect : player.getActivePotionEffects())
		{
			player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
		}
	}
	
	@SuppressWarnings("unused")
	private static void transferEntityToWorld(Entity entityIn, int lastDimension, WorldServer oldWorldIn, WorldServer toWorldIn, VMTeleporter teleporter)
	{
		WorldProvider pOld = oldWorldIn.provider;
		WorldProvider pNew = toWorldIn.provider;
		double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
		double posX = entityIn.posX * moveFactor;
		double posZ = entityIn.posZ * moveFactor;
		double d2 = 8.0D;
		float rotationYaw = entityIn.rotationYaw;
		oldWorldIn.theProfiler.startSection("moving");
		if (false && entityIn.dimension == -1)
		{
			posX = MathHelper.clamp_double(posX / 8.0D, toWorldIn.getWorldBorder().minX() + 16.0D, toWorldIn.getWorldBorder().maxX() - 16.0D);
			posZ = MathHelper.clamp_double(posZ / 8.0D, toWorldIn.getWorldBorder().minZ() + 16.0D, toWorldIn.getWorldBorder().maxZ() - 16.0D);
			entityIn.setLocationAndAngles(posX, entityIn.posY, posZ, entityIn.rotationYaw, entityIn.rotationPitch);
			if (entityIn.isEntityAlive())
			{
				oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
			}
		}
		else if (false && entityIn.dimension == 0)
		{
			posX = MathHelper.clamp_double(posX * 8.0D, toWorldIn.getWorldBorder().minX() + 16.0D, toWorldIn.getWorldBorder().maxX() - 16.0D);
			posZ = MathHelper.clamp_double(posZ * 8.0D, toWorldIn.getWorldBorder().minZ() + 16.0D, toWorldIn.getWorldBorder().maxZ() - 16.0D);
			entityIn.setLocationAndAngles(posX, entityIn.posY, posZ, entityIn.rotationYaw, entityIn.rotationPitch);
			if (entityIn.isEntityAlive())
			{
				oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
			}
		}
		
		if (entityIn.dimension == 1)
		{
			BlockPos blockpos;
			if (lastDimension == 1)
			{
				blockpos = toWorldIn.getSpawnPoint();
			}
			else
			{
				blockpos = toWorldIn.getSpawnCoordinate();
			}
			posX = (double)blockpos.getX();
			entityIn.posY = (double)blockpos.getY();
			posZ = (double)blockpos.getZ();
			entityIn.setLocationAndAngles(posX, entityIn.posY, rotationYaw, 90.0F, 0.0F);
			if (entityIn.isEntityAlive())
			{
				oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
			}
		}
		oldWorldIn.theProfiler.endSection();
		if (lastDimension != 1)
		{
			oldWorldIn.theProfiler.startSection("placing");
			posX = (double)MathHelper.clamp_int((int)posX, -29999872, 29999872);
			posZ = (double)MathHelper.clamp_int((int)posZ, -29999872, 29999872);
			if (entityIn.isEntityAlive())
			{
				entityIn.setLocationAndAngles(posX, entityIn.posY, posZ, entityIn.rotationYaw, entityIn.rotationPitch);
				teleporter.placeInPortal(entityIn, rotationYaw);
				toWorldIn.spawnEntityInWorld(entityIn);
				toWorldIn.updateEntityWithOptionalForce(entityIn, false);
			}
			oldWorldIn.theProfiler.endSection();
		}
		entityIn.setWorld(toWorldIn);
	}
	
	private static void preparePlayer(EntityPlayerMP playerIn, WorldServer worldIn)
	{
		WorldServer worldserver = playerIn.getServerWorld();
		if (worldIn != null)
		{
			worldIn.getPlayerChunkMap().removePlayer(playerIn);
		}
		worldserver.getPlayerChunkMap().addPlayer(playerIn);
		worldserver.getChunkProvider().provideChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);
	}
	
	/**
	 * Updates the time and weather for the given player to those of the given world
	 */
	private static void updateTimeAndWeatherForPlayer(EntityPlayerMP player, WorldServer worldIn, int dimension)
	{
		MinecraftServer server = player.worldObj.getMinecraftServer();
		WorldServer worldServer = server.worldServerForDimension(dimension);
		MinecraftServer mcServer = worldServer.getMinecraftServer();
		WorldBorder worldborder = mcServer.worldServers[0].getWorldBorder();
		player.connection.sendPacket(new SPacketWorldBorder(worldborder, SPacketWorldBorder.Action.INITIALIZE));
		player.connection.sendPacket(new SPacketTimeUpdate(worldIn.getTotalWorldTime(), worldIn.getWorldTime(), worldIn.getGameRules().getBoolean("doDaylightCycle")));
		if (worldIn.isRaining())
		{
			player.connection.sendPacket(new SPacketChangeGameState(1, 0.0F));
			player.connection.sendPacket(new SPacketChangeGameState(7, worldIn.getRainStrength(1.0F)));
			player.connection.sendPacket(new SPacketChangeGameState(8, worldIn.getThunderStrength(1.0F)));
		}
	}
	
	/**
	 * sends the players inventory to himself
	 */
	private static void syncPlayerInventory(EntityPlayerMP playerIn)
	{
		playerIn.sendContainerToPlayer(playerIn.inventoryContainer);
		playerIn.setPlayerHealthUpdated();
		playerIn.connection.sendPacket(new SPacketHeldItemChange(playerIn.inventory.currentItem));
	}
}