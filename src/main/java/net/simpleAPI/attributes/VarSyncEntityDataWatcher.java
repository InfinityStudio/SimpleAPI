package net.simpleAPI.attributes;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.simpleAPI.PrimitiveType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ci010
 */
class VarSyncEntityDataWatcher<T> extends VarSyncable<T> {
	private DataParameter<T> id;
	private EntityDataManager delegate;

	VarSyncEntityDataWatcher(DataParameter<T> id, EntityDataManager watcher, String name, T data)
	{
		super(name, PrimitiveType.ofUnsafe(data.getClass()), UpdateMode.CONSTANTLY);
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
	public UpdateMode getUpdateMode()
	{
		return UpdateMode.CONSTANTLY;
	}

	@Override
	protected void set0(Object data) {
		delegate.set(id, (T) data);
	}

	public static class Ranged<T extends Number> extends VarSyncEntityDataWatcher<T> {
		private PrimitiveType type;
		private T max, min;

		Ranged(DataParameter<T> id, EntityDataManager watcher, String name, T data, T min, T max)
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

	public static class VarWatchingEnum<T extends Enum<T>> extends VarSyncable<T> {
		private DataParameter<String> id;
		private EntityDataManager delegate;
		private Class<T> tClass;

		VarWatchingEnum(Entity entity, T v, String name)
		{
			super(name, null, UpdateMode.CONSTANTLY);
			this.tClass = (Class<T>) v.getClass();
			this.delegate = entity.getDataManager();
			this.id = EntityDataManager.createKey(entity.getClass(), DataSerializers.STRING);
			this.delegate.set(id, v.name());
		}

		@Override
		public T get() {return Enum.valueOf(tClass, delegate.get(id));}

		@Override
		public UpdateMode getUpdateMode()
		{
			return UpdateMode.CONSTANTLY;
		}

		@Override
		protected void set0(Object data) {
			delegate.set(id, data.toString());
		}
	}

}
