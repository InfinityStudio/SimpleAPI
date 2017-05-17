package net.simpleAPI.impl.document;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author ci010
 */
public class ServerReqOpenTile extends ServerReqOpenDoc
{
	BlockPos tilePos;

	@Override
	public ICapabilityProvider getProvider(World world)
	{
		return world.getTileEntity(tilePos);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		super.fromBytes(buf);
		tilePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		super.toBytes(buf);
		buf.writeInt(tilePos.getX()).writeInt(tilePos.getY()).writeInt(tilePos.getZ());
	}
}
