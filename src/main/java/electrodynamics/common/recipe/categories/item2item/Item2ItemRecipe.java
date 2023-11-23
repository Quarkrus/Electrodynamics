package electrodynamics.common.recipe.categories.item2item;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;

import electrodynamics.common.recipe.ElectrodynamicsRecipe;
import electrodynamics.common.recipe.recipeutils.CountableIngredient;
import electrodynamics.common.recipe.recipeutils.ProbableFluid;
import electrodynamics.common.recipe.recipeutils.ProbableGas;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentProcessor;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class Item2ItemRecipe extends ElectrodynamicsRecipe {

	private CountableIngredient[] ITEM_INPUTS;
	private ItemStack OUTPUT;

	public Item2ItemRecipe(ResourceLocation recipeID, CountableIngredient[] inputs, ItemStack output, double experience, int ticks, double usagePerTick, ProbableItem[] itemBiproducts, ProbableFluid[] fluidBiproducts, ProbableGas[] gasBiproducts) {
		super(recipeID, experience, ticks, usagePerTick, itemBiproducts, fluidBiproducts, gasBiproducts);
		ITEM_INPUTS = inputs;
		OUTPUT = output;
	}

	@Override
	public boolean matchesRecipe(ComponentProcessor pr) {
		Pair<List<Integer>, Boolean> pair = areItemsValid(getCountedIngredients(), ((ComponentInventory) pr.getHolder().getComponent(IComponentType.Inventory)).getInputsForProcessor(pr.getProcessorNumber()));
		if (pair.getSecond()) {
			setItemArrangement(pr.getProcessorNumber(), pair.getFirst());
			return true;
		}
		return false;
	}

	@Override
	public ItemStack assemble(RecipeWrapper inv) {
		return getResultItem();
	}

	@Override
	public ItemStack getResultItem() {
		return OUTPUT;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();
		for (CountableIngredient ing : ITEM_INPUTS) {
			list.add(ing);
		}
		return list;
	}

	public List<CountableIngredient> getCountedIngredients() {
		List<CountableIngredient> list = new ArrayList<>();
		for (CountableIngredient ing : ITEM_INPUTS) {
			list.add(ing);
		}
		return list;
	}

}
