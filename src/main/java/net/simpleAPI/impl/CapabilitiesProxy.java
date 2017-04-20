package net.simpleAPI.impl;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

/**
 * @author ci010
 */
enum CapabilitiesProxy
{
	INSTANCE;
	private IdentityHashMap<String, Capability<?>> providers;

	CapabilitiesProxy()
	{
		try
		{
			Field providers = CapabilityManager.class.getDeclaredField("providers");
			this.providers = (IdentityHashMap<String, Capability<?>>) providers.get(CapabilityManager.INSTANCE);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	Capability<?> getCapability(String id)
	{
		if (id == null) return null;
		return providers.get(id);
	}
}
