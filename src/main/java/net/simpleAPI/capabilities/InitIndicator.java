package net.simpleAPI.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author ci010
 */
public interface InitIndicator
{
	NBTTagCompound getTag();

	ICapabilityProvider getContext();
}
