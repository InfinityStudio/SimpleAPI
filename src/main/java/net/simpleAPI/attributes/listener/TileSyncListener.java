package net.simpleAPI.attributes.listener;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.simpleAPI.attributes.*;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * This is a coremod delegate class for {@link TileEntity} to satisfy {@link UpdateMode#CONSTANTLY}.
 *
 * @author ci010
 */
public class TileSyncListener {
	public static void $onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		System.out.println("Call hook $onDataPacket");
	}

	public static SPacketUpdateTileEntity $getUpdatePacket(TileEntity entity) {
		System.out.println("Call hook $getUpdatePacket");
		AttributeView capability = entity.getCapability(AttributeView.CAPABILITY, null);
		if (capability == null) return null;
		NBTTagCompound compound = new NBTTagCompound();
		for (Var<?> var : capability.getVarsByMode(UpdateMode.CONSTANTLY)) {
			VarSyncable<?> sync = (VarSyncable<?>) var;
			if (sync.dirty()) {
				sync.writeToNBT(compound);
			}
		}
		if (compound.hasNoTags()) return null;
		return new SPacketUpdateTileEntity(entity.getPos(), entity.getBlockMetadata(), compound);
	}

}
