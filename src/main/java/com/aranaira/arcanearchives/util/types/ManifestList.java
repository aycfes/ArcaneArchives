package com.aranaira.arcanearchives.util.types;

import com.aranaira.arcanearchives.inventory.ContainerManifest;
import com.aranaira.arcanearchives.util.ItemComparison;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ManifestList extends ReferenceList<ManifestEntry>
{
	private ContainerManifest mListener;
	private String mFilterText;
	private ItemStack mSearchItem;

	public ManifestList(List<ManifestEntry> reference) {
		super(reference);
		this.mFilterText = null;
	}

	public ManifestList(List<ManifestEntry> reference, String filterText) {
		super(reference);
		this.mFilterText = filterText;
	}

	public ManifestList filtered() {
		if(mFilterText == null && mSearchItem == null) return this;

		String filter = "";

		if (mFilterText != null) {
			filter = mFilterText.toLowerCase();
		}

		String finalFilter = filter;
		return stream().filter((entry) -> {
			if(entry == null) return false;

			ItemStack stack = entry.getStack();

			if (mSearchItem != null) {
				return ItemComparison.areStacksEqualIgnoreSize(mSearchItem, stack);
			}

			String display = stack.getDisplayName().toLowerCase();
			if (display.contains(finalFilter)) return true;
			String resource = stack.getItem().getRegistryName().toString().toLowerCase();
			if (resource.contains(finalFilter)) return true;

			// Other hooks to be added at a later point
			if (stack.getItem() == Items.ENCHANTED_BOOK) {
				Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
				for (Map.Entry<Enchantment, Integer> ench : map.entrySet()) {
					String enchName = ench.getKey().getTranslatedName(ench.getValue());
					if (enchName.toLowerCase().contains(finalFilter)) return true;
				}
			}

			return false;
		}).collect(Collectors.toCollection(ManifestList::new));
	}

	public ManifestList() {
		super(new ArrayList<>());
	}

	/**
	 * Register provided {@link ContainerManifest} as a listener to {@link #deserializationFinished()} events
	 *
	 * @param containerManifest a {@link ContainerManifest}
	 */
	public void setListener(ContainerManifest containerManifest) {
		this.mListener = containerManifest;
	}

	/**
	 * Call this after this ManifestList has finished being populated from an external source.
	 * For now this means from a packet from the server
	 *
	 * If a {@link ContainerManifest} listener has been registered to this manifest then
	 * notify it that this {@link ManifestList} has been populated
	 */
	public void deserializationFinished() {
		if (this.mListener != null) {
			this.mListener.ensureCapacity(size());
		}
	}

	@Nullable
	public ManifestEntry getEntryForSlot(int slot) {
		if(slot < size() && slot >= 0) return get(slot);
		return null;
	}

	public ItemStack getItemStackForSlot(int slot) {
		if(slot < size() && slot >= 0) return get(slot).getStack();
		return ItemStack.EMPTY;
	}

	public String getSearchText() {
		return this.mFilterText;
	}

	public ItemStack getSearchItem () {
		return this.mSearchItem;
	}

	public void setSearchText(String searchTerm) {
		this.mFilterText = searchTerm;
	}

	public void setSearchItem (ItemStack stack) {
		this.mSearchItem = stack;
	}

	@Override
	public ManifestListIterable iterable() {
		return new ManifestListIterable(new ManifestIterator(iterator()));
	}

	public ManifestList sorted(Comparator<ManifestEntry> c) {
		ManifestList copy = new ManifestList(new ArrayList<>(), null);
		copy.addAll(this);
		copy.sort(c);
		return copy;
	}

	@Override
	public void clear() {
		super.clear();
	}

	public class ManifestListIterable extends ReferenceListIterable<ManifestEntry> {
		ManifestListIterable(ManifestIterator iter) {
			super(iter);
		}

		public int getSlot() {
			return ((ManifestIterator) iter).getSlot();
		}
	}

	public class ManifestIterator implements Iterator<ManifestEntry> {
		private int slot = 0;
		private Iterator<ManifestEntry> iter;

		public ManifestIterator(Iterator<ManifestEntry> iter) {
			this.iter = iter;
		}

		public int getSlot() {
			return slot;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public ManifestEntry next() {
			slot++;
			return iter.next();
		}
	}
}
