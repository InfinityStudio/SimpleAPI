package net.simpleAPI.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * @author ci010
 */
public interface ItemEntityHandler
{
	/**
	 * Retrieves the normal 'lifespan' of this item when it is dropped on the ground as a EntityItem.
	 * This is in ticks, standard result is 6000, or 5 mins.
	 *
	 * @param itemStack The current ItemStack
	 * @param world     The world the entity is in
	 * @return The normal lifespan in ticks.
	 */
	int getEntityLifespan(ItemStack itemStack, World world);

	/**
	 * Determines if this Item has a special entity for when they are in the world.
	 * Is called when a EntityItem is spawned in the world, if true and Item#createCustomEntity
	 * returns non null, the EntityItem will be destroyed and the new Entity will be added to the world.
	 *
	 * @param stack The current item stack
	 * @return True of the item has a custom entity, If true, Item#createCustomEntity will be called
	 */
	boolean hasCustomEntity(ItemStack stack);

	/**
	 * This function should return a new entity to replace the dropped item.
	 * Returning null here will not kill the EntityItem and will leave it to function normally.
	 * Called when the item it placed in a world.
	 *
	 * @param world     The world object
	 * @param location  The EntityItem object, useful for getting the position of the entity
	 * @param itemstack The current item stack
	 * @return A new Entity object to spawn or null
	 */
	@Nullable
	Entity createEntity(World world, Entity location, ItemStack itemstack);

	/**
	 * Called by the default implemetation of EntityItem's onUpdate method, allowing for cleaner
	 * control over the update of the item without having to write a subclass.
	 *
	 * @param entityItem The entity Item
	 * @return Return true to skip any further update code.
	 */
	boolean onEntityItemUpdate(net.minecraft.entity.item.EntityItem entityItem);
}
