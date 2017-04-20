package net.simpleAPI.impl.item;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.simpleAPI.item.*;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * @author ci010
 */
public class ItemToolAdapter extends ItemTool implements ItemAdapter
{
	public ItemToolAdapter(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block>
			effectiveBlocksIn)
	{
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
	}

	public ItemToolAdapter(ToolMaterial materialIn, Set<Block> effectiveBlocksIn)
	{
		super(materialIn, effectiveBlocksIn);
	}

	private ItemUpdate update;
	private ItemUsing using;
	private ItemEntityInteract interact;
	private ItemBlockInteract blockInteract;
	private ItemEntityHandler handler;
	private ItemCreate create;
	private ItemBlockDestroy destroy;

	public ItemUpdate getUpdate() {return update;}

	public ItemUsing getUsing() {return using;}

	public ItemEntityInteract getInteract() {return interact;}

	public ItemBlockInteract getBlockInteract() {return blockInteract;}

	public ItemEntityHandler getHandler() {return handler;}

	public ItemCreate getCreate() {return create;}

	public ItemBlockDestroy getDestroy() {return destroy;}

	private Function<ICapabilityProvider, Map<ResourceLocation, ICapabilityProvider>> capabilityFactory;

	public Function<ICapabilityProvider, Map<ResourceLocation, ICapabilityProvider>> getCapabilityFactory() {return capabilityFactory;}

	public ItemToolAdapter setCapabilityFactory(Function<ICapabilityProvider, Map<ResourceLocation, ICapabilityProvider>> capabilityFactory)
	{
		Preconditions.checkNotNull(capabilityFactory);
		this.capabilityFactory = capabilityFactory;
		return this;
	}

	@SubscribeEvent
	public void bindCapability(AttachCapabilitiesEvent<Item> event)
	{
		if (event.getObject() != this) return;
		Map<ResourceLocation, ICapabilityProvider> caps = getCapabilityFactory().apply(((AttachCapabilitiesEvent.Item) event).getItemStack());
		if (caps == null) return;
		for (Map.Entry<ResourceLocation, ICapabilityProvider> entry : caps.entrySet())
			event.addCapability(entry.getKey(), entry.getValue());
	}

	public ItemToolAdapter setUpdate(ItemUpdate update)
	{
		this.update = update;
		return this;
	}

	public ItemToolAdapter setUsing(ItemUsing using)
	{
		this.using = using;
		return this;
	}

	public ItemToolAdapter setInteract(ItemEntityInteract interact)
	{
		this.interact = interact;
		return this;
	}

	public ItemToolAdapter setBlockInteract(ItemBlockInteract blockInteract)
	{
		this.blockInteract = blockInteract;
		return this;
	}

	public ItemToolAdapter setHandler(ItemEntityHandler handler)
	{
		this.handler = handler;
		return this;
	}

	public ItemToolAdapter setCreate(ItemCreate create)
	{
		this.create = create;
		return this;
	}

	public ItemToolAdapter setDestroy(ItemBlockDestroy destroy)
	{
		this.destroy = destroy;
		return this;
	}

	@Override
	public ItemAdapter setSubTypes(String[] subTypes)
	{
		return null;
	}

	@Override
	public String[] getSubTypes()
	{
		return new String[0];
	}

	@Override
	public int getMetadata(int damage) {return this.hasSubtypes ? damage : 0;}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		System.out.println("onItemUse");
		if (using != null)
			return blockInteract.onInteract(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		if (using != null)
			return using.getMaxItemUseDuration(stack);
		return super.getMaxItemUseDuration(stack);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (using != null) return using.onStartToUse(worldIn, playerIn, handIn);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		System.out.println("onItemUseFinish");
		if (using != null)
			return using.onUseFinish(stack, worldIn, entityLiving);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player)
	{
		return super.onDroppedByPlayer(item, player);
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		super.onArmorTick(world, player, itemStack);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
	{
		return super.onEntitySwing(entityLiving, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (update != null) update.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		System.out.println("stop using");
		if (using != null)
			using.onStoppedUsing(stack, worldIn, entityLiving, timeLeft);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		System.out.println("onItemUseFirst");
		if (using != null)
			return blockInteract.onPreInteract(player, world, pos, side, hitX, hitY, hitZ, hand);
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		System.out.println("tick");
		if (using != null)
			using.onUsingTick(stack, player, count);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		System.out.println("hitEntity");
		if (interact != null) return interact.hitEntity(stack, target, attacker);
		return super.hitEntity(stack, target, attacker);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
	{
		System.out.println("itemInteractionForEntity");
		if (interact != null) return interact.itemInteractionForEntity(stack, playerIn, target, hand);
		return super.itemInteractionForEntity(stack, playerIn, target, hand);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		System.out.println("onLeftClickEntity");
		if (interact != null) return interact.onLeftClickEntity(stack, player, entity);
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		System.out.println("onBlockDestroyed");
		if (destroy != null)
			return destroy.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		System.out.println("canHarvestBlock");
		if (destroy != null)
			return destroy.canHarvestBlock(blockIn);
		return super.canHarvestBlock(blockIn);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		System.out.println("onBlockStartBreak");
		if (destroy != null)
			return destroy.onBlockStartBreak(itemstack, pos, player);
		return super.onBlockStartBreak(itemstack, pos, player);
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world)
	{
		if (handler != null)
			return handler.getEntityLifespan(itemStack, world);
		return super.getEntityLifespan(itemStack, world);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		if (handler != null)
			return handler.hasCustomEntity(stack);
		return super.hasCustomEntity(stack);
	}

	@Override
	@Nullable
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		if (handler != null)
			return handler.createEntity(world, location, itemstack);
		return super.createEntity(world, location, itemstack);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		if (handler != null)
			return handler.onEntityItemUpdate(entityItem);
		return super.onEntityItemUpdate(entityItem);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		System.out.println("onCreated");
		if (create != null)
			create.onCreated(stack, worldIn, playerIn);
	}
}
