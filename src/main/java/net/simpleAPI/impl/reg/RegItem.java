package net.simpleAPI.impl.reg;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ci010
 */
public class RegItem extends RegComponent<Item>
{
	private String ore;
	private int subTypes;

	public RegItem(Item component)
	{
		super(component);
	}

	public RegItem setOre(String ore)
	{
		this.ore = ore;
		return this;
	}

	public RegItem setSubTypes(int subTypes)
	{
		this.subTypes = subTypes;
		return this;
	}

	@Override
	public void register()
	{
		GameRegistry.register(getComponent());
		if (ore != null) OreDictionary.registerOre(ore, getComponent());
	}

	@Override
	public void registerClient()
	{
		if (!getComponent().getHasSubtypes())
			ModelLoader.setCustomModelResourceLocation(getComponent(), 0,
					new ModelResourceLocation(getComponent().getRegistryName(),
							"inventory"));
		else for (int i = 0; i < subTypes; i++)
			ModelLoader.setCustomModelResourceLocation(getComponent(), 1,
					new ModelResourceLocation(getComponent().getRegistryName(),
							"inventory"));
	}
}
