package net.simpleAPI.ui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.simpleAPI.ui.element.Group;

/**
 * @author ci010
 */
public interface Document
{
	@CapabilityInject(Document.class)
	Capability<Document> CAPABILITY = null;

	Group getRoot();

	interface Runtime extends Document
	{
		ResourceLocation location();

		EntityPlayer getOpenedPlayer();

		ICapabilityProvider getCapabilityProvider();

		Object find(String id);
	}
}
