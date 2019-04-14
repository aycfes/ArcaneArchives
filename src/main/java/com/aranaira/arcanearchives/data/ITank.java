package com.aranaira.arcanearchives.data;

import net.minecraftforge.fluids.FluidTank;

import java.util.UUID;

public interface ITank
{
	FluidTank getTank();

	int getUpgrades();

	UUID getTankId();

	int getCapacity();
}
