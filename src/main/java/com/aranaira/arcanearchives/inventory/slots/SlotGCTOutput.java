package com.aranaira.arcanearchives.inventory.slots;

import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.inventory.handlers.InfiniteItemHandler;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class SlotGCTOutput extends SlotItemHandler
{
	private ContainerGemCuttersTable container;
	private GemCuttersTableRecipe curRecipe;
	private InfiniteItemHandler handler;

	public SlotGCTOutput(ContainerGemCuttersTable container, InfiniteItemHandler handler, int xPosition, int yPosition)
	{
		super(handler, 0, xPosition, yPosition);
		this.container = container;
		this.handler = handler;
	}

	public void setCurRecipe(GemCuttersTableRecipe curRecipe)
	{
		this.curRecipe = curRecipe;
		if (curRecipe != null)
			this.handler.setCurrentStack(curRecipe.getOutput());
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return container.RECIPE_STATUS.getOrDefault(curRecipe, false);
	}

	@Nonnull
	@Override
	public ItemStack decrStackSize(int amount)
	{
		if (!canTakeStack(null)) return ItemStack.EMPTY;

		return super.decrStackSize(amount);
		// this should do something with amount crafted
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return false;
	}

	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();

		container.getTile().markDirty();
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
	{
		// GemCuttersTableRecipe recipe = getCurRecipe(); //container.getTile().getRecipe();
		if(curRecipe == null) return ItemStack.EMPTY;

		GemCuttersTableTileEntity tile = container.getTile();
		ItemStackHandler tileInv = tile.getInventory();
		InvWrapper ply = new InvWrapper(container.playerInventory);

		//boolean matches = false;
		//if(curRecipe.matchesRecipe(tileInv, ply)) matches = true;

		/*if(thePlayer.world.isRemote)
		{
			if (!matches)
			{
				stack = ItemStack.EMPTY;
			}
		} else
		{
			if(!matches)
			{
				stack = ItemStack.EMPTY;
			} else if(!curRecipe.consume(tileInv, ply))
			{
				stack = ItemStack.EMPTY;
			}
		}*/
		if (!thePlayer.world.isRemote) curRecipe.consume(tileInv, ply);


		tile.updateOutput();

		onSlotChanged();

		return stack;
	}
}
