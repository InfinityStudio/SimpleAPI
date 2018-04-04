package net.simpleAPI.attributes;

import net.minecraft.nbt.*;
import net.simpleAPI.PrimitiveType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ci010
 */
class VarSyncPrimitive<T> extends VarSyncable<T> {
	private T value;

	VarSyncPrimitive(String id, T data, UpdateMode frequency)
	{
		super(id, PrimitiveType.ofUnsafe(data.getClass()), frequency);
		this.value = data;
		this.set(data);
	}

	@Override
	public T get()
	{
		return value;
	}


	@Override
	protected void set0(Object data) {
		this.value = (T) data;
	}


	public static class Ranged<T extends Number> extends VarSyncPrimitive<T> {
		private T max, min;

		public Ranged(String id, T data, T max, T min, UpdateMode frequency)
		{
			super(id, data, frequency);
			this.max = max;
			this.min = min;
		}


		@Override
		public void set(T data)
		{
			if (super.type.greater(max, data) && super.type.less(min, data))
				super.set(data);
		}
	}

	public static class VEnum<T extends Enum<T>> extends VarSyncPrimitive<T> {

		private Class<? extends Enum> type;

		public VEnum(String name, T v, UpdateMode frequency)
		{
			super(name, v, frequency);
			type = v.getClass();
		}

		@Override
		public void readFromNBT(NBTTagCompound tag)
		{
			this.set0(Enum.valueOf(type, tag.getString(super.name)));
		}

		@Override
		public void writeToNBT(NBTTagCompound tag)
		{
			tag.setString(super.name, this.get().name());
		}
	}
}
