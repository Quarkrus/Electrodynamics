package electrodynamics.datagen.server.recipe.types.custom.item2item;

import java.util.function.Consumer;

import electrodynamics.api.References;
import electrodynamics.common.block.subtype.SubtypeGlass;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractElectrodynamicsFinishedRecipe.RecipeCategory;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.FinishedRecipeItemOutput;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

public class ElectrodynamicsReinforcedAlloyerRecipes extends AbstractRecipeGenerator {

	public static int REINFORCEDALLOYER_REQUIRED_TICKS = 50;
	public static double REINFORCEDALLOYER_USAGE_PER_TICK = 50.0;

	private final String modID;

	public ElectrodynamicsReinforcedAlloyerRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsReinforcedAlloyerRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer) {

		newRecipe(new ItemStack(CUSTOM_GLASS[SubtypeGlass.aluminum.ordinal()], 5), 0.5F, 50, 50.0, "aluminum_glass")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_ALUMINUM, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.DUST_SALTPETER, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(INGOTS[SubtypeIngot.bronze.ordinal()], 7), 0.1F, 50, 50.0, "ingot_bronze")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_TIN, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_COPPER, 4)
				//
				.complete(consumer);

		newRecipe(new ItemStack(CUSTOM_GLASS[SubtypeGlass.clear.ordinal()], 16), 0.1F, 50, 50.0, "clear_glass")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.COAL_COKE, 1)
				//
				.addItemTagInput(Tags.Items.SAND, 16)
				//
				.complete(consumer);

		newRecipe(new ItemStack(INGOTS[SubtypeIngot.hslasteel.ordinal()], 2), 0.3F, 50, 50.0, "ingot_hsla_steel")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_STAINLESSSTEEL, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_MOLYBDENUM, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.NETHERITE_INGOT, 2), 1.0F, 50, 50.0, "ingot_netherite")
				//
				.addItemStackInput(new ItemStack(Items.NETHERITE_SCRAP))
				//
				.addItemTagInput(Tags.Items.INGOTS_GOLD, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(INGOTS[SubtypeIngot.stainlesssteel.ordinal()], 4), 0.4F, 50, 50.0, "ingot_stainless_steel")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_CHROMIUM, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_STEEL, 3)
				//
				.complete(consumer);

		newRecipe(new ItemStack(INGOTS[SubtypeIngot.steel.ordinal()], 4), 0.3F, 50, 50.0, "ingot_steel")
				//
				.addItemTagInput(Tags.Items.INGOTS_IRON, 2)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.COAL_COKE, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(INGOTS[SubtypeIngot.superconductive.ordinal()], 4), 0.3F, 50, 50.0, "ingot_superconductive_endereye")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_SILVER, 5)
				//
				.addItemStackInput(new ItemStack(Items.ENDER_EYE, 4))
				//
				.complete(consumer);

		newRecipe(new ItemStack(INGOTS[SubtypeIngot.superconductive.ordinal()], 4), 0.3F, 50, 50.0, "ingot_superconductive_netherite")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_SILVER, 5)
				//
				.addItemTagInput(Tags.Items.INGOTS_NETHERITE, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(INGOTS[SubtypeIngot.titaniumcarbide.ordinal()], 4), 0.3F, 50, 50.0, "ingot_titanium_carbide")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_TITANIUM, 1)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.COAL_COKE, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(INGOTS[SubtypeIngot.vanadiumsteel.ordinal()], 8), 0.3F, 50, 50.0, "vanadium_steel_ingot")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_STEEL, 8)
				//
				.addItemTagInput(ElectrodynamicsTags.Items.INGOT_VANADIUM, 1)
				//
				.complete(consumer);

	}

	public FinishedRecipeItemOutput newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name) {
		return FinishedRecipeItemOutput.of(ElectrodynamicsRecipeInit.REINFORCED_ALLOYER_SERIALIZER.get(), stack, xp, ticks, usagePerTick).name(RecipeCategory.ITEM_2_ITEM, modID, "reinforced_alloyer/" + name);
	}

}
