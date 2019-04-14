package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.data.ClientTank;
import com.aranaira.arcanearchives.data.ServerTank;
import com.aranaira.arcanearchives.data.TankHelper;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.util.ItemComparison;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.UUID;

public class RadiantTankTileEntity extends ImmanenceTileEntity
{
	private ClientTank clientTank;
	private ServerTank serverTank;

	public RadiantTankTileEntity()
	{
		super("radianttrove");

		refreshTanks();
	}

	public void refreshTanks () {
		if(world != null)
		{
			if (world.isRemote && clientTank == null && this.tileID != null) {
				clientTank = TankHelper.getClientTank(this.tileID);
			} else if (!world.isRemote && serverTank == null && this.tileID != null) {
				serverTank = TankHelper.getServerTank(this.tileID, this.world);
			}
		}
	}

	public FluidTank getTank () {
		refreshTanks();
		if (world.isRemote) {
			return clientTank.getTank();
		} else {
			return serverTank.getTank();
		}
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
	{
		return super.hasCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public static class Tags
	{
		public static final String FLUID_ID = "fluid_id";

		private Tags()
		{
		}
	}
}
