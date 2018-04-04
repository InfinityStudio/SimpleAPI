package net.simpleAPI.impl.reg;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ci010
 */
public class RegBlock extends RegComponent<Block>
{
	private String ore;

	public RegBlock(Block component)
	{
		super(component);
	}

	public RegBlock setOre(String ore)
	{
		this.ore = ore;
		return this;
	}

	@Override
	public void register()
	{
//		GameRegistry.register(getComponent());
		if (ore != null)
			OreDictionary.registerOre(ore, getComponent());
	}

	@Override
	public void registerClient()
	{
		Block block = getComponent();
		Item item = Item.getItemFromBlock(block);
		NonNullList<ItemStack> stacks = NonNullList.create();
//		block.getSubBlocks(item, item.getCreativeTab(), stacks);
		for (int i = 0; i < stacks.size(); i++)
			ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(block.getRegistryName
					(), "inventory"));
	}
}
