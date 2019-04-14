package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class ReadOnlyFluidTank extends FluidTank
{
	public ReadOnlyFluidTank(int capacity)
	{
		super(capacity);
	}

	public ReadOnlyFluidTank(@Nullable FluidStack fluidStack, int capacity)
	{
		super(fluidStack, capacity);
	}

	public ReadOnlyFluidTank(Fluid fluid, int amount, int capacity)
	{
		super(fluid, amount, capacity);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public int fillInternal(FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Nullable
	@Override
	public FluidStack drainInternal(FluidStack resource, boolean doDrain)
	{
		return null;
	}

	@Nullable
	@Override
	public FluidStack drainInternal(int maxDrain, boolean doDrain)
	{
		return null;
	}

	@Override
	public void setCanFill(boolean canFill)
	{
	}

	@Override
	public void setCanDrain(boolean canDrain)
	{
	}
}
