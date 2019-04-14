package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TankHelper
{
	public static UUID INVALID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	// TODO: This needs to be cleared whenever the player enters a new world
	private static Map<UUID, ClientNetwork> CLIENT_MAP = new HashMap<>();

	public static void clearClientCache()
	{
		CLIENT_MAP.clear();
	}

	@Nonnull
	public static ServerTank getServerTank (@Nonnull RadiantTankTileEntity te)
	{
		assert !te.tileID.equals(INVALID);
		assert te.getWorld().getMapStorage() != null;

		World world = te.getWorld();

		TankSavedData savedData = (TankSavedData) world.getMapStorage().getOrLoadData(TankSavedData.class, TankSavedData.id);

		if (savedData == null) {
			savedData = new TankSavedData();
			world.getMapStorage().setData(TankSavedData.id, savedData);
		}

		return savedData.getTank(te.tileID);
	}

	@Nonnull
	public static ClientTank getClientTank (@Nonnull UUID uuid) {
		assert !uuid.equals(INVALID);
	}
}
