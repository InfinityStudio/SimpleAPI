package net.simpleAPI.attributes.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.simpleAPI.attributes.AttributeView;
import net.simpleAPI.attributes.Var;
import net.simpleAPI.attributes.VarSyncable;

import java.io.IOException;

/**
 * @author ci010
 */
public class SPacketChunkAttr implements Packet<INetHandlerPlayClient> {
	public SPacketChunkAttr() {}

	private int chunkX;
	private int chunkZ;
	private NBTTagCompound tag;

	public SPacketChunkAttr(int chunkX, int chunkZ, NBTTagCompound tag) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.tag = tag;
	}

	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
		chunkX = buf.readInt();
		chunkZ = buf.readInt();
		tag = buf.readCompoundTag();
	}

	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeInt(chunkX);
		buf.writeInt(chunkZ);
		buf.writeCompoundTag(tag);
	}

	@Override
	public void processPacket(INetHandlerPlayClient handler) {
		this.handle();
	}

	@SideOnly(Side.CLIENT)
	private void handle() {
		Chunk c = Minecraft.getMinecraft().world.getChunkProvider().getLoadedChunk(chunkX, chunkZ);
		if (c == null) {
			return;
		}
		AttributeView cap = c.getCapability(AttributeView.CAPABILITY, null);
		if (cap == null) {
			return;
		}
		for (String key : tag.getKeySet()) {
			Var<?> var = cap.getByName(key);
			if (var == null) {
				continue;
			}
			((VarSyncable<Object>) var).readFromNBT(tag);
		}
	}

}
