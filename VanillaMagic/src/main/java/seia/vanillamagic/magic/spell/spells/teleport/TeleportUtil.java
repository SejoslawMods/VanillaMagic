package seia.vanillamagic.magic.spell.spells.teleport;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventTeleportEntity;

public class TeleportUtil
{
	private TeleportUtil()
	{
	}
	
	public static void teleportEntitySynchronized(EntityPlayerMP playerMP, EntityPlayerSP playerSP, BlockPos pos)
	{
		teleportEntity(playerMP, pos);
		teleportEntity(playerSP, pos);
	}
	
	public static Entity entityChangeDimension(Entity entity, int newDimId)
	{
		if  (!MinecraftForge.EVENT_BUS.post(new EventTeleportEntity.ChangeDimension(entity, entity.getPosition(), newDimId)))
			return entity.changeDimension(newDimId);
		return null;
	}
	
	public static void teleportEntity(Entity entityToBeTeleport, BlockPos teleportTo)
	{
		if  (!MinecraftForge.EVENT_BUS.post(new EventTeleportEntity(entityToBeTeleport, teleportTo)))
			entityToBeTeleport.setPositionAndUpdate(teleportTo.getX(), teleportTo.getY(), teleportTo.getZ());
	}
	
	public static void changePlayerDimensionWithoutPortal(EntityPlayerMP player, int dimension, BlockPos pos)
	{
		if  (MinecraftForge.EVENT_BUS.post(new EventTeleportEntity.ChangeDimension(player, pos, dimension))) return;
		
		MinecraftServer server = player.world.getMinecraftServer();
		WorldServer worldServer = server.getWorld(dimension);
		VMTeleporter vmTele = new VMTeleporter(worldServer, pos.getX(), pos.getY(), pos.getZ());
		boolean has = false;
		for (int i = 0 ; i < worldServer.customTeleporters.size(); ++i)
		{
			if (worldServer.customTeleporters.get(i) instanceof VMTeleporter)
			{
				has = true;
				break;
			}
		}
		
		if (!has) worldServer.customTeleporters.add(vmTele);
		
		for (int i = 0 ; i < worldServer.customTeleporters.size(); ++i)
		{
			if (worldServer.customTeleporters.get(i) instanceof VMTeleporter)
			{
				transferPlayerToDimension(player, dimension, (VMTeleporter)worldServer.customTeleporters.get(i));
				break;
			}
		}
	}

	/**
	 * It will teleport You to:
	 * CoordX = player.posX
	 * etc.
	 */
	public static void changePlayerDimensionWithoutPortal(EntityPlayerMP player, int dimension)
	{
		changePlayerDimensionWithoutPortal(player, dimension, new BlockPos(player.posX, player.posY, player.posZ));
	}
	
	public static Entity changeDimension(Entity entity, int newDimId, BlockPos newPos)
	{
		if (!entity.world.isRemote && !entity.isDead)
		{
			if (!ForgeHooks.onTravelToDimension(entity, newDimId)) return null;
            MinecraftServer minecraftServer = entity.getServer();
            int currentDim = entity.dimension;
            WorldServer worldServerCurrent = minecraftServer.getWorld(currentDim);
            WorldServer worldServerNew = minecraftServer.getWorld(newDimId);
            entity.dimension = newDimId;

            if (currentDim == 1 && newDimId == 1)
            {
            	worldServerNew = minecraftServer.getWorld(0);
                entity.dimension = 0;
            }

            entity.world.removeEntity(entity);
            entity.isDead = false;
            BlockPos blockpos;

            if  (newDimId == 1) blockpos = worldServerNew.getSpawnCoordinate();
            else
            {
                double d0 = entity.posX;
                double d1 = entity.posZ;

                if  (newDimId == -1)
                {
                    d0 = MathHelper.clamp(d0 / 8.0D, worldServerNew.getWorldBorder().minX() + 16.0D, worldServerNew.getWorldBorder().maxX() - 16.0D);
                    d1 = MathHelper.clamp(d1 / 8.0D, worldServerNew.getWorldBorder().minZ() + 16.0D, worldServerNew.getWorldBorder().maxZ() - 16.0D);
                }
                else if  (newDimId == 0)
                {
                    d0 = MathHelper.clamp(d0 * 8.0D, worldServerNew.getWorldBorder().minX() + 16.0D, worldServerNew.getWorldBorder().maxX() - 16.0D);
                    d1 = MathHelper.clamp(d1 * 8.0D, worldServerNew.getWorldBorder().minZ() + 16.0D, worldServerNew.getWorldBorder().maxZ() - 16.0D);
                }

                d0 = (double)MathHelper.clamp((int)d0, -29999872, 29999872);
                d1 = (double)MathHelper.clamp((int)d1, -29999872, 29999872);
                entity.setLocationAndAngles(d0, entity.posY, d1, 90.0F, 0.0F);
                blockpos = new BlockPos(entity);
            }

            worldServerCurrent.updateEntityWithOptionalForce(entity, false);

            if (entity != null)
            {
                if (currentDim == 1 && newDimId == 1)
                {
                    BlockPos blockpos1 = worldServerNew.getTopSolidOrLiquidBlock(worldServerNew.getSpawnPoint());
                    entity.moveToBlockPosAndAngles(blockpos1, entity.rotationYaw, entity.rotationPitch);
                }
                else entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);

                boolean flag = entity.forceSpawn;
                entity.forceSpawn = true;
                worldServerNew.spawnEntity(entity);
                entity.forceSpawn = flag;
                worldServerNew.updateEntityWithOptionalForce(entity, false);
            }

            entity.isDead = true;
            worldServerCurrent.resetUpdateEntityTick();
            worldServerNew.resetUpdateEntityTick();
            return entity;
		}
		else return null;
	}
	
	private static void transferPlayerToDimension(EntityPlayerMP player, int dimension, VMTeleporter teleporter)
	{
		MinecraftServer server = player.world.getMinecraftServer();
		WorldServer worldServer = server.getWorld(dimension);
		MinecraftServer mcServer = worldServer.getMinecraftServer();
		int i = player.dimension;
		WorldServer worldserver = mcServer.getWorld(player.dimension);
		player.dimension = dimension;
		WorldServer worldserver1 = mcServer.getWorld(player.dimension);
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
			player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potioneffect));
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
		
		if (false && entityIn.dimension == -1)
		{
			posX = MathHelper.clamp(posX / 8.0D, toWorldIn.getWorldBorder().minX() + 16.0D, toWorldIn.getWorldBorder().maxX() - 16.0D);
			posZ = MathHelper.clamp(posZ / 8.0D, toWorldIn.getWorldBorder().minZ() + 16.0D, toWorldIn.getWorldBorder().maxZ() - 16.0D);
			entityIn.setLocationAndAngles(posX, entityIn.posY, posZ, entityIn.rotationYaw, entityIn.rotationPitch);
			if (entityIn.isEntityAlive()) oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
		}
		else if (false && entityIn.dimension == 0)
		{
			posX = MathHelper.clamp(posX * 8.0D, toWorldIn.getWorldBorder().minX() + 16.0D, toWorldIn.getWorldBorder().maxX() - 16.0D);
			posZ = MathHelper.clamp(posZ * 8.0D, toWorldIn.getWorldBorder().minZ() + 16.0D, toWorldIn.getWorldBorder().maxZ() - 16.0D);
			entityIn.setLocationAndAngles(posX, entityIn.posY, posZ, entityIn.rotationYaw, entityIn.rotationPitch);
			if (entityIn.isEntityAlive()) oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
		}
		
		if (entityIn.dimension == 1)
		{
			BlockPos blockpos;
			if (lastDimension == 1) blockpos = toWorldIn.getSpawnPoint();
			else blockpos = toWorldIn.getSpawnCoordinate();
			
			posX = (double)blockpos.getX();
			entityIn.posY = (double)blockpos.getY();
			posZ = (double)blockpos.getZ();
			entityIn.setLocationAndAngles(posX, entityIn.posY, rotationYaw, 90.0F, 0.0F);
			if (entityIn.isEntityAlive()) oldWorldIn.updateEntityWithOptionalForce(entityIn, false);
		}
		
		if (lastDimension != 1)
		{
			posX = (double)MathHelper.clamp((int)posX, -29999872, 29999872);
			posZ = (double)MathHelper.clamp((int)posZ, -29999872, 29999872);
			if (entityIn.isEntityAlive())
			{
				entityIn.setLocationAndAngles(posX, entityIn.posY, posZ, entityIn.rotationYaw, entityIn.rotationPitch);
				teleporter.placeInPortal(entityIn, rotationYaw);
				toWorldIn.spawnEntity(entityIn);
				toWorldIn.updateEntityWithOptionalForce(entityIn, false);
			}
		}
		entityIn.setWorld(toWorldIn);
	}
	
	private static void preparePlayer(EntityPlayerMP playerIn, WorldServer worldIn)
	{
		WorldServer worldserver = playerIn.getServerWorld();
		if (worldIn != null) worldIn.getPlayerChunkMap().removePlayer(playerIn);
		worldserver.getPlayerChunkMap().addPlayer(playerIn);
		worldserver.getChunkProvider().provideChunk((int)playerIn.posX >> 4, (int)playerIn.posZ >> 4);
	}
	
	/**
	 * Updates the time and weather for the given player to those of the given world
	 */
	private static void updateTimeAndWeatherForPlayer(EntityPlayerMP player, WorldServer worldIn, int dimension)
	{
		MinecraftServer server = player.world.getMinecraftServer();
		WorldServer worldServer = server.getWorld(dimension);
		MinecraftServer mcServer = worldServer.getMinecraftServer();
		WorldBorder worldborder = mcServer.worlds[0].getWorldBorder();
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