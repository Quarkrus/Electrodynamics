package electrodynamics.datagen.server.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import electrodynamics.datagen.server.recipe.types.custom.fluid2fluid.ElectrodynamicsElectrolyticSeparatorRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluid2item.ElectrodynamicsChemicalCrystallizerRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid.ElectrodynamicsChemicalMixerRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid.ElectrodynamicsFermentationPlantRecipes;
import electrodynamics.datagen.server.recipe.types.custom.fluiditem2fluid.ElectrodynamicsMineralWasherRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsEnergizedAlloyerRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsLatheRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsMineralCrusherRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsMineralGrinderRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsOxidationFurnaceRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsReinforcedAlloyerRecipes;
import electrodynamics.datagen.server.recipe.types.custom.item2item.ElectrodynamicsWireMillRecipes;
import electrodynamics.datagen.server.recipe.types.vanilla.ElectrodynamicsCraftingTableRecipes;
import electrodynamics.datagen.server.recipe.types.vanilla.ElectrodynamicsSmeltingRecipes;
import electrodynamics.datagen.server.recipe.types.vanilla.ElectrodynamicsStonecuttingRecipes;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

public class ElectrodynamicsRecipeProvider extends RecipeProvider {

	public final List<AbstractRecipeGenerator> GENERATORS = new ArrayList<>();

	public ElectrodynamicsRecipeProvider(DataGenerator gen) {
		super(gen);
		addRecipes();
	}

	public void addRecipes() {
		GENERATORS.add(new ElectrodynamicsCraftingTableRecipes());
		GENERATORS.add(new ElectrodynamicsSmeltingRecipes());
		GENERATORS.add(new ElectrodynamicsStonecuttingRecipes());
		GENERATORS.add(new ElectrodynamicsElectrolyticSeparatorRecipes());
		GENERATORS.add(new ElectrodynamicsChemicalCrystallizerRecipes());
		GENERATORS.add(new ElectrodynamicsMineralWasherRecipes());
		GENERATORS.add(new ElectrodynamicsFermentationPlantRecipes());
		GENERATORS.add(new ElectrodynamicsChemicalMixerRecipes());
		GENERATORS.add(new ElectrodynamicsEnergizedAlloyerRecipes());
		GENERATORS.add(new ElectrodynamicsLatheRecipes());
		GENERATORS.add(new ElectrodynamicsMineralCrusherRecipes());
		GENERATORS.add(new ElectrodynamicsMineralGrinderRecipes());
		GENERATORS.add(new ElectrodynamicsOxidationFurnaceRecipes());
		GENERATORS.add(new ElectrodynamicsReinforcedAlloyerRecipes());
		GENERATORS.add(new ElectrodynamicsWireMillRecipes());
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		for (AbstractRecipeGenerator generator : GENERATORS) {
			generator.addRecipes(consumer);
		}
	}

}
