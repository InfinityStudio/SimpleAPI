package net.simpleAPI.attributes;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.simpleAPI.capabilities.NBTInitIndicator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * @author ci010
 */
class AttriImpl implements AttributeView {
	static Capability.IStorage<AttributeView> STORAGE = new Capability.IStorage<AttributeView>() {
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<AttributeView> capability, AttributeView instance, EnumFacing side)
		{
			NBTTagCompound tag = new NBTTagCompound();
			for (Var<?> var : instance.getAll()) {
				VarSyncable<?> sync = (VarSyncable<?>) var;
				sync.writeToNBT(tag);
			}
			return tag;
		}

		@Override
		public void readNBT(Capability<AttributeView> capability, AttributeView instance, EnumFacing side, NBTBase nbt)
		{
			if (nbt instanceof NBTInitIndicator && instance instanceof AttriImpl) {
				AttriImpl impl = (AttriImpl) instance;
				NBTTagCompound tag = ((NBTInitIndicator) nbt).getTag();
				Map<String, VarSync<?>> constantly = Maps.newTreeMap(), lazy = Maps.newTreeMap();
				for (String propName : tag.getKeySet()) {
					NBTTagCompound compoundTag = tag.getCompoundTag(propName);
					String type = compoundTag.getString("type");
					if (type.equals("")) continue;

					String mode = compoundTag.getString("mode");
					if (mode.equals("")) mode = "lazy";

					if (compoundTag.hasKey("value")) {
						double value = compoundTag.getDouble("value");
					}
				}
				impl.map = ImmutableBiMap.<String, Var<?>>builder().putAll(constantly).putAll(lazy).build();
				impl.constantly = ImmutableList.copyOf(constantly.values());
				impl.lazy = ImmutableList.copyOf(lazy.values());
				impl.list = ImmutableList.<Var<?>>builder().addAll(impl.lazy).addAll(impl.constantly).build();
			}
			else {
				if (nbt.getId() != 10) {
					return;
				}
				NBTTagCompound tag = (NBTTagCompound) nbt;
				for (Var<?> var : instance.getAll()) {
					VarSyncable<?> sync = (VarSyncable<?>) var;
					sync.readFromNBT(tag);
				}
			}
		}
	};

	static {
		CapabilityManager.INSTANCE.register(AttributeView.class, STORAGE, AttriImpl::new);
	}

	protected ImmutableList<Var<?>> list, constantly, lazy;
	protected ImmutableBiMap<String, Var<?>> map;

	private AttriImpl() {}

	AttriImpl(Map<String, Var<?>> constantly, Map<String, Var<?>> lazy) {
		this.map = ImmutableBiMap.<String, Var<?>>builder().putAll(constantly).putAll(lazy).build();
		this.constantly = ImmutableList.copyOf(constantly.values());
		this.lazy = ImmutableList.copyOf(lazy.values());
		this.list = ImmutableList.<Var<?>>builder().addAll(this.lazy).addAll(this.constantly).build();
	}

	@Override
	public Var getById(int id)
	{
		return list.get(id);
	}

	@Override
	public Var getByName(@Nonnull String name)
	{
		return map.get(name);
	}

	@Nonnull
	@Override
	public Collection<? extends Var<?>> getAll()
	{
		return list;
	}

	@Override
	public Var<?>[] toArray()
	{
		return new Var[0];
	}

	@Override
	public int size()
	{
		return list.size();
	}

	@Nonnull
	@Override
	public Collection<String> allPresent()
	{
		return map.keySet();
	}

	@Nonnull
	@Override
	public ImmutableList<Var<?>> getVarsByMode(@Nonnull UpdateMode mode)
	{
		return mode == UpdateMode.CONSTANTLY ? constantly : lazy;
	}
}
