package net.simpleAPI.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ci010
 */
public class SlotRuled extends Slot
{
	private InventoryRule rule;

	public SlotRuled(IInventory inventoryIn, int index, int xPosition, int yPosition, InventoryRule rule)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.rule = rule;
	}

	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
	}

	@Override
	protected void onCrafting(ItemStack stack)
	{
		super.onCrafting(stack);
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount)
	{
		super.onCrafting(stack, amount);
	}

	@Override
	public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_)
	{
		super.onSlotChange(p_75220_1_, p_75220_2_);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return rule.isUsebleByPlayer(playerIn);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return rule.isItemValid(stack);
	}

	@Override
	public int getSlotStackLimit()
	{
		return rule.getInventoryStackLimit();
	}
}
