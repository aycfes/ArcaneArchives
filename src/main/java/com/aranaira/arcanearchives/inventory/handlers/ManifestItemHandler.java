package com.aranaira.arcanearchives.inventory.handlers;

import com.aranaira.arcanearchives.util.types.ManifestEntry;
import com.aranaira.arcanearchives.util.types.ManifestList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ManifestItemHandler implements IItemHandlerModifiable {
	private ManifestList manifestBase;
	private ManifestList manifestActive = null;
	private int mNumSlots;

	public ManifestItemHandler (ManifestList manifest) {
		this.manifestBase = manifest;
		this.mNumSlots = 81;
	}

	private void updateManifet () {
		if (manifestActive == null) {
			manifestActive = manifestBase.filtered();
		}
	}

	@Override
	public int getSlots() {
		return mNumSlots;
	}

	public void setSlots(int numSlots) {
		this.mNumSlots = numSlots;
	}

	@Override
	public ItemStack getStackInSlot (int slot) {
		updateManifet();
		return manifestActive.getItemStackForSlot(slot);
	}

	@Override
	public ItemStack insertItem (int slot, ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItem (int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit (int slot) {
		return 0;
	}

	@Nullable
	public ManifestEntry getManifestEntryInSlot (int slot) {
		updateManifet();
		return manifestActive.getEntryForSlot(slot);
	}

	@Override
	public void setStackInSlot (int slot, ItemStack stack) {
	}

	public String getSearchText () {
		return manifestBase.getSearchText();
	}

	public ItemStack getSearchItem () {
		return manifestBase.getSearchItem();
	}

	public void setSearchText (String s) {
		manifestBase.setSearchText(s);
		manifestActive = manifestBase.filtered();
	}

	public void setSearchItem (ItemStack s) {
		manifestBase.setSearchItem(s);
		manifestActive = manifestBase.filtered();
	}

	public void clear () {
		manifestBase.setSearchText(null);
		manifestActive = manifestBase.filtered();
	}

	public void nullify () {
		manifestActive = null;
	}
}
