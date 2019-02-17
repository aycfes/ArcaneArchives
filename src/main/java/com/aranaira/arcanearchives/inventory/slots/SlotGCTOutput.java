package com.aranaira.arcanearchives.inventory.slots;

import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class SlotGCTOutput extends SlotItemHandler
{
	private ContainerGemCuttersTable container;
	private GemCuttersTableTileEntity tile;

	public SlotGCTOutput(ContainerGemCuttersTable containerGemCuttersTable, int xPosition, int yPosition)
	{
		super(new ItemStackHandler(0), 0, xPosition, yPosition);
		this.container = containerGemCuttersTable;
		this.tile = containerGemCuttersTable.getTile();
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
	{
		GemCuttersTableRecipe recipe = tile.getRecipe();
		if(recipe == null) return ItemStack.EMPTY;

		if (!container.RECIPE_STATUS.getOrDefault(recipe, false)) return ItemStack.EMPTY;

		ItemStackHandler tileInv = tile.getInventory();
		InvWrapper ply = new InvWrapper(container.playerInventory);

		if(!thePlayer.world.isRemote)
		{
			if(!recipe.consume(tileInv, ply)) return ItemStack.EMPTY;
		}

		if(thePlayer instanceof EntityPlayerMP)
		{
			tile.updateOutput();
			((EntityPlayerMP) thePlayer).sendAllContents(container, container.getInventory());
		}

		return stack;
	}
}
