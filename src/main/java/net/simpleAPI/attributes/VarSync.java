package net.simpleAPI.attributes;


import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public interface VarSync<T> extends Var<T>
{
	void readFromNBT(NBTTagCompound tag);

	void writeToNBT(NBTTagCompound tag);

	interface Listener<T>
	{
		<R extends VarSync<T>> void onChange(R value);
	}

	void addListener(Listener<T> listener);

	void removeListener(Listener<T> listener);

	UpdateMode getUpdateMode();

	boolean needSave();
}
