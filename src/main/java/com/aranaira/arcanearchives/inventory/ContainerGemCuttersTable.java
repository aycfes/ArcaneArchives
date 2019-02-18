package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.handlers.InfiniteItemHandler;
import com.aranaira.arcanearchives.inventory.slots.SlotGCTOutput;
import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipeList;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ContainerGemCuttersTable extends Container
{
	public Map<GemCuttersTableRecipe, Boolean> RECIPE_STATUS = new HashMap<>();
	public IInventory playerInventory;
	private GemCuttersTableTileEntity tile;
	private boolean isServer;
	private GemCuttersTableTileEntity.GemCuttersTableItemHandler tileInventory;
	private InfiniteItemHandler tileOutput;
	private SlotGCTOutput outputSlot;
	private Runnable updateRecipeGUI;

	public ContainerGemCuttersTable(GemCuttersTableTileEntity tile, IInventory playerInventory, boolean serverSide)
	{
		this.tile = tile;
		this.isServer = serverSide;
		this.playerInventory = playerInventory;
		this.tileInventory = tile.getInventory();
		this.tileOutput = tile.getOutput();

		int i = 35;
		for(int y = 2; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new Slot(playerInventory, i, 23 + (18 * x), 166 + (18 * y)));
				i--;
			}
		}

		for(int x = 8; x > -1; x--)
		{
			this.addSlotToContainer(new Slot(playerInventory, i, 23 + (18 * x), 224));
			i--;
		}

		outputSlot = new SlotGCTOutput(this, tileOutput, 95, 18);

		this.addSlotToContainer(outputSlot);

		//selector - 1 - 8
		{
			int y = 0;

			for(int x = 6; x > -1; x--)
			{
				this.addSlotToContainer(new SlotRecipeHandler(x, x * 18 + 41, y * 18 + 70, tile));
			}
		}

		i = 17;
		for(int y = 1; y > -1; y--)
		{
			for(int x = 8; x > -1; x--)
			{
				this.addSlotToContainer(new SlotItemHandler(tileInventory, i, x * 18 + 23, y * 18 + 105));
				i--;
			}
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	public void setUpdateRecipeGUI(Runnable updateRecipeGUI)
	{
		this.updateRecipeGUI = updateRecipeGUI;
		this.tileInventory.addHook(this::updateRecipeStatus);
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack;
		final Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			//Chest inventory
			if(index == 36)
			{
				slotStack = slot.onTake(playerIn, slotStack);
				if(slotStack.isEmpty()) return ItemStack.EMPTY;
				if(!mergeItemStack(slotStack, 45, 62, true))
				{
					return ItemStack.EMPTY;
				} else
				{
					tile.updateOutput();
				}
			} else if(index < 36)
			{
				if(!mergeItemStack(slotStack, 45, 62, true)) return ItemStack.EMPTY;
			}
			//Players inventory
			else
			{
				if(!mergeItemStack(slotStack, 0, 36, true)) return ItemStack.EMPTY;
			}

			if(slotStack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			} else
			{
				slot.onSlotChanged();
			}
		} else
		{
			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
	{
		boolean temp = super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
		this.getTile().updateOutput();
		return temp;
	}

	@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(slotId <= 43 && slotId >= 37)
		{
			getTile().setRecipe(getSlot(slotId).getStack());
			outputSlot.setCurRecipe(getTile().getRecipe());

			if(player.world.isRemote)
			{
				runUpdate();
			}

			return ItemStack.EMPTY;
		}

		/*if(slotId == 36)
		{
			GemCuttersTableRecipe recipe = getTile().getRecipe();
			if(recipe == null) return ItemStack.EMPTY;

			if(recipe.matchesRecipe(tileInventory, new InvWrapper(playerInventory)))
			{
				return super.slotClick(slotId, dragType, clickTypeIn, player);
			} else
			{
				return ItemStack.EMPTY;
			}
		}*/

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public GemCuttersTableTileEntity getTile()
	{
		return tile;
	}

	public void updateRecipeStatus()
	{
		RECIPE_STATUS.clear();

		for(GemCuttersTableRecipe recipe : GemCuttersTableRecipeList.getRecipeList())
		{
			RECIPE_STATUS.put(recipe, recipe.matchesRecipe(tileInventory, new InvWrapper(playerInventory)));
		}

		outputSlot.setCurRecipe(tile.getRecipe());
		runUpdate();
	}

	public void runUpdate()
	{
		if(updateRecipeGUI != null) updateRecipeGUI.run();
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		this.tileInventory.deleteHook(this::updateRecipeStatus);
	}

}
