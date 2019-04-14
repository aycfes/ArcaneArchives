package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.inventory.handlers.ReadOnlyFluidTank;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

import java.util.UUID;

import static com.aranaira.arcanearchives.data.ServerTank.Tags;

public class ClientTank implements ITank
{
	private ReadOnlyFluidTank tank = new ReadOnlyFluidTank(ServerTank.BASE_CAPACITY);
	private int upgrades = 0;
	private UUID tankId;

	public ClientTank(NBTTagCompound tag)
	{
		this.update(tag);
	}

	public void update (NBTTagCompound tag) {
		tank.readFromNBT(tag);
		this.upgrades = tag.getInteger(Tags.UPGRADES);
		this.tankId = NBTUtil.getUUIDFromTag(tag.getCompoundTag(Tags.TANK_ID));
	}

	@Override
	public ReadOnlyFluidTank getTank()
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
	public int getCapacity()
	{
		return 0;
	}
}
