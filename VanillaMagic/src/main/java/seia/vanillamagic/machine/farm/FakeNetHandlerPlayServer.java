package seia.vanillamagic.machine.farm;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FakeNetHandlerPlayServer extends NetHandlerPlayServer
{
	public FakeNetHandlerPlayServer(EntityPlayerMP playerMP) 
	{
		super(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManager(EnumPacketDirection.CLIENTBOUND), playerMP);
	}
	
	@Nullable
	public NetworkManager getNetworkManager() 
	{
		return null;
	}
	
	public void kickPlayerFromServer(String string) 
	{
	}
	
	public void processInput(CPacketInput packet) 
	{
	}
	
	public void processPlayer(CPacketPlayer packet) 
	{
	}
	
	public void setPlayerLocation(double x, double y, double z, float yaw, float pitch) 
	{
	}
	
	public void processPlayerDigging(CPacketPlayerDigging packet) 
	{
	}
	
	public void processPlayerBlockPlacement(CPacketPlayerTryUseItem packet) 
	{
	}
	
	public void onDisconnect(ITextComponent text) 
	{
	}
	
	public void sendPacket(Packet<?> packet) 
	{
	}
	
	public void processHeldItemChange(CPacketHeldItemChange packet) 
	{
	}
	
	public void processChatMessage(CPacketChatMessage packet) 
	{
	}
	
	public void handleAnimation(CPacketAnimation packet) 
	{
	}
	
	public void processEntityAction(CPacketEntityAction packet) 
	{
	}
	
	public void processUseEntity(CPacketUseEntity packet) 
	{
	}
	
	public void processClientStatus(CPacketClientStatus packet) 
	{
	}
	
	public void processCloseWindow(CPacketCloseWindow packet) 
	{
	}
	
	public void processClickWindow(CPacketClickWindow packet) 
	{
	}
	
	public void processEnchantItem(CPacketEnchantItem packet) 
	{
	}
	
	public void processCreativeInventoryAction(CPacketCreativeInventoryAction packet) 
	{
	}
	
	public void processConfirmTransaction(CPacketConfirmTransaction packet) 
	{
	}
	
	public void processUpdateSign(CPacketUpdateSign packet) 
	{
	}
	
	public void processKeepAlive(CPacketKeepAlive packet) 
	{
	}
	
	public void processPlayerAbilities(CPacketPlayerAbilities packet) 
	{
	}
	
	public void processTabComplete(CPacketTabComplete packet) 
	{
	}
	
	public void processClientSettings(CPacketClientSettings packet) 
	{
	}
	
	public void handleSpectate(CPacketSpectate packet) 
	{
	}
	
	public void handleResourcePackStatus(CPacketResourcePackStatus packet) 
	{
	}
}