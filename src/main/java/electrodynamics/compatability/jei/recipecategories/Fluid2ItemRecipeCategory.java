package electrodynamics.compatability.jei.recipecategories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;

import electrodynamics.common.recipe.categories.fluid2item.Fluid2ItemRecipe;
import electrodynamics.common.recipe.recipeutils.FluidIngredient;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

public abstract class Fluid2ItemRecipeCategory extends ElectrodynamicsRecipeCategory<Fluid2ItemRecipe> {

    private static final int INPUT_FLUID_SLOT = 0;
    private static final int INPUT_FLUID_STACK_SLOT = 1;
    private static final int OUTPUT_ITEM_SLOT = 2;

    private int[] MAJOR_PROCESSING_ARROW_COORDS;
    private int[] MINOR_PROCESSING_ARROW_COORDS;

    private int[] FLUID_BUCKET_OFFSET;
    private int[] INPUT_FLUID_TANK;
    private int[] OUTPUT_ITEM_OFFSET;

    private int[] MAJOR_PROCESSING_ARROW_OFFSET;
    private int[] MINOR_PROCESSING_ARROW_OFFSET;

    private StartDirection MAJOR_ARROW_START_DIRECTION;
    private StartDirection MINOR_ARROW_START_DIRECTION;

    private LoadingCache<Integer, ArrayList<IDrawableAnimated>> CACHED_ARROWS;

    public Fluid2ItemRecipeCategory(IGuiHelper guiHelper, String modID, String recipeGroup, String guiTexture, ItemStack inputMachine,
	    ArrayList<int[]> inputCoordinates, int smeltTime, StartDirection majorArrowDirection, StartDirection minorArrowDirection,
	    int textYHeight) {

	/*
	 * INPUT COORDIANTES Layout
	 * 
	 * first array is gui background
	 * 
	 * second array is first processing arrow
	 * 
	 * third array is second processing arrow
	 * 
	 * array has following structure [startX,startY,xOffsec,yOffset]
	 * 
	 * 
	 * 
	 * fourth array is offset of input fluid bucket
	 * 
	 * fifth array is offset of input fluid tank
	 * [startx,endy,width,height,maxcapacity]
	 * 
	 * sixth array is offset of output item
	 * 
	 * 
	 *
	 *
	 * seventh array is offset of Major Processing arrow Eighth array is offset of
	 * Minor Processing arrow
	 * 
	 * 
	 * 
	 */
	super(guiHelper, modID, recipeGroup, guiTexture, inputMachine, inputCoordinates.get(0), Fluid2ItemRecipe.class, textYHeight, smeltTime);

	MAJOR_PROCESSING_ARROW_COORDS = inputCoordinates.get(1);
	MINOR_PROCESSING_ARROW_COORDS = inputCoordinates.get(2);

	FLUID_BUCKET_OFFSET = inputCoordinates.get(3);
	INPUT_FLUID_TANK = inputCoordinates.get(4);
	OUTPUT_ITEM_OFFSET = inputCoordinates.get(5);

	MAJOR_PROCESSING_ARROW_OFFSET = inputCoordinates.get(6);
	MINOR_PROCESSING_ARROW_OFFSET = inputCoordinates.get(7);

	MAJOR_ARROW_START_DIRECTION = majorArrowDirection;
	MINOR_ARROW_START_DIRECTION = minorArrowDirection;

	CACHED_ARROWS = CacheBuilder.newBuilder().maximumSize(25).build(new CacheLoader<Integer, ArrayList<IDrawableAnimated>>() {

	    @Override
	    public ArrayList<IDrawableAnimated> load(Integer cookTime) {

		IDrawableAnimated largeArrow = guiHelper
			.drawableBuilder(getGuiTexture(), MAJOR_PROCESSING_ARROW_COORDS[0], MAJOR_PROCESSING_ARROW_COORDS[1],
				MAJOR_PROCESSING_ARROW_COORDS[2], MAJOR_PROCESSING_ARROW_COORDS[3])
			.buildAnimated(cookTime, MAJOR_ARROW_START_DIRECTION, false);

		IDrawableAnimated smallArrow = guiHelper
			.drawableBuilder(getGuiTexture(), MINOR_PROCESSING_ARROW_COORDS[0], MINOR_PROCESSING_ARROW_COORDS[1],
				MINOR_PROCESSING_ARROW_COORDS[2], MINOR_PROCESSING_ARROW_COORDS[3])
			.buildAnimated(cookTime, MINOR_ARROW_START_DIRECTION, false);

		IDrawableAnimated[] arrows = { largeArrow, smallArrow };
		return new ArrayList<>(Arrays.asList(arrows));
	    }
	});

    }

    @Override
    public void setIngredients(Fluid2ItemRecipe recipe, IIngredients ingredients) {
	ItemStack bucket = getBucket(recipe);
	ingredients.setInput(VanillaTypes.ITEM, bucket);
	ingredients.setInputLists(VanillaTypes.FLUID, getFluids(recipe));
	ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, Fluid2ItemRecipe recipe, IIngredients ingredients) {
	IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
	IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

	guiItemStacks.init(INPUT_FLUID_STACK_SLOT, true, FLUID_BUCKET_OFFSET[0], FLUID_BUCKET_OFFSET[1]);

	guiItemStacks.init(OUTPUT_ITEM_SLOT, false, OUTPUT_ITEM_OFFSET[0], OUTPUT_ITEM_OFFSET[1]);

	int fluidInputAmount = ((FluidIngredient) recipe.getIngredients().get(0)).getFluidStack().getAmount();

	int leftHeightOffset = (int) Math.ceil(fluidInputAmount / (float) INPUT_FLUID_TANK[4] * INPUT_FLUID_TANK[3]);
	int leftStartY = INPUT_FLUID_TANK[1] - leftHeightOffset + 1;

	guiFluidStacks.init(INPUT_FLUID_SLOT, true, INPUT_FLUID_TANK[0], leftStartY, INPUT_FLUID_TANK[2], leftHeightOffset, fluidInputAmount, true,
		null);

	guiItemStacks.set(ingredients);
	guiFluidStacks.set(ingredients);
    }

    @Override
    public void draw(Fluid2ItemRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
	ArrayList<IDrawableAnimated> arrows = getArrows(recipe);

	arrows.get(0).draw(matrixStack, MAJOR_PROCESSING_ARROW_OFFSET[0], MAJOR_PROCESSING_ARROW_OFFSET[1]);
	arrows.get(1).draw(matrixStack, MINOR_PROCESSING_ARROW_OFFSET[0], MINOR_PROCESSING_ARROW_OFFSET[1]);
	drawSmeltTime(recipe, matrixStack, getYHeight());
    }

    protected ArrayList<IDrawableAnimated> getArrows(Fluid2ItemRecipe recipe) {
	return CACHED_ARROWS.getUnchecked(getArrowSmeltTime());
    }

    protected void drawSmeltTime(Fluid2ItemRecipe recipe, MatrixStack matrixStack, int y) {
	int smeltTimeSeconds = getArrowSmeltTime() / 20;
	TranslationTextComponent outputString = new TranslationTextComponent("gui.jei.category." + getRecipeGroup() + ".info.power",
		smeltTimeSeconds);
	Minecraft minecraft = Minecraft.getInstance();
	FontRenderer fontRenderer = minecraft.fontRenderer;
	int outputWidth = fontRenderer.getStringPropertyWidth(outputString);
	fontRenderer.func_243248_b(matrixStack, outputString, getBackground().getWidth() - outputWidth, y + 6, 0xFF808080);
    }

    public List<List<FluidStack>> getFluids(Fluid2ItemRecipe recipe) {
    	List<List<FluidStack>> ingredients = new ArrayList<>();
		List<FluidStack> fluids = new ArrayList<>();
		for(FluidStack fluid : ((FluidIngredient) recipe.getIngredients().get(0)).getMatchingFluidStacks()) {
			if(!fluid.getFluid().getRegistryName().toString().toLowerCase().contains("flow")) {
				fluids.add(fluid);
			}
		}
		ingredients.add(fluids);
		return ingredients;
    }

    public ItemStack getBucket(Fluid2ItemRecipe recipe) {
	FluidStack stack = ((FluidIngredient) recipe.getIngredients().get(0)).getFluidStack();
	ItemStack bucket = new ItemStack(stack.getFluid().getFilledBucket());
	bucket.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(h -> {
	    h.fill(stack, FluidAction.EXECUTE);
	});
	return bucket;
    }

}
