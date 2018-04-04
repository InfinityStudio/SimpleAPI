package net.simpleAPI.attributes;


import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public interface VarSync<T> extends Var<T>
{
	void readFromNBT(NBTTagCompound tag);

	void writeToNBT(NBTTagCompound tag);

	UpdateMode getUpdateMode();

	boolean dirty();
}
