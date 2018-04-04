package net.simpleAPI.attributes.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.simpleAPI.attributes.AttributeView;
import net.simpleAPI.attributes.Var;
import net.simpleAPI.attributes.VarSync;
import net.simpleAPI.attributes.VarSyncable;

import java.io.IOException;

/**
 * @author ci010
 */
public class SPacketWorldAttr implements Packet<INetHandlerPlayClient> {
	private NBTTagCompound tag;

	public SPacketWorldAttr() {}

	public SPacketWorldAttr(NBTTagCompound tag) {
		this.tag = tag;
	}

	@Override
	public void readPacketData(PacketBuffer buf) throws IOException {
		tag = buf.readCompoundTag();
	}

	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeCompoundTag(tag);
	}

	@Override
	public void processPacket(INetHandlerPlayClient handler) {
		this.handle();
	}
	@SideOnly(Side.CLIENT)
	private void handle() {
		World w = Minecraft.getMinecraft().world;
		AttributeView cap = w.getCapability(AttributeView.CAPABILITY, null);
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
