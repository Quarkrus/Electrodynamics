package electrodynamics.common.recipe.categories.item2item;

import electrodynamics.common.recipe.categories.item2item.specificmachines.EnergizedAlloyerRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.LatheRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.MineralCrusherRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.MineralGrinderRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.OxidationFurnaceRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.ReinforcedAlloyerRecipe;
import electrodynamics.common.recipe.categories.item2item.specificmachines.WireMillRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class Item2ItemRecipeTypes {

	public static final RecipeSerializer<WireMillRecipe> WIRE_MILL_JSON_SERIALIZER = new Item2ItemRecipeSerializer<>(WireMillRecipe::new);
	public static final RecipeSerializer<MineralGrinderRecipe> MINERAL_CRUSHER_JSON_SERIALIZER = new Item2ItemRecipeSerializer<>(MineralGrinderRecipe::new);
	public static final RecipeSerializer<MineralCrusherRecipe> MINERAL_GRINDER_JSON_SERIALIZER = new Item2ItemRecipeSerializer<>(MineralCrusherRecipe::new);
	public static final RecipeSerializer<LatheRecipe> LATHE_JSON_SERIALIZER = new Item2ItemRecipeSerializer<>(LatheRecipe::new);
	public static final RecipeSerializer<OxidationFurnaceRecipe> OXIDATION_FURNACE_JSON_SERIALIZER = new Item2ItemRecipeSerializer<>(OxidationFurnaceRecipe::new);
	public static final RecipeSerializer<EnergizedAlloyerRecipe> ENERGIZED_ALLOYER_JSON_SERIALIZER = new Item2ItemRecipeSerializer<>(EnergizedAlloyerRecipe::new);
	public static final RecipeSerializer<ReinforcedAlloyerRecipe> REINFORCED_ALLOYER_JSON_SERIALIZER = new Item2ItemRecipeSerializer<>(ReinforcedAlloyerRecipe::new);
}
