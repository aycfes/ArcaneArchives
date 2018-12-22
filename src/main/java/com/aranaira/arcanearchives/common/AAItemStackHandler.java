package com.aranaira.arcanearchives.common;

import javax.annotation.Nonnull;

import com.aranaira.arcanearchives.util.handlers.ConfigHandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class AAItemStackHandler extends ItemStackHandler 
{
	
	public AAItemStackHandler(int i) 
	{
		super(i);
	}
	
	//TODO Add in config for slot limit multiplier.
	@Override
	public int getSlotLimit(int slot) {
		return 64 * ConfigHandler.ConfigValues.iRadiantChestMultiplier;
	}
	
	@Override
	protected int getStackLimit(int slot, ItemStack stack) 
	{
		if (stack.getMaxStackSize() == 1)
		{
			return 1;
		}
		//return Math.min(getSlotLimit(slot), stack.getMaxStackSize() * 4);
		return getSlotLimit(slot);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		// TODO Auto-generated method stub
		return super.extractItem(slot, amount, simulate);
	}
}