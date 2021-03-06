package net.simpleAPI.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author ci010
 */
public abstract class BlockLit extends BlockBaseContainer
{
	public static final PropertyBool IS_LIT = PropertyBool.create("light");

	protected BlockLit(Material materialIn)
	{
		super(materialIn, new IProperty[]{IS_LIT, BlockHorizontal.FACING});
		this.setDefaultState(this.blockState.getBaseState().withProperty(IS_LIT, false)
				.withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));
	}

	@Override
	@Nonnull
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(this);
	}

	@Override
	@Nonnull
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockHorizontal.FACING, IS_LIT)
		{
			@Override
			@Nonnull
			protected StateImplementation createState(@Nonnull Block block, ImmutableMap<IProperty<?>, Comparable<?>>
					properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
			{
				return new State(block, properties);
			}
		};
	}

	private class State extends BlockStateContainer.StateImplementation
	{
		State(Block blockIn, ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn)
		{
			super(blockIn, propertiesIn);
		}

		@Override
		@Nonnull
		public IBlockState withMirror(@Nonnull Mirror mirrorIn)
		{
			return this.withRotation(mirrorIn.toRotation(this.getValue(BlockHorizontal.FACING)));
		}

		@Override
		@Nonnull
		public IBlockState withRotation(@Nonnull Rotation rot)
		{
			return this.withProperty(BlockHorizontal.FACING, rot.rotate(this.getValue(BlockHorizontal.FACING)));
		}

		@Override
		public boolean hasComparatorInputOverride()
		{
			return true;
		}

		@Override
		public EnumBlockRenderType getRenderType()
		{
			return EnumBlockRenderType.MODEL;
		}

		@Override
		public int getComparatorInputOverride(@Nonnull World worldIn, @Nonnull BlockPos pos)
		{
			return Container.calcRedstone(worldIn.getTileEntity(pos));
		}

		@Override
		public int getLightValue()
		{
			if (this.getValue(IS_LIT)) return (int) (0.875F * 15F);
			return 0;
		}
	}
}
