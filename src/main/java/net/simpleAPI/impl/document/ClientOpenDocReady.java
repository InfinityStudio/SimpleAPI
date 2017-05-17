package net.simpleAPI.impl.document;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author ci010
 */
public class ClientOpenDocReady implements IMessage
{
	String[] required;

	@Override
	public void fromBytes(ByteBuf buf)
	{
		required = new String[buf.readShort()];
		for (int i = 0; i < required.length; i++)
			required[i] = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeShort(required.length);
		for (int i = 0; i < required.length; i++)
			ByteBufUtils.writeUTF8String(buf, required[i]);
	}
}
