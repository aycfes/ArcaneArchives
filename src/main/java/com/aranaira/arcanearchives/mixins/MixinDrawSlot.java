package com.aranaira.arcanearchives.mixins;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.util.ManifestTracking;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiContainer.class)
public class MixinDrawSlot
{
	private IntArrayList tracked = null;
	private Int2ObjectArrayMap<List<NBTTagCompound>> trackedTags = null;

	@Inject(method="drawSlot", at = @At("HEAD"), cancellable = true)
	private void drawSlot(Slot slot, CallbackInfo ci)
	{
		if (tracked == null)
		{
			Minecraft mc = Minecraft.getMinecraft();
			tracked = ManifestTracking.get(mc.player.dimension);
			trackedTags = ManifestTracking.getTags(mc.player.dimension);
		}

		ItemStack stack = slot.getStack();
		if(!stack.isEmpty())
		{
			int pack = RecipeItemHelper.pack(stack);
			if(tracked != null && tracked.contains(pack))
			{
				boolean highlight = true;

				if(stack.hasTagCompound() && trackedTags != null && trackedTags.containsKey(pack))
				{
					List<NBTTagCompound> tags = trackedTags.get(pack);
					NBTTagCompound stackTag = stack.getTagCompound();
					assert stackTag != null;
					boolean foundMatchingTag = false;

					for(NBTTagCompound tag : tags)
					{
						if(stackTag.equals(tag))
						{
							foundMatchingTag = true;
							break;
						}
					}

					if(!foundMatchingTag)
					{
						highlight = false;
					}
				} else if(stack.hasTagCompound() && trackedTags == null)
				{
					highlight = false;
				}

				if(highlight)
				{
					GlStateManager.disableDepth();
					float partialTicks = this.mc.getTickLength();
					drawRect(slot.xPos, slot.yPos, slot.xPos + 16, slot.yPos + 16, ConfigHandler.MANIFEST_HIGHLIGHT);
				}
			}
		}

		super.drawSlot(slot);
}
