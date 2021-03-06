package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.blocks.MonitoringCrystal;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.*;
import com.aranaira.arcanearchives.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ScepterRevelationItem extends ItemTemplate
{
	public static final String NAME = "item_scepterrevelation";

	public ScepterRevelationItem() {
		super(NAME);
		setMaxStackSize(1);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (hand != EnumHand.MAIN_HAND) return EnumActionResult.SUCCESS;

		if (world.isAirBlock(pos)) return EnumActionResult.SUCCESS;

		player.swingArm(hand);

		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		}

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		Style def = new Style().setColor(TextFormatting.GOLD);
		Style error = new Style().setColor(TextFormatting.DARK_RED).setBold(true);

		if (block == BlockRegistry.RADIANT_CHEST) {
			RadiantChestTileEntity te = WorldUtil.getTileEntity(RadiantChestTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			if (te.getChestName().isEmpty()) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_chest.unnamed").setStyle(def));
			} else {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_chest.name", te.getChestName()).setStyle(def));
			}

			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_chest.slots", te.countEmptySlots()).setStyle(def));
		} else if (block == BlockRegistry.RADIANT_CRAFTING_TABLE) {
			RadiantCraftingTableTileEntity te = WorldUtil.getTileEntity(RadiantCraftingTableTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			ItemStackHandler inventory = te.getInventory();
			boolean empty = true;
			List<TextComponentTranslation> names = new ArrayList<>();
			for (int i = 0; i < inventory.getSlots(); i++) {
				ItemStack stack = inventory.getStackInSlot(i);
				if (!stack.isEmpty()) {
					empty = false;
					names.add(new TextComponentTranslation(stack.getTranslationKey()+".name"));
				}
			}

			if (empty) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_crafting.empty").setStyle(def));
			} else {
				TextComponentString base = new TextComponentString("");
				for (int i = 0; i < names.size(); i++) {
					base.appendSibling(names.get(i));
					if (i != names.size() - 1) {
						base.appendText(", ");
					}
				}
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_crafting.contains", base).setStyle(def));
			}
		} else if (block == BlockRegistry.RADIANT_TROVE) {
			RadiantTroveTileEntity te = WorldUtil.getTileEntity(RadiantTroveTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			TroveItemHandler inventory = te.getInventory();
			ItemStack reference = inventory.getItemCurrent();
			int count = inventory.getCount();
			int upgrades = inventory.getUpgrades();
			int maxCount = inventory.getMaxCount();

			if (reference.isEmpty()) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_trove.empty").setStyle(def));
			} else {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_trove.item", new TextComponentTranslation(reference.getTranslationKey()+".name")).setStyle(def));
			}

			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_trove.count", count, maxCount).setStyle(def));
			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_trove.upgrades", upgrades).setStyle(def));
		} else if (block == BlockRegistry.RADIANT_RESONATOR) {
			RadiantResonatorTileEntity te = WorldUtil.getTileEntity(RadiantResonatorTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_resonator.progress", te.getPercentageComplete()).setStyle(def));
			RadiantResonatorTileEntity.TickResult res = te.canTick();
			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.radiant_resonator.status", new TextComponentTranslation(res.getKey()).setStyle(new Style().setColor(res.getFormat()))));
		} else if (block == BlockRegistry.MONITORING_CRYSTAL) {
			MonitoringCrystalTileEntity te = WorldUtil.getTileEntity(MonitoringCrystalTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			EnumFacing te_facing = state.getValue(MonitoringCrystal.FACING).getOpposite();
			player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.monitoring_crystal.facing", te_facing.getName()).setStyle(def));
		} else if (block == BlockRegistry.ACCESSOR) {
			AccessorTileEntity te = WorldUtil.getTileEntity(AccessorTileEntity.class, world, pos);
			if (te == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.missing_te").setStyle(error));
				return EnumActionResult.SUCCESS;
			}

			BlockTemplate parent = te.getParentBlock();
			BlockPos parentPos = te.getParent();
			if (parent == null) {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.accessor.invalid").setStyle(error));
			} else {
				player.sendMessage(new TextComponentTranslation("arcanearchives.data.scepter.accessor.accessor_to", new TextComponentTranslation(parent.getTranslationKey()+".name"), parentPos.getX(), parentPos.getY(), parentPos.getZ()).setStyle(def));
			}
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.scepterofrevelation"));
	}

	@Override
    public EnumRarity getRarity(ItemStack stack) {
    	return EnumRarity.RARE;
	}
}
