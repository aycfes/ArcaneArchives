package com.aranaira.arcanearchives.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TankSavedData extends WorldSavedData
{
	public static final String id = "Arcane-Archives-Tanks";

	public Map<UUID, ServerTank> tankData = new HashMap<>();

	public TankSavedData(String name)
	{
		super(name);
	}

	public TankSavedData()
	{
		super(id);
	}

	public void clearServerMap () {
		tankData.clear();
	}

	@Nonnull
	public ServerTank getTank (@Nonnull UUID tankId) {
		if (!tankData.containsKey(tankId)) {
			tankData.put(tankId, new ServerTank(tankId));
		}

		return tankData.get(tankId);
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		tankData.clear();

		NBTTagList list = nbt.getTagList("tankData", 10);
		for (int i = 0; i < list.tagCount(); i++) {
			ServerTank tank = new ServerTank(list.getCompoundTagAt(i));
			tankData.put(tank.getTankId(), tank);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagList list = new NBTTagList();

		for (ServerTank i : tankData.values()) {
			list.appendTag(i.serializeNBT());
		}

		compound.setTag("tankData", list);
		return compound;
	}
}
