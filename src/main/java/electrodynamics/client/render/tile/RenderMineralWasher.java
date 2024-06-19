package electrodynamics.client.render.tile;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.vertex.PoseStack;

import electrodynamics.common.tile.machines.TileMineralWasher;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class RenderMineralWasher extends AbstractTileRenderer<TileMineralWasher> {

	public RenderMineralWasher(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(TileMineralWasher tileEntityIn, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

		ItemStack stack = tileEntityIn.<ComponentInventory>getComponent(IComponentType.Inventory).getInputsForProcessor(0).get(0);

		if (stack.isEmpty()) {

			return;

		}

		Direction dir = tileEntityIn.getFacing();

		matrixStackIn.pushPose();

		double scale = 12;

		matrixStackIn.translate(0.5 + dir.getStepX() / scale, stack.getItem() instanceof BlockItem ? 0.48 : 0.39, 0.5 + dir.getStepZ() / scale);

		matrixStackIn.scale(0.35f, 0.35f, 0.35f);

		matrixStackIn.scale(0.3f, 0.3f, 0.3f);

		matrixStackIn.translate(0, -0.2, 0);

		renderItem(stack, ItemDisplayContext.NONE, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, tileEntityIn.getLevel(), 0);

		matrixStackIn.popPose();

	}
	
	@Override
	public AABB getRenderBoundingBox(TileMineralWasher blockEntity) {
	    return super.getRenderBoundingBox(blockEntity).inflate(1);
	}
}
