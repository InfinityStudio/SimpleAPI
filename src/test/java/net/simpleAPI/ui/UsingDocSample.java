package net.simpleAPI.ui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.simpleAPI.attributes.AttributeView;


/**
 * @author ci010
 */
public class UsingDocSample
{
	interface DocumentContainer
	{
		Capability<DocumentContainer> CAPABILITY = null;

		void openBy(EntityPlayer player);
	}

	void genericWayToOpen(EntityPlayer player, ICapabilityProvider provider)
	{
		DocumentContainer container = provider.getCapability(DocumentContainer.CAPABILITY, null);
		if (container != null)
			container.openBy(player);
	}

	interface OpenDocument
	{
		Capability<OpenDocument> CAPABILITY = null;

		boolean tryOpen(ICapabilityProvider provider);
	}

	void alternative(EntityPlayer player, ICapabilityProvider provider)
	{
		OpenDocument capability = player.getCapability(OpenDocument.CAPABILITY, null);
		if (capability != null)
			capability.tryOpen(provider);
	}

	public void tryOpen(EntityPlayer player, TileEntity tileEntity)
	{

		if (player.getEntityWorld().isRemote) return;
		Document capability = tileEntity.getCapability(Document.CAPABILITY, null);
		if (capability != null)
		{
			((EntityPlayerMP) player).connection.sendPacket(null
					//new OpenTileDocumentMessage(player, tileEntity);
			);
		}
	}
}
