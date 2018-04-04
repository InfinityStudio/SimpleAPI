package net.simpleAPI.attributes;

import net.minecraft.nbt.*;
import net.simpleAPI.PrimitiveType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ci010
 */
public abstract class VarSyncable<T> implements Var<T> {
	protected String name;
	protected PrimitiveType type;
	private boolean dirty = false;
	private UpdateMode updateMode;
	private List<Listener<T>> listeners = new ArrayList<>();

	@Override
	public UpdateMode getUpdateMode()
	{
		return updateMode;
	}

	VarSyncable(String name, PrimitiveType type, UpdateMode updateMode) {
		this.name = name;
		this.type = type;
		this.updateMode = updateMode;
	}

	public boolean dirty()
	{
		if (dirty) {
			dirty = false;
			return true;
		}
		return false;
	}

	protected abstract void set0(Object data);

	public void readFromNBT(NBTTagCompound tag)
	{
		if (!tag.hasKey(name)) return;
		if (type == null)
			this.set0(tag.getString(name));
		else switch (type) {
			case BOOL:
				this.set0(tag.getBoolean(name));
				break;
			case BYTE:
				this.set0(tag.getByte(name));
				break;
			case SHORT:
				this.set0(tag.getShort(name));
				break;
			case INT:
				this.set0(tag.getInteger(name));
				break;
			case LONG:
				this.set0(tag.getLong(name));
				break;
			case FLOAT:
				this.set0(tag.getFloat(name));
				break;
			case DOUBLE:
				this.set0(tag.getDouble(name));
				break;
		}
	}

	@Override
	public void set(T value)
	{
		if (value != get()) {
			this.dirty = true;
			set0(value);
			for (Listener<T> listener : this.listeners)
				listener.onChange(this);
		}
	}

	@Override
	public String toString() {
		return "Var{" +
				"name='" + name + '\'' +
				", value=" + get() +
				", type=" + type +
				", updateMode=" + updateMode +
				'}';
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

	private Number getNum() {
		return (Number) this.get();
	}

	public void writeToNBT(NBTTagCompound tag)
	{
		NBTBase nbt;
		if (this.type == null) {
			nbt = new NBTTagString((String) this.get());
		}
		else switch (this.type) {
			case BOOL:
				nbt = new NBTTagByte((byte) (((Boolean) this.get()) ? 1 : 0));
				break;
			case BYTE:
				nbt = new NBTTagByte(getNum().byteValue());
				break;
			case SHORT:
				nbt = new NBTTagShort(getNum().shortValue());
				break;
			case INT:
				nbt = new NBTTagInt(getNum().intValue());
				break;
			case LONG:
				nbt = new NBTTagLong(getNum().longValue());
				break;
			case FLOAT:
				nbt = new NBTTagFloat(getNum().floatValue());
				break;
			case DOUBLE:
				nbt = new NBTTagDouble(getNum().doubleValue());
				break;
			case CHARACTER:
				nbt = new NBTTagByte(getNum().byteValue());
				break;
			default:
				nbt = new NBTTagString((String) this.get());
				break;
		}
		tag.setTag(this.name, nbt);
	}

}
