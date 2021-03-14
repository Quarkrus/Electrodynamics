package electrodynamics.common.tile;

import electrodynamics.DeferredRegisters;
import electrodynamics.common.inventory.container.ContainerO2OProcessor;
import electrodynamics.common.recipe.MachineRecipes;
import electrodynamics.common.settings.Constants;
import electrodynamics.common.tile.generic.GenericTileTicking;
import electrodynamics.common.tile.generic.component.ComponentType;
import electrodynamics.common.tile.generic.component.type.ComponentContainerProvider;
import electrodynamics.common.tile.generic.component.type.ComponentDirection;
import electrodynamics.common.tile.generic.component.type.ComponentElectrodynamic;
import electrodynamics.common.tile.generic.component.type.ComponentInventory;
import electrodynamics.common.tile.generic.component.type.ComponentPacketHandler;
import electrodynamics.common.tile.generic.component.type.ComponentProcessor;
import electrodynamics.common.tile.generic.component.type.ComponentProcessorType;
import electrodynamics.common.tile.generic.component.type.ComponentTickable;
import net.minecraft.util.Direction;

public class TileMineralGrinder extends GenericTileTicking {
    public TileMineralGrinder() {
	super(DeferredRegisters.TILE_MINERALGRINDER.get());
	addComponent(new ComponentDirection());
	addComponent(new ComponentPacketHandler());
	addComponent(new ComponentTickable());
	addComponent(new ComponentElectrodynamic(this).addRelativeInputDirection(Direction.NORTH));
	addComponent(new ComponentInventory().setInventorySize(5).addSlotsOnFace(Direction.UP, 0)
		.addSlotsOnFace(Direction.DOWN, 1));
	addComponent(new ComponentContainerProvider("container.mineralgrinder")
		.setCreateMenuFunction((id, player) -> new ContainerO2OProcessor(id, player,
			getComponent(ComponentType.Inventory), getCoordsArray())));
	addComponent(new ComponentProcessor(this).addUpgradeSlots(2, 3, 4)
		.setCanProcess(component -> MachineRecipes.canProcess(this))
		.setProcess(component -> MachineRecipes.process(this))
		.setRequiredTicks(Constants.MINERALGRINDER_REQUIRED_TICKS)
		.setJoulesPerTick(Constants.MINERALGRINDER_USAGE_PER_TICK)
		.setType(ComponentProcessorType.ObjectToObject));
    }
}
