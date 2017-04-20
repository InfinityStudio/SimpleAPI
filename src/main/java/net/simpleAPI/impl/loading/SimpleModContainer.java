package net.simpleAPI.impl.loading;

import net.simpleAPI.impl.reg.RegComponent;

import java.nio.file.Path;
import java.util.List;

/**
 * @author ci010
 */
class SimpleModContainer
{
	private String modid;
	Path root;
	private List<RegComponent<?>> components;

	public String getModid()
	{
		return modid;
	}

	void add(RegComponent<?> component) {components.add(component);}
}
