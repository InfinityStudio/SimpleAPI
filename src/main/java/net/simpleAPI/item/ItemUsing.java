package net.simpleAPI.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * @author ci010
 */
public interface ItemUsing
{
	int getMaxItemUseDuration(ItemStack stack);

	ActionResult<ItemStack> onStartToUse(World worldIn, EntityPlayer playerIn, EnumHand handIn);

	/**
	 * Called each tick while using an item.
	 *
	 * @param stack  The Item being used
	 * @param player The Player using the item
	 * @param count  The amount of time in tick the item has been used for continuously
	 */
	void onUsingTick(ItemStack stack, EntityLivingBase player, int count);

	ItemStack onUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving);

	void onStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft);
}
