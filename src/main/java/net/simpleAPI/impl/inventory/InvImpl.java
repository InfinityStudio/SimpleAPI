package net.simpleAPI.impl.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import javafx.beans.InvalidationListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.simpleAPI.inventory.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author ci010
 */
public class InvImpl implements Inventory, ISidedInventory, IItemHandlerModifiable
{
	private ItemStack[] stacks;
	private int size;

	private EnumMap<EnumFacing, int[]> sideMap;
	private EnumMap<EnumFacing, IItemHandlerModifiable> handlerModifiableEnumMap;

	private InventoryElement[] elements;
	private ImmutableMap<String, InventoryElement> elementMap;

	private List<InvalidationListener> listeners = Lists.newArrayList();

	private IItemHandler wrap = new InventoryRule.InvWrapIItemHandler(this);

	void build(EnumMap<EnumFacing, int[]> sideMap, int size, ArrayList<InventoryElement> elements)
	{
		this.sideMap = sideMap.clone();
		this.size = size;
		this.elements = new InventoryElement[size];
		ImmutableMap.Builder<String, InventoryElement> builder = ImmutableMap.builder();
		for (InventoryElement element : elements)
		{
			if (element instanceof InventorySlot)
				this.elements[element.id()] = element;
			else if (element instanceof InventorySpace)
				for (int j = 0; j < ((InventorySpace) element).getSlots(); j++)
					this.elements[element.id() + j] = element;
			builder.put(element.name(), element);
		}
		this.stacks = new ItemStack[this.size];
		this.elementMap = builder.build();
		handlerModifiableEnumMap = new EnumMap<EnumFacing, IItemHandlerModifiable>(EnumFacing.class);
		for (EnumFacing face : sideMap.keySet())
			handlerModifiableEnumMap.put(face, new InventoryRule.SidedInvWrapIItemHandler(this, face));
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return sideMap != null ? sideMap.get(side).clone() : new int[0];
	}

	@Override
	public int getSizeInventory()
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

	@Override
	public int getSlots()
	{
		return wrap.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return stacks[index];
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return wrap.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return wrap.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (this.stacks[index] != null)
		{
			ItemStack stack;
			if (this.stacks[index].getCount() <= size)
			{
				stack = this.stacks[index];
				this.stacks[index] = null;
				this.markDirty();
				return stack;
			}
			else
			{
				stack = this.stacks[index].splitStack(size);
				if (this.stacks[index].getCount() == 0)
					this.stacks[index] = null;
				this.markDirty();
				return stack;
			}
		}
		else
			return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (this.stacks[index] != null)
		{
			ItemStack itemstack = this.stacks[index];
			this.stacks[index] = null;
			return itemstack;
		}
		else
			return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stacks[index] = stack;
		if (stack != null && stack.getCount() > this.getInventoryStackLimit())
			stack.setCount(this.getInventoryStackLimit());
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {return 64;}

	@Override
	public void markDirty()
	{
		for (InvalidationListener listener : listeners)
			listener.invalidated(this);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return elements[index].getRule().isItemValid(stack);
	}

	@Override
	public int getField(int id) {return 0;}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {return 0;}

	@Override
	public void clear()
	{
		for (int i = 0; i < this.stacks.length; i++)
			stacks[i] = null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		if (sideMap.containsKey(direction))
			for (int i : sideMap.get(direction))
				if (index == i)
					return this.elements[index].getRule().isItemValid(itemStackIn);
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		if (sideMap.containsKey(direction))
			for (int i : sideMap.get(direction))
				if (index == i)
					return this.elements[index].getRule().isItemValid(stack);
		return false;
	}

	@Override
	public String getName()
	{
		return "Inventory_Impl";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(this.getName());
	}

	@Override
	public void addListener(InvalidationListener listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener)
	{
		listeners.remove(listener);
	}

	@Override
	public InventoryElement getByName(@Nonnull String name)
	{
		return elementMap.get(name);
	}

	@Nonnull
	@Override
	public Collection<InventoryElement> getAll()
	{
		return elementMap.values();
	}

	@Override
	public InventoryElement[] toArray()
	{
		return Arrays.copyOf(elements, elements.length);
	}

	@Override
	public int size()
	{
		return listeners.size();
	}

	@Nonnull
	@Override
	public Collection<String> allPresent()
	{
		return this.elementMap.keySet();
	}

	@Override
	public InventoryElement getById(int id)
	{
		return elements[id];
	}

	@Override
	public IInventory asIInventory()
	{
		return this;
	}

	@Override
	public ISidedInventory asSideInventory()
	{
		return this;
	}

	@Override
	public IItemHandlerModifiable asItemHandler()
	{
		return this;
	}

	@Nullable
	@Override
	public IItemHandlerModifiable getBySide(EnumFacing side)
	{
		return handlerModifiableEnumMap.get(side);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		this.setInventorySlotContents(slot, stack);
	}
}
