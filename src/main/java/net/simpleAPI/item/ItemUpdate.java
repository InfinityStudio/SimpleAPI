package net.simpleAPI.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author ci010
 */
public interface ItemUpdate
{
	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
	 * update it's contents.
	 */
	void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected);
}
