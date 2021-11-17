package electrodynamics.common.fluid.types;

import electrodynamics.DeferredRegisters;
import electrodynamics.api.References;
import electrodynamics.common.fluid.FluidNonPlaceable;

public class FluidMolybdenum extends FluidNonPlaceable {

	public static final String FORGE_TAG = "molybdenum";
	
    public FluidMolybdenum() {
	super(() -> DeferredRegisters.ITEM_CANISTERREINFORCED, References.ID, "molybdenum");
    }

}
