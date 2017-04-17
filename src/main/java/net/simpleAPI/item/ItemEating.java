package net.simpleAPI.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * @author ci010
 */
public abstract class ItemEating implements ItemUsing
{
	private boolean alwaysEdible;

	@Override
	public ActionResult<ItemStack> onStartToUse(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (playerIn.canEat(this.alwaysEdible))
		{
			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		else return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
	}

	@Override
	public ItemStack onUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			entityplayer.getFoodStats().addStats((ItemFood) stack.getItem(), stack);
			worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
			entityplayer.addStat(StatList.getObjectUseStats(stack.getItem()));
		}
		stack.shrink(1);
		return stack;
	}
}
