package com.aranaira.arcanearchives.items.itemblocks;

import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class RadiantTankItem extends ItemBlock
{
	public RadiantTankItem(Block block)
	{
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		FluidStack fluid;

		if (stack.hasTagCompound()) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag("tank");
			fluid = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(RadiantTankTileEntity.Tags.HANDLER_ITEM));
			if (fluid != null) {
				tooltip.add(I18n.format("arcanearchives.tooltip.tank.fluid", fluid.getLocalizedName()));
				tooltip.add(I18n.format("arcanearchives.tooltip.tank.amount", fluid.amount, RadiantTankTileEntity.BASE_CAPACITY * tag.getInteger("upgrades")));
			} else {
				tooltip.add(I18n.format("arcanearchives.tooltip.tank.fluid", "None"));
				tooltip.add(I18n.format("arcanearchives.tooltip.tank.amount", 0, RadiantTankTileEntity.BASE_CAPACITY * tag.getInteger("upgrades")));
			}
		}

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
