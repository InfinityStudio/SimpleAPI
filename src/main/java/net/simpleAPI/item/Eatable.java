package net.simpleAPI.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author ci010
 */
public interface Eatable
{
	int getHealAmount();

	List<PotionEffect> createEffect(ItemStack stack, World worldIn, EntityPlayer player);
}
