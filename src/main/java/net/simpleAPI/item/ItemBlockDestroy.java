package net.simpleAPI.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ci010
 */
public interface ItemBlockDestroy
{
	/**
	 * Check whether this Item can harvest the given Block
	 */
	boolean canHarvestBlock(IBlockState blockIn);

	/**
	 * Called before a block is broken.  Return true to prevent default block harvesting.
	 * <p>
	 * Note: In SMP, this is called on both client and server sides!
	 *
	 * @param itemstack The current ItemStack
	 * @param pos       Block's position in world
	 * @param player    The Player that is wielding the item
	 * @return True to prevent harvesting, false to continue as normal
	 */
	boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player);

	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
	 */
	boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving);
}
