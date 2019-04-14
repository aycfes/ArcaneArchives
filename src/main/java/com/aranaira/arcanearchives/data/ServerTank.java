package com.aranaira.arcanearchives.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

import java.util.UUID;

public class ServerTank implements INBTSerializable<NBTTagCompound>, ITank
{
	public static int BASE_CAPACITY = Fluid.BUCKET_VOLUME * 16;
	private FluidTank tank = new FluidTank(BASE_CAPACITY);
	private int upgrades = 0;
	private UUID tankId;

	public ServerTank (UUID id) {
		this.tankId = id;
	}

	public ServerTank (NBTTagCompound tag) {
		this.deserializeNBT(tag);
	}

	@Override
	public FluidTank getTank()
	{
		return tank;
	}

	@Override
	public int getUpgrades()
	{
		return upgrades;
	}

	@Override
	public UUID getTankId()
	{
		return tankId;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound result = new NBTTagCompound();
		result.setInteger(Tags.UPGRADES, upgrades);
		result.setTag(Tags.TANK_ID, NBTUtil.createUUIDTag(this.tankId));
		tank.writeToNBT(result);
		return result;
	}

	@Override
	public int getCapacity() {
		return BASE_CAPACITY * (upgrades + 1);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.upgrades = nbt.getInteger(Tags.UPGRADES);
		this.tankId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(Tags.TANK_ID));
		if (tank.getCapacity() != getCapacity()) {
			tank.setCapacity(getCapacity());
		}
		tank.readFromNBT(nbt);
	}

	public static class Tags {
		public static final String UPGRADES = "upgrades";
		public static final String TANK_ID = "tank_id";
	}
}
