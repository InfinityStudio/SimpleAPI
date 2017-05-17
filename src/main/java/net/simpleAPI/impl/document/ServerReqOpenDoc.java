package net.simpleAPI.impl.document;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * A server to client message.
 *
 * @author ci010
 */
abstract class ServerReqOpenDoc implements IMessage
{
	EnumFacing facing;
	ResourceLocation location;
	boolean cached;

	public abstract ICapabilityProvider getProvider(World world);

	@Override
	public void fromBytes(ByteBuf buf)
	{
//		content = ByteBufUtils.readUTF8String(buf);
		facing = EnumFacing.values()[buf.readByte()];
		location = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
//		ByteBufUtils.writeUTF8String(buf, content);
		buf.writeByte(facing.ordinal());
		ByteBufUtils.writeUTF8String(buf, location.toString());
	}
}
