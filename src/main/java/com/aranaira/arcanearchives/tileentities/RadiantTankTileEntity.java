package com.aranaira.arcanearchives.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class RadiantTankTileEntity extends ImmanenceTileEntity
{
	public static final int BASE_CAPACITY = Fluid.BUCKET_VOLUME * 16;
	public static int MAX_UPGRADES = 10;
	private final FluidTank inventory = new FluidTank(BASE_CAPACITY);
	private int upgrades = 0;

	public boolean wasCreativeDrop = false;

	public RadiantTankTileEntity() {
		super("radianttank");
	}

	public void update() {
		if(world.isRemote) return;

		defaultServerSideUpdate();
	}

	public int getCapacity () {
		return BASE_CAPACITY * (upgrades + 1);
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("upgrades", upgrades);
		compound.setTag(Tags.HANDLER_ITEM, this.inventory.writeToNBT(new NBTTagCompound()));

		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.upgrades = compound.getInteger("upgrades");
		validateCapacity();
		this.inventory.readFromNBT(compound.getCompoundTag(Tags.HANDLER_ITEM));
		validateCapacity();
	}

	public int getUpgrades() {
		return upgrades;
	}

	private void validateCapacity () {
		if (inventory.getCapacity() != getCapacity()) {
			inventory.setCapacity(getCapacity());
		}
	}

	public NBTTagCompound serializeStack (NBTTagCompound tag) {
		if (inventory.getFluid() != null) {
			tag.setTag(FluidHandlerItemStack.FLUID_NBT_KEY, inventory.writeToNBT(new NBTTagCompound()));
		}
		tag.setInteger("upgrades", upgrades);
		return tag;
	}

	public FluidTank getInventory() {
		return inventory;
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public void deserializeStack (NBTTagCompound tag) {
		this.upgrades = tag.getInteger("upgrades");
		this.inventory.readFromNBT(tag.getCompoundTag(FluidHandlerItemStack.FLUID_NBT_KEY));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	public void onRightClickUpgrade (EntityPlayer player, ItemStack upgrade) {
		assert !player.world.isRemote;
		if (upgrades + 1 <= MAX_UPGRADES) {
			upgrades += 1;
			upgrade.shrink(1);
			validateCapacity();
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.success.upgraded_tank", upgrades, MAX_UPGRADES), true);
			defaultServerSideUpdate();
		} else {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.error.upgrade_tank_failed", upgrades, MAX_UPGRADES), true);
		}
	}

	public static class Tags {
		public static final String HANDLER_ITEM = "handler_item";

		private Tags() {
		}
	}
}
