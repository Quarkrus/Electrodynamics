package electrodynamics.common.tile.pipelines.fluids;

import electrodynamics.common.block.subtype.SubtypeMachine;
import electrodynamics.common.inventory.container.tile.ContainerFluidVoid;
import electrodynamics.prefab.tile.components.IComponentType;
import electrodynamics.prefab.tile.components.type.ComponentContainerProvider;
import electrodynamics.prefab.tile.components.type.ComponentFluidHandlerSimple;
import electrodynamics.prefab.tile.components.type.ComponentInventory;
import electrodynamics.prefab.tile.components.type.ComponentInventory.InventoryBuilder;
import electrodynamics.prefab.tile.components.type.ComponentPacketHandler;
import electrodynamics.prefab.tile.components.type.ComponentTickable;
import electrodynamics.prefab.tile.types.GenericFluidTile;
import electrodynamics.prefab.utilities.CapabilityUtils;
import electrodynamics.registers.ElectrodynamicsBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class TileFluidVoid extends GenericFluidTile {

public static final int CAPACITY = 128000;
	
	public static final int INPUT_SLOT = 0;

	public TileFluidVoid(BlockPos worldPos, BlockState blockState) {
		super(ElectrodynamicsBlockTypes.TILE_FLUIDVOID.get(), worldPos, blockState);
		addComponent(new ComponentTickable(this).tickServer(this::tickServer));
		addComponent(new ComponentPacketHandler(this));
		addComponent(new ComponentFluidHandlerSimple(CAPACITY, this, "").setInputDirections(Direction.values()));
		addComponent(new ComponentInventory(this, InventoryBuilder.newInv().bucketInputs(1)).valid((slot, stack, i) -> CapabilityUtils.hasFluidItemCap(stack)));
		addComponent(new ComponentContainerProvider(SubtypeMachine.fluidvoid, this).createMenu((id, player) -> new ContainerFluidVoid(id, player, getComponent(IComponentType.Inventory), getCoordsArray())));
	}

	private void tickServer(ComponentTickable tick) {
		
		ComponentInventory inv = getComponent(IComponentType.Inventory);
		
		ComponentFluidHandlerSimple simple = getComponent(IComponentType.FluidHandler);
		
		simple.drain(simple.getFluidAmount(), FluidAction.EXECUTE);
		
		ItemStack input = inv.getItem(INPUT_SLOT);
		
		if(input.isEmpty() || !CapabilityUtils.hasFluidItemCap(input)) {
			return;
		}
		
		IFluidHandlerItem handler = CapabilityUtils.getFluidHandlerItem(input);
		
		handler.drain(Integer.MAX_VALUE, FluidAction.EXECUTE);
		
		inv.setItem(INPUT_SLOT, handler.getContainer());
		
	}

}