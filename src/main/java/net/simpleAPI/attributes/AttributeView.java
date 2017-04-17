package net.simpleAPI.attributes;


import com.google.common.collect.ImmutableList;
import net.simpleAPI.Overview;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;

/**
 * @author ci010
 */
public interface AttributeView extends Overview<VarSync<?>>
{
	@CapabilityInject(AttributeView.class)
	Capability<AttributeView> CAPABILITY = null;

	@Nonnull
	ImmutableList<VarSync<?>> getVarsByMode(@Nonnull UpdateMode mode);
}
