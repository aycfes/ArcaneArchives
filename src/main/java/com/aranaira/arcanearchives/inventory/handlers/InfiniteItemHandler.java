package com.aranaira.arcanearchives.inventory.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class InfiniteItemHandler implements IItemHandlerModifiable
{
	private ItemStack currentStack;

	public InfiniteItemHandler(ItemStack currentStack)
	{
		this.currentStack = currentStack;
	}

	public void setCurrentStack(ItemStack currentStack)
	{
		if(currentStack == null) currentStack = ItemStack.EMPTY;

		this.currentStack = currentStack;
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack)
	{
	}

	@Override
	public int getSlots()
	{
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return currentStack.copy();
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
	{
		return stack;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return currentStack.copy();
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return currentStack.getMaxStackSize();
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack)
	{
		return false;
	}

	public void deserializeNBT(NBTTagCompound nbt)
	{
		currentStack = new ItemStack(nbt);
		if(nbt.hasKey("quantity_override")) currentStack.setCount(nbt.getInteger("quantity_override"));
	}

	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		currentStack.writeToNBT(compound);
		compound.setInteger("quantity_override", currentStack.getCount());
		return compound;
	}
}
