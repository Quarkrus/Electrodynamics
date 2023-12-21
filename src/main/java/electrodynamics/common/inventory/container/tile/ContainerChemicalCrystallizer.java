package electrodynamics.common.inventory.container.tile;

import electrodynamics.common.item.subtype.SubtypeItemUpgrade;
import electrodynamics.common.tile.machines.TileChemicalCrystallizer;
import electrodynamics.prefab.inventory.container.GenericContainerBlockEntity;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotFluid;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotRestricted;
import electrodynamics.prefab.inventory.container.slot.item.type.SlotUpgrade;
import electrodynamics.prefab.utilities.math.Color;
import electrodynamics.registers.ElectrodynamicsMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;

public class ContainerChemicalCrystallizer extends GenericContainerBlockEntity<TileChemicalCrystallizer> {

	public static final SubtypeItemUpgrade[] VALID_UPGRADES = new SubtypeItemUpgrade[] { SubtypeItemUpgrade.advancedspeed, SubtypeItemUpgrade.basicspeed, SubtypeItemUpgrade.itemoutput, SubtypeItemUpgrade.experience };

	public ContainerChemicalCrystallizer(int id, Inventory playerinv) {
		this(id, playerinv, new SimpleContainer(5), new SimpleContainerData(3));
	}

	public ContainerChemicalCrystallizer(int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(ElectrodynamicsMenuTypes.CONTAINER_CHEMICALCRYSTALLIZER.get(), id, playerinv, inventory, inventorydata);
	}

	public ContainerChemicalCrystallizer(MenuType<?> type, int id, Inventory playerinv, Container inventory, ContainerData inventorydata) {
		super(type, id, playerinv, inventory, inventorydata);
	}

	@Override
	public void addInventorySlots(Container inv, Inventory playerinv) {
		addSlot(new SlotRestricted(inv, nextIndex(), 82, 31).setIOColor(new Color(255, 0, 0, 255)));
		addSlot(new SlotFluid(inv, nextIndex(), 82, 51));
		addSlot(new SlotUpgrade(inv, nextIndex(), 150, 14, VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 150, 34, VALID_UPGRADES));
		addSlot(new SlotUpgrade(inv, nextIndex(), 150, 54, VALID_UPGRADES));
	}
}