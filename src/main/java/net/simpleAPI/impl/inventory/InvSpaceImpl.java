package net.simpleAPI.impl.inventory;

import net.simpleAPI.inventory.Inventory;
import net.simpleAPI.inventory.InventoryRule;
import net.simpleAPI.inventory.InventorySpace;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

/**
 * @author ci010
 */
public class InvSpaceImpl implements InventorySpace
{
	private int size, offset;
	private InventoryRule rule = InventoryRule.COMMON;
	private Inventory parent;
	private String name;

	public InvSpaceImpl(String name, Inventory delegate, int id, int size, InventoryRule rule)
	{
		this.name = name;
		this.size = size;
		this.offset = id;
		this.parent = delegate;
		this.rule = rule == null ? InventoryRule.COMMON : rule;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (slot < size)
			return parent.asItemHandler().insertItem(slot + offset, stack, simulate);
		return null;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (slot < size)
			return parent.asItemHandler().extractItem(slot + offset, amount, simulate);
		return null;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return rule.getInventoryStackLimit();
	}

	@Override
	public Iterator<ItemStack> iterator()
	{
		return new Iterator<ItemStack>()
		{
			int current = 0;

			@Override
			public boolean hasNext()
			{
				return current < parent.asItemHandler().getSlots();
			}

			@Override
			public ItemStack next()
			{
				return parent.asItemHandler().getStackInSlot(current++);
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int id()
	{
		return offset;
	}

	@Override
	public InventoryRule getRule()
	{
		return rule;
	}

	@Override
	public Inventory parent()
	{
		return parent;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public int getSlots()
	{
		return size;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return parent.asItemHandler().getStackInSlot(slot + offset);
	}
}
