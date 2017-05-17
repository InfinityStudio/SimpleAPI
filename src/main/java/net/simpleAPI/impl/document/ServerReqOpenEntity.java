package net.simpleAPI.impl.document;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author ci010
 */
public class ServerReqOpenEntity extends ServerReqOpenDoc
{
	int entityId;

	@Override
	public ICapabilityProvider getProvider(World world)
	{
		return world.getEntityByID(entityId);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
		entityId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
		buf.writeInt(entityId);
	}
}
