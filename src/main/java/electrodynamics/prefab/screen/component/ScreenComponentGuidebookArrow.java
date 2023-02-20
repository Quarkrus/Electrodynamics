package electrodynamics.prefab.screen.component;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.api.References;
import electrodynamics.api.screen.IScreenWrapper;
import electrodynamics.api.screen.ITexture;
import electrodynamics.client.guidebook.ScreenGuidebook;
import net.minecraft.resources.ResourceLocation;

public class ScreenComponentGuidebookArrow extends ScreenComponentGeneric {

	private boolean shouldRender = false;
	private int page;

	public ScreenComponentGuidebookArrow(ITexture texture, IScreenWrapper gui, int x, int y, int page) {
		super(texture, gui, x, y);
		this.page = page;
	}

	@Override
	public void renderBackground(PoseStack stack, int xAxis, int yAxis, int guiWidth, int guiHeight) {
		if (isVisible()) {
			super.renderBackground(stack, xAxis, yAxis, guiWidth, guiHeight);
		}
	}

	public void setShouldRender(boolean shouldRender) {
		this.shouldRender = shouldRender;
	}

	@Override
	public boolean isVisible() {

		return (ScreenGuidebook.currPageNumber == page || ScreenGuidebook.currPageNumber + 1 == page) && shouldRender;

	}

	public enum ArrowTextures implements ITexture {
		ARROW_DOWN(11, 7, 0, 0, 11, 7, "arrowdown"), ARROW_UP(11, 7, 0, 0, 11, 7, "arrowup");

		private final int textureWidth;
		private final int textureHeight;
		private final int textureU;
		private final int textureV;
		private final int imageWidth;
		private final int imageHeight;
		private final ResourceLocation loc;

		private ArrowTextures(int textureWidth, int textureHeight, int textureU, int textureV, int imageWidth, int imageHeight, String name) {
			this.textureWidth = textureWidth;
			this.textureHeight = textureHeight;
			this.textureU = textureU;
			this.textureV = textureV;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			loc = new ResourceLocation(References.ID + ":textures/screen//guidebook/buttons/" + name + ".png");
		}

		@Override
		public ResourceLocation getLocation() {
			return loc;
		}

		@Override
		public int imageHeight() {
			return imageHeight;
		}

		@Override
		public int imageWidth() {
			return imageWidth;
		}

		@Override
		public int textureHeight() {
			return textureHeight;
		}

		@Override
		public int textureU() {
			return textureU;
		}

		@Override
		public int textureV() {
			return textureV;
		}

		@Override
		public int textureWidth() {
			return textureWidth;
		}

	}

}
