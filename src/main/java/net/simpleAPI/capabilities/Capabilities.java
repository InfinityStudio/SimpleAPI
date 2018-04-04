package net.simpleAPI.capabilities;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author ci010
 */
public class Capabilities
{
	/**
	 * Return a {@link Capability.IStorage} which just does nothing.
	 *
	 * @return A empty {@link Capability.IStorage}.
	 */
	public static <T> Capability.IStorage<T> emptyStorage() {return (Capability.IStorage<T>) STORE;}

	/**
	 * Helper method to create {@link CapabilityDispatcher}.
	 *
	 * @param event The event.
	 * @return A new {@link CapabilityDispatcher}.
	 */
	public static CapabilityDispatcher gatherCapabilities(AttachCapabilitiesEvent event)
	{
		MinecraftForge.EVENT_BUS.post(event);
		return event.getCapabilities().size() > 0 ? new CapabilityDispatcher(event.getCapabilities()) : null;
	}

	/**
	 * Create a new builder for a capability.
	 *
	 * @param capability The capability instance.
	 * @param <T>        The capability type.
	 * @return The capability builder.
	 * @see Builder
	 */
	public static <T> Builder<T> newBuilder(Capability<T> capability)
	{
		return new Builder<T>(capability);
	}

	/**
	 * If your capability instance would like to load the {@link ItemStack} which the instance attaches to. You could
	 * implement {@link Predicate} and create a builder here.
	 */
	public static <T extends Predicate<ItemStack>> BuilderInContext<T, ItemStack> newBuilder(Capability<T> capability, ItemStack stack)
	{
		return new BuilderInContext<T, ItemStack>(capability, stack);
	}

	public static <T extends Predicate<Entity>> BuilderInContext<T, Entity> newBuilder(Capability<T> capability, Entity entity)
	{
		return new BuilderInContext<T, Entity>(capability, entity);
	}

	public static <T extends Predicate<TileEntity>> BuilderInContext<T, TileEntity> newBuilder(Capability<T> capability, TileEntity entity)
	{
		return new BuilderInContext<T, TileEntity>(capability, entity);
	}

	public static class Builder<T>
	{
		public final Capability<T> capability;
		private EnumMap<EnumFacing, T> instances = new EnumMap<EnumFacing, T>(EnumFacing.class);
		private T single;
		private byte size = 0;
		private byte[] index = new byte[6];

		public Builder(Capability<T> capability) {this.capability = capability;}

		public Builder<T> append(T instance, EnumFacing... facings)
		{
			Preconditions.checkNotNull(instance);
			if (facings == null || facings.length == 0)
				if (instances.isEmpty())
					if (single == null)
						single = instance;
					else throw new IllegalArgumentException("Cannot addItem two free instance to all facing for one " +
							"Capability!");
				else throw new IllegalArgumentException("All faces are occupied! cannot addItem new instance.");
			else
			{
				for (EnumFacing facing : facings)
					if (!instances.containsKey(facing))
					{
						index[facing.ordinal()] = size;
						instances.put(facing, instance);
					}
					else throw new IllegalArgumentException("The face " + facing + " was already occupied!");
			}
			++size;
			return this;
		}

		public Builder<T> appendDefaultInstance(EnumFacing... facings)
		{
			return append(capability.getDefaultInstance(), facings);
		}

		public ICapabilityProvider buildUnsaved()
		{
			if (instances.isEmpty() && single != null)
				return new Common<T>(single, capability);
			return new Sided<T>(capability, this.instances);
		}

		public ICapabilitySerializable<NBTBase> build()
		{
			if (instances.isEmpty() && single != null)
				return new CommonSerial<T>(single, capability);
			return new SidedSerial<T>(capability, this.instances, size, index);
		}
	}

	public static class BuilderInContext<T extends Predicate<C>, C>
	{
		private C context;
		public final Capability<T> capability;
		private EnumMap<EnumFacing, T> instances = new EnumMap<EnumFacing, T>(EnumFacing.class);
		private T single;
		private byte size = 0;
		private byte[] index = new byte[6];

		private BuilderInContext(Capability<T> capability, C context)
		{
			this.context = context;
			this.capability = capability;
		}

		public boolean append(T instance, EnumFacing... facings)
		{
			Preconditions.checkNotNull(instance);
			boolean pass = instance.apply(context);
			if (!pass) return false;
			if (facings == null)
				if (instances.isEmpty())
					if (single == null)
						single = instance;
					else throw new IllegalArgumentException("Cannot addItem two free instance to all facing for one " +
							"Capability!");
				else throw new IllegalArgumentException("All faces are occupied! cannot addItem new instance.");
			else
				for (EnumFacing facing : facings)
					if (!instances.containsKey(facing))
					{
						index[facing.ordinal()] = size;
						instances.put(facing, instance);
					}
					else throw new IllegalArgumentException("The face " + facing + " was already occupied!");
			++size;
			return true;
		}


		public boolean appendDefaultInstance(EnumFacing... facings)
		{
			return append(capability.getDefaultInstance(), facings);
		}

		public ICapabilityProvider build()
		{
			if (instances.isEmpty() && single != null)
				return new Common<T>(single, capability);
			return new Sided<T>(capability, this.instances);
		}

		public ICapabilitySerializable<NBTBase> buildSerializable()
		{
			if (instances.isEmpty() && single != null)
				return new CommonSerial<T>(single, capability);
			return new SidedSerial<T>(capability, this.instances, size, index);
		}
	}

	public static class Common<T> implements ICapabilityProvider
	{
		protected T real;
		protected Capability<T> capability;

		public Common(T real, Capability<T> capability)
		{
			this.real = real;
			this.capability = capability;
		}

		public Common(Capability<T> capability)
		{
			this.capability = capability;
			this.real = capability.getDefaultInstance();
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == this.capability;
		}

		@Override
		public <C> C getCapability(Capability<C> capability, EnumFacing facing)
		{
			if (hasCapability(capability, facing))
				return (C) real;
			return null;
		}
	}


	public static class CommonSerial<T> extends Common<T> implements ICapabilitySerializable<NBTBase>
	{
		public CommonSerial(T real, Capability<T> capability)
		{
			super(real, capability);
		}

		public CommonSerial(Capability<T> capability)
		{
			super(capability);
		}

		@Override
		public NBTBase serializeNBT()
		{
			return capability.getStorage().writeNBT(capability, this.real, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			this.capability.getStorage().readNBT(capability, real, null, nbt);
		}
	}

	public static class Sided<T> implements ICapabilityProvider
	{
		protected Capability<T> capability;
		protected T[] sidedInstances;

		public Sided(Capability<T> capability, EnumMap<EnumFacing, T> sideMap)
		{
			this.capability = capability;
			sidedInstances = (T[]) new Object[EnumFacing.values().length];
			for (Map.Entry<EnumFacing, T> entry : sideMap.entrySet())
				sidedInstances[entry.getKey().ordinal()] = entry.getValue();
		}

		public Sided(Capability<T> capability, EnumFacing... side)
		{
			EnumMap<EnumFacing, T> sideMap = new EnumMap<EnumFacing, T>(EnumFacing.class);
			if (side == null || side.length == 0)
				for (EnumFacing value : EnumFacing.VALUES)
					sideMap.put(value, capability.getDefaultInstance());
			else
				for (EnumFacing facing : side)
					sideMap.put(facing, capability.getDefaultInstance());
			this.capability = capability;
			sidedInstances = (T[]) new Object[EnumFacing.values().length];
			for (Map.Entry<EnumFacing, T> entry : sideMap.entrySet())
				sidedInstances[entry.getKey().ordinal()] = entry.getValue();
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return this.capability == capability && sidedInstances[facing.ordinal()] != null;
		}

		@Override
		public <C> C getCapability(Capability<C> capability, EnumFacing facing)
		{
			return (C) sidedInstances[facing.ordinal()];
		}
	}

	public static class SidedSerial<T> extends Sided<T> implements ICapabilitySerializable<NBTBase>
	{
		protected byte[] index;
		protected byte size;

		public SidedSerial(Capability<T> capability, EnumMap<EnumFacing, T> sideMap, byte size, byte[] index)
		{
			super(capability, sideMap);
		}

		@Override
		public NBTBase serializeNBT()
		{
			NBTTagList lst = new NBTTagList();
			for (byte i = 0; i < size; i++)
				lst.appendTag(this.capability.writeNBT(sidedInstances[org.apache.commons.lang3.ArrayUtils.indexOf(index, i)], null));
			return lst;
		}

		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			if (nbt instanceof NBTTagList)
			{
				NBTTagList lst = (NBTTagList) nbt;
				for (byte i = 0; i < size; i++)
					capability.readNBT(sidedInstances[org.apache.commons.lang3.ArrayUtils.indexOf(index, i)], null, lst.get(i));
			}
		}
	}

	private static final Capability.IStorage<Object> STORE = new Capability.IStorage<Object>()
	{
		@Override
		public NBTBase writeNBT(Capability<Object> capability, Object instance, EnumFacing side) {return null;}

		@Override
		public void readNBT(Capability<Object> capability, Object instance, EnumFacing side, NBTBase nbt) {}
	};

	private Capabilities() {}
}
