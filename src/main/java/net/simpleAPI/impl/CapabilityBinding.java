package net.simpleAPI.impl;

import com.google.common.base.Function;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.simpleAPI.capabilities.Capabilities;
import net.simpleAPI.impl.attribute.NBTInitIndicator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ci010
 */
public enum CapabilityBinding
{
	INSTANCE;

	public Function<ICapabilityProvider, Map<ResourceLocation, ICapabilityProvider>> createCapabilitesFactory
			(JsonElement capabilities)
	{
		if (capabilities == null) return null;
		if (!capabilities.isJsonArray()) return null;
		List<CapInfo> list = new ArrayList<CapInfo>();
		for (JsonElement element : capabilities.getAsJsonArray())
		{
			JsonObject obj = element.getAsJsonObject();
			String type = MCJsonUtil.getString(obj, "type", null);
			Capability capability = CapabilitiesFinder.INSTANCE.getCapability(type);
			if (capability == null) {continue;}
			ResourceLocation name = new ResourceLocation(MCJsonUtil.getString(obj, "name", capability.getName()));
			boolean store = MCJsonUtil.getBool(obj, "store", false);
			NBTTagCompound init = null;
			try {init = JsonToNBT.getTagFromJson(obj.get("construct").toString());}
			catch (NBTException e) {e.printStackTrace();}
			list.add(new CapInfo(capability, name, store, init));
		}
		return new _Factory(list);
	}

	private class _Factory implements Function<ICapabilityProvider, Map<ResourceLocation, ICapabilityProvider>>
	{
		List<CapInfo> infos;

		_Factory(List<CapInfo> infos)
		{
			this.infos = infos;
		}

		@Nullable
		@Override
		public Map<ResourceLocation, ICapabilityProvider> apply(@Nullable ICapabilityProvider input)
		{
			Map<ResourceLocation, ICapabilityProvider> map = new TreeMap<ResourceLocation, ICapabilityProvider>();
			for (CapInfo info : infos)
			{
				Capability.IStorage storage = info.capability.getStorage();
				Object instance = info.capability.getDefaultInstance();
				if (info.init != null)
					storage.readNBT(info.capability, instance, null, new NBTInitIndicator(info.init, input));
				map.put(info.name, info.store ? new Capabilities.CommonSerial<Object>(instance, info.capability) :
						new Capabilities.Common<Object>(instance, info.capability));
			} return map;
		}
	}

	private class CapInfo
	{
		Capability capability;
		ResourceLocation name;
		boolean store;
		NBTTagCompound init;

		CapInfo(Capability capability, ResourceLocation name, boolean store, NBTTagCompound init)
		{
			this.capability = capability;
			this.name = name;
			this.store = store;
			this.init = init;
		}
	}

}
