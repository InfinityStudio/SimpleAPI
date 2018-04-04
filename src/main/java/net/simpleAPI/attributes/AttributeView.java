package net.simpleAPI.attributes;


import com.google.common.collect.ImmutableList;
import net.simpleAPI.Overview;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;

/**
 * @author ci010
 */
public interface AttributeView extends Overview<Var<?>> {
	@CapabilityInject(AttributeView.class)
	Capability<AttributeView> CAPABILITY = null;

	Capability.IStorage<AttributeView> CAPABILITY_STORAGE = AttriImpl.STORAGE;

	@Nonnull
	ImmutableList<Var<?>> getVarsByMode(@Nonnull UpdateMode mode);
}
