package net.simpleAPI.block;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ci010
 */
public abstract class PropertyHandler<T extends Comparable<T>>
{
	private static Map<Class, Function<IProperty, PropertyHandler>> handlerFactory = new HashMap<Class, Function<IProperty, PropertyHandler>>();

	static
	{
		handlerFactory.put(Integer.class, new Function<IProperty, PropertyHandler>()
		{
			@Nullable
			@Override
			public PropertyHandler apply(@Nullable IProperty input)
			{
				int size = input.getAllowedValues().size();
				double ceil = Math.ceil(Math.log(size) / Math.log(2));
				return new PropertyHandler<Integer>(input, (int) ceil, size)
				{
					@Override
					public Integer getValueFromMeta(int meta) {return meta;}

					@Override
					public int getMetaFromValue(Integer value) {return value;}
				};
			}
		});
		handlerFactory.put(Boolean.class, new Function<IProperty, PropertyHandler>()
		{
			@Nullable
			@Override
			public PropertyHandler apply(@Nullable IProperty input)
			{
				return new PropertyHandler<Boolean>(input, 1, 1)
				{
					@Override
					public Boolean getValueFromMeta(int meta) {return meta == 1;}

					@Override
					public int getMetaFromValue(Boolean value) {return value ? 1 : 0;}
				};
			}
		});
		handlerFactory.put(EnumFacing.class, new Function<IProperty, PropertyHandler>()
		{
			@Nullable
			@Override
			public PropertyHandler apply(@Nullable IProperty input)
			{
				return new PropertyHandler<EnumFacing>(input, 3, 7)
				{
					@Override
					public EnumFacing getValueFromMeta(int meta) {return EnumFacing.values()[meta];}

					@Override
					public int getMetaFromValue(EnumFacing value) {return value.ordinal();}
				};
			}
		});
	}

	public static PropertyHandler createHandler(IProperty<?> property)
	{
		Preconditions.checkNotNull(property);
		return handlerFactory.get(property.getValueClass()).apply(property);
	}

	private int useBit, mask;
	private IProperty<T> property;

	public PropertyHandler(IProperty<T> property, int useBit, int mask)
	{
		this.property = property;
		this.useBit = useBit;
		this.mask = mask;
	}

	public int getUseBit() {return useBit;}

	public int getMask() {return mask;}

	public abstract T getValueFromMeta(int meta);

	public abstract int getMetaFromValue(T value);

	public IBlockState getStateFromMeta(IBlockState state, int meta)
	{
		return state.withProperty(property, getValueFromMeta(meta));
	}

	public int getMetaFromState(IBlockState state)
	{
		return getMetaFromValue(state.getValue(property));
	}
}
