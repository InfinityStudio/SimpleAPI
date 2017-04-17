package net.simpleAPI.impl.attribute;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.simpleAPI.PrimitiveType;
import net.simpleAPI.attributes.AttributeFactory;
import net.simpleAPI.attributes.UpdateMode;
import net.simpleAPI.attributes.Var;
import net.simpleAPI.attributes.VarSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author ci010
 */
public class EntityVarFactory extends CommonAttributeFactory implements AttributeFactory
{
	private Entity entity;

	public EntityVarFactory(Entity entity)
	{
		this.entity = entity;
	}

	protected <T> DataSerializer<T> getSerializer(T o)
	{
		if (String.class.isAssignableFrom(o.getClass()))
			return (DataSerializer<T>) DataSerializers.STRING;
		Optional<DataSerializer> serializerOptional = PrimitiveType.of(o.getClass()).transform(new Function<PrimitiveType, DataSerializer>()
		{
			@Nullable
			@Override
			public DataSerializer apply(@Nullable PrimitiveType input)
			{
				switch (input)
				{
					case BOOL:
						return DataSerializers.BOOLEAN;
					case BYTE:
						return DataSerializers.BYTE;
					case LONG:
						return DataSerializersAddon.LONG;
					case SHORT:
						return DataSerializersAddon.SHORT;
					case INT:
						return DataSerializers.VARINT;
					case FLOAT:
						return DataSerializers.FLOAT;
					case DOUBLE:
						return DataSerializersAddon.DOUBLE;
					case CHARACTER:
						return DataSerializersAddon.CHARACTER;
				}
				return null;
			}
		});
		return serializerOptional.or(DataSerializers.STRING);
	}

	protected <T> DataParameter<T> registerDataWatcher(T o)
	{
		DataSerializer<T> byId = getSerializer(o);
		DataParameter<T> key = EntityDataManager.createKey(entity.getClass(), byId);
		entity.getDataManager().register(key, o);
		return key;
	}

	@Override
	public <T extends Number> Var<T> newNumber(@Nonnull String name, @Nonnull T v, T min, T max, UpdateMode mode)
	{
		Preconditions.checkNotNull(v, "The default value cannot be null!");
		Preconditions.checkNotNull(name, "The variable's name cannot be null!");
		if (mode == null) mode = UpdateMode.LAZY;
		VarSync<T> varSync = mode == UpdateMode.LAZY ? new VarSyncPrimitive.Ranged<T>(name, v, max, min, mode) :
				new VarSyncEntityDataWatcher.Ranged<T>(this.registerDataWatcher(v),
						entity.getDataManager(),
						name, v, min, max);
		if (mode == UpdateMode.CONSTANTLY) constantly.put(name, varSync);
		else lazy.put(name, varSync);
		return varSync;
	}

	protected <T> VarSync<T> produceVar(String name, UpdateMode mode, T v)
	{
		if (mode == UpdateMode.LAZY) return new VarSyncPrimitive<T>(name, v, mode);
		return new VarSyncEntityDataWatcher<T>(registerDataWatcher(v), entity.getDataManager(), name, v);
	}

	@Override
	public <T extends Enum<T>> Var<T> newEnum(@Nonnull String name, @Nonnull T e, UpdateMode mode)
	{
		Preconditions.checkNotNull(e, "The default value cannot be null!");
		Preconditions.checkNotNull(name, "The variable's name cannot be null!");
		if (mode == null) mode = UpdateMode.LAZY;
		VarSync<T> v = mode == UpdateMode.LAZY ? new VarSyncPrimitive.VEnum<T>(name, e, mode) :
				new VarSyncEntityDataWatcher.VarWatchingEnum<T>(entity, e, name);
		if (mode == UpdateMode.CONSTANTLY) constantly.put(name, v);
		else lazy.put(name, v);
		return v;
	}
}
