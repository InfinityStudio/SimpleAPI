package net.simpleAPI.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

/**
 * The rule of an {@link InventoryElement} should fallow.
 * <p>The {@link InventoryElement} could be a {@link InventorySlot} or multiple slots, {@link InventorySpace},
 * owned by {@link Inventory}.</p>
 *
 * @author ci010
 * @see Inventory
 */
public interface InventoryRule
{
	/**
	 * @param player the player.
	 * @return if a player should use this inventory.
	 */
	boolean isUsebleByPlayer(EntityPlayer player);

	/**
	 * @param stack the new item stack.
	 * @return if a item stack could place on this inventory.
	 */
	boolean isItemValid(ItemStack stack);

	/**
	 * @return The maximum size of item stack could be.
	 */
	int getInventoryStackLimit();

	InventoryRule COMMON = new InventoryRule()
	{
		@Override
		public boolean isUsebleByPlayer(EntityPlayer player)
		{
			return true;
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return true;
		}

		@Override
		public int getInventoryStackLimit()
		{
			return 64;
		}
	};

	class SidedInvWrapIItemHandler extends SidedInvWrapper
	{
		private InventoryElement[] elements;

		public SidedInvWrapIItemHandler(Inventory inv, EnumFacing side)
		{
			super(inv.asSideInventory(), side);
			elements = inv.toArray();
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return elements[slot].getRule().getInventoryStackLimit();
		}
	}

	class InvWrapIItemHandler extends InvWrapper
	{
		private InventoryElement[] elements;

		public InvWrapIItemHandler(Inventory inv)
		{
			super(inv.asIInventory());
			elements = inv.toArray();
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return elements[slot].getRule().getInventoryStackLimit();
		}
	}
}
