package net.simpleAPI.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ci010
 */
public interface ItemBlockInteract
{
	/**
	 * This is called when the item is used, before the block is activated.
	 *
	 * @param player The Player that used the item
	 * @param world  The Current World
	 * @param pos    Target position
	 * @param side   The side of the target hit
	 * @param hand   Which hand the item is being held in.
	 * @return Return PASS to allow vanilla handling, any other to skip normal code.
	 */
	EnumActionResult onPreInteract(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY,
								   float hitZ, EnumHand hand);

	EnumActionResult onInteract(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing,
								float hitX, float hitY, float hitZ);
}
