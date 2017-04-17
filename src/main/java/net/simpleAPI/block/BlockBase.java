package net.simpleAPI.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

/**
 * @author ci010
 */
public class BlockBase extends Block
{
	private PropertyHandler[] handlers;

	public BlockBase(Material blockMaterialIn, MapColor blockMapColorIn, IProperty[] properties)
	{
		super(blockMaterialIn, blockMapColorIn);
		this.handlers = new PropertyHandler[properties.length];
		for (int i = 0; i < properties.length; i++)
			handlers[i] = PropertyHandler.createHandler(properties[i]);
	}

	public BlockBase(Material materialIn, IProperty[] properties)
	{
		super(materialIn);
		this.handlers = new PropertyHandler[properties.length];
		for (int i = 0; i < properties.length; i++)
			handlers[i] = PropertyHandler.createHandler(properties[i]);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState baseState = this.blockState.getBaseState();
		for (PropertyHandler propertyHandler : handlers)
		{
			baseState = propertyHandler.getStateFromMeta(baseState, meta & propertyHandler.getMask());
			meta >>= propertyHandler.getUseBit();
		}
		return baseState;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = 0;
		for (PropertyHandler handler : handlers)
		{
			meta |= (handler.getMetaFromState(state) & handler.getMask());
			meta <<= handler.getUseBit();
		}
		return meta;
	}
}
