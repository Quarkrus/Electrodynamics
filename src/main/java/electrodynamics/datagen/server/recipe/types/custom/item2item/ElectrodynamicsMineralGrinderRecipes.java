package electrodynamics.datagen.server.recipe.types.custom.item2item;

import java.util.function.Consumer;

import electrodynamics.api.References;
import electrodynamics.common.item.subtype.SubtypeCrystal;
import electrodynamics.common.item.subtype.SubtypeDust;
import electrodynamics.common.item.subtype.SubtypeImpureDust;
import electrodynamics.common.item.subtype.SubtypeIngot;
import electrodynamics.common.recipe.ElectrodynamicsRecipeInit;
import electrodynamics.common.recipe.recipeutils.ProbableItem;
import electrodynamics.common.tags.ElectrodynamicsTags;
import electrodynamics.datagen.utils.recipe.AbstractElectrodynamicsFinishedRecipe.RecipeCategory;
import electrodynamics.datagen.utils.recipe.AbstractRecipeGenerator;
import electrodynamics.datagen.utils.recipe.FinishedRecipeItemOutput;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

public class ElectrodynamicsMineralGrinderRecipes extends AbstractRecipeGenerator {

	public static double MINERALGRINDER_USAGE_PER_TICK = 350.0;
	public static int MINERALGRINDER_REQUIRED_TICKS = 200;

	private final String modID;

	public ElectrodynamicsMineralGrinderRecipes(String modID) {
		this.modID = modID;
	}

	public ElectrodynamicsMineralGrinderRecipes() {
		this(References.ID);
	}

	@Override
	public void addRecipes(Consumer<IFinishedRecipe> consumer) {

		for (SubtypeIngot ingot : SubtypeIngot.values()) {
			if (ingot.grindedDust != null) {
				newRecipe(new ItemStack(ingot.grindedDust.get()), 0, 200, 350.0, "dust_" + ingot.name() + "_from_ingot")
						//
						.addItemTagInput(ingot.tag, 1)
						//
						.complete(consumer);
			}

		}

		newRecipe(new ItemStack(DUSTS[SubtypeDust.iron.ordinal()]), 0, 200, 350.0, "dust_iron_from_ingot")
				//
				.addItemTagInput(Tags.Items.INGOTS_IRON, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.gold.ordinal()]), 0, 200, 350.0, "dust_gold_from_ingot")
				//
				.addItemTagInput(Tags.Items.INGOTS_GOLD, 1)
				//
				.complete(consumer);

		for (SubtypeImpureDust dust : SubtypeImpureDust.values()) {
			newRecipe(new ItemStack(dust.grindedDust.get()), 0.1F, 200, 350.0, "dust_" + dust.name() + "_from_imp_dust")
					//
					.addItemTagInput(dust.tag, 1)
					//
					.complete(consumer);
		}

		newRecipe(new ItemStack(DUSTS[SubtypeDust.copper.ordinal()], 2), 0.1F, 200, 350.0, "dust_copper_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_COPPER, 2)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.gold.ordinal()], 2), 0.5F, 200, 350.0, "dust_gold_from_ore")
				//
				.addItemTagInput(ItemTags.GOLD_ORES, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.iron.ordinal()], 2), 0.3F, 200, 350.0, "dust_iron_from_ore")
				//
				.addItemTagInput(Tags.Items.ORES_IRON, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.lead.ordinal()], 2), 0.3F, 200, 350.0, "dust_lead_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_LEAD, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.molybdenum.ordinal()], 2), 0.3F, 200, 350.0, "dust_molybdenum_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_MOLYBDENUM, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.silver.ordinal()], 2), 0.5F, 200, 350.0, "dust_silver_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_SILVER, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.tin.ordinal()], 2), 0.1F, 200, 350.0, "dust_tin_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_TIN, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.vanadium.ordinal()], 2), 0.1F, 200, 350.0, "dust_vanadium_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_VANADIUM, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COBBLESTONE), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.COAL, 3), 0.3F, 200, 350.0, "gem_coal_from_ore")
				//
				.addItemTagInput(Tags.Items.ORES_COAL, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.DIAMOND), 0.05))
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.DIAMOND, 3), 1.0F, 200, 350.0, "gem_diamond_from_ore")
				//
				.addItemTagInput(Tags.Items.ORES_DIAMOND, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COAL), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.EMERALD, 3), 1F, 200, 350.0, "gem_emerald_from_ore")
				//
				.addItemTagInput(Tags.Items.ORES_EMERALD, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.DIAMOND), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.LAPIS_LAZULI, 9), 0.4F, 200, 350.0, "gem_lapis_from_ore")
				//
				.addItemTagInput(Tags.Items.ORES_LAPIS, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.DIAMOND), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.QUARTZ, 7), 0.7F, 200, 350.0, "gem_quartz_from_ore")
				//
				.addItemTagInput(Tags.Items.ORES_QUARTZ, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COAL), 0.1))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.endereye.ordinal()], 2), 1F, 200, 350.0, "dust_ender_eye")
				//
				.addItemTagInput(Tags.Items.ENDER_PEARLS, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.niter.ordinal()], 3), 0.1F, 200, 350.0, "dust_niter_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_SALTPETER, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(CRYSTALS[SubtypeCrystal.potassiumchloride.ordinal()], 3), 0.3F, 200, 350.0, "pot_chloride_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_POTASSIUMCHLORIDE, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.REDSTONE, 6), 0.4F, 200, 350.0, "dust_redstone_from_ore")
				//
				.addItemTagInput(Tags.Items.ORES_REDSTONE, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.DIAMOND), 0.05))
				//
				.complete(consumer);

		newRecipe(new ItemStack(DUSTS[SubtypeDust.sulfur.ordinal()], 3), 0.2F, 200, 350.0, "dust_sulfur_from_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_SULFUR, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.COAL), 0.05))
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.COBBLESTONE, 1), 0.01F, 200, 350.0, "cobblestone_from_stone")
				//
				.addItemTagInput(Tags.Items.STONE, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.GRAVEL), 0.4))
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.GRAVEL, 1), 0.01F, 200, 350.0, "gravel_from_cobblestone")
				//
				.addItemTagInput(Tags.Items.COBBLESTONE, 1)
				//
				.addItemBiproduct(new ProbableItem(new ItemStack(Items.SAND), 0.4))
				//
				.complete(consumer);

		newRecipe(new ItemStack(Items.SAND, 1), 0.01F, 200, 350.0, "sand_from_gravel")
				//
				.addItemTagInput(Tags.Items.GRAVEL, 1)
				//
				.complete(consumer);

		newRecipe(new ItemStack(CRYSTALS[SubtypeCrystal.halite.ordinal()], 3), 0.1F, 200, 350.0, "halite_cystal_from_halite_ore")
				//
				.addItemTagInput(ElectrodynamicsTags.Items.ORE_SALT, 1)
				//
				.complete(consumer);

	}

	public FinishedRecipeItemOutput newRecipe(ItemStack stack, float xp, int ticks, double usagePerTick, String name) {
		return FinishedRecipeItemOutput.of(ElectrodynamicsRecipeInit.MINERAL_GRINDER_SERIALIZER.get(), stack, xp, ticks, usagePerTick).name(RecipeCategory.ITEM_2_ITEM, modID, "mineral_grinder/" + name);
	}

}
