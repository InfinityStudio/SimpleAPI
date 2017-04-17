package net.simpleAPI.impl.attribute;

import net.minecraft.nbt.NBTTagCompound;
import net.simpleAPI.attributes.UpdateMode;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.simpleAPI.attributes.VarSync;

/**
 * @author ci010
 */
public class VarSyncEntityAttribute implements VarSync<Double>
{
	private IAttributeInstance instance;

	public VarSyncEntityAttribute(IAttributeInstance instance)
	{
		this.instance = instance;
	}

	@Override
	public void set(Double value)
	{
		instance.setBaseValue(value);
	}

	@Override
	public Double get()
	{
		return instance.getAttributeValue();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		String name = instance.getAttribute().getName();
		double aDouble = tag.getDouble(name);
		instance.setBaseValue(aDouble);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		tag.setDouble(instance.getAttribute().getName(), instance.getBaseValue());
	}

	@Override
	public void addListener(Listener<Double> listener)
	{

	}

	@Override
	public void removeListener(Listener<Double> listener)
	{

	}


	@Override
	public UpdateMode getUpdateMode() {return UpdateMode.CONSTANTLY;}

	@Override
	public boolean needSave() {return true;}
}
