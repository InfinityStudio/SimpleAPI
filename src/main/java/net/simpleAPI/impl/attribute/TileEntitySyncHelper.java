package net.simpleAPI.impl.attribute;

import net.simpleAPI.attributes.AttributeView;
import net.simpleAPI.attributes.UpdateMode;
import net.simpleAPI.attributes.VarSync;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Collection;

/**
 * This is a delegate class for {@link TileEntity} to satisfy {@link UpdateMode#CONSTANTLY}.
 *
 * @author ci010
 */
public class TileEntitySyncHelper implements VarSync.Listener<Object>
{
	private WeakReference<TileEntity> tileEntity;
	private NBTTagCompound queue;

	public static TileEntitySyncHelper of(TileEntity tileEntity)
	{
		AttributeView capability = tileEntity.getCapability(AttributeView.CAPABILITY, null);
		if (capability == null) return null;
		TileEntitySyncHelper helper = new TileEntitySyncHelper(tileEntity);
		Collection<VarSync<?>> varsBy = capability.getVarsByMode(UpdateMode.CONSTANTLY);
		for (VarSync<?> varSync : varsBy)
			varSync.addListener((VarSync.Listener) helper);
		return helper;
	}

	private TileEntitySyncHelper(TileEntity entity)
	{
		this.tileEntity = new WeakReference<TileEntity>(entity);
		this.queue = new NBTTagCompound();
	}

	/**
	 * Delegate to {@link TileEntity#onDataPacket(NetworkManager, SPacketUpdateTileEntity)}.
	 */
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		if (tileEntity.get() == null)
		{
//			DebugLogger.fatal("Try to update a non-exist tileEntity!");
			return;
		}
		AttributeView capability = tileEntity.get().getCapability(AttributeView.CAPABILITY, null);
		NBTTagCompound tag = pkt.getNbtCompound();
		for (String s : tag.getKeySet())
		{
			VarSync byName = capability.getByName(s);
			if (byName == null)
			{
//				DebugLogger.fatal("Cannot handle TileEntity data packet for name {}. This should not happen!", s);
				return;
			}
			byName.readFromNBT(tag);
		}
	}


	/**
	 * Delegate to {@link TileEntity#getUpdatePacket()}.
	 */
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		TileEntity tileEntity = this.tileEntity.get();
		if (tileEntity == null) return null;
		if (queue.hasNoTags()) return null;
		NBTTagCompound tag;
		synchronized (this)
		{
			tag = queue.copy();
			queue = new NBTTagCompound();
		}
		return new SPacketUpdateTileEntity(tileEntity.getPos(), tileEntity.getBlockMetadata(), tag);
	}

	@Override
	public void onChange(VarSync<Object> value)
	{
		value.writeToNBT(queue);
	}
}
