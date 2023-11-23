package electrodynamics.compatibility.jei.utils.label;

import electrodynamics.compatibility.jei.recipecategories.utils.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;

public abstract class AbstractLabelWrapper {

	private int COLOR;
	private int Y_POS;
	private int X_POS;
	private boolean xIsEnd;

	public AbstractLabelWrapper(int color, int yPos, int endXPos, boolean xIsEnd) {
		COLOR = color;
		Y_POS = yPos;
		X_POS = endXPos;
		this.xIsEnd = xIsEnd;
	}

	public int getColor() {
		return COLOR;
	}

	public int getYPos() {
		return Y_POS;
	}

	public int getXPos() {
		return X_POS;
	}

	public boolean xIsEnd() {
		return xIsEnd;
	}

	public abstract Component getComponent(AbstractRecipeCategory<?> category, Object recipe);
}
