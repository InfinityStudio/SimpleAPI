package net.simpleAPI.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * @author ci010
 */
public interface ItemEntityInteract
{
	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
	 * the damage on the stack.
	 */
	boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * Returns true if the item can be used on the given entity, e.g. shears on sheep.
	 */
	boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand);

	/**
	 * Called when the player Left Clicks (attacks) an entity.
	 * Processed before damage is done, if return value is true further processing is canceled
	 * and the entity is not attacked.
	 *
	 * @param stack  The Item being used
	 * @param player The player that is attacking
	 * @param entity The entity being attacked
	 * @return True to cancel the rest of the interaction.
	 */
	boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity);
}
