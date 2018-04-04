package net.simpleAPI.impl.document;


import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.simpleAPI.attributes.AttributeView;
import net.simpleAPI.attributes.UpdateMode;
import net.simpleAPI.attributes.Var;
import net.simpleAPI.attributes.VarSync;

/**
 * @author ci010
 */
public class ContainerAdapter extends Container
{
	public ContainerAdapter(EntityPlayer player, ICapabilityProvider provider, boolean displayPlayerInventory)
	{
		AttributeView capability = provider.getCapability(AttributeView.CAPABILITY, null);
		ImmutableList<Var<?>> varsByMode = capability.getVarsByMode(UpdateMode.LAZY);
		VarSync.Listener listener = value ->
		{
			for (IContainerListener l : listeners)
				if (l instanceof EntityPlayer)
				{
					EntityPlayer p = (EntityPlayer) l;
					NBTTagCompound tagCompound = new NBTTagCompound();
//					value.writeToNBT(tagCompound);
					//network.sendPacket(player, new UpdatePacket(windowId, tagCompound));
				}
		};
		for (Var<?> sync : varsByMode)
			sync.addListener(listener);//listen server side
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return false;
	}
}
