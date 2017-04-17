package net.simpleAPI.impl.attribute;

import net.minecraft.nbt.NBTTagCompound;
import net.simpleAPI.PrimitiveType;
import net.simpleAPI.attributes.UpdateMode;
import net.simpleAPI.attributes.VarSync;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ci010
 */
public class VarSyncPrimitive<T> implements VarSync<T>
{
	private UpdateMode updateModel;
	private T value;
	private String name;
	private PrimitiveType type;

	private List<Listener<T>> listeners = new ArrayList<Listener<T>>();

	public VarSyncPrimitive(String id, T data, UpdateMode frequency)
	{
		this.name = id;
		this.value = data;
		this.updateModel = frequency;
		this.type = PrimitiveType.ofUnsafe(data.getClass());
		this.set(data);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		if (tag.getTag(name) == null) return;
		if (type == null)
			this.value = (T) tag.getString(name);
		else switch (type)
		{
			case BOOL:
				this.value = (T) (Object) tag.getBoolean(name);
				break;
			case BYTE:
				this.value = (T) (Object) tag.getByte(name);
				break;
			case SHORT:
				this.value = (T) (Object) tag.getShort(name);
				break;
			case INT:
				this.value = (T) (Object) tag.getInteger(name);
				break;
			case LONG:
				this.value = (T) (Object) tag.getLong(name);
				break;
			case FLOAT:
				this.value = (T) (Object) tag.getFloat(name);
				break;
			case DOUBLE:
				this.value = (T) (Object) tag.getDouble(name);
				break;
		}
//		this.value = (NBTBases.instance().deserialize(tag.getTag(name)));
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
//		tag.setTag(name, NBTBases.instance().serialize(this.get()));
	}

	@Override
	public void addListener(Listener<T> listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeListener(Listener<T> listener)
	{
		listeners.remove(listeners);
	}

	@Override
	public UpdateMode getUpdateMode()
	{
		return updateModel;
	}

	@Override
	public boolean needSave()
	{
		return true;
	}

	@Override
	public void set(T value)
	{
		if (value != this.value)
			this.value = value;
		for (Listener<T> listener : listeners)
			listener.onChange(this);
	}

	@Override
	public T get()
	{
		return value;
	}

	protected void load(T data)
	{
		this.value = data;
	}

	public static class Ranged<T extends Number> extends VarSyncPrimitive<T>
	{
		private T max, min;
		private PrimitiveType type;

		public Ranged(String id, T data, T max, T min, UpdateMode frequency)
		{
			super(id, data, frequency);
			this.type = PrimitiveType.ofUnsafe(data.getClass());
			this.max = max;
			this.min = min;
		}

		@Override
		public void set(T data)
		{
			if (type.greater(max, data) && type.less(min, data))
				super.set(data);
		}
	}

	public static class VEnum<T extends Enum<T>> extends VarSyncPrimitive<T>
	{
		private Class<? extends Enum> type;

		public VEnum(String name, T v, UpdateMode frequency)
		{
			super(name, v, frequency);
			type = v.getClass();
		}

		@Override
		public void readFromNBT(NBTTagCompound tag)
		{
			this.load((T) Enum.valueOf(type, tag.getString(super.name)));
		}

		@Override
		public void writeToNBT(NBTTagCompound tag)
		{
			tag.setString(super.name, this.get().name());
		}
	}
}
