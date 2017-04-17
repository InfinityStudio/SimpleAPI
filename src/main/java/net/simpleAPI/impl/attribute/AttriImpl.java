package net.simpleAPI.impl.attribute;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.simpleAPI.attributes.AttributeView;
import net.simpleAPI.attributes.UpdateMode;
import net.simpleAPI.attributes.VarSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * @author ci010
 */
public class AttriImpl implements AttributeView
{
	public static Capability.IStorage<AttributeView> STORAGE = new Capability.IStorage<AttributeView>()
	{
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<AttributeView> capability, AttributeView instance, EnumFacing side)
		{
			for (VarSync<?> sync : instance.getAll())
			{

			}
			return null;
		}

		@Override
		public void readNBT(Capability<AttributeView> capability, AttributeView instance, EnumFacing side, NBTBase nbt)
		{
			if (nbt instanceof NBTInitIndicator && instance instanceof AttriImpl)
			{
				AttriImpl impl = (AttriImpl) instance;
				NBTTagCompound tag = ((NBTInitIndicator) nbt).getTag();
				Map<String, VarSync<?>> constant = Maps.newTreeMap(), lazy = Maps.newTreeMap();
				for (String propName : tag.getKeySet())
				{
					NBTTagCompound compoundTag = tag.getCompoundTag(propName);
					String type = compoundTag.getString("type");
					if (type.equals("")) continue;

					String mode = compoundTag.getString("mode");
					if (mode.equals("")) mode = "lazy";

					if (compoundTag.hasKey("value"))
					{
						double value = compoundTag.getDouble("value");
					}

				}
				impl.init(constant, lazy);
			}
			else for (VarSync<?> sync : instance.getAll())
			{
			}
		}
	};
	protected ImmutableList<VarSync<?>> list, constantly, lazy;
	protected ImmutableBiMap<String, VarSync<?>> map;
	private boolean init;

	public AttriImpl() {}

	private void init(Map<String, VarSync<?>> constantly, Map<String, VarSync<?>> lazy)
	{
		if (init) return;
		this.map = ImmutableBiMap.<String, VarSync<?>>builder().putAll(constantly).putAll(lazy).build();
		this.constantly = ImmutableList.copyOf(constantly.values());
		this.lazy = ImmutableList.copyOf(lazy.values());
		this.list = ImmutableList.<VarSync<?>>builder().addAll(this.lazy).addAll(this.constantly).build();
		this.init = true;
	}

	@Override
	public VarSync getById(int id)
	{
		return list.get(id);
	}

	@Override
	public VarSync getByName(@Nonnull String name)
	{
		return map.get(name);
	}

	@Nonnull
	@Override
	public Collection<? extends VarSync<?>> getAll()
	{
		return list;
	}

	@Override
	public VarSync<?>[] toArray()
	{
		return new VarSync[0];
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
	public ImmutableList<VarSync<?>> getVarsByMode(@Nonnull UpdateMode mode)
	{
		return mode == UpdateMode.CONSTANTLY ? constantly : lazy;
	}
}
