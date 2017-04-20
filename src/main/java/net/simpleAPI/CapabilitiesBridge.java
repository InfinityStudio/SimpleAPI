package net.simpleAPI;

import com.google.gson.JsonObject;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author ci010
 */
public interface CapabilitiesBridge
{
	void addNickName(Capability<?> capability, String name);

	Optional<Capability<?>> findCapability(String name);

	<T> void registerConstructFunction(Capability<T> capability, Function<JsonObject, T> function);

	<T> Function<JsonObject, T> getConstructFunction(Capability<T> capability);
}
