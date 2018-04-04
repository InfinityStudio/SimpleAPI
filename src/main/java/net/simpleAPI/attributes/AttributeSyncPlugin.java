package net.simpleAPI.attributes;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author ci010
 */
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("net.simpleAPI.attributes")
public class AttributeSyncPlugin implements IFMLLoadingPlugin {
	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"net.simpleAPI.attributes.listener.TileEntitySyncTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return "net.simpleAPI.attributes.AttributeAPI";
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
