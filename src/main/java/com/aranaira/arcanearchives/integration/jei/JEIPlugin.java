package com.aranaira.arcanearchives.integration.jei;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.integration.jei.gct.GCTCategory;
import com.aranaira.arcanearchives.integration.jei.gct.GCTWrapper;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import mezz.jei.api.*;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@mezz.jei.api.JEIPlugin
public class JEIPlugin implements IModPlugin
{
	public static final String GEM_CUTTERS_TABLE = ArcaneArchives.MODID + ".gem_cutters_table";

	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 3;
	public static IJeiHelpers jeiHelpers;
	public static ICraftingGridHelper craftingGridHelper;
	public static IRecipeRegistry recipeRegistry;
	public static IJeiRuntime runtime;

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper helper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new GCTCategory(helper));
	}

	@Override
	public void register(@Nonnull IModRegistry registry) {
		jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		// crafting helper used by the shaped table wrapper
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);

		registry.getRecipeTransferRegistry().addRecipeTransferHandler(new CraftingStationRecipeTransferInfo());
		registry.addRecipeCatalyst(new ItemStack(BlockRegistry.RADIANT_CRAFTING_TABLE, 1, 0), VanillaRecipeCategoryUid.CRAFTING);

		registry.handleRecipes(GCTRecipe.class, GCTWrapper::new, GEM_CUTTERS_TABLE);
		registry.addRecipes(GCTRecipeList.getRecipeList(), GEM_CUTTERS_TABLE);
		registry.addRecipeCatalyst(new ItemStack(BlockRegistry.GEMCUTTERS_TABLE), GEM_CUTTERS_TABLE);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
		runtime = jeiRuntime;
		recipeRegistry = jeiRuntime.getRecipeRegistry();
	}
}
