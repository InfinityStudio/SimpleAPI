package net.simpleAPI.impl.document;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.simpleAPI.attributes.AttributeView;
import net.simpleAPI.inventory.Inventory;
import net.simpleAPI.ui.element.Element;

import java.net.URL;

/**
 * @author ci010
 */
public class DocHelper
{
	static void openDoc(EntityPlayer player, ICapabilityProvider provider)
	{
		if (player.world.isRemote) return;
		//send ServerReqOpenDoc

	}

	static void showGUIProcess(EntityPlayer player)
	{
		if (player.world.isRemote) return;
		EntityPlayerMP mp = (EntityPlayerMP) player;
//		mp.getNextWindowId();
//		mp.closeContainer();
		//send packet
		//guiOwner.createContainer(mp.inventory, mp);
//		mp.openContainer = new ContainerAdapter();
//		mp.openContainer.windowId = mp.currentWindowId;
//		mp.openContainer.addListener(mp);
//		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.
//				Open(mp, mp.openContainer));
	}


	@SideOnly(Side.CLIENT)
	static IMessage handleMessageClient(EntityPlayer player, ServerReqOpenDoc messageOpenTile) throws Exception
	{
		try
		{
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(messageOpenTile.location);
		}
		catch (Exception e)
		{
			//no such resource

		}
//		Element parse = DocumentParser.INSTANCE.parse(new URL(messageOpenTile.content).openStream());
		ICapabilityProvider provider = messageOpenTile.getProvider(player.world);
		AttributeView capability = messageOpenTile.getProvider(player.world).getCapability(AttributeView.CAPABILITY,
				messageOpenTile.facing);
		Inventory inventory = messageOpenTile.getProvider(player.world).getCapability(Inventory.CAPABILITY,
				messageOpenTile.facing);

		return null;
	}
}
