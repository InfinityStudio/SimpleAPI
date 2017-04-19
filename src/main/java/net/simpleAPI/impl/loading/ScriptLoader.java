package net.simpleAPI.impl.loading;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.relauncher.Side;
import net.simpleAPI.impl.reg.RegComponent;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * @author ci010
 */
public class ScriptLoader
{
	private Side side;
	private Map<String, ComponentFactory> factoryMap;

	private Map<String, SimpleModContainer> containerMap = new TreeMap<>();

	public ScriptLoader(Side side, Map<String, ComponentFactory> factoryMap)
	{
		this.side = side;
		this.factoryMap = factoryMap;
	}

	public void loadAll(Path root)
	{
	}

	public void load(String script)
	{
		JsonElement parse = new JsonParser().parse(script);
		if (!parse.isJsonObject()) throw new IllegalArgumentException("The mod entry need to be a json object!");
		JsonObject obj = parse.getAsJsonObject();
		for (Map.Entry<String, JsonElement> entry : obj.entrySet())
			if (!entry.getValue().isJsonObject())
			{
				//TODO log
			}
			else
			{
				SimpleModContainer container = containerMap.get(entry.getKey());
				if (container == null) containerMap.put(entry.getKey(), container = new SimpleModContainer());
				loadMod(container, entry.getValue().getAsJsonObject());
			}
	}

	private void loadItemsLocation()
	{

	}

	private void loadMod(SimpleModContainer container, JsonObject object)
	{
		loadForgeMetaData(object);
		boolean byLocation = Optional.ofNullable(object.get("loadByLocation")).filter(JsonElement::isJsonPrimitive).map
				(JsonElement::getAsBoolean).orElse(false);
		if (byLocation)
		{

		}
		else for (Map.Entry<String, JsonElement> entry : object.entrySet())
			if (entry.getValue().isJsonArray())
			{
				ComponentFactory componentFactory = factoryMap.get(entry.getKey());
				if (componentFactory != null)
					for (JsonElement element : entry.getValue().getAsJsonArray())
						if (element.isJsonObject())
						{
							RegComponent<?> component = componentFactory.createComponent(container.getModid(), element.getAsJsonObject(), side);
							if (component != null) container.add(component);
						}
			}
	}

	private ModMetadata loadForgeMetaData(JsonObject object)
	{

		return null;
	}
}
