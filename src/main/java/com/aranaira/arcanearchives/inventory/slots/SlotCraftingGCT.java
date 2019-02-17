package com.aranaira.arcanearchives.inventory.slots;

import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class SlotCraftingGCT extends Slot
{
	private EntityPlayer player;
	private int amountCrafted;
	private GemCuttersTableTileEntity tile;
	private ContainerGemCuttersTable container;

	public SlotCraftingGCT(GemCuttersTableTileEntity te, ContainerGemCuttersTable container, EntityPlayer par1EntityPlayer, int par5, int par6)
	{
		super(SlotIRecipe.emptyInventory, 0, par5, par6);
		this.player = par1EntityPlayer;
		this.tile = te;
		this.container = container;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount)
	{
		if(this.getHasStack())
		{
			this.amountCrafted += Math.min(amount, this.getStack().getCount());
		}

		return super.decrStackSize(amount);
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount)
	{
		this.amountCrafted += amount;
		this.onCrafting(stack);
	}

	@Override
	public ItemStack getStack()
	{
		return tile.getOutput();
	}

	@Override
	protected void onSwapCraft(int amount)
	{
		this.amountCrafted += amount;
	}

	@Override
	protected void onCrafting(ItemStack stack)
	{
		GemCuttersTableRecipe recipe = tile.getRecipe();
		if(recipe == null) return;

		if(this.amountCrafted > 0)
		{
			stack.onCrafting(this.player.world, this.player, this.amountCrafted);
		}

		this.amountCrafted = 0;
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
	{
		GemCuttersTableRecipe recipe = tile.getRecipe();
		if(recipe == null) return ItemStack.EMPTY;

		if (!container.RECIPE_STATUS.getOrDefault(recipe, false)) return ItemStack.EMPTY;

		if(thePlayer.world.isRemote || !recipe.consume(tile.getInventory(), new InvWrapper(thePlayer.inventory)))
		{
			return ItemStack.EMPTY;
		}

		this.onCrafting(stack);

		return stack;
	}
}
