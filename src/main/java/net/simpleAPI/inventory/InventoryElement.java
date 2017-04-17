package net.simpleAPI.inventory;

import net.minecraft.item.ItemStack;

/**
 * @author ci010
 */
public interface InventoryElement extends Iterable<ItemStack>
{
	int id();

	InventoryRule getRule();

	Inventory parent();

	String name();
}
