package electrodynamics.prefab.screen.types;

import java.util.HashSet;
import java.util.Set;

import electrodynamics.prefab.inventory.container.GenericContainer;
import electrodynamics.prefab.screen.GenericScreen;
import electrodynamics.prefab.screen.component.AbstractScreenComponent;
import electrodynamics.prefab.screen.component.types.gauges.ScreenComponentFluidGauge;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * 
 * This is a simple addon class that allows for a clean integration for fluid and gas lookups with JEI
 * 
 * Note the tile does not need to be a GenericMaterialTile to use this class
 * 
 * @author skip999
 *
 * @param <T>
 */
public class GenericMaterialScreen<T extends GenericContainer> extends GenericScreen<T> {

	private Set<ScreenComponentFluidGauge> fluidGauges = new HashSet<>();

	public GenericMaterialScreen(T screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void addComponent(AbstractScreenComponent component) {
		super.addComponent(component);

		if (component instanceof ScreenComponentFluidGauge gauge) {
			fluidGauges.add(gauge);
		}

	}

	public Set<ScreenComponentFluidGauge> getFluidGauges() {
		return fluidGauges;
	}

}
