package net.simpleAPI.impl.attribute;

import io.netty.buffer.Unpooled;
import net.simpleAPI.PrimitiveType;
import net.simpleAPI.attributes.UpdateMode;
import net.simpleAPI.attributes.VarSync;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import java.io.IOException;
import java.util.List;

/**
 * @author ci010
 */
public class VarSyncEntityDataWatcher<T> implements VarSync<T>
{
	private DataParameter<T> id;
	private String name;
	private EntityDataManager delegate;

	private List<Listener<T>> listeners;

	protected VarSyncEntityDataWatcher(DataParameter<T> id, EntityDataManager watcher, String name, T data)
	{
		if (data == null)
			throw new NullPointerException("The initial data cannot be null!");
		if (name == null)
			throw new NullPointerException("The getName cannot be null!");
		this.name = name;
		this.delegate = watcher;
		this.id = id;
	}

	public DataParameter<T> getId()
	{
		return this.id;
	}

	protected EntityDataManager getDelegate()
	{
		return this.delegate;
	}

	public T get()
	{
		return delegate.get(id);
	}

	@Override
	public void addListener(Listener<T> listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeListener(Listener<T> listener)
	{
		listeners.remove(listener);
	}

	public void set(T data)
	{
		if (data != get())
		{
			this.delegate.set(id, data);
			for (Listener<T> listener : this.listeners)
				listener.onChange(this);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
		packetBuffer.writeCompoundTag(tag.getCompoundTag(this.name));
		try
		{
			this.set(this.id.getSerializer().read(packetBuffer));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
		this.id.getSerializer().write(packetBuffer, this.get());
		try
		{
			NBTTagCompound newTag = packetBuffer.readCompoundTag();
			tag.setTag(this.name, newTag);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		return this.get().toString();
	}

	@Override
	public UpdateMode getUpdateMode()
	{
		return UpdateMode.CONSTANTLY;
	}

	@Override
	public boolean needSave()
	{
		return false;
	}

	public static class Ranged<T extends Number> extends VarSyncEntityDataWatcher<T>
	{
		private PrimitiveType type;
		private T max, min;

		public Ranged(DataParameter<T> id, EntityDataManager watcher, String name, T data, T min, T max)
		{
			super(id, watcher, name, data);
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

	public static class VarWatchingEnum<T extends Enum<T>> implements VarSync<T>
	{
		private DataParameter<String> id;
		private String name;
		private EntityDataManager delegate;
		private Class<T> tClass;

		VarWatchingEnum(Entity entity, T v, String name)
		{
			this.name = name;
			this.tClass = (Class<T>) v.getClass();
			this.delegate = entity.getDataManager();
			this.id = EntityDataManager.createKey(entity.getClass(), DataSerializers.STRING);
			this.delegate.set(id, v.name());
		}

		@Override
		public void readFromNBT(NBTTagCompound tag)
		{
			delegate.set(id, tag.getString(name));
		}

		@Override
		public void writeToNBT(NBTTagCompound tag)
		{
			tag.setString(name, delegate.get(id));
		}

		@Override
		public void addListener(Listener<T> listener)
		{

		}

		@Override
		public void removeListener(Listener<T> listener)
		{

		}

		@Override
		public void set(T value) {delegate.set(id, value.name());}

		@Override
		public T get() {return Enum.valueOf(tClass, delegate.get(id));}

		@Override
		public UpdateMode getUpdateMode()
		{
			return UpdateMode.CONSTANTLY;
		}

		@Override
		public boolean needSave()
		{
			return false;
		}
	}

}
