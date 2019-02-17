package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.inventory.slots.SlotCraftingGCT;
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
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import thaumcraft.common.container.slot.SlotOutput;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ContainerGemCuttersTable extends Container
{
	public IInventory playerInventory;
	public Map<GemCuttersTableRecipe, Boolean> RECIPE_STATUS = new HashMap<>();
	private GemCuttersTableTileEntity tile;
	private boolean isServer;
	private GemCuttersTableTileEntity.GemCuttersTableItemHandler tileInventory;
	// private SlotCraftingGCT outputSlot;
	private SlotGCTOutput outputSlot;
	private Runnable updateRecipeGUI;
	private EntityPlayer player;

	public ContainerGemCuttersTable(GemCuttersTableTileEntity tile, IInventory playerInventory, EntityPlayer player, boolean serverSide)
	{
		this.tile = tile;
		this.isServer = serverSide;
		this.playerInventory = playerInventory;
		this.tileInventory = tile.getInventory();
		this.player = player;

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

		// outputSlot = new SlotCraftingGCT(tile, this, player, 95, 18);

		outputSlot = new SlotGCTOutput(this, 95, 18);

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
		this.tileInventory.addHook(updateRecipeGUI);
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			//Chest inventory
			if(index < 36)
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
		if(!player.world.isRemote)
		{
			if(slotId <= 43 && slotId >= 37)
			{
				return ItemStack.EMPTY;
			}
		}

		if(!player.world.isRemote && slotId == 36)
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
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public GemCuttersTableTileEntity getTile()
	{
		return tile;
	}

	public Map<GemCuttersTableRecipe, Boolean> updateRecipeStatus()
	{
		Map<GemCuttersTableRecipe, Boolean> map = new HashMap<>();

		for(GemCuttersTableRecipe recipe : GemCuttersTableRecipeList.getRecipeList())
		{
			map.put(recipe, recipe.matchesRecipe(tileInventory, new InvWrapper(playerInventory)));
		}

		RECIPE_STATUS = map;

		return map;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		this.tileInventory.deleteHook(this.updateRecipeGUI);
	}

}
