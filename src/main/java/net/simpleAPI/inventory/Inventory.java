package net.simpleAPI.inventory;

import javafx.beans.Observable;
import net.simpleAPI.Overview;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

/**
 * @author ci010
 */
public interface Inventory extends Observable, Overview<InventoryElement>
{
	@CapabilityInject(Inventory.class)
	Capability<Inventory> CAPABILITY = null;

	@Nullable
	IInventory asIInventory();

	@Nullable
	ISidedInventory asSideInventory();

	@Nullable
	IItemHandlerModifiable asItemHandler();

	@Nullable
	IItemHandlerModifiable getBySide(EnumFacing side);
}
