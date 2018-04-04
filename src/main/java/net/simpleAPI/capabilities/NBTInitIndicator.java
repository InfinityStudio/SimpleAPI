package net.simpleAPI.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author ci010
 */
public class NBTInitIndicator extends NBTTagEnd implements InitIndicator
{
	private NBTTagCompound tag;
	private ICapabilityProvider context;

	public NBTInitIndicator(NBTTagCompound tagCompound, ICapabilityProvider context)
	{
		this.tag = tagCompound;
		this.context = context;
	}

	public ICapabilityProvider getContext() {return context;}

	public NBTTagCompound getTag() {return tag;}

	@Override
	public String toString()
	{
		return "NBTInitIndicator{" +
				"tag=" + tag +
				'}';
	}

	@Override
	public NBTInitIndicator copy()
	{
		return new NBTInitIndicator(tag, context);
	}
}
