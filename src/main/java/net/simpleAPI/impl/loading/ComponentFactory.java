package net.simpleAPI.impl.loading;

import com.google.gson.JsonObject;
import net.minecraftforge.fml.relauncher.Side;
import net.simpleAPI.impl.reg.RegComponent;

/**
 * @author ci010
 */
public interface ComponentFactory
{
	RegComponent<?> createComponent(String modid, JsonObject json, Side side);
}
