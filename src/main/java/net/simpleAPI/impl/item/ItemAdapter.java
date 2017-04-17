package net.simpleAPI.impl.item;

import com.google.common.base.Function;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.simpleAPI.item.*;

import java.util.Map;

/**
 * @author ci010
 */
public interface ItemAdapter
{
	ItemUpdate getUpdate();

	ItemUsing getUsing();

	ItemEntityInteract getInteract();

	ItemBlockInteract getBlockInteract();

	ItemEntityHandler getHandler();

	ItemCreate getCreate();

	ItemBlockDestroy getDestroy();

	Function<ICapabilityProvider, Map<ResourceLocation, ICapabilityProvider>> getCapabilityFactory();

	ItemAdapter setCapabilityFactory(Function<ICapabilityProvider, Map<ResourceLocation, ICapabilityProvider>> capabilityFactory);

	ItemAdapter setUpdate(ItemUpdate update);

	ItemAdapter setUsing(ItemUsing using);

	ItemAdapter setInteract(ItemEntityInteract interact);

	ItemAdapter setBlockInteract(ItemBlockInteract blockInteract);

	ItemAdapter setHandler(ItemEntityHandler handler);

	ItemAdapter setCreate(ItemCreate create);

	ItemAdapter setDestroy(ItemBlockDestroy destroy);
}
