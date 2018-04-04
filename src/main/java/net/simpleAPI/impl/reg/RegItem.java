package net.simpleAPI.impl.reg;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.simpleAPI.impl.item.ItemAdapter;

/**
 * @author ci010
 */
public class RegItem extends RegComponent<Item>
{
	private String ore;
	private String[] subTypes;

	public RegItem(Item component)
	{
		super(component);
	}

	public RegItem setOre(String ore)
	{
		this.ore = ore;
		return this;
	}

	public RegItem setSubTypes(String[] subTypes)
	{
		this.subTypes = subTypes;
		return this;
	}

	@Override
	public void register()
	{
//		GameRegistry.register(getComponent());
//		if (ore != null) OreDictionary.registerOre(ore, getComponent());
	}

	@Override
	public void registerClient()
	{
		if (!getComponent().getHasSubtypes())
			ModelLoader.setCustomModelResourceLocation(getComponent(), 0,
					new ModelResourceLocation(getComponent().getRegistryName(),
							"inventory"));
		else for (int i = 0; i < subTypes.length; i++)
		{
			Item component = getComponent();
			String id = component.getRegistryName() + "_" + subTypes[i];
			ModelLoader.setCustomModelResourceLocation(component, i,
					new ModelResourceLocation(id, "inventory"));
		}
	}
}
