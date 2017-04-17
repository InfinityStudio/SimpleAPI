package net.simpleAPI.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ItemCreate
{
	/**
	 * Called when item is crafted/smelted. Used only by maps so far.
	 */
	void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn);
}
